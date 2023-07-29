package com.project.meditationsoundmixture.fragments

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.os.SystemClock
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.content.ContextCompat
import androidx.datastore.preferences.core.preferencesKey
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.project.meditationsoundmixture.BuildConfig
import com.project.meditationsoundmixture.Constants.isBackPressedCallback
import com.project.meditationsoundmixture.Extension.setDarkLightMode
import com.project.meditationsoundmixture.R
import com.project.meditationsoundmixture.databinding.FragmentSettingBinding
import com.project.meditationsoundmixture.datashare.DataShare
import com.project.meditationsoundmixture.ui.FeedbackScreen
import com.project.meditationsoundmixture.ui.LanguagesScreen
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch


class SettingFragment : Fragment() {
    lateinit var binding:FragmentSettingBinding
    lateinit var mContext: Context
    var mLastClickTime: Long = 0
     val MIN_CLICK_INTERVAL: Long = 800


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentSettingBinding.inflate(inflater, container, false)
        return binding.root

    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
         isBackPressedCallback = 0
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        mContext = context

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        requireActivity().setDarkLightMode()
        checkModeStatus()
        clickListner()
    }

    private fun checkModeStatus() {
        CoroutineScope(Dispatchers.Main).launch{
            DataShare.getInstance(mContext).getAllValue(
                preferencesKey("DarkLightMode"),false
            ).collect {
                when(it){
                    true ->{
                        binding.switchMode.isChecked = true
                    }
                    false ->{
                        binding.switchMode.isChecked = false
                    }
                }
            }
        }
    }

    private fun clickListner() {
        with(binding)
        {
            settingConsttwo.setOnClickListener {
                val intent=Intent(context,FeedbackScreen::class.java)
                startActivity(intent)

            }
            settingConstthree.setOnClickListener {
                val intent=Intent(context,LanguagesScreen::class.java)
                startActivity(intent)

            }
            switchMode.setOnCheckedChangeListener { buttonView, isChecked ->
                if (isChecked){
                    AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
                    lifecycleScope.launch {
                        DataShare.getInstance(mContext).setAllValue(
                            true, preferencesKey("DarkLightMode")
                        )
                    }
                }
                else{
                    AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
                    lifecycleScope.launch {
                        DataShare.getInstance(mContext).setAllValue(
                            false, preferencesKey("DarkLightMode")
                        )
                    }
                }
            }
            backSetting.setOnClickListener {
                findNavController().popBackStack()
            }
            settingConstfive.setOnClickListener {
                if (isClickQuickly()){
                }
                else{
                    shareApp()
                }
            }
        }
    }

    private fun shareApp() {
        try {
            val shareIntent = Intent(Intent.ACTION_SEND)
            shareIntent.type = "text/plain"
            shareIntent.putExtra(Intent.EXTRA_SUBJECT, "Call Announcer")
            var shareMessage = "\nLet me recommend you this application\n\n"
            shareMessage =
                """
                        ${shareMessage}https://play.google.com/store/apps/details?id=${BuildConfig.APPLICATION_ID}}
                        """.trimIndent()
            shareIntent.putExtra(Intent.EXTRA_TEXT, shareMessage)
            startActivity(Intent.createChooser(shareIntent, "choose one"))
        } catch (e: Exception) {
        }
    }

    private fun isClickQuickly() : Boolean {
        val currentClickTime= SystemClock.uptimeMillis()
        val elapsedTime=currentClickTime-mLastClickTime
        mLastClickTime=currentClickTime
        return elapsedTime < MIN_CLICK_INTERVAL
    }


}