package com.project.meditationsoundmixture.ui

import android.annotation.SuppressLint
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.os.SystemClock
import android.view.View
import com.example.spanishspeakandtranslate.data.local.shared_pref.TinyDB
import com.project.meditationsoundmixture.Constants.MIN_CLICK_INTERVAL
import com.project.meditationsoundmixture.Extension.SharedPref
import com.project.meditationsoundmixture.Extension.setDarkLightMode
import com.project.meditationsoundmixture.R
import com.project.meditationsoundmixture.ViewModel.MainViewModel
import com.project.meditationsoundmixture.databinding.ActivitySplashScreenBinding
import com.project.meditationsoundmixture.util.BaseClass
import org.koin.android.ext.android.inject
import java.util.*


@SuppressLint("CustomSplashScreen")
class SplashScreen : BaseClass() {
    var daycount = 0
    private var totalCount = 0
    private lateinit var names: Array<String>
    var mLastClickTime: Long = 0

    private val binding by lazy {
        ActivitySplashScreenBinding.inflate(layoutInflater)
    }

    private val tinyDB by lazy {
        TinyDB(this)
    }

    private var sharedPref: SharedPref? = null
    private val mainViewModel: MainViewModel by inject()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        setupVideoView()
        setLocalizationAppStart()
        names = resources.getStringArray(R.array.quotations)
        val randomIndex = Random().nextInt(names.size)
        val randomName = names[randomIndex]

        sharedPref = SharedPref(this)

        totalCount = sharedPref?.getInteger("TotalCount")!!
      //  shortToast("${totalCount}")
        totalCount++

        sharedPref?.putInteger("TotalCount", totalCount)
       // shortToast("${totalCount}")
        Handler(Looper.getMainLooper()).postDelayed({
            binding.quotationTxt.visibility = View.VISIBLE
            binding.quotationTxt.text = randomName
            binding.btnSplash.visibility = View.VISIBLE
        }, 5000)

        /* mainViewModel = ViewModelProvider(this).get(AddAlarmViewModel::class.java)*/
        val calendar: Calendar = Calendar.getInstance()
        val day: Int = calendar.get(Calendar.DAY_OF_WEEK)
        when (day) {
            Calendar.SUNDAY -> {

                sharedPref?.putInteger("Sunday", 1)

            }
            Calendar.MONDAY -> {
                sharedPref?.putInteger("Monday", 1)


            }
            Calendar.TUESDAY -> {
                sharedPref?.putInteger("Tuesday", 1)


            }
            Calendar.WEDNESDAY -> {
                sharedPref?.putInteger("Wednesday", 1)

            }
            Calendar.THURSDAY -> {
                sharedPref?.putInteger("Thursday", 1)


            }
            Calendar.FRIDAY -> {
                sharedPref?.putInteger("Friday", 1)

            }
            Calendar.SATURDAY -> {
                sharedPref?.putInteger("Saturday", 1)

            }

        }

        binding.btnSplash.setOnClickListener {
            //Toast.makeText(this@SplashScreen, "heelo", Toast.LENGTH_SHORT).show()
            if (!isClickMultiple()){
                daycount = 1
                val intent = Intent(this, MainActivity::class.java)
                startActivity(intent)
            }
        }
    }

    override fun onResume() {
        super.onResume()
        this@SplashScreen.setDarkLightMode()
        binding.videoViewID.start()
    }

    private fun isClickMultiple() : Boolean {
        val currentClickTime= SystemClock.uptimeMillis()
        val elapsedTime=currentClickTime-mLastClickTime
        mLastClickTime=currentClickTime
        return elapsedTime < MIN_CLICK_INTERVAL
    }

    private fun setupVideoView() {
        val path = "android.resource://" + packageName + "/" + R.raw.sequencenew
        binding.videoViewID.setVideoURI(Uri.parse(path))


        val isPlaying = binding.videoViewID.isPlaying


        if (isPlaying) {
            binding.videoViewID.pause()
        } else {
            binding.videoViewID.start()
        }
    }


}