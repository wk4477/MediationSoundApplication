package com.project.meditationsoundmixture.Extension

import android.app.Activity
import android.content.Context
import android.content.SharedPreferences
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.media.MediaPlayer
import android.net.Uri
import android.os.Handler
import android.os.Looper
import android.os.SystemClock
import android.view.View
import android.view.WindowManager
import android.widget.Toast
import androidx.appcompat.app.AppCompatDelegate
import androidx.datastore.preferences.core.preferencesKey
import com.project.meditationsoundmixture.Constants.LANGUAGE_CODE
import com.project.meditationsoundmixture.Constants.SHARED_PREFERENCES_NAME
import com.project.meditationsoundmixture.Constants.mLastClickTime
import com.project.meditationsoundmixture.datashare.DataShare
import com.project.meditationsoundmixture.model.ChangeLanguage
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.io.IOException
import java.io.InputStream
import java.net.HttpURLConnection
import java.net.URL
import java.util.*


var toast: Toast? = null
fun Context.shortToast(msg: String) {
    toast?.cancel()
    toast = Toast.makeText(this, msg, Toast.LENGTH_SHORT)
    toast?.show()
}
fun View.onSingleClick(debounceTime: Long = 1200L, action: ((View)) -> Unit) {
    this.setOnClickListener(object : View.OnClickListener {
        private var lastClickTime: Long = 0
        override fun onClick(v: View) {
            if (SystemClock.elapsedRealtime() - lastClickTime < debounceTime) return
            else action(this@onSingleClick)
            lastClickTime = SystemClock.elapsedRealtime()
        }
    })
}
fun getBitmapFromURL(strURL: String?): Bitmap? {
    return try {
        val url = URL(strURL)
        val connection: HttpURLConnection = url.openConnection() as HttpURLConnection
        connection.setDoInput(true)
        connection.connect()
        val input: InputStream = connection.getInputStream()
        BitmapFactory.decodeStream(input)
    } catch (e: IOException) {
        e.printStackTrace()
        null
    }
}
fun Int.getUri(context: Context): Uri? {
    val mediaPath: Uri? =
        Uri.parse("android.resource://" + context.packageName.toString() + "/" + this)
    return mediaPath
}

inline fun MediaPlayer.playMediaPlayer(context: Context, songId: Int, onStart: ((String) -> Unit)) {

    this.reset()
    songId.getUri(context)?.let { setDataSource(context, it) }
    prepare()
    start().apply {
        onStart.invoke("Playing")
    }

}

fun Context.disableMultiClick(view: View) {

    view.isClickable = false
    Looper.myLooper()?.let {
        Handler(it).postDelayed({
            view.isClickable = true
        }, 1500)
    }
}

fun Activity.setDarkLightMode(){
    CoroutineScope(Dispatchers.Main).launch{
        DataShare.getInstance(this@setDarkLightMode).getAllValue(
            preferencesKey("DarkLightMode"),false
        ).collect {
            window.decorView.setSystemUiVisibility(
                View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                        or View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                        or View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                        or View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                        or View.SYSTEM_UI_FLAG_FULLSCREEN
                        or View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY)
            window.setFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS, WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS)
            when(it){
                true ->{
                    AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
                }
                false ->{
                    AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
                }
            }
        }
    }
}


fun Context.getLanguage(): String {
    val systemDefault = Locale.getDefault().language
    //Logger.debug("systemDefault", systemDefault)
    return getSharedPreferences().getString(LANGUAGE_CODE, systemDefault)!!
}
fun Context.setLanguage(languageCode: String) {
    getSharedPreferences().edit().putString(LANGUAGE_CODE, languageCode)
        .apply()
}

fun Context.getSharedPreferences(): SharedPreferences {
    return getSharedPreferences(SHARED_PREFERENCES_NAME, Context.MODE_PRIVATE)
}

fun disableClick(): Boolean {
    if (SystemClock.elapsedRealtime() - mLastClickTime < 1000) {
        return false
    }
    mLastClickTime = SystemClock.elapsedRealtime()
    return true
}

fun Context.setLanguageCode(languageCode: String) {
    getSharedPreferences().edit().putString(LANGUAGE_CODE, languageCode)
        .apply()
}

private var prefs: SharedPreferences? = null



