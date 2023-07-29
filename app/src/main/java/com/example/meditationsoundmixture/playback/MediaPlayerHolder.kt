package com.project.meditationsoundmixture.playback

import android.bluetooth.BluetoothDevice
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.media.AudioAttributes
import android.media.AudioManager
import android.media.MediaPlayer
import java.util.concurrent.ScheduledExecutorService


class MediaPlayerHolder(private val mMusicService: MusicService?) :
    MediaPlayer.OnCompletionListener, MediaPlayer.OnPreparedListener {
    private val mContext: Context
    private val mAudioManager: AudioManager
    private var mMediaPlayer: MediaPlayer? = null
    private var mPlaybackInfoListener: PlaybackInfoListener? = null
    private var mExecutor: ScheduledExecutorService? = null
    private var mSeekBarPositionUpdateTask: Runnable? = null
    private var sReplaySong = false

    @PlaybackInfoListener.State
    private var mState: Int = 0
    private var mNotificationActionsReceiver: NotificationReceiver? = null
    private var mMusicNotificationManager: MusicNotificationManager? = null
    private var mCurrentAudioFocusState = AUDIO_NO_FOCUS_NO_DUCK
    private var mPlayOnFocusGain: Boolean = false
    private val mOnAudioFocusChangeListener =
        AudioManager.OnAudioFocusChangeListener { focusChange ->
            when (focusChange) {
                AudioManager.AUDIOFOCUS_GAIN -> mCurrentAudioFocusState = AUDIO_FOCUSED
                AudioManager.AUDIOFOCUS_LOSS_TRANSIENT_CAN_DUCK ->
                    // Audio focus was lost, but it's possible to duck (i.e.: play quietly)
                    mCurrentAudioFocusState = AUDIO_NO_FOCUS_CAN_DUCK
                AudioManager.AUDIOFOCUS_LOSS_TRANSIENT -> {
                    // Lost audio focus, but will gain it back (shortly), so note whether
                    // playback should resume
                    mCurrentAudioFocusState = AUDIO_NO_FOCUS_NO_DUCK
                    mPlayOnFocusGain =
                         mState == PlaybackInfoListener.State.PLAYING || mState == PlaybackInfoListener.State.RESUMED
                }
                AudioManager.AUDIOFOCUS_LOSS ->
                    // Lost audio focus, probably "permanently"
                    mCurrentAudioFocusState = AUDIO_NO_FOCUS_NO_DUCK
            }

            if (mMediaPlayer != null) {
                // Update the player state based on the change
                configurePlayerState()
            }
        }

    init {
        mContext = mMusicService!!.applicationContext
        mAudioManager = mContext.getSystemService(Context.AUDIO_SERVICE) as AudioManager
    }

    private fun registerActionsReceiver() {
        mNotificationActionsReceiver = NotificationReceiver()
        val intentFilter = IntentFilter()

        intentFilter.addAction(MusicNotificationManager.PREV_ACTION)
        intentFilter.addAction(MusicNotificationManager.PLAY_PAUSE_ACTION)
        intentFilter.addAction(MusicNotificationManager.NEXT_ACTION)
        intentFilter.addAction(BluetoothDevice.ACTION_ACL_CONNECTED)
        intentFilter.addAction(BluetoothDevice.ACTION_ACL_DISCONNECTED)
        intentFilter.addAction(Intent.ACTION_HEADSET_PLUG)
        intentFilter.addAction(AudioManager.ACTION_AUDIO_BECOMING_NOISY)

        mMusicService!!.registerReceiver(mNotificationActionsReceiver, intentFilter)
    }

    private fun unregisterActionsReceiver() {
        if (mMusicService != null && mNotificationActionsReceiver != null) {
            try {
                mMusicService.unregisterReceiver(mNotificationActionsReceiver)
            } catch (e: IllegalArgumentException) {
                e.printStackTrace()
            }

        }
    }


    fun registerNotificationActionsReceiver(isReceiver: Boolean) {

        if (isReceiver) {
            registerActionsReceiver()
        } else {
            unregisterActionsReceiver()
        }
    }


    fun onPauseActivity() {
        stopUpdatingCallbackWithPosition()
    }

    private fun tryToGetAudioFocus() {

        val result = mAudioManager.requestAudioFocus(
            mOnAudioFocusChangeListener,
            AudioManager.STREAM_MUSIC,
            AudioManager.AUDIOFOCUS_GAIN
        )
        if (result == AudioManager.AUDIOFOCUS_REQUEST_GRANTED) {
            mCurrentAudioFocusState = AUDIO_FOCUSED
        } else {
            mCurrentAudioFocusState = AUDIO_NO_FOCUS_NO_DUCK
        }
    }

    private fun giveUpAudioFocus() {
        if (mAudioManager.abandonAudioFocus(mOnAudioFocusChangeListener) == AudioManager.AUDIOFOCUS_REQUEST_GRANTED) {
            mCurrentAudioFocusState = AUDIO_NO_FOCUS_NO_DUCK
        }
    }

    fun setPlaybackInfoListener(listener: PlaybackInfoListener) {
        mPlaybackInfoListener = listener
    }

    private fun setStatus(@PlaybackInfoListener.State state: Int) {

        mState = state
        if (mPlaybackInfoListener != null) {
            mPlaybackInfoListener!!.onStateChanged(state)
        }
    }

    private fun resumeMediaPlayer() {
        if (!isPlaying()) {
            mMediaPlayer!!.start()
            setStatus(PlaybackInfoListener.State.RESUMED)
            mMusicService!!.startForeground(
                MusicNotificationManager.NOTIFICATION_ID,
                mMusicNotificationManager!!.createNotification()
            )
        }
    }

    private fun pauseMediaPlayer() {
        setStatus(PlaybackInfoListener.State.PAUSED)
        mMediaPlayer!!.pause()
        mMusicService!!.stopForeground(false)
        mMusicNotificationManager!!.notificationManager.notify(
            MusicNotificationManager.NOTIFICATION_ID,
            mMusicNotificationManager!!.createNotification()
        )
    }

    private fun resetSong() {
        mMediaPlayer!!.seekTo(0)
        mMediaPlayer!!.start()
        setStatus(PlaybackInfoListener.State.PLAYING)
    }


    private fun stopUpdatingCallbackWithPosition() {
        if (mExecutor != null) {
            mExecutor!!.shutdownNow()
            mExecutor = null
            mSeekBarPositionUpdateTask = null
        }
    }




    fun isPlaying(): Boolean {
        return mMediaPlayer!!.isPlaying
    }

    fun resumeOrPause() {

        if (isPlaying()) {
            pauseMediaPlayer()
        } else {
            resumeMediaPlayer()
        }
    }

    @PlaybackInfoListener.State
    fun getState(): Int {
        return mState
    }


    /**
     * Reconfigures the player according to audio focus settings and starts/restarts it. This method
     * starts/restarts the MediaPlayer instance respecting the current audio focus state. So if we
     * have focus, it will play normally; if we don't have focus, it will either leave the player
     * paused or set it to a low volume, depending on what is permitted by the current focus
     * settings.
     */
    private fun configurePlayerState() {

        if (mCurrentAudioFocusState == AUDIO_NO_FOCUS_NO_DUCK) {
            // We don't have audio focus and can't duck, so we have to pause
            pauseMediaPlayer()
        } else {

            if (mCurrentAudioFocusState == AUDIO_NO_FOCUS_CAN_DUCK) {
                // We're permitted to play, but only if we 'duck', ie: play softly
                mMediaPlayer!!.setVolume(VOLUME_DUCK, VOLUME_DUCK)
            } else {
                mMediaPlayer!!.setVolume(VOLUME_NORMAL, VOLUME_NORMAL)
            }

            // If we were playing when we lost focus, we need to resume playing.
            if (mPlayOnFocusGain) {
                resumeMediaPlayer()
                mPlayOnFocusGain = false
            }
        }
    }

    private inner class NotificationReceiver : BroadcastReceiver() {

        override fun onReceive(context: Context, intent: Intent) {
            // TODO Auto-generated method stub
            val action = intent.action

            if (action != null) {

                when (action) {
                    MusicNotificationManager.PLAY_PAUSE_ACTION -> resumeOrPause()

                    AudioManager.ACTION_AUDIO_BECOMING_NOISY -> if (isPlaying()) {
                        pauseMediaPlayer()
                    }
                }
            }
        }
    }

    companion object {

        // The volume we set the media player to when we lose audio focus, but are
        // allowed to reduce the volume instead of stopping playback.
        private val VOLUME_DUCK = 0.2f

        // The volume we set the media player when we have audio focus.
        private val VOLUME_NORMAL = 1.0f

        // we don't have audio focus, and can't duck (play at a low volume)
        private val AUDIO_NO_FOCUS_NO_DUCK = 0

        // we don't have focus, but can duck (play at a low volume)
        private val AUDIO_NO_FOCUS_CAN_DUCK = 1

        // we have full audio focus
        private val AUDIO_FOCUSED = 2
    }

    override fun onCompletion(p0: MediaPlayer?) {
        TODO("Not yet implemented")
    }

    override fun onPrepared(p0: MediaPlayer?) {
        TODO("Not yet implemented")
    }
}
