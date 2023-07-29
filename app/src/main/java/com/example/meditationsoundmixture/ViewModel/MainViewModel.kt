package com.project.meditationsoundmixture.ViewModel

import android.app.Activity
import android.app.Application
import android.media.AudioManager
import android.media.MediaPlayer
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.project.meditationsoundmixture.Application.App
import com.project.meditationsoundmixture.Constants
import com.project.meditationsoundmixture.Constants.PAUSED
import com.project.meditationsoundmixture.MediaPlayers
import com.project.meditationsoundmixture.MeditatioinSoundServices.MusicPlayerService
import com.project.meditationsoundmixture.R
import com.project.meditationsoundmixture.fragments.Homeitemss
import com.project.meditationsoundmixture.model.MusicItems
import com.project.meditationsoundmixture.playback.MusicNotificationManager
import com.project.meditationsoundmixture.playback.PlaybackInfoListener
import com.project.meditationsoundmixture.ui.MainActivity

class MainViewModel(app :Application) : AndroidViewModel(app) {
    var cone: Int? = null
    var positonview: Int? = null
    var position: Int? = null
    var musicItem: MusicItems? = null
    var musicextra = ArrayList<MusicItems>()
    var muiclist = MutableLiveData<ArrayList<MusicItems>>()

   // var mediaPlayerService: MusicPlayerService? = null
    var musiclistitem = MutableLiveData<ArrayList<Homeitemss>>()
    var homeitems = ArrayList<Homeitemss>()
    var musiclistone = MutableLiveData<ArrayList<MusicItems>>()
    var musiclisttwo = MutableLiveData<ArrayList<MusicItems>>()
    var musicdata = ArrayList<MusicItems>()
    var musicchange = ArrayList<MusicItems>()
    private var mPlaybackInfoListener: PlaybackInfoListener? = null
    var musicService: MusicPlayerService
    lateinit var audioManager: AudioManager
    var state = PAUSED

    @PlaybackInfoListener.State
    private var mState: Int = 0

    var musicdatatwo = ArrayList<MusicItems>()
    var musicdatathree = ArrayList<MusicItems>()
    var homeviewpageritems = ArrayList<Homeitemss>()
    var stateofMediaplayer = MutableLiveData<Boolean>()
    var optionItemClick: ((isPlay: Int) -> Unit)? = null
    var optionItemClick2: ((isPlay: Int) -> Unit)? = null
    var optionItemClick3: ((isPlay: Int) -> Unit)? = null
    var optionItemClick4: ((isPlay: Int) -> Unit)? = null
    private var mMusicNotificationManager: MusicNotificationManager? = null

    init {
        musicService = MusicPlayerService()
        getMusiclist()
        getMusiclistwo()
        getMusiclisthree()
    }


    fun getMusiclist() {
        getAudiolistone()
    }

    fun play() {
        getInstancemediaplayerone().start()
        state = Constants.PLAYING

    }

    fun pause() {
        state = Constants.PAUSED

    }

    fun resume() {
        state = Constants.RESUMED

    }

    fun getMusiclistwo() {
        getAudiolisttwo()
    }

    fun getMusiclisthree() {
        getAudiolisthree()
    }

//    fun gethomeviewpagerlist(activity: Activity) {
//        gethomeviewpagerlistone(activity)
//    }
//
//    private fun gethomeviewpagerlistone(activity: Activity): MutableLiveData<ArrayList<Homeitemss>> {
//        with(activity) {
//            homeitems.add(
//                Homeitemss(
//                    R.drawable.bgone,
//                    getString(R.string.gentletitle),
//                    "Morning Sound With Multiple Musics",
//                    R.raw.slowmorning
//                )
//            )
//            homeitems.add(
//                Homeitemss(
//                    R.drawable.bgtwo,
//                    "Peacefull Lake",
//                    "Peacefull Song With Multiple Musics",
//                    R.raw.lake
//                )
//            )
//            homeitems.add(
//                Homeitemss(
//                    R.drawable.bgthree,
//                    "Sunrise",
//                    "Sunrise Sound with Multiple Musics",
//                    R.raw.sunriseambient
//                )
//            )
//            homeitems.add(
//                Homeitemss(
//                    R.drawable.bgfour,
//                    "Soft Piano",
//                    "Soft Piano with Multiple Musics",
//                    R.raw.piano_1
//                )
//            )
//            homeitems.add(
//                Homeitemss(
//                    R.drawable.bgfive,
//                    "Heaven",
//                    "Heaven Sound  with Multiple Musics",
//                    R.raw.heaven
//                )
//            )
//            homeitems.add(
//                Homeitemss(
//                    R.drawable.bgsix,
//                    "Perfect Rain",
//                    "Multiple Sound with Rain Effects",
//                    R.raw.perfect_rain
//                )
//            )
//            homeitems.add(
//                Homeitemss(
//                    R.drawable.bgseven,
//                    "Inspiraton",
//                    "Multiple Sound with Rain Effects",
//                    R.raw.insp
//                )
//            )
//            homeitems.add(
//                Homeitemss(
//                    R.drawable.bgeight,
//                    "Autumn Forest",
//                    "Forest Sound with Different Effects",
//                    R.raw.autumn_forest
//                )
//            )
//            homeitems.add(
//                Homeitemss(
//                    R.drawable.bgnine,
//                    "Convent",
//                    "Convent Areas Sound",
//                    R.raw.convent
//                )
//            )
//            homeitems.add(
//                Homeitemss(
//                    R.drawable.bgten,
//                    "Sea Side Relaxation",
//                    "Best Relaxation Sounds",
//                    R.raw.bells
//                )
//            )
//            homeitems.add(
//                Homeitemss(
//                    R.drawable.bgeleven,
//                    "Temple In Hills",
//                    "Temple Area Sound With Hills",
//                    R.raw.temple_in_the_hills
//                )
//            )
//            homeitems.add(
//                Homeitemss(
//                    R.drawable.bgtwelve,
//                    "Mystic Temple",
//                    "Temple Area Sound With Hills",
//                    R.raw.mystic_temple
//                )
//            )
//            homeitems.add(
//                Homeitemss(
//                    R.drawable.bgthirteen,
//                    "Scratching",
//                    "Scratching Sounds With Effects",
//                    R.raw.asmr_scratching
//                )
//            )
//            homeitems.add(
//                Homeitemss(
//                    R.drawable.bgfouteen,
//                    "Page Turning",
//                    "Page Turning With Effects",
//                    R.raw.asmr_page_turning
//                )
//            )
//            homeitems.add(
//                Homeitemss(
//                    R.drawable.bgfifteen,
//                    "Chewing",
//                    "Chewing Sound Effects",
//                    R.raw.asmr_chewing
//                )
//            )
//            homeitems.add(
//                Homeitemss(
//                    R.drawable.bgsixteen,
//                    "Whispering",
//                    "Whispering Sound Effects",
//                    R.raw.asmr_whispering
//                )
//            )
//            homeitems.add(
//                Homeitemss(
//                    R.drawable.bgseven,
//                    "Breathing",
//                    "Breathing Sound Effects",
//                    R.raw.asmr_breathing
//                )
//            )
//            homeitems.add(
//                Homeitemss(
//                    R.drawable.bgeighteen,
//                    "Crackling",
//                    "Crackling Sound Effect",
//                    R.raw.asmr_crackling
//                )
//            )
//            homeitems.add(
//                Homeitemss(
//                    R.drawable.bgnineteen,
//                    "Cat Purring",
//                    "Cat Purring with Effects",
//                    R.raw.asmr_cat_purring
//                )
//            )
//        }
//        musiclistitem.value = homeitems
//        return musiclistitem
//    }

    private fun getAudiolistone(): MutableLiveData<ArrayList<MusicItems>> {
        musicdata.add(MusicItems(R.drawable.flute, "Flute", R.raw.flutemusic))
        musicdata.add(MusicItems(R.drawable.gongbell, "Gong bell", R.raw.jsb))
        musicdata.add(MusicItems(R.drawable.guitar, "Guitar", R.raw.guitar))
        musicdata.add(MusicItems(R.drawable.indiansinger, "Indian Singers", R.raw.flutetwo))
        musicdata.add(MusicItems(R.drawable.piano, "Piano", R.raw.gue))
        musicdata.add(MusicItems(R.drawable.bell, "Bells", R.raw.flute))
        musicdata.add(MusicItems(R.drawable.wind, "Wind Chimes", R.raw.cwinds))
        musicdata.add(MusicItems(R.drawable.forestguitar, "Guitar in Forest", R.raw.fg))
        muiclist.value = musicdata
        return muiclist

    }

    private fun getAudiolisttwo() {
        musicdatatwo.add(MusicItems(R.drawable.water, "Water \n Tracking", R.raw.water))
        musicdatatwo.add(MusicItems(R.drawable.ocean, "Ocean \nWaves", R.raw.oceanwaves))
        musicdatatwo.add(MusicItems(R.drawable.bonefire, "Bone Fire lit", R.raw.bonefire))
        musicdatatwo.add(MusicItems(R.drawable.cafefire, "Cave Fire", R.raw.cvfire))
        musicdatatwo.add(MusicItems(R.drawable.softwind, "Soft Wind", R.raw.thunderw))
        musicdatatwo.add(MusicItems(R.drawable.breathing, "Breathing", R.raw.asmr_car_engine))
        musicdatatwo.add(MusicItems(R.drawable.autumnforest, "Autumn Forest", R.raw.hpbirds))
        musicdatatwo.add(MusicItems(R.drawable.riverstream, "Rivers Stream", R.raw.ocr))
        musicdatatwo.add(MusicItems(R.drawable.creek, "Creek", R.raw.wc))
        musicdatatwo.add(MusicItems(R.drawable.fire, "Fire", R.raw.fire))
        musicdatatwo.add(MusicItems(R.drawable.heaven, "Heaven", R.raw.hv))
        musicdatatwo.add(MusicItems(R.drawable.softthunder, "Soft\nThunder", R.raw.rn))
        musiclistone.value = musicdatatwo
    }

    private fun getAudiolisthree() {
        musicdatathree.add(MusicItems(R.drawable.koyal, "Koyal", R.raw.koyalbirds))
        musicdatathree.add(MusicItems(R.drawable.lovebirds, "Love Birds", R.raw.lovebirds))
        musicdatathree.add(MusicItems(R.drawable.peacock, "Peacock", R.raw.peacock))
        musicdatathree.add(MusicItems(R.drawable.birdsinging, "Birds Singing", R.raw.birdssinging))
        musicdatathree.add(MusicItems(R.drawable.birdsinging, "Airtel", R.raw.airtel))
        musicdatathree.add(MusicItems(R.drawable.crows, "Crows", R.raw.crows))
        musicdatathree.add(MusicItems(R.drawable.mulbirds, "Morning Birds", R.raw.morning_birds))
        musicdatathree.add(MusicItems(R.drawable.sweetpigeon, "Sweet Birds", R.raw.parrotsfly))

        musiclisttwo.value = musicdatathree
    }

    fun getInstancemediaplayerone(): MediaPlayer {
        if (MediaPlayers.mediaplayerone == null) {
            MediaPlayers.mediaplayerone = MediaPlayer()
        }
        return MediaPlayers.mediaplayerone as MediaPlayer
    }

    fun getInstancemediaplayertwo(): MediaPlayer {
        if (MediaPlayers.mediaplayertwo == null) {
            MediaPlayers.mediaplayertwo = MediaPlayer()
        }
        return MediaPlayers.mediaplayertwo as MediaPlayer
    }

    fun getInstancemediaplayerthree(): MediaPlayer {
        if (MediaPlayers.mediaPlayerthree == null) {
            MediaPlayers.mediaPlayerthree = MediaPlayer()
        }
        return MediaPlayers.mediaPlayerthree as MediaPlayer
    }

    fun getInstancemediaplayerfour(): MediaPlayer {
        if (MediaPlayers.mediaPlayerfour == null) {
            MediaPlayers.mediaPlayerfour = MediaPlayer()
        }
        return MediaPlayers.mediaPlayerfour as MediaPlayer
    }

    fun getInstancemediaplayerfive(): MediaPlayer {
        if (MediaPlayers.mediaPlayerfive == null) {
            MediaPlayers.mediaPlayerfive = MediaPlayer()
        }
        return MediaPlayers.mediaPlayerfive as MediaPlayer
    }

    fun getInstancemediaplayersix(): MediaPlayer {
        if (MediaPlayers.mediaPlayersix == null) {
            MediaPlayers.mediaPlayersix = MediaPlayer()
        }
        return MediaPlayers.mediaPlayersix as MediaPlayer
    }

    fun isPlaying(): Boolean {
        return getInstancemediaplayerone().isPlaying || getInstancemediaplayertwo().isPlaying || getInstancemediaplayerthree().isPlaying || getInstancemediaplayerfour().isPlaying || getInstancemediaplayerfive().isPlaying || getInstancemediaplayersix().isPlaying
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
            getInstancemediaplayerone().start()
            setStatus(PlaybackInfoListener.State.RESUMED)
            musicService.startForeground(
                MusicNotificationManager.NOTIFICATION_ID,
                mMusicNotificationManager!!.createNotification()
            )
        }
    }

    private fun pauseMediaPlayer() {
        setStatus(PlaybackInfoListener.State.PAUSED)
        getInstancemediaplayerone().pause()
        musicService.stopForeground(false)
        mMusicNotificationManager!!.notificationManager.notify(
            MusicNotificationManager.NOTIFICATION_ID,
            mMusicNotificationManager!!.createNotification()
        )
    }

    fun pausePlay() {
        if (getInstancemediaplayerone().isPlaying || getInstancemediaplayertwo().isPlaying || getInstancemediaplayerthree().isPlaying || getInstancemediaplayerfour().isPlaying || getInstancemediaplayerfive().isPlaying || getInstancemediaplayersix().isPlaying) {
            getInstancemediaplayerone().pause()
            getInstancemediaplayertwo().pause()
            getInstancemediaplayerthree().pause()
            getInstancemediaplayerfour().pause()
            getInstancemediaplayerfive().pause()
            getInstancemediaplayersix().pause()
            musicService.showServiceNotification(true)

        } else {
            musicService.showServiceNotification(false)
            getInstancemediaplayerone().start()
            getInstancemediaplayertwo().start()
            getInstancemediaplayerthree().start()
            getInstancemediaplayerfour().start()
            getInstancemediaplayerfive().start()
            getInstancemediaplayersix().start()

        }

    }

    fun exitApplication() {
        Log.e("TAG", "onReceive: enter", )
    }

}