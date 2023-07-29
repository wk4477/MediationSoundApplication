package com.project.meditationsoundmixture.util

import android.app.Activity
import android.content.Intent
import android.content.res.Configuration
import android.content.res.Resources
import android.os.Bundle
import android.util.DisplayMetrics
import android.util.Log
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.res.ResourcesCompat
import com.example.spanishspeakandtranslate.data.local.shared_pref.TinyDB
import com.google.android.material.snackbar.Snackbar
import com.google.android.play.core.appupdate.AppUpdateManager
import com.google.android.play.core.install.InstallStateUpdatedListener
import com.google.android.play.core.install.model.AppUpdateType
import com.google.android.play.core.install.model.InstallStatus
import com.google.android.play.core.install.model.UpdateAvailability
import com.project.meditationsoundmixture.Extension.setLanguage
import com.project.meditationsoundmixture.R
import com.project.meditationsoundmixture.ui.MainActivity
import java.util.*

open class BaseClass : AppCompatActivity() {

    private var appUpdateManager: AppUpdateManager? = null
    private var mainParent: View? = null
    private var MY_REQUEST_CODE = 100


    private val tinyDB by lazy {
        TinyDB(this)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setLocalizationAppStart()
    }

    fun setLocalizationAppStart() {
        val ok = tinyDB.getOutputCode("outputCodeKey")
        Log.d("TAGTiny", "onCreate: ${ok}")
        val myLocale = Locale(ok!!)
        Log.d("*****TAG", myLocale.toString())
        tinyDB.getString("int")

        val res: Resources = this.resources
        val dm: DisplayMetrics = res.displayMetrics
        val conf: Configuration = res.configuration
        conf.setLocale(myLocale)
        res.updateConfiguration(conf, dm)

    }

    fun switchLocale(activity: Activity?, code: String) {
        if (activity != null) {
            val myLocale = Locale(code)
            val res: Resources = activity.resources
            val dm: DisplayMetrics = res.displayMetrics
            val conf: Configuration = res.configuration
            conf.setLocale(myLocale)
            res.updateConfiguration(conf, dm)
            val intent = Intent(applicationContext, MainActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            startActivity(intent)
            finish()
            overridePendingTransition(0, 0)
            // activity.recreate()
        }
    }

    fun checkUpdate(parent: View) {
        mainParent = parent
        val appUpdateInfoTask = appUpdateManager?.appUpdateInfo
        appUpdateInfoTask?.addOnSuccessListener { appUpdateInfo ->
            if (appUpdateInfo.updateAvailability() == UpdateAvailability.UPDATE_AVAILABLE && appUpdateInfo.isUpdateTypeAllowed(
                    AppUpdateType.FLEXIBLE
                )
            ) {
                appUpdateManager?.startUpdateFlowForResult(
                    appUpdateInfo,
                    AppUpdateType.FLEXIBLE,
                    this,
                    MY_REQUEST_CODE
                )
            }
        }
    }

    private val listener: InstallStateUpdatedListener =
        InstallStateUpdatedListener { installState ->
            if (installState.installStatus() == InstallStatus.DOWNLOADED) {

                mainParent?.let { showSnackBarForCompleteUpdate(it) }
            }
        }

    private fun showSnackBarForCompleteUpdate(parent: View) {
        Snackbar.make(
            parent, getString(R.string.update_message),
            Snackbar.LENGTH_INDEFINITE
        ).apply {
            setAction(getString(R.string.restart)) {
                appUpdateManager?.completeUpdate()
            }
            setActionTextColor(ResourcesCompat.getColor(resources, R.color.primaryColorDark, theme))
        }.show()
    }

    override fun onDestroy() {
        super.onDestroy()
        appUpdateManager?.unregisterListener(listener)
    }

    override fun onResume() {
        super.onResume()

        appUpdateManager?.appUpdateInfo
            ?.addOnSuccessListener { appUpdateInfo ->
                if (appUpdateInfo.installStatus() == InstallStatus.DOWNLOADED) {
                    mainParent?.let { showSnackBarForCompleteUpdate(it) }
                }
            }
    }

}