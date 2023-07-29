package com.project.meditationsoundmixture

import android.support.v4.media.session.PlaybackStateCompat

object Constants {

    const val SESSION_NUMBER_1 = 1
    const val SESSION_START_TIME_1 = "Music"
    const val SESSION_END_TIME_1 = ""
    const val PLAYING = PlaybackStateCompat.STATE_PLAYING
    const val PAUSED = PlaybackStateCompat.STATE_PAUSED
    const val RESUMED = PlaybackStateCompat.STATE_NONE
    const val SESSION_NUMBER_2 = 2
    const val SESSION_START_TIME_2 = "Birds"
    const val SESSION_END_TIME_2 = ""

    const val SESSION_NUMBER_3 = 3
    const val SESSION_START_TIME_3 = "Nature"
    const val SESSION_END_TIME_3 = ""
    const val PREV_ACTION = "PREV"
    const val PLAY_PAUSE_ACTION = "PLAY_PAUSE"
    const val NEXT_ACTION = "NEXT"
    const val SESSION_NUMBER_4 = 4
    const val SESSION_START_TIME_4 = "Others"
    const val SESSION_END_TIME_4 = ""
    const val LANGUAGE_CODE = "LanguageCode"
    const val SHARED_PREFERENCES_NAME = "Meditation Sound"
    var mLastClickTime: Long = 0
    var languageSelected :String = ""
    var isHomeSelected : ((Boolean) ->Unit)? = null
    var isCloseService : ((Boolean) ->Unit)? = null
    const val MIN_CLICK_INTERVAL: Long = 900

    var isBackPressedCallback = 0
}