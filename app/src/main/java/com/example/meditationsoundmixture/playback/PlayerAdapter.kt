package com.project.meditationsoundmixture.playback

import android.media.MediaPlayer

interface PlayerAdapter {

    fun isMediaPlayer(): Boolean

    fun isPlaying(): Boolean

    fun isReset(): Boolean


    @PlaybackInfoListener.State
    fun getState(): Int

    fun getPlayerPosition(): Int

    fun getMediaPlayer(): MediaPlayer?
    fun initMediaPlayer()

    fun release()

    fun resumeOrPause()

    fun reset()

    fun instantReset()

    fun skip(isNext: Boolean)

    fun seekTo(position: Int)

    fun setPlaybackInfoListener(playbackInfoListener: PlaybackInfoListener)

    fun registerNotificationActionsReceiver(isRegister: Boolean)


    fun onPauseActivity()

    fun onResumeActivity()
}
