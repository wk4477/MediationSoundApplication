package com.project.meditationsoundmixture.ui

import android.app.Dialog
import android.content.Context
import android.content.SharedPreferences
import android.media.AudioManager
import android.media.ToneGenerator.MAX_VOLUME
import android.os.Bundle
import android.os.CountDownTimer
import android.os.Handler
import android.util.Log
import android.view.KeyEvent
import android.view.View
import android.view.Window
import android.widget.ImageView
import android.widget.SeekBar
import android.widget.SeekBar.OnSeekBarChangeListener
import androidx.appcompat.app.AppCompatActivity
import androidx.datastore.preferences.core.preferencesKey
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager.widget.ViewPager
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.project.meditationsoundmixture.*
import com.project.meditationsoundmixture.Constants.SESSION_END_TIME_1
import com.project.meditationsoundmixture.Constants.SESSION_END_TIME_2
import com.project.meditationsoundmixture.Constants.SESSION_END_TIME_3
import com.project.meditationsoundmixture.Constants.SESSION_NUMBER_1
import com.project.meditationsoundmixture.Constants.SESSION_NUMBER_2
import com.project.meditationsoundmixture.Constants.SESSION_NUMBER_3
import com.project.meditationsoundmixture.Constants.SESSION_START_TIME_1
import com.project.meditationsoundmixture.Constants.SESSION_START_TIME_2
import com.project.meditationsoundmixture.Constants.SESSION_START_TIME_3
import com.project.meditationsoundmixture.Dialog.DialogRecyclerViewAdapter
import com.project.meditationsoundmixture.Extension.disableMultiClick
import com.project.meditationsoundmixture.Extension.onSingleClick
import com.project.meditationsoundmixture.Extension.setDarkLightMode
import com.project.meditationsoundmixture.Extension.shortToast
import com.project.meditationsoundmixture.MediationMenu.Adapter.MeditationMenuAdapter
import com.project.meditationsoundmixture.ViewModel.MainViewModel
import com.project.meditationsoundmixture.adapter.MeditationSoundAdapter
import com.project.meditationsoundmixture.adapter.SliderAdapter
import com.project.meditationsoundmixture.adapter.playMediaPlayer
import com.project.meditationsoundmixture.databinding.ActivityMediationSoundBinding
import com.project.meditationsoundmixture.databinding.DialougseekbarBinding
import com.project.meditationsoundmixture.datastore.DataShare
import com.project.meditationsoundmixture.model.MusicItems
import com.project.meditationsoundmixture.model.SliderItems
import io.github.luizgrp.sectionedrecyclerviewadapter.SectionedRecyclerViewAdapter
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Runnable
import kotlinx.coroutines.launch
import org.koin.android.ext.android.inject
import java.util.*
import java.util.concurrent.TimeUnit
import kotlin.math.ln


class MediationSound : AppCompatActivity(), SlotSection.ClickListener,
    MeditationSoundAdapter.OnItemClickListener {
    lateinit var imageUrl: ArrayList<SliderItems>
    lateinit var sliderView: ViewPager
    lateinit var sliderAdapter: SliderAdapter
    lateinit var sharedPreferences: SharedPreferences

    /*   lateinit var timer: CountDownTimer*/
    lateinit var dialog: BottomSheetDialog
    lateinit var dialogss: BottomSheetDialog
    private var countDownTimer: CountDownTimer? = null
    private lateinit var dialogs: Dialog
    var alreadyAdd = 0
    var imageone: Int? = null
    private var sectionedAdapter: SectionedRecyclerViewAdapter? = null
   private var slotModelArrayList1: ArrayList<MusicItems> = ArrayList()
  private  var slotModelArrayList2: ArrayList<MusicItems> = ArrayList()
   private var slotModelArrayList3: ArrayList<MusicItems> = ArrayList()
    lateinit var dialogRecyclerViewAdapter: DialogRecyclerViewAdapter
    private val mainViewModel: MainViewModel by inject()
    private var sessionModelArrayList: ArrayList<SessionModel> = ArrayList()
    var pos = 0
    lateinit var courseRV: RecyclerView
    lateinit var courseRVAdapter: MeditationMenuAdapter
    lateinit var courseList: ArrayList<MusicItems>
    private var dialogMainBinding: DialougseekbarBinding? = null
    private var dialogone: Dialog? = null
    lateinit var runnable: Runnable
    lateinit var runnableone: java.lang.Runnable
    lateinit var mHandler: Handler
    lateinit var mHandlerone: Handler
    var isenabled = false
    lateinit var audioManager: AudioManager
    lateinit var audioManagerone: AudioManager
    var seekBarProgressix: Int = 0
    var seekBarProgressfive: Int = 0
    var seekBarProgressfour: Int = 0
    var seekBarProgressthree: Int = 0
    var seekBarProgresstwo: Int = 0
    var seekBarProgressone: Int = 0
    var isplaying = false
    var currVolume = 0
    var maxVolume = 0

    companion object {
        var arrayd: ArrayList<MusicItems> = ArrayList()
        var arraynew: ArrayList<MusicItems> = ArrayList()
        lateinit var soundAdapter: MeditationSoundAdapter
        lateinit var binding: ActivityMediationSoundBinding
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        requestWindowFeature(Window.FEATURE_NO_TITLE)
        super.onCreate(savedInstanceState)
        binding = ActivityMediationSoundBinding.inflate(layoutInflater)
        setContentView(binding.root)


        sharedPreferences = getSharedPreferences("Mypref", Context.MODE_PRIVATE)
        mHandler = Handler()
        audioManager = getSystemService(AUDIO_SERVICE) as AudioManager
         maxVolume = audioManager.getStreamMaxVolume(AudioManager.STREAM_MUSIC)
        binding.seekbar.max = maxVolume

        currVolume = audioManager.getStreamVolume(AudioManager.STREAM_MUSIC)
        binding.seekbar.progress = currVolume

        binding.seekbar.setOnSeekBarChangeListener(object : OnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: SeekBar, i: Int, b: Boolean) {
                audioManager.setStreamVolume(
                    AudioManager.STREAM_MUSIC,
                    i,
                    0
                )
            }

            override fun onStartTrackingTouch(seekBar: SeekBar) {

            }

            override fun onStopTrackingTouch(seekBar: SeekBar) {

            }
        })
        binding.addVol.setOnClickListener {
            if (mainViewModel.isPlaying()) {
                countDownTimer?.cancel()
                mainViewModel.pausePlay()
                mainViewModel.getInstancemediaplayerone().pause()
                binding.addVol.setImageResource(R.drawable.ic_playbtn)
            } else {
                setTimer(remainingTimer)
                mainViewModel.pausePlay()
                mainViewModel.getInstancemediaplayerone().start()
                binding.addVol.setImageResource(R.drawable.icpausenew)
            }
        }
        mainViewModel.stateofMediaplayer.observe(this, androidx.lifecycle.Observer {
            if (isplaying) {
                binding.addVol.setImageResource(R.drawable.icpausenew)
                mainViewModel.pausePlay()
                // shortToast("helloworld")
            } else {
                //shortToast("alpha123")
                binding.addVol.setImageResource(R.drawable.ic_playbtn)
            }

        })
        mainViewModel.optionItemClick = { value: Int ->
            binding.addVol.setImageResource(R.drawable.ic_playbtn)
        }
        mainViewModel.optionItemClick2 = { value: Int ->
            binding.addVol.setImageResource(R.drawable.ic_pause)
        }
        setupDummyData()
        slotModelArrayList1 = mainViewModel.muiclist.value!!
        slotModelArrayList2 = mainViewModel.musiclisttwo.value!!
        slotModelArrayList3 = mainViewModel.musiclistone.value!!
        Log.d("ade", "onCreate: " + slotModelArrayList1.size + "" + slotModelArrayList2.size)

        // shortToast(arrayd.size.toString())
        Log.d("_alpha", "onCreate: " + arrayd.size.toString())
        setRecyclerData()
        allClickListener()
        loadTimerTime()

    }

    private fun loadTimerTime() {
        val systemTime = System.currentTimeMillis()
        val saveTime = sharedPreferences.getLong("SaveTimeMili", 0L)
        if (systemTime > saveTime) {
            binding.timeText.text = "00:00"
        } else {
            val time = saveTime - systemTime
            Log.e("timeselected", "onCreate: ${time}")
    //            val strTime = String.format("%02d min, %02d sec",
    //                TimeUnit.MILLISECONDS.toMinutes(time),
    //                TimeUnit.MILLISECONDS.toSeconds(time) -
    //                        TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(time))
    //            )
    //
    //            Log.e("timeselected", "onCreate: ${strTime}", )
            if (saveTime == 0L) {
            } else {
                //binding.timeText.text = strTime
                setTimer(time)
            }
        }
    }

    override fun onResume() {
        super.onResume()
        this@MediationSound.setDarkLightMode()
    }
    private fun allClickListener() {
        with(binding)
        {
            sliderView = findViewById(R.id.slider)
            sliderView.offscreenPageLimit = 5
            imageUrl = ArrayList()
            val tim = intent.getStringArrayExtra("med")
            Log.d("_ha", "onCreate: " + tim)
            addconstsound.onSingleClick {
                bottomSheetDialog()
            }
            imgBackAr.setOnClickListener {
                onBackPressed()
            }
            selectTimer.setOnClickListener {
              //  isFirstTime = true
               // stopCounter()
                binding.timeClock.visibility = View.VISIBLE
             //   countDownTimer?.cancel()
                val timePicker = MyTimePicker()
                timePicker.setTitle("Select time")
                //timePicker.includeHours = false
                timePicker.setOnTimeSetOption("Set time") { minute, second ->

                    val alpha: Long = minute * 60000 + second * 1000.toLong()
                    // binding.timeClock.visibility = View.GONE
                    val system = System.currentTimeMillis()
                    Log.e("timeselected", "SYSTEM: ${system}")
                    Log.e("timeselected", "onTick: ${alpha}")
                    val total  = system + alpha
                    Log.e("timeselected", "secondDuration: ${total}")
                    val editor: SharedPreferences.Editor = sharedPreferences.edit()
                    editor.putLong("SaveTimeMili",total)
                    editor.apply()
                    setTimer(alpha)
                }
                timePicker.show(supportFragmentManager, "time_picker")
            }
            sliderDownItems()

        }
    }

    private fun stopCounter() {
        if (countDownTimer != null) {
            countDownTimer?.cancel()
        }
    }

    var remainingTimer:Long=0
    //var isFirstTime: Boolean = false
    private fun setTimer(duration: Long) {
        countDownTimer?.cancel()
        countDownTimer = object : CountDownTimer(duration, 1000) {
            override fun onTick(millisUntilFinished: Long) {

//                if (isFirstTime) {
//                    cancel()
//                    isFirstTime = false
//                }
                remainingTimer = millisUntilFinished
                Log.e("TAG", "onTick: ${millisUntilFinished}", )
                val secondDuration = String.format(
                    Locale.ENGLISH, "%02d : %02d",
                    TimeUnit.MILLISECONDS.toMinutes(millisUntilFinished),
                    TimeUnit.MILLISECONDS.toSeconds(millisUntilFinished) -
                            TimeUnit.MINUTES.toSeconds(
                                TimeUnit.MILLISECONDS.toMinutes(
                                    millisUntilFinished
                                )
                            )
                )
              //  binding.timeText.text = secondDuration
                lifecycleScope.launch {
                    DataShare.getInstance(this@MediationSound).getAllValue(
                        preferencesKey("addTimer"),"0:0"
                    ).collect{
                        Log.e("TAG", "onTick: ${it}")
                        if (it.isEmpty() || it == "0:0"){
                            binding.timeText.text = "Timer"
                            val editor: SharedPreferences.Editor = sharedPreferences.edit()
                            editor.putLong("SaveTimeMili",0L)
                            editor.apply()
                        }
                        else{

                            if (sharedPreferences.getBoolean("switchValue",false)){
                                binding.timeText.text = secondDuration
                            }
//                            DataShare.getInstance(this@MediationSound).getAllValue(
//                                preferencesKey("switchValue"),false
//                            ).collect{
//                                if (it){
//                                    Log.e("TAG", "onTickSwitch: ${it}", )
//                                    binding.timeText.text = secondDuration
//                                }
//                                else{
//                                    Log.e("TAG", "onTickSwitch: ${it}", )
//                                }
//                            }
                        }
                    }
                }
            }

            override fun onFinish() {
                if (mainViewModel.getInstancemediaplayerone().isPlaying ||
                    mainViewModel.getInstancemediaplayertwo().isPlaying ||
                    mainViewModel.getInstancemediaplayerthree().isPlaying ||
                    mainViewModel.getInstancemediaplayerfour().isPlaying ||
                    mainViewModel.getInstancemediaplayerfive().isPlaying ||
                    mainViewModel.getInstancemediaplayersix().isPlaying) {
                    mainViewModel.getInstancemediaplayerone().pause()
                    mainViewModel.getInstancemediaplayertwo().pause()
                    mainViewModel.getInstancemediaplayerthree().pause()
                    mainViewModel.getInstancemediaplayerfour().pause()
                    mainViewModel.getInstancemediaplayerfive().pause()
                    mainViewModel.getInstancemediaplayersix().pause()
                    binding.addVol.setImageResource(R.drawable.ic_playbtn)
                }
                binding.timeClock.visibility = View.VISIBLE
                lifecycleScope.launch {
                    DataShare.getInstance(this@MediationSound).setAllValue(
                        "0:0",
                        preferencesKey("addTimer")
                    )
                }

//                lifecycleScope.launch {
//                    DataShare.getInstance(this@MediationSound).setAllValue(
//                        false, preferencesKey("switchValue")
//                    )
//                }
                val editor: SharedPreferences.Editor = sharedPreferences.edit()
                editor.putLong("SaveTimeMili",0L)
                editor.putBoolean("switchValue",false)
                editor.apply()
                binding.timeText.text = "Timer"
                countDownTimer?.cancel()
               // isFirstTime = true
            }


        }
        (countDownTimer as CountDownTimer).start()

//        lifecycleScope.launch {
//            DataShare.getInstance(this@MediationSound).getAllValue(preferencesKey("switchValue"),false)
//                .collect{
//                    if (!it){
//                        countDownTimer?.onFinish()
//                    }
//            }
//        }

    }

    private fun seekbarDialog() {
        dialogMainBinding = DialougseekbarBinding.inflate(layoutInflater)
        dialogone = Dialog(this@MediationSound, R.style.CustomAlertDialog)
        dialogMainBinding?.root?.let { dialogone?.setContentView(it)}
        dialogMainBinding?.seekBarAdaptersseven?.max = maxVolume
        dialogMainBinding?.seekBarAdaptersseven?.progress = currVolume
        Log.e("TAG", "seekbarDialog: ${dialogMainBinding?.seekBarAdaptersseven?.max}", )
        Log.e("TAG", "seekbarDialog: ${dialogMainBinding?.seekBarAdaptersseven?.progress}", )
        dialogone!!.setOnKeyListener { dialog, keyCode, event ->
            volumeUpDown(event)
            super.onKeyDown(keyCode, event)
        }
        dialogone!!.setCancelable(false)
        loadData()
        updateViews()
        dialogMainBinding?.addSongs?.onSingleClick {
            dialogone?.dismiss()
            bottomSheetDialog()
        }
        dialogMainBinding?.seekBarAdaptersseven?.setOnSeekBarChangeListener(object :
            OnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: SeekBar, i: Int, b: Boolean) {
                audioManager.setStreamVolume(
                    AudioManager.STREAM_MUSIC,
                    i,
                    0
                )
            }

            override fun onStartTrackingTouch(seekBar: SeekBar) {

            }

            override fun onStopTrackingTouch(seekBar: SeekBar) {

            }
        })
        dialogMainBinding?.crossbtn?.onSingleClick {
            dialogone?.dismiss()

        }
//        dialogMainBinding?.delone?.setOnClickListener {
//            if (mainViewModel.musicchange.size == 1)
//                dialogMainBinding?.constLysix?.visibility = View.GONE
//            mainViewModel.musicchange.clear()
//            arraynew.clear()
//            soundAdapter.notifyDataSetChanged()
//        }
        if (mainViewModel.homeviewpageritems.size == 1 || mainViewModel.positonview == 0) {
            setSeekbarClick(0)
        }
        else if (mainViewModel.homeviewpageritems.size == 2 || mainViewModel.positonview == 1) {
           // dialogMainBinding?.seekBarAdaptersfour?.max = 50
            setSeekbarClick(1)
//            dialogMainBinding?.imgseekbarfour?.setImageResource(mainViewModel.homeviewpageritems.get(1).viewimage)
//            dialogMainBinding?.seekBarAdaptersfour?.setOnSeekBarChangeListener(object :
//                OnSeekBarChangeListener {
//                var p: Int = 0
//                override fun onProgressChanged(p0: SeekBar?, p1: Int, p2: Boolean) {
//                    p = p1
//
//                    val log1 = (Math.log(50.0 - p1) / Math.log(50.0)).toFloat()
//                    mainViewModel.getInstancemediaplayerone().setVolume(1 - log1, 1 - log1)
//                    mainViewModel.getInstancemediaplayerone().seekTo(p1)
//
//
//                }
//
//                override fun onStartTrackingTouch(p0: SeekBar?) {
//
//                }
//
//                override fun onStopTrackingTouch(p0: SeekBar?) {
//
//                }
//
//            })

        }
        else if (mainViewModel.homeviewpageritems.size == 3 || mainViewModel.positonview == 2) {
            setSeekbarClick(2)
//            dialogMainBinding?.imgseekbarfour?.setImageResource(
//                mainViewModel.homeviewpageritems.get(
//                    2
//                ).viewimage
//            )
//            dialogMainBinding?.seekBarAdaptersfour?.setOnSeekBarChangeListener(object :
//                OnSeekBarChangeListener {
//                var p: Int = 0
//                override fun onProgressChanged(p0: SeekBar?, p1: Int, p2: Boolean) {
//                    p = p1
//
//                    val log1 = (Math.log(50.0 - p1) / Math.log(50.0)).toFloat()
//                    mainViewModel.getInstancemediaplayerone().setVolume(1 - log1, 1 - log1)
//                    mainViewModel.getInstancemediaplayerone().seekTo(p1)
//
//
//                }
//
//                override fun onStartTrackingTouch(p0: SeekBar?) {
//
//                }
//
//                override fun onStopTrackingTouch(p0: SeekBar?) {
//
//                }
//
//            })

        }
        else if (mainViewModel.homeviewpageritems.size == 4 || mainViewModel.positonview == 3) {
            setSeekbarClick(3)
//            dialogMainBinding?.imgseekbarfour?.setImageResource(
//                mainViewModel.homeviewpageritems.get(
//                    3
//                ).viewimage
//            )
//            dialogMainBinding?.seekBarAdaptersfour?.setOnSeekBarChangeListener(object :
//                OnSeekBarChangeListener {
//                var p: Int = 0
//                override fun onProgressChanged(p0: SeekBar?, p1: Int, p2: Boolean) {
//                    p = p1
//
//                    val log1 = (Math.log(50.0 - p1) / Math.log(50.0)).toFloat()
//                    mainViewModel.getInstancemediaplayerone().setVolume(1 - log1, 1 - log1)
//                    mainViewModel.getInstancemediaplayerone().seekTo(p1)
//
//
//                }
//
//                override fun onStartTrackingTouch(p0: SeekBar?) {
//
//                }
//
//                override fun onStopTrackingTouch(p0: SeekBar?) {
//
//                }
//
//            })

        }
        else if (mainViewModel.homeviewpageritems.size == 5 && mainViewModel.positonview == 4) {
            setSeekbarClick(4)
//            dialogMainBinding?.imgseekbarfour?.setImageResource(
//                mainViewModel.homeviewpageritems.get(
//                    4
//                ).viewimage
//            )
//            dialogMainBinding?.seekBarAdaptersfour?.setOnSeekBarChangeListener(object :
//                OnSeekBarChangeListener {
//                var p: Int = 0
//                override fun onProgressChanged(p0: SeekBar?, p1: Int, p2: Boolean) {
//                    p = p1
//
//                    val log1 = (Math.log(50.0 - p1) / Math.log(50.0)).toFloat()
//                    mainViewModel.getInstancemediaplayerone().setVolume(1 - log1, 1 - log1)
//                    mainViewModel.getInstancemediaplayerone().seekTo(p1)
//
//
//                }
//
//                override fun onStartTrackingTouch(p0: SeekBar?) {
//
//                }
//
//                override fun onStopTrackingTouch(p0: SeekBar?) {
//
//                }
//
//            })

        }
        else if (mainViewModel.homeviewpageritems.size == 6 && mainViewModel.positonview == 5) {
            setSeekbarClick(5)
//            dialogMainBinding?.imgseekbarfour?.setImageResource(
//                mainViewModel.homeviewpageritems.get(
//                    5
//                ).viewimage
//            )
//            dialogMainBinding?.seekBarAdaptersfour?.setOnSeekBarChangeListener(object :
//                OnSeekBarChangeListener {
//                var p: Int = 0
//                override fun onProgressChanged(p0: SeekBar?, p1: Int, p2: Boolean) {
//                    p = p1
//
//                    val log1 = (Math.log(50.0 - p1) / Math.log(50.0)).toFloat()
//                    mainViewModel.getInstancemediaplayerone().setVolume(1 - log1, 1 - log1)
//                    mainViewModel.getInstancemediaplayerone().seekTo(p1)
//
//
//                }
//
//                override fun onStartTrackingTouch(p0: SeekBar?) {
//
//                }
//
//                override fun onStopTrackingTouch(p0: SeekBar?) {
//
//                }
//
//            })

        }
        else if (mainViewModel.homeviewpageritems.size == 7 && mainViewModel.positonview == 6) {
            setSeekbarClick(6)
//            dialogMainBinding?.imgseekbarfour?.setImageResource(
//                mainViewModel.homeviewpageritems.get(
//                    6
//                ).viewimage
//            )
//            dialogMainBinding?.seekBarAdaptersfour?.setOnSeekBarChangeListener(object :
//                OnSeekBarChangeListener {
//                var p: Int = 0
//                override fun onProgressChanged(p0: SeekBar?, p1: Int, p2: Boolean) {
//                    p = p1
//
//                    val log1 = (Math.log(50.0 - p1) / Math.log(50.0)).toFloat()
//                    mainViewModel.getInstancemediaplayerone().setVolume(1 - log1, 1 - log1)
//                    mainViewModel.getInstancemediaplayerone().seekTo(p1)
//
//
//                }
//
//                override fun onStartTrackingTouch(p0: SeekBar?) {
//
//                }
//
//                override fun onStopTrackingTouch(p0: SeekBar?) {
//
//                }
//
//            })

        }
        else if (mainViewModel.homeviewpageritems.size == 8 && mainViewModel.positonview == 7) {
            setSeekbarClick(7)
//            dialogMainBinding?.imgseekbarfour?.setImageResource(
//                mainViewModel.homeviewpageritems.get(
//                    7
//                ).viewimage
//            )
//            dialogMainBinding?.seekBarAdaptersfour?.setOnSeekBarChangeListener(object :
//                OnSeekBarChangeListener {
//                var p: Int = 0
//                override fun onProgressChanged(p0: SeekBar?, p1: Int, p2: Boolean) {
//                    p = p1
//
//                    val log1 = (Math.log(50.0 - p1) / Math.log(50.0)).toFloat()
//                    mainViewModel.getInstancemediaplayerone().setVolume(1 - log1, 1 - log1)
//                    mainViewModel.getInstancemediaplayerone().seekTo(p1)
//
//
//                }
//
//                override fun onStartTrackingTouch(p0: SeekBar?) {
//
//                }
//
//                override fun onStopTrackingTouch(p0: SeekBar?) {
//
//                }
//
//            })

        }
        else if (mainViewModel.homeviewpageritems.size == 9 && mainViewModel.positonview == 8) {
            setSeekbarClick(8)
//            dialogMainBinding?.imgseekbarfour?.setImageResource(
//                mainViewModel.homeviewpageritems.get(
//                    8
//                ).viewimage
//            )
//            dialogMainBinding?.seekBarAdaptersfour?.setOnSeekBarChangeListener(object :
//                OnSeekBarChangeListener {
//                var p: Int = 0
//                override fun onProgressChanged(p0: SeekBar?, p1: Int, p2: Boolean) {
//                    p = p1
//
//                    val log1 = (Math.log(50.0 - p1) / Math.log(50.0)).toFloat()
//                    mainViewModel.getInstancemediaplayerone().setVolume(1 - log1, 1 - log1)
//                    mainViewModel.getInstancemediaplayerone().seekTo(p1)
//
//
//                }
//
//                override fun onStartTrackingTouch(p0: SeekBar?) {
//
//                }
//
//                override fun onStopTrackingTouch(p0: SeekBar?) {
//
//                }
//
//            })

        }
        else if (mainViewModel.homeviewpageritems.size == 10 && mainViewModel.positonview == 9) {
            setSeekbarClick(9)
//            dialogMainBinding?.imgseekbarfour?.setImageResource(
//                mainViewModel.homeviewpageritems.get(
//                    9
//                ).viewimage
//            )
//            dialogMainBinding?.seekBarAdaptersfour?.setOnSeekBarChangeListener(object :
//                OnSeekBarChangeListener {
//                var p: Int = 0
//                override fun onProgressChanged(p0: SeekBar?, p1: Int, p2: Boolean) {
//                    p = p1
//
//                    val log1 = (Math.log(50.0 - p1) / Math.log(50.0)).toFloat()
//                    mainViewModel.getInstancemediaplayerone().setVolume(1 - log1, 1 - log1)
//                    mainViewModel.getInstancemediaplayerone().seekTo(p1)
//
//
//                }
//
//                override fun onStartTrackingTouch(p0: SeekBar?) {
//
//                }
//
//                override fun onStopTrackingTouch(p0: SeekBar?) {
//
//                }
//
//            })

        }
        else if (mainViewModel.homeviewpageritems.size == 11 && mainViewModel.positonview == 10) {
            setSeekbarClick(10)
//            dialogMainBinding?.imgseekbarfour?.setImageResource(
//                mainViewModel.homeviewpageritems.get(
//                    10
//                ).viewimage
//            )
//            dialogMainBinding?.seekBarAdaptersfour?.setOnSeekBarChangeListener(object :
//                OnSeekBarChangeListener {
//                var p: Int = 0
//                override fun onProgressChanged(p0: SeekBar?, p1: Int, p2: Boolean) {
//                    p = p1
//
//                    val log1 = (Math.log(50.0 - p1) / Math.log(50.0)).toFloat()
//                    mainViewModel.getInstancemediaplayerone().setVolume(1 - log1, 1 - log1)
//                    mainViewModel.getInstancemediaplayerone().seekTo(p1)
//
//
//                }
//
//                override fun onStartTrackingTouch(p0: SeekBar?) {
//
//                }
//
//                override fun onStopTrackingTouch(p0: SeekBar?) {
//
//                }
//
//            })

        }
        else if (mainViewModel.homeviewpageritems.size == 12 && mainViewModel.positonview == 11) {
            setSeekbarClick(11)
//            dialogMainBinding?.imgseekbarfour?.setImageResource(
//                mainViewModel.homeviewpageritems.get(
//                    11
//                ).viewimage
//            )
//            dialogMainBinding?.seekBarAdaptersfour?.setOnSeekBarChangeListener(object :
//                OnSeekBarChangeListener {
//                var p: Int = 0
//                override fun onProgressChanged(p0: SeekBar?, p1: Int, p2: Boolean) {
//                    p = p1
//
//                    val log1 = (Math.log(50.0 - p1) / Math.log(50.0)).toFloat()
//                    mainViewModel.getInstancemediaplayerone().setVolume(1 - log1, 1 - log1)
//                    mainViewModel.getInstancemediaplayerone().seekTo(p1)
//
//
//                }
//
//                override fun onStartTrackingTouch(p0: SeekBar?) {
//
//                }
//
//                override fun onStopTrackingTouch(p0: SeekBar?) {
//
//                }
//
//            })

        }
        else if (mainViewModel.homeviewpageritems.size == 13 && mainViewModel.positonview == 12) {
            setSeekbarClick(12)

//            dialogMainBinding?.imgseekbarfour?.setImageResource(
//                mainViewModel.homeviewpageritems.get(
//                    12
//                ).viewimage
//            )
//            dialogMainBinding?.seekBarAdaptersfour?.setOnSeekBarChangeListener(object :
//                OnSeekBarChangeListener {
//                var p: Int = 0
//                override fun onProgressChanged(p0: SeekBar?, p1: Int, p2: Boolean) {
//                    p = p1
//
//                    val log1 = (Math.log(50.0 - p1) / Math.log(50.0)).toFloat()
//                    mainViewModel.getInstancemediaplayerone().setVolume(1 - log1, 1 - log1)
//                    mainViewModel.getInstancemediaplayerone().seekTo(p1)
//
//
//                }
//
//                override fun onStartTrackingTouch(p0: SeekBar?) {
//
//                }
//
//                override fun onStopTrackingTouch(p0: SeekBar?) {
//
//                }
//
//            })

        }
        else if (mainViewModel.homeviewpageritems.size == 14 && mainViewModel.positonview == 13) {
            setSeekbarClick(13)

//            dialogMainBinding?.imgseekbarfour?.setImageResource(
//                mainViewModel.homeviewpageritems.get(
//                    13
//                ).viewimage
//            )
//            dialogMainBinding?.seekBarAdaptersfour?.setOnSeekBarChangeListener(object :
//                OnSeekBarChangeListener {
//                var p: Int = 0
//                override fun onProgressChanged(p0: SeekBar?, p1: Int, p2: Boolean) {
//                    p = p1
//
//                    val log1 = (Math.log(50.0 - p1) / Math.log(50.0)).toFloat()
//                    mainViewModel.getInstancemediaplayerone().setVolume(1 - log1, 1 - log1)
//                    mainViewModel.getInstancemediaplayerone().seekTo(p1)
//
//
//                }
//
//                override fun onStartTrackingTouch(p0: SeekBar?) {
//
//                }
//
//                override fun onStopTrackingTouch(p0: SeekBar?) {
//
//                }
//
//            })

        }
        else if (mainViewModel.homeviewpageritems.size == 15 && mainViewModel.positonview == 14) {
            setSeekbarClick(14)

//            dialogMainBinding?.imgseekbarfour?.setImageResource(
//                mainViewModel.homeviewpageritems.get(
//                    14
//                ).viewimage
//            )
//            dialogMainBinding?.seekBarAdaptersfour?.setOnSeekBarChangeListener(object :
//                OnSeekBarChangeListener {
//                var p: Int = 0
//                override fun onProgressChanged(p0: SeekBar?, p1: Int, p2: Boolean) {
//                    p = p1
//
//                    val log1 = (Math.log(50.0 - p1) / Math.log(50.0)).toFloat()
//                    mainViewModel.getInstancemediaplayerone().setVolume(1 - log1, 1 - log1)
//                    mainViewModel.getInstancemediaplayerone().seekTo(p1)
//
//
//                }
//
//                override fun onStartTrackingTouch(p0: SeekBar?) {
//
//                }
//
//                override fun onStopTrackingTouch(p0: SeekBar?) {
//
//                }
//
//            })

        }
        else if (mainViewModel.homeviewpageritems.size == 16 && mainViewModel.positonview == 15) {
            setSeekbarClick(15)

//            dialogMainBinding?.imgseekbarfour?.setImageResource(
//                mainViewModel.homeviewpageritems.get(
//                    15
//                ).viewimage
//            )
//            dialogMainBinding?.seekBarAdaptersfour?.setOnSeekBarChangeListener(object :
//                OnSeekBarChangeListener {
//                var p: Int = 0
//                override fun onProgressChanged(p0: SeekBar?, p1: Int, p2: Boolean) {
//                    p = p1
//
//                    val log1 = (Math.log(50.0 - p1) / Math.log(50.0)).toFloat()
//                    mainViewModel.getInstancemediaplayerone().setVolume(1 - log1, 1 - log1)
//                    mainViewModel.getInstancemediaplayerone().seekTo(p1)
//
//
//                }
//
//                override fun onStartTrackingTouch(p0: SeekBar?) {
//
//                }
//
//                override fun onStopTrackingTouch(p0: SeekBar?) {
//
//                }
//
//            })

        }
        else if (mainViewModel.homeviewpageritems.size == 17 && mainViewModel.positonview == 16) {
            setSeekbarClick(16)

//            dialogMainBinding?.imgseekbarfour?.setImageResource(
//                mainViewModel.homeviewpageritems.get(
//                    16
//                ).viewimage
//            )
//            dialogMainBinding?.seekBarAdaptersfour?.setOnSeekBarChangeListener(object :
//                OnSeekBarChangeListener {
//                var p: Int = 0
//                override fun onProgressChanged(p0: SeekBar?, p1: Int, p2: Boolean) {
//                    p = p1
//
//                    val log1 = (Math.log(50.0 - p1) / Math.log(50.0)).toFloat()
//                    mainViewModel.getInstancemediaplayerone().setVolume(1 - log1, 1 - log1)
//                    mainViewModel.getInstancemediaplayerone().seekTo(p1)
//
//
//                }
//
//                override fun onStartTrackingTouch(p0: SeekBar?) {
//
//                }
//
//                override fun onStopTrackingTouch(p0: SeekBar?) {
//
//                }
//
//            })

        }
        else if (mainViewModel.homeviewpageritems.size == 18 && mainViewModel.positonview == 17) {
            setSeekbarClick(17)
//            dialogMainBinding?.imgseekbarfour?.setImageResource(
//                mainViewModel.homeviewpageritems.get(
//                    17
//                ).viewimage
//            )
//            dialogMainBinding?.seekBarAdaptersfour?.setOnSeekBarChangeListener(object :
//                OnSeekBarChangeListener {
//                var p: Int = 0
//                override fun onProgressChanged(p0: SeekBar?, p1: Int, p2: Boolean) {
//                    p = p1
//
//                    val log1 = (Math.log(50.0 - p1) / Math.log(50.0)).toFloat()
//                    mainViewModel.getInstancemediaplayerone().setVolume(1 - log1, 1 - log1)
//                    mainViewModel.getInstancemediaplayerone().seekTo(p1)
//
//
//                }
//
//                override fun onStartTrackingTouch(p0: SeekBar?) {
//
//                }
//
//                override fun onStopTrackingTouch(p0: SeekBar?) {
//
//                }
//
//            })

        }
        else if (mainViewModel.homeviewpageritems.size == 19 && mainViewModel.positonview == 18) {
            setSeekbarClick(18)

//            dialogMainBinding?.imgseekbarfour?.setImageResource(
//                mainViewModel.homeviewpageritems.get(
//                    18
//                ).viewimage
//            )
//            dialogMainBinding?.seekBarAdaptersfour?.setOnSeekBarChangeListener(object :
//                OnSeekBarChangeListener {
//                var p: Int = 0
//                override fun onProgressChanged(p0: SeekBar?, p1: Int, p2: Boolean) {
//                    p = p1
//
//                    val log1 = (Math.log(50.0 - p1) / Math.log(50.0)).toFloat()
//                    mainViewModel.getInstancemediaplayerone().setVolume(1 - log1, 1 - log1)
//                    mainViewModel.getInstancemediaplayerone().seekTo(p1)
//
//
//                }
//
//                override fun onStartTrackingTouch(p0: SeekBar?) {
//
//                }
//
//                override fun onStopTrackingTouch(p0: SeekBar?) {
//
//                }
//
//            })

        }
        else if (mainViewModel.homeviewpageritems.size == 20 && mainViewModel.positonview == 19) {
            setSeekbarClick(19)

//            dialogMainBinding?.imgseekbarfour?.setImageResource(
//                mainViewModel.homeviewpageritems.get(
//                    19
//                ).viewimage
//            )
//            dialogMainBinding?.seekBarAdaptersfour?.setOnSeekBarChangeListener(object :
//                OnSeekBarChangeListener {
//                var p: Int = 0
//                override fun onProgressChanged(p0: SeekBar?, p1: Int, p2: Boolean) {
//                    p = p1
//
//                    val log1 = (Math.log(50.0 - p1) / Math.log(50.0)).toFloat()
//                    mainViewModel.getInstancemediaplayerone().setVolume(1 - log1, 1 - log1)
//                    mainViewModel.getInstancemediaplayerone().seekTo(p1)
//
//
//                }
//
//                override fun onStartTrackingTouch(p0: SeekBar?) {
//
//                }
//
//                override fun onStopTrackingTouch(p0: SeekBar?) {
//
//                }
//
//            })

        }
        Log.e("listdelete", " outside: ${mainViewModel.musicchange.size}", )
        when (mainViewModel.musicchange.size) {
            1 -> {
                dialogMainBinding?.constLysix?.visibility = View.VISIBLE
                dialogMainBinding?.constLytwo?.visibility = View.GONE
                dialogMainBinding?.constLythree?.visibility = View.GONE
                dialogMainBinding?.constLyfour?.visibility = View.VISIBLE
                dialogMainBinding?.constLyfive?.visibility = View.GONE
                dialogMainBinding?.constLyone?.visibility = View.GONE
//                dialogMainBinding?.delone?.setOnClickListener {
//
//                    dialogMainBinding?.constLysix?.visibility = View.GONE
//                    mainViewModel.musicchange.clear()
//                    arraynew.clear()
//                    mainViewModel.getInstancemediaplayertwo().stop()
//                    soundAdapter.notifyDataSetChanged()
//
//
//                }

                dialogMainBinding?.imgseekbarsix?.setImageResource(mainViewModel.musicchange[0].musicimage)
                dialogMainBinding?.seekBarAdapterssix?.max = 50
                dialogMainBinding?.seekBarAdaptersfour?.max = 50
                dialogMainBinding?.seekBarAdaptersfour?.setOnSeekBarChangeListener(object :
                    OnSeekBarChangeListener {
                    var p: Int = 0
                    override fun onProgressChanged(p0: SeekBar?, p1: Int, p2: Boolean) {
                        p = p1

                        val log1 = (Math.log(50.0 - p1) / Math.log(50.0)).toFloat()
                        mainViewModel.getInstancemediaplayerone().setVolume(1 - log1, 1 - log1)
                        mainViewModel.getInstancemediaplayerone().seekTo(p1)
                        p0?.setProgress(p1)


                    }

                    override fun onStartTrackingTouch(p0: SeekBar?) {

                    }

                    override fun onStopTrackingTouch(p0: SeekBar?) {

                    }

                })
                dialogMainBinding?.seekBarAdapterssix?.setOnSeekBarChangeListener(object :
                    OnSeekBarChangeListener {
                    var p: Int = 0
                    override fun onProgressChanged(p0: SeekBar?, p1: Int, p2: Boolean) {
                        p = p1

                        val volume2 =
                            (1 - ln((MAX_VOLUME - p1).toDouble()) / ln(MAX_VOLUME.toDouble())).toFloat()
                        mainViewModel.getInstancemediaplayertwo().setVolume(volume2, volume2)

                        mainViewModel.getInstancemediaplayertwo().seekTo(p1)
                        p0?.progress = p1
                        saveData()


                    }

                    override fun onStartTrackingTouch(p0: SeekBar?) {

                    }

                    override fun onStopTrackingTouch(p0: SeekBar?) {

                    }

                })


            }
            2 -> {
                dialogMainBinding?.imgseekbarsix?.setImageResource(mainViewModel.musicchange.get(0).musicimage)
                dialogMainBinding?.imgseekbarfive?.setImageResource(mainViewModel.musicchange.get(1).musicimage)
                dialogMainBinding?.constLyone?.visibility = View.GONE
                dialogMainBinding?.constLytwo?.visibility = View.GONE
                dialogMainBinding?.constLythree?.visibility = View.GONE
                dialogMainBinding?.constLyfour?.visibility = View.VISIBLE
                dialogMainBinding?.constLyfive?.visibility = View.VISIBLE
                dialogMainBinding?.constLysix?.visibility = View.VISIBLE

//                dialogMainBinding?.delone?.setOnClickListener {
//
//                    dialogMainBinding?.constLysix?.visibility = View.GONE
//                    mainViewModel.musicchange.clear()
//                    arraynew.clear()
//                    mainViewModel.getInstancemediaplayertwo().stop()
//                    soundAdapter.notifyDataSetChanged()
//
//
//                }
//                dialogMainBinding?.deltwo?.setOnClickListener {
//
//
//                    dialogMainBinding?.constLyfive?.visibility = View.GONE
//                    mainViewModel.musicchange.clear()
//                    arraynew.clear()
//                    mainViewModel.getInstancemediaplayerthree().stop()
//                    soundAdapter.notifyDataSetChanged()
//
//
//                }
                dialogMainBinding?.seekBarAdaptersfour?.setOnSeekBarChangeListener(object :
                    OnSeekBarChangeListener {
                    var p: Int = 0
                    override fun onProgressChanged(p0: SeekBar?, p1: Int, p2: Boolean) {
                        p = p1

                        val log1 = (Math.log(50.0 - p1) / Math.log(50.0)).toFloat()
                        mainViewModel.getInstancemediaplayerone().setVolume(1 - log1, 1 - log1)
                        mainViewModel.getInstancemediaplayerone().seekTo(p1)
                        p0?.setProgress(p1)


                    }

                    override fun onStartTrackingTouch(p0: SeekBar?) {

                    }

                    override fun onStopTrackingTouch(p0: SeekBar?) {

                    }

                })
                dialogMainBinding?.seekBarAdapterssix?.setOnSeekBarChangeListener(object :
                    OnSeekBarChangeListener {
                    var p: Int = 0
                    override fun onProgressChanged(p0: SeekBar?, p1: Int, p2: Boolean) {
                        p = p1

                        val volume2 =
                            (1 - ln((MAX_VOLUME - p1).toDouble()) / ln(MAX_VOLUME.toDouble())).toFloat()
                        mainViewModel.getInstancemediaplayertwo().setVolume(volume2, volume2)

                        mainViewModel.getInstancemediaplayertwo().seekTo(p1)
                        p0?.setProgress(p1)
                        saveData()


                    }

                    override fun onStartTrackingTouch(p0: SeekBar?) {

                    }

                    override fun onStopTrackingTouch(p0: SeekBar?) {

                    }

                })
                dialogMainBinding?.seekBarAdaptersfive?.setOnSeekBarChangeListener(object :
                    OnSeekBarChangeListener {
                    override fun onProgressChanged(p0: SeekBar?, p1: Int, p2: Boolean) {


                        val log1 = (Math.log(50.0 - p1) / Math.log(50.0)).toFloat()
                        mainViewModel.getInstancemediaplayerthree().setVolume(1 - log1, 1 - log1)
                        mainViewModel.getInstancemediaplayerthree().seekTo(p1)
                        saveData()


                    }

                    override fun onStartTrackingTouch(p0: SeekBar?) {

                    }

                    override fun onStopTrackingTouch(p0: SeekBar?) {

                    }

                })
            }
            3 -> {
                Log.e("listdelete", "enter 3: ${mainViewModel.musicchange.size}", )
                dialogMainBinding?.imgseekbarsix?.setImageResource(mainViewModel.musicchange.get(0).musicimage)
                dialogMainBinding?.imgseekbarfive?.setImageResource(mainViewModel.musicchange.get(1).musicimage)
                dialogMainBinding?.imgseekbarthree?.setImageResource(mainViewModel.musicchange.get(2).musicimage)
                dialogMainBinding?.constLyone?.visibility = View.GONE
                dialogMainBinding?.constLytwo?.visibility = View.GONE
                dialogMainBinding?.constLythree?.visibility = View.VISIBLE
                dialogMainBinding?.constLyfour?.visibility = View.VISIBLE
                dialogMainBinding?.constLyfive?.visibility = View.VISIBLE
                dialogMainBinding?.constLysix?.visibility = View.VISIBLE
//                dialogMainBinding?.delone?.setOnClickListener {
//
//                    dialogMainBinding?.constLysix?.visibility = View.GONE
//                    mainViewModel.musicchange.clear()
//                    arraynew.clear()
//                    mainViewModel.getInstancemediaplayertwo().stop()
//                    soundAdapter.notifyDataSetChanged()
//
//
//                }
//                dialogMainBinding?.deltwo?.setOnClickListener {
//
//
//                    dialogMainBinding?.constLyfive?.visibility = View.GONE
//                    mainViewModel.musicchange.clear()
//                    arraynew.clear()
//                    mainViewModel.getInstancemediaplayerthree().stop()
//                    soundAdapter.notifyDataSetChanged()
//
//
//                }
//                dialogMainBinding?.delthree?.setOnClickListener {
//
//                    dialogMainBinding?.constLythree?.visibility = View.GONE
//                    mainViewModel.musicchange.clear()
//                    arraynew.clear()
//                    mainViewModel.getInstancemediaplayerfour().stop()
//                    soundAdapter.notifyDataSetChanged()
//
//
//                }

                dialogMainBinding?.seekBarAdaptersfour?.setOnSeekBarChangeListener(object :
                    OnSeekBarChangeListener {
                    var p: Int = 0
                    override fun onProgressChanged(p0: SeekBar?, p1: Int, p2: Boolean) {
                        p = p1

                        val log1 = (Math.log(50.0 - p1) / Math.log(50.0)).toFloat()
                        mainViewModel.getInstancemediaplayerone().setVolume(1 - log1, 1 - log1)
                        mainViewModel.getInstancemediaplayerone().seekTo(p1)


                    }

                    override fun onStartTrackingTouch(p0: SeekBar?) {

                    }

                    override fun onStopTrackingTouch(p0: SeekBar?) {

                    }

                })
                dialogMainBinding?.seekBarAdapterssix?.setOnSeekBarChangeListener(object :
                    OnSeekBarChangeListener {
                    var p: Int = 0


                    override fun onProgressChanged(p0: SeekBar?, p1: Int, p2: Boolean) {
                        p = p1

                        val volume2 =
                            (1 - ln((MAX_VOLUME - p1).toDouble()) / ln(MAX_VOLUME.toDouble())).toFloat()
                        mainViewModel.getInstancemediaplayertwo().setVolume(volume2, volume2)

                        mainViewModel.getInstancemediaplayertwo().seekTo(p1)
                        saveData()


                    }

                    override fun onStartTrackingTouch(p0: SeekBar?) {

                    }

                    override fun onStopTrackingTouch(p0: SeekBar?) {

                    }

                })
                dialogMainBinding?.seekBarAdaptersfive?.setOnSeekBarChangeListener(object :
                    OnSeekBarChangeListener {
                    override fun onProgressChanged(p0: SeekBar?, p1: Int, p2: Boolean) {

                        /* val volume3 =
                             (1 - ln((MAX_VOLUME - p1).toDouble()) / ln(MAX_VOLUME.toDouble())).toFloat()
                         mainViewModel.getInstancemediaplayertwo().setVolume(volume3, volume3)*/


                        val log1 = (Math.log(50.0 - p1) / Math.log(50.0)).toFloat()
                        mainViewModel.getInstancemediaplayerthree().setVolume(1 - log1, 1 - log1)
                        mainViewModel.getInstancemediaplayerthree().seekTo(p1)
                        saveData()


                    }

                    override fun onStartTrackingTouch(p0: SeekBar?) {

                    }

                    override fun onStopTrackingTouch(p0: SeekBar?) {

                    }

                })
                dialogMainBinding?.seekBarAdaptersthree?.setOnSeekBarChangeListener(object :
                    OnSeekBarChangeListener {
                    override fun onProgressChanged(p0: SeekBar?, p1: Int, p2: Boolean) {

                        /* val volume3 =
                             (1 - ln((MAX_VOLUME - p1).toDouble()) / ln(MAX_VOLUME.toDouble())).toFloat()
                         mainViewModel.getInstancemediaplayertwo().setVolume(volume3, volume3)*/


                        val log1 = (Math.log(50.0 - p1) / Math.log(50.0)).toFloat()
                        mainViewModel.getInstancemediaplayerfour().setVolume(1 - log1, 1 - log1)
                        mainViewModel.getInstancemediaplayerfour().seekTo(p1)
                        saveData()


                    }

                    override fun onStartTrackingTouch(p0: SeekBar?) {

                    }

                    override fun onStopTrackingTouch(p0: SeekBar?) {

                    }

                })
            }
            4 -> {
                Log.e("listdelete", "enter 4: ${mainViewModel.musicchange.size}", )
//                dialogMainBinding?.delone?.setOnClickListener {
//                    Log.e("listdelete", "enter 4: ${mainViewModel.musicchange.size}", )
//                    dialogMainBinding?.constLysix?.visibility = View.GONE
//                    mainViewModel.musicchange.clear()
//                    arraynew.clear()
//                    mainViewModel.getInstancemediaplayertwo().stop()
//                    soundAdapter.notifyDataSetChanged()
//
//
//                }
//                dialogMainBinding?.deltwo?.setOnClickListener {
//
//
//                    dialogMainBinding?.constLyfive?.visibility = View.GONE
//                    mainViewModel.musicchange.clear()
//                    arraynew.clear()
//                    mainViewModel.getInstancemediaplayerthree().stop()
//                    soundAdapter.notifyDataSetChanged()
//
//
//                }
//                dialogMainBinding?.delthree?.setOnClickListener {
//
//                    dialogMainBinding?.constLythree?.visibility = View.GONE
//                    mainViewModel.musicchange.clear()
//                    arraynew.clear()
//                    mainViewModel.getInstancemediaplayerfour().stop()
//                    soundAdapter.notifyDataSetChanged()
//
//
//                }
//                dialogMainBinding?.delfour?.setOnClickListener {
//
//
//                    dialogMainBinding?.constLytwo?.visibility = View.GONE
//                    mainViewModel.musicchange.clear()
//                    arraynew.clear()
//                    mainViewModel.getInstancemediaplayerfive().stop()
//                    soundAdapter.notifyDataSetChanged()
//
//
//                }

                dialogMainBinding?.seekBarAdaptersfour?.setOnSeekBarChangeListener(object :
                    OnSeekBarChangeListener {
                    var p: Int = 0
                    override fun onProgressChanged(p0: SeekBar?, p1: Int, p2: Boolean) {
                        p = p1

                        val log1 = (Math.log(50.0 - p1) / Math.log(50.0)).toFloat()
                        mainViewModel.getInstancemediaplayerone().setVolume(1 - log1, 1 - log1)
                        mainViewModel.getInstancemediaplayerone().seekTo(p1)


                    }

                    override fun onStartTrackingTouch(p0: SeekBar?) {

                    }

                    override fun onStopTrackingTouch(p0: SeekBar?) {

                    }

                })

                dialogMainBinding?.imgseekbarsix?.setImageResource(mainViewModel.musicchange[0].musicimage)
                dialogMainBinding?.imgseekbarfive?.setImageResource(mainViewModel.musicchange[1].musicimage)
                dialogMainBinding?.imgseekbarthree?.setImageResource(mainViewModel.musicchange[2].musicimage)
                dialogMainBinding?.imgseekbar?.setImageResource(mainViewModel.musicchange[3].musicimage)
                dialogMainBinding?.constLyone?.visibility = View.GONE
                dialogMainBinding?.constLytwo?.visibility = View.VISIBLE
                dialogMainBinding?.constLythree?.visibility = View.VISIBLE
                dialogMainBinding?.constLyfour?.visibility = View.VISIBLE
                dialogMainBinding?.constLyfive?.visibility = View.VISIBLE
                dialogMainBinding?.constLysix?.visibility = View.VISIBLE
                dialogMainBinding?.seekBarAdapterssix?.setOnSeekBarChangeListener(object :
                    OnSeekBarChangeListener {
                    var p: Int = 0


                    override fun onProgressChanged(p0: SeekBar?, p1: Int, p2: Boolean) {
                        p = p1

                        val volume2 =
                            (1 - ln((MAX_VOLUME - p1).toDouble()) / ln(MAX_VOLUME.toDouble())).toFloat()
                        mainViewModel.getInstancemediaplayertwo().setVolume(volume2, volume2)

                        mainViewModel.getInstancemediaplayertwo().seekTo(p1)
                        saveData()


                    }

                    override fun onStartTrackingTouch(p0: SeekBar?) {

                    }

                    override fun onStopTrackingTouch(p0: SeekBar?) {

                    }

                })
                dialogMainBinding?.seekBarAdaptersfive?.setOnSeekBarChangeListener(object :
                    OnSeekBarChangeListener {
                    override fun onProgressChanged(p0: SeekBar?, p1: Int, p2: Boolean) {

                        /* val volume3 =
                             (1 - ln((MAX_VOLUME - p1).toDouble()) / ln(MAX_VOLUME.toDouble())).toFloat()
                         mainViewModel.getInstancemediaplayertwo().setVolume(volume3, volume3)*/


                        val log1 = (Math.log(50.0 - p1) / Math.log(50.0)).toFloat()
                        mainViewModel.getInstancemediaplayerthree().setVolume(1 - log1, 1 - log1)
                        mainViewModel.getInstancemediaplayerthree().seekTo(p1)
                        saveData()


                    }

                    override fun onStartTrackingTouch(p0: SeekBar?) {

                    }

                    override fun onStopTrackingTouch(p0: SeekBar?) {

                    }

                })
                dialogMainBinding?.seekBarAdaptersthree?.setOnSeekBarChangeListener(object :
                    OnSeekBarChangeListener {
                    override fun onProgressChanged(p0: SeekBar?, p1: Int, p2: Boolean) {

                        /* val volume3 =
                             (1 - ln((MAX_VOLUME - p1).toDouble()) / ln(MAX_VOLUME.toDouble())).toFloat()
                         mainViewModel.getInstancemediaplayertwo().setVolume(volume3, volume3)*/


                        val log1 = (Math.log(50.0 - p1) / Math.log(50.0)).toFloat()
                        mainViewModel.getInstancemediaplayerfour().setVolume(1 - log1, 1 - log1)
                        mainViewModel.getInstancemediaplayerfour().seekTo(p1)
                        saveData()


                    }

                    override fun onStartTrackingTouch(p0: SeekBar?) {

                    }

                    override fun onStopTrackingTouch(p0: SeekBar?) {

                    }

                })
                dialogMainBinding?.seekBarAdapters?.setOnSeekBarChangeListener(object :
                    OnSeekBarChangeListener {
                    override fun onProgressChanged(p0: SeekBar?, p1: Int, p2: Boolean) {


                        val log1 = (Math.log(50.0 - p1) / Math.log(50.0)).toFloat()
                        mainViewModel.getInstancemediaplayerfive().setVolume(1 - log1, 1 - log1)
                        mainViewModel.getInstancemediaplayerfive().seekTo(p1)
                        saveData()


                    }

                    override fun onStartTrackingTouch(p0: SeekBar?) {

                    }

                    override fun onStopTrackingTouch(p0: SeekBar?) {

                    }

                })
            }
            5 -> {

                Log.e("listdelete", "enter: ${mainViewModel.musicchange.size}", )
                dialogMainBinding?.imgseekbarsix?.setImageResource(mainViewModel.musicchange[0].musicimage)
                dialogMainBinding?.imgseekbarfive?.setImageResource(mainViewModel.musicchange[1].musicimage)
                dialogMainBinding?.imgseekbarthree?.setImageResource(mainViewModel.musicchange[2].musicimage)
                dialogMainBinding?.imgseekbar?.setImageResource(mainViewModel.musicchange[3].musicimage)
                dialogMainBinding?.imageseekbar?.setImageResource(mainViewModel.musicchange[4].musicimage)
//                dialogMainBinding?.delone?.setOnClickListener {
//                    Log.e("listdelete", "enter: ${mainViewModel.musicchange.size}", )
//                    dialogMainBinding?.constLysix?.visibility = View.GONE
//                    if (mainViewModel.musicchange.size >4){
//                        mainViewModel.musicchange.removeAt(4)
//                        arraynew.removeAt(4)
//                    }
//                    else if(mainViewModel.musicchange.size >3) {
//                        mainViewModel.musicchange.removeAt(3)
//                        arraynew.removeAt(3)
//                    }else if(mainViewModel.musicchange.size >2) {
//                        mainViewModel.musicchange.removeAt(2)
//                        arraynew.removeAt(2)
//                    }else if(mainViewModel.musicchange.size >1) {
//                        mainViewModel.musicchange.removeAt(1)
//                        arraynew.removeAt(1)
//                    }else{
//                        mainViewModel.musicchange.removeAt(0)
//                        arraynew.removeAt(0)
//                    }
//                    //mainViewModel.musicchange.clear()
//                   // arraynew.clear()
//                    mainViewModel.getInstancemediaplayertwo().stop()
//                    soundAdapter.notifyDataSetChanged()
//
//                }
//                dialogMainBinding?.deltwo?.setOnClickListener {
//
//                    dialogMainBinding?.constLyfive?.visibility = View.GONE
////                    mainViewModel.musicchange.removeAt(3)
////                    arraynew.removeAt(3)
//                    if(mainViewModel.musicchange.size >3) {
//                        mainViewModel.musicchange.removeAt(3)
//                        arraynew.removeAt(3)
//                    }else if(mainViewModel.musicchange.size >2) {
//                        mainViewModel.musicchange.removeAt(2)
//                        arraynew.removeAt(2)
//                    }else if(mainViewModel.musicchange.size >1) {
//                        mainViewModel.musicchange.removeAt(1)
//                        arraynew.removeAt(1)
//                    }else{
//                        mainViewModel.musicchange.removeAt(0)
//                        arraynew.removeAt(0)
//                    }
//                   // mainViewModel.musicchange.clear()
//                   // arraynew.clear()
//                    mainViewModel.getInstancemediaplayerthree().stop()
//                    soundAdapter.notifyDataSetChanged()
//
//
//                }
//                dialogMainBinding?.delthree?.setOnClickListener {
//
//                    dialogMainBinding?.constLythree?.visibility = View.GONE
//                    //.musicchange.clear()
//                   // arraynew.clear()
//                     if(mainViewModel.musicchange.size >2) {
//                        mainViewModel.musicchange.removeAt(2)
//                        arraynew.removeAt(2)
//                    }else if(mainViewModel.musicchange.size >1) {
//                        mainViewModel.musicchange.removeAt(1)
//                        arraynew.removeAt(1)
//                    }else{
//                        mainViewModel.musicchange.removeAt(0)
//                        arraynew.removeAt(0)
//                    }
//                    //mainViewModel.musicchange.removeAt(2)
//                    //arraynew.removeAt(2)
//                    mainViewModel.getInstancemediaplayerfour().stop()
//                    soundAdapter.notifyDataSetChanged()
//
//
//                }
//                dialogMainBinding?.delfour?.setOnClickListener {
//
//                    dialogMainBinding?.constLytwo?.visibility = View.GONE
//                    if(mainViewModel.musicchange.size >1) {
//                        mainViewModel.musicchange.removeAt(1)
//                        arraynew.removeAt(1)
//                    }else{
//                        mainViewModel.musicchange.removeAt(0)
//                        arraynew.removeAt(0)
//                    }
//                  //  mainViewModel.musicchange.removeAt(1)
//                   // arraynew.removeAt(1)
//                  //  mainViewModel.musicchange.clear()
//                    //arraynew.clear()
//                    mainViewModel.getInstancemediaplayerfive().stop()
//                    soundAdapter.notifyDataSetChanged()
//
//
//                }
//                dialogMainBinding?.delfive?.setOnClickListener {
//                    dialogMainBinding?.constLyone?.visibility = View.GONE
//                    mainViewModel.musicchange.removeAt(0)
//                    arraynew.removeAt(0)
//                  //  mainViewModel.musicchange.clear()
//                   // arraynew.clear()
//                    mainViewModel.getInstancemediaplayersix().stop()
//                    soundAdapter.notifyDataSetChanged()
//
//
//                }

                dialogMainBinding?.constLyone?.visibility = View.VISIBLE
                dialogMainBinding?.constLytwo?.visibility = View.VISIBLE
                dialogMainBinding?.constLythree?.visibility = View.VISIBLE
                dialogMainBinding?.constLyfour?.visibility = View.VISIBLE
                dialogMainBinding?.constLyfive?.visibility = View.VISIBLE
                dialogMainBinding?.constLysix?.visibility = View.VISIBLE
                dialogMainBinding?.seekBarAdaptersfour?.setOnSeekBarChangeListener(object :
                    OnSeekBarChangeListener {
                    var p: Int = 0
                    override fun onProgressChanged(p0: SeekBar?, p1: Int, p2: Boolean) {
                        p = p1

                        val log1 = (Math.log(50.0 - p1) / Math.log(50.0)).toFloat()
                        mainViewModel.getInstancemediaplayerone().setVolume(1 - log1, 1 - log1)
                        mainViewModel.getInstancemediaplayerone().seekTo(p1)


                    }

                    override fun onStartTrackingTouch(p0: SeekBar?) {

                    }

                    override fun onStopTrackingTouch(p0: SeekBar?) {

                    }

                })
                dialogMainBinding?.seekBarAdapterssix?.setOnSeekBarChangeListener(object :
                    OnSeekBarChangeListener {
                    var p: Int = 0


                    override fun onProgressChanged(p0: SeekBar?, p1: Int, p2: Boolean) {
                        p = p1

                        val volume2 =
                            (1 - ln((MAX_VOLUME - p1).toDouble()) / ln(MAX_VOLUME.toDouble())).toFloat()
                        mainViewModel.getInstancemediaplayertwo().setVolume(volume2, volume2)

                        mainViewModel.getInstancemediaplayertwo().seekTo(p1)
                        saveData()


                    }

                    override fun onStartTrackingTouch(p0: SeekBar?) {

                    }

                    override fun onStopTrackingTouch(p0: SeekBar?) {

                    }

                })
                dialogMainBinding?.seekBarAdaptersfive?.setOnSeekBarChangeListener(object :
                    OnSeekBarChangeListener {
                    override fun onProgressChanged(p0: SeekBar?, p1: Int, p2: Boolean) {

                        /* val volume3 =
                             (1 - ln((MAX_VOLUME - p1).toDouble()) / ln(MAX_VOLUME.toDouble())).toFloat()
                         mainViewModel.getInstancemediaplayertwo().setVolume(volume3, volume3)*/


                        val log1 = (Math.log(50.0 - p1) / Math.log(50.0)).toFloat()
                        mainViewModel.getInstancemediaplayerthree().setVolume(1 - log1, 1 - log1)
                        mainViewModel.getInstancemediaplayerthree().seekTo(p1)


                    }

                    override fun onStartTrackingTouch(p0: SeekBar?) {

                    }

                    override fun onStopTrackingTouch(p0: SeekBar?) {

                    }

                })
                dialogMainBinding?.seekBarAdaptersthree?.setOnSeekBarChangeListener(object :
                    OnSeekBarChangeListener {
                    override fun onProgressChanged(p0: SeekBar?, p1: Int, p2: Boolean) {
                        val log1 = (Math.log(50.0 - p1) / Math.log(50.0)).toFloat()
                        mainViewModel.getInstancemediaplayerfour().setVolume(1 - log1, 1 - log1)
                        mainViewModel.getInstancemediaplayerfour().seekTo(p1)


                    }

                    override fun onStartTrackingTouch(p0: SeekBar?) {

                    }

                    override fun onStopTrackingTouch(p0: SeekBar?) {

                    }

                })
                dialogMainBinding?.seekBarAdapters?.setOnSeekBarChangeListener(object :
                    OnSeekBarChangeListener {
                    override fun onProgressChanged(p0: SeekBar?, p1: Int, p2: Boolean) {

                        /* val volume3 =
                             (1 - ln((MAX_VOLUME - p1).toDouble()) / ln(MAX_VOLUME.toDouble())).toFloat()
                         mainViewModel.getInstancemediaplayertwo().setVolume(volume3, volume3)*/


                        val log1 = (Math.log(50.0 - p1) / Math.log(50.0)).toFloat()
                        mainViewModel.getInstancemediaplayerfive().setVolume(1 - log1, 1 - log1)
                        mainViewModel.getInstancemediaplayerfive().seekTo(p1)


                    }

                    override fun onStartTrackingTouch(p0: SeekBar?) {

                    }

                    override fun onStopTrackingTouch(p0: SeekBar?) {

                    }

                })
                dialogMainBinding?.seekBarAdapter?.setOnSeekBarChangeListener(object :
                    OnSeekBarChangeListener {
                    override fun onProgressChanged(p0: SeekBar?, p1: Int, p2: Boolean) {

                        /* val volume3 =
                             (1 - ln((MAX_VOLUME - p1).toDouble()) / ln(MAX_VOLUME.toDouble())).toFloat()
                         mainViewModel.getInstancemediaplayertwo().setVolume(volume3, volume3)*/


                        val log1 = (Math.log(50.0 - p1) / Math.log(50.0)).toFloat()
                        mainViewModel.getInstancemediaplayersix().setVolume(1 - log1, 1 - log1)
                        mainViewModel.getInstancemediaplayersix().seekTo(p1)


                    }

                    override fun onStartTrackingTouch(p0: SeekBar?) {

                    }

                    override fun onStopTrackingTouch(p0: SeekBar?) {

                    }

                })
            }
        }
        dialogMainBinding?.delone?.setOnClickListener {
            Log.e("listdelete", "enter: ${mainViewModel.musicchange.size}", )
            dialogMainBinding?.constLysix?.visibility = View.GONE
            if (mainViewModel.musicchange.size >4){
                mainViewModel.musicchange.removeAt(4)
                arraynew.removeAt(4)
            }
            else if(mainViewModel.musicchange.size >3) {
                mainViewModel.musicchange.removeAt(3)
                arraynew.removeAt(3)
            }else if(mainViewModel.musicchange.size >2) {
                mainViewModel.musicchange.removeAt(2)
                arraynew.removeAt(2)
            }else if(mainViewModel.musicchange.size >1) {
                mainViewModel.musicchange.removeAt(1)
                arraynew.removeAt(1)
            }else{
                mainViewModel.musicchange.removeAt(0)
                arraynew.removeAt(0)
            }
            //mainViewModel.musicchange.clear()
            // arraynew.clear()
            mainViewModel.getInstancemediaplayertwo().stop()
            soundAdapter.notifyDataSetChanged()

        }
        dialogMainBinding?.deltwo?.setOnClickListener {

            dialogMainBinding?.constLyfive?.visibility = View.GONE
//                    mainViewModel.musicchange.removeAt(3)
//                    arraynew.removeAt(3)
            if(mainViewModel.musicchange.size >3) {
                mainViewModel.musicchange.removeAt(3)
                arraynew.removeAt(3)
            }else if(mainViewModel.musicchange.size >2) {
                mainViewModel.musicchange.removeAt(2)
                arraynew.removeAt(2)
            }else if(mainViewModel.musicchange.size >1) {
                mainViewModel.musicchange.removeAt(1)
                arraynew.removeAt(1)
            }else{
                mainViewModel.musicchange.removeAt(0)
                arraynew.removeAt(0)
            }
            // mainViewModel.musicchange.clear()
            // arraynew.clear()
            mainViewModel.getInstancemediaplayerthree().stop()
            soundAdapter.notifyDataSetChanged()


        }
        dialogMainBinding?.delthree?.setOnClickListener {

            dialogMainBinding?.constLythree?.visibility = View.GONE
            //.musicchange.clear()
            // arraynew.clear()
            if(mainViewModel.musicchange.size >2) {
                mainViewModel.musicchange.removeAt(2)
                arraynew.removeAt(2)
            }else if(mainViewModel.musicchange.size >1) {
                mainViewModel.musicchange.removeAt(1)
                arraynew.removeAt(1)
            }else{
                mainViewModel.musicchange.removeAt(0)
                arraynew.removeAt(0)
            }
            //mainViewModel.musicchange.removeAt(2)
            //arraynew.removeAt(2)
            mainViewModel.getInstancemediaplayerfour().stop()
            soundAdapter.notifyDataSetChanged()


        }
        dialogMainBinding?.delfour?.setOnClickListener {

            dialogMainBinding?.constLytwo?.visibility = View.GONE
            if(mainViewModel.musicchange.size >1) {
                mainViewModel.musicchange.removeAt(1)
                arraynew.removeAt(1)
            }else{
                mainViewModel.musicchange.removeAt(0)
                arraynew.removeAt(0)
            }
            //  mainViewModel.musicchange.removeAt(1)
            // arraynew.removeAt(1)
            //  mainViewModel.musicchange.clear()
            //arraynew.clear()
            mainViewModel.getInstancemediaplayerfive().stop()
            soundAdapter.notifyDataSetChanged()


        }
        dialogMainBinding?.delfive?.setOnClickListener {
            dialogMainBinding?.constLyone?.visibility = View.GONE
            mainViewModel.musicchange.removeAt(0)
            arraynew.removeAt(0)
            //  mainViewModel.musicchange.clear()
            // arraynew.clear()
            mainViewModel.getInstancemediaplayersix().stop()
            soundAdapter.notifyDataSetChanged()


        }
        dialogone?.show()
    }

    private fun setSeekbarClick(valueIndex: Int) {
        dialogMainBinding?.imgseekbarfour?.setImageResource(
            mainViewModel.homeviewpageritems.get(
                valueIndex
            ).viewimage
        )
        dialogMainBinding?.seekBarAdaptersfour?.setOnSeekBarChangeListener(object :
            OnSeekBarChangeListener {
            var p: Int = 0
            override fun onProgressChanged(p0: SeekBar?, p1: Int, p2: Boolean) {
                p = p1

                val log1 = (Math.log(50.0 - p1) / Math.log(50.0)).toFloat()
                mainViewModel.getInstancemediaplayerone().setVolume(1 - log1, 1 - log1)
                mainViewModel.getInstancemediaplayerone().seekTo(p1)
                p0?.setProgress(p1)

            }

            override fun onStartTrackingTouch(p0: SeekBar?) {

            }

            override fun onStopTrackingTouch(p0: SeekBar?) {

            }

        })
    }

    private fun sliderDownItems() {
        CoroutineScope(Dispatchers.Default).launch {
            val intentdata = intent.getIntExtra("GentleMorning", 0)
            val itone = intent.getStringExtra("gone")
            val done = intent.getStringExtra("vone")
//            val intentdatatwo = intent.getIntExtra("GentleMorning", 1)
//            val ittwo = intent.getStringExtra("gtwo")
//            val dtwo = intent.getStringExtra("vtwo")
//            val intentdatathree = intent.getIntExtra("GentleMorning", 2)
//            val itthree = intent.getStringExtra("gthree")
//            val dthree = intent.getStringExtra("vthree")
//            val intentdatafour = intent.getIntExtra("GentleMorning", 3)
//            val itfour = intent.getStringExtra("gfour")
//            val dfour = intent.getStringExtra("vfour")
//            val intentdatafive = intent.getIntExtra("GentleMorning", 4)
//            val itfive = intent.getStringExtra("gfive")
//            val dfive = intent.getStringExtra("vfive")
//            val intentdatasix = intent.getIntExtra("GentleMorning", 5)
//            val itsix = intent.getStringExtra("gsix")
//            val dsix = intent.getStringExtra("vsix")
//            val intentdataseven = intent.getIntExtra("GentleMorning", 6)
//            val itseven = intent.getStringExtra("gseven")
//            val dseven = intent.getStringExtra("vseven")
//            val intentdataeight = intent.getIntExtra("GentleMorning", 7)
//            val iteight = intent.getStringExtra("geight")
//            val deight = intent.getStringExtra("veight")
//            val intentdatanine = intent.getIntExtra("GentleMorning", 8)
//            val itnine = intent.getStringExtra("gnine")
//            val dnine = intent.getStringExtra("vnine")
//            val intentdataten = intent.getIntExtra("GentleMorning", 9)
//            val itten = intent.getStringExtra("gten")
//            val dten = intent.getStringExtra("vten")
//            val intentdataeleven = intent.getIntExtra("GentleMorning", 10)
//            val iteleven = intent.getStringExtra("geleven")
//            val deleven = intent.getStringExtra("veleven")
//            val intentdatatwelve = intent.getIntExtra("GentleMorning", 11)
//            val ittwelve = intent.getStringExtra("gtwelve")
//            val dtwelve = intent.getStringExtra("vtwelve")
//            val intentdatathirteen = intent.getIntExtra("GentleMorning", 12)
//            val itthirteen = intent.getStringExtra("gthirteen")
//            val dthirteen = intent.getStringExtra("vthirteen")
//            val intentdatafourteen = intent.getIntExtra("GentleMorning", 13)
//            val itfourteen = intent.getStringExtra("gfourteen")
//            val dfourteen = intent.getStringExtra("vfourteen")
//            val intentdatafiveteen = intent.getIntExtra("GentleMorning", 14)
//            val itfiveteen = intent.getStringExtra("gfiveteen")
//            val dfiveteen = intent.getStringExtra("vfivteen")
//            val intentdatasixteen = intent.getIntExtra("GentleMorning", 15)
//            val itsixteen = intent.getStringExtra("gsixteen")
//            val dsixteen = intent.getStringExtra("vsixteen")
//            val intentdatasevneteen = intent.getIntExtra("GentleMorning", 16)
//            val itseventeen = intent.getStringExtra("gseventeen")
//            val dseventeen = intent.getStringExtra("vseventeen")
//            val intentdataeighteen = intent.getIntExtra("GentleMorning", 17)
//            val iteighteen = intent.getStringExtra("geighteen")
//            val deighteen = intent.getStringExtra("veighteen")
//            val intentdatanineteen = intent.getIntExtra("GentleMorning", 18)
//            val itnineteen = intent.getStringExtra("gnineteen")
//            val dnineteen = intent.getStringExtra("vnineteen")
            when {
                intentdata == 0 -> {
                    binding.txtGentleMorning.text = itone.toString()
                    binding.mt.text = done
                    imageUrl.add(SliderItems(R.drawable.gentle_morning_one))
                    imageUrl.add(SliderItems(R.drawable.gentle_morning_two))
                    sliderAdapter = SliderAdapter(this@MediationSound, imageUrl)
                }
                intentdata == 1 -> {
                    binding.txtGentleMorning.text = itone.toString()
                    binding.mt.text = done
                    imageUrl.add(SliderItems(R.drawable.peacefulllakeone))
                    imageUrl.add(SliderItems(R.drawable.peacefullaketwo))
                    sliderAdapter = SliderAdapter(this@MediationSound, imageUrl)
                }
                intentdata.equals(2) -> {
                    binding.txtGentleMorning.text = itone.toString()
                    binding.mt.text = done
                    imageUrl.add(SliderItems(R.drawable.sunriseone))
                    imageUrl.add(SliderItems(R.drawable.sunrisetwo))
                    sliderAdapter = SliderAdapter(this@MediationSound, imageUrl)
                }
                intentdata.equals(3) -> {
                    binding.txtGentleMorning.text = itone.toString()
                    binding.mt.text = done
                    imageUrl.add(SliderItems(R.drawable.softpianoone))
                    imageUrl.add(SliderItems(R.drawable.softpianotwo))
                    sliderAdapter = SliderAdapter(this@MediationSound, imageUrl)
                }
                intentdata.equals(4) -> {
                    binding.txtGentleMorning.text = itone.toString()
                    binding.mt.text = done
                    imageUrl.add(SliderItems(R.drawable.heavenone))
                    imageUrl.add(SliderItems(R.drawable.heaventwo))
                    sliderAdapter = SliderAdapter(this@MediationSound, imageUrl)
                }
                intentdata.equals(5) -> {
                    binding.txtGentleMorning.text = itone.toString()
                    binding.mt.text = done
                    imageUrl.add(SliderItems(R.drawable.perfectrainone))
                    imageUrl.add(SliderItems(R.drawable.perfectraintwo))
                    sliderAdapter = SliderAdapter(this@MediationSound, imageUrl)
                }
                intentdata.equals(6) -> {
                    binding.txtGentleMorning.text = itone.toString()
                    binding.mt.text = done
                    imageUrl.add(SliderItems(R.drawable.inspirationone))
                    imageUrl.add(SliderItems(R.drawable.instwo))
                    sliderAdapter = SliderAdapter(this@MediationSound, imageUrl)
                }
                intentdata.equals(7) -> {
                    binding.txtGentleMorning.text = itone.toString()
                    binding.mt.text = done
                    imageUrl.add(SliderItems(R.drawable.autumnforestone))
                    imageUrl.add(SliderItems(R.drawable.autumnforestwo))
                    sliderAdapter = SliderAdapter(this@MediationSound, imageUrl)
                }
                intentdata.equals(8) -> {
                    binding.txtGentleMorning.text = itone.toString()
                    binding.mt.text = done
                    imageUrl.add(SliderItems(R.drawable.conventone))
                    imageUrl.add(SliderItems(R.drawable.conventtwo))
                    sliderAdapter = SliderAdapter(this@MediationSound, imageUrl)
                }
                intentdata.equals(9) -> {
                    binding.txtGentleMorning.text = itone.toString()
                    binding.mt.text = done
                    imageUrl.add(SliderItems(R.drawable.seaone))
                    imageUrl.add(SliderItems(R.drawable.seatwo))
                    sliderAdapter = SliderAdapter(this@MediationSound, imageUrl)
                }
                intentdata.equals(10) -> {
                    binding.txtGentleMorning.text = itone.toString()
                    binding.mt.text = done
                    imageUrl.add(SliderItems(R.drawable.templeinhillsone))
                    imageUrl.add(SliderItems(R.drawable.templeinhillstwo))
                    sliderAdapter = SliderAdapter(this@MediationSound, imageUrl)
                }
                intentdata.equals(11) -> {
                    binding.txtGentleMorning.text = itone.toString()
                    binding.mt.text = done
                    imageUrl.add(SliderItems(R.drawable.mystic_temple_one))
                    imageUrl.add(SliderItems(R.drawable.mystic_temple_two))
                    sliderAdapter = SliderAdapter(this@MediationSound, imageUrl)
                }
                intentdata.equals(12) -> {
                    binding.txtGentleMorning.text = itone.toString()
                    binding.mt.text = done
                    imageUrl.add(SliderItems(R.drawable.scratching_one))
                    imageUrl.add(SliderItems(R.drawable.scratching_two))
                    sliderAdapter = SliderAdapter(this@MediationSound, imageUrl)
                }
                intentdata.equals(13) -> {
                    binding.txtGentleMorning.text = itone.toString()
                    binding.mt.text = done
                    imageUrl.add(SliderItems(R.drawable.page_one))
                    imageUrl.add(SliderItems(R.drawable.page_two))
                    sliderAdapter = SliderAdapter(this@MediationSound, imageUrl)
                }
                intentdata.equals(14) -> {
                    binding.txtGentleMorning.text = itone.toString()
                    binding.mt.text = done
                    imageUrl.add(SliderItems(R.drawable.chewing_one))
                    imageUrl.add(SliderItems(R.drawable.chewing_two))
                    sliderAdapter = SliderAdapter(this@MediationSound, imageUrl)
                }
                intentdata.equals(15) -> {
                    binding.txtGentleMorning.text = itone.toString()
                    binding.mt.text = done
                    imageUrl.add(SliderItems(R.drawable.whispers_one))
                    imageUrl.add(SliderItems(R.drawable.whispers_two))
                    sliderAdapter = SliderAdapter(this@MediationSound, imageUrl)
                }
                intentdata.equals(16) -> {
                    binding.txtGentleMorning.text = itone.toString()
                    binding.mt.text = done
                    imageUrl.add(SliderItems(R.drawable.breathing_one))
                    imageUrl.add(SliderItems(R.drawable.breathing_two))
                    sliderAdapter = SliderAdapter(this@MediationSound, imageUrl)
                }
                intentdata.equals(17) -> {
                    binding.txtGentleMorning.text = itone.toString()
                    binding.mt.text = done
                    imageUrl.add(SliderItems(R.drawable.crackling_one))
                    imageUrl.add(SliderItems(R.drawable.crackling_two))
                    sliderAdapter = SliderAdapter(this@MediationSound, imageUrl)
                }
                intentdata.equals(18) -> {
                    binding.txtGentleMorning.text = itone.toString()
                    binding.mt.text = done
                    imageUrl.add(SliderItems(R.drawable.cat_one))
                    imageUrl.add(SliderItems(R.drawable.cat_two))
                    sliderAdapter = SliderAdapter(this@MediationSound, imageUrl)
                }

            }
            sliderView.adapter = sliderAdapter

        }.invokeOnCompletion {
        }

    }

    private fun setRecyclerData() {

        val layoutmanager = LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
        binding.recycle.layoutManager = layoutmanager
        soundAdapter = MeditationSoundAdapter(this, arraynew, this@MediationSound)
        binding.recycle.adapter = soundAdapter


    }

    override fun onBackPressed() {
        /*dialog.dismiss()*/
        super.onBackPressed()
        //  Constants.isHomeSelected?.invoke(true)

    }

    private fun bottomSheetDialog() {
        dialog = BottomSheetDialog(this)
        val view =
            layoutInflater.inflate(R.layout.dialougescreen, null)
        val recycle = view.findViewById<RecyclerView>(R.id.recyclerView)
        val backBtn = view.findViewById<ImageView>(R.id.imgBackArBtm)
        backBtn.setOnClickListener {
            dialog.dismiss()
        }
        setupDummyData()
        val glm = GridLayoutManager(this, 4)
        glm.spanSizeLookup = object : GridLayoutManager.SpanSizeLookup() {
            override fun getSpanSize(position: Int): Int {
                return if (sectionedAdapter!!.getSectionItemViewType(position) == SectionedRecyclerViewAdapter.VIEW_TYPE_HEADER) {
                    4
                } else 1
            }
        }
        recycle.layoutManager = glm
        recycle.adapter = sectionedAdapter
        dialog.setCancelable(true)
        dialog.setContentView(view)
        dialog.show()
    }

    private fun setupDummyData() {
        val sessionModelArrayList: ArrayList<SessionModel> = ArrayList()

        val sessionModel1 = SessionModel(
            sessionNumber = SESSION_NUMBER_1,
            sessionStartTime = SESSION_START_TIME_1,
            sessionEndTime = SESSION_END_TIME_1,
            slotModel = slotModelArrayList1
        )


        //        Session2 details

        if (mainViewModel.musiclistone.value != null) {
            /*slotModelArrayList2=mainViewModel.musiclistone.value!!*/
        }
        val sessionModel2 = SessionModel(
            sessionNumber = SESSION_NUMBER_2,
            sessionStartTime = SESSION_START_TIME_2,
            sessionEndTime = SESSION_END_TIME_2,
            slotModel = slotModelArrayList2
        )
        val sessionModel3 = SessionModel(
            sessionNumber = SESSION_NUMBER_3,
            sessionStartTime = SESSION_START_TIME_3,
            sessionEndTime = SESSION_END_TIME_3,
            slotModel = slotModelArrayList3
        )

        sessionModelArrayList.add(sessionModel1)
        sessionModelArrayList.add(sessionModel2)
        sessionModelArrayList.add(sessionModel3)

        setupAdapter(sessionModelArrayList)
    }

    private fun setupAdapter(sessionModelArrayList: ArrayList<SessionModel>) {
        sectionedAdapter = SectionedRecyclerViewAdapter()
        val loadSlotsUseCase = LoadSlotsUseCase()

        for (i in 0 until sessionModelArrayList.size) {
            sectionedAdapter!!.addSection(
                SlotSection(
                    sessionModelArrayList[i].sessionStartTime + " " + sessionModelArrayList[i]
                        .sessionEndTime,
                    sessionModelArrayList[i].sessionNumber,
                    loadSlotsUseCase.execute(
                        sessionModelArrayList[i].slotModel
                    ),
                    this,
                    this
                )
            )
        }


    }

    override fun onItemRootViewClicked(
        v: View?,
        slotModel: MusicItems?,
        sessionNumber: Int,
        position: Int,
    ) {

        if (mainViewModel.musicchange.contains(slotModel)){
            shortToast("already added")
        }else{
            if (mainViewModel.musicchange.size <= 4) {
                mainViewModel.position = position
                mainViewModel.musicItem = slotModel
                if (mainViewModel.musicchange != null) {
                    if (slotModel != null) {
                        if (mainViewModel.musicchange != null) {
                            mainViewModel.musicchange.add(slotModel)
                            arraynew.clear()
                            arraynew.addAll(mainViewModel.musicchange)
                            soundAdapter.notifyDataSetChanged()

                        }
                    }
                }
                v?.let { disableMultiClick(it) }

            }
            else {
                shortToast("No More Option to be Selected")
            }
        }

        if (mainViewModel.musicchange.size == 1) {
            slotModel?.raw?.let {
                mainViewModel.getInstancemediaplayertwo().playMediaPlayer(this@MediationSound, it) {
                    //this.shortToast(it)
                }
            }
            mainViewModel.getInstancemediaplayertwo().start()
            mainViewModel.getInstancemediaplayertwo().isLooping = true
            dialog.dismiss()
            seekbarDialog()
        }
        else if (mainViewModel.musicchange.size == 2) {
            slotModel?.raw?.let {
                mainViewModel.getInstancemediaplayerthree()
                    .playMediaPlayer(this@MediationSound, it) {
                        //this.shortToast(it)
                    }
            }
            mainViewModel.getInstancemediaplayerthree().start()
            mainViewModel.getInstancemediaplayerthree().isLooping = true
            dialog.dismiss()
            seekbarDialog()
        }
        else if (mainViewModel.musicchange.size == 3) {
            slotModel?.raw?.let {
                mainViewModel.getInstancemediaplayerfour()
                    .playMediaPlayer(this@MediationSound, it) {
                       // this.shortToast(it)
                    }
            }
            mainViewModel.getInstancemediaplayerfour().start()
            mainViewModel.getInstancemediaplayerfour().isLooping = true
            dialog.dismiss()
            seekbarDialog()
        }
        else if (mainViewModel.musicchange.size == 4) {
            slotModel?.raw?.let {
                mainViewModel.getInstancemediaplayerfive()
                    .playMediaPlayer(this@MediationSound, it) {
                      //  this.shortToast(it)
                    }
            }
            mainViewModel.getInstancemediaplayerfive().start()
            mainViewModel.getInstancemediaplayerfive().isLooping = true
            dialog.dismiss()
            seekbarDialog()
        }
        else if (mainViewModel.musicchange.size == 5) {
            slotModel?.raw?.let {
                mainViewModel.getInstancemediaplayersix()
                    .playMediaPlayer(this@MediationSound, it) {
                       // this.shortToast(it)
                    }
            }
            mainViewModel.getInstancemediaplayersix().start()
            mainViewModel.getInstancemediaplayersix().isLooping = true
            dialog.dismiss()
            seekbarDialog()
        }
        else {
            shortToast("No More Option to be Selected")
        }

//        if (mainViewModel.musicchange.contains(slotModel)){
//            shortToast("already added")
//        }
//        else{
//            if (mainViewModel.musicchange.size <= 4) {
//                mainViewModel.position = position
//                mainViewModel.musicItem = slotModel
//                if (mainViewModel.musicchange != null) {
//                    if (slotModel != null) {
//                            mainViewModel.musicchange.add(slotModel)
//                            arraynew.clear()
//                            arraynew.addAll(mainViewModel.musicchange)
//                            soundAdapter.notifyDataSetChanged()
//                    }
//                }
//                v?.let { disableMultiClick(it) }
//                if (mainViewModel.musicchange.size == 1) {
//                    slotModel?.raw?.let {
//                        mainViewModel.getInstancemediaplayertwo().playMediaPlayer(this@MediationSound, it) {
//                            // this.shortToast(it)
//                        }
//                    }
//                    mainViewModel.getInstancemediaplayertwo().start()
//                    mainViewModel.getInstancemediaplayertwo().isLooping = true
//                    dialog.dismiss()
//                    seekbarDialog()
//                }
//                else if (mainViewModel.musicchange.size == 2) {
//                    slotModel?.raw?.let {
//                        mainViewModel.getInstancemediaplayerthree()
//                            .playMediaPlayer(this@MediationSound, it) {
//                                //  this.shortToast(it)
//                            }
//                    }
//                    mainViewModel.getInstancemediaplayerthree().start()
//                    mainViewModel.getInstancemediaplayerthree().isLooping = true
//                    dialog.dismiss()
//                    seekbarDialog()
//                }
//                else if (mainViewModel.musicchange.size == 3) {
//                    slotModel?.raw?.let {
//                        mainViewModel.getInstancemediaplayerfour()
//                            .playMediaPlayer(this@MediationSound, it) {
//                                //  this.shortToast(it)
//                            }
//                    }
//                    mainViewModel.getInstancemediaplayerfour().start()
//                    mainViewModel.getInstancemediaplayerfour().isLooping = true
//                    dialog.dismiss()
//                    seekbarDialog()
//                }
//                else if (mainViewModel.musicchange.size == 4) {
//                    slotModel?.raw?.let {
//                        mainViewModel.getInstancemediaplayerfive()
//                            .playMediaPlayer(this@MediationSound, it) {
//                                //this.shortToast(it)
//                            }
//                    }
//                    mainViewModel.getInstancemediaplayerfive().start()
//                    mainViewModel.getInstancemediaplayerfive().isLooping = true
//                    dialog.dismiss()
//                    seekbarDialog()
//                }
//                else if (mainViewModel.musicchange.size == 5) {
//                    slotModel?.raw?.let {
//                        mainViewModel.getInstancemediaplayersix()
//                            .playMediaPlayer(this@MediationSound, it) {
//                                //this.shortToast(it)
//                            }
//                    }
//                    mainViewModel.getInstancemediaplayersix().start()
//                    mainViewModel.getInstancemediaplayersix().isLooping = true
//                    dialog.dismiss()
//                    seekbarDialog()
//                }
//                else {
//                    shortToast("No More Option to be Selected")
//                }
//
//            }
//            else {
//                shortToast("No More Option to be Selected")
//            }
//
//        }


        /*showDialog()*/
        /* seekbarDialog()*/

    }

    override fun onItemClicksser(position: Int) {
        when (position) {
            0 -> {
                seekbarDialog()
            }
            1 -> {
                seekbarDialog()
            }
            2 -> {
                seekbarDialog()
            }
            3 -> {
                seekbarDialog()
            }
            4 -> {
                seekbarDialog()
            }
        }
    }

    fun saveData() {
        val sharedPreferences = getSharedPreferences("my_preference", MODE_PRIVATE)
        val editor = sharedPreferences.edit()
        dialogMainBinding?.seekBarAdapterssix?.progress
            ?.let { editor.putInt("my_seekBarsix", it) }
        dialogMainBinding?.seekBarAdaptersfive?.progress
            ?.let { editor.putInt("my_seekBarfive", it) }
        dialogMainBinding?.seekBarAdaptersfour?.progress
            ?.let { editor.putInt("my_seekBarfour", it) }
        dialogMainBinding?.seekBarAdaptersthree?.progress
            ?.let { editor.putInt("my_seekBarthree", it) }
        dialogMainBinding?.seekBarAdapters?.progress
            ?.let { editor.putInt("my_seekBartwo", it) }
        dialogMainBinding?.seekBarAdapter?.progress
            ?.let { editor.putInt("my_seekBarone", it) }
        editor.apply()
    }

    fun loadData() {
        val sharedPreferences = getSharedPreferences("my_preference", MODE_PRIVATE)
        seekBarProgressix = sharedPreferences.getInt("my_seekBarsix", 20)
        seekBarProgressfive = sharedPreferences.getInt("my_seekBarfive", 20)
        seekBarProgressfour = sharedPreferences.getInt("my_seekBarfour", 2)
        seekBarProgressthree = sharedPreferences.getInt("my_seekBarthree", 20)
        seekBarProgresstwo = sharedPreferences.getInt("my_seekBartwo", 20)
        seekBarProgressone = sharedPreferences.getInt("my_seekBarone", 20)
    }

    private fun updateViews() {
        dialogMainBinding?.seekBarAdapterssix?.progress = seekBarProgressix
        dialogMainBinding?.seekBarAdaptersfive?.progress = seekBarProgressfive
        dialogMainBinding?.seekBarAdaptersfour?.progress = seekBarProgressfour
        dialogMainBinding?.seekBarAdaptersthree?.progress = seekBarProgressthree
        dialogMainBinding?.seekBarAdapters?.progress = seekBarProgresstwo
        dialogMainBinding?.seekBarAdapter?.progress = seekBarProgressone

    }

    override fun dispatchKeyEvent(event: KeyEvent): Boolean {
        volumeUpDown(event)
        return super.dispatchKeyEvent(event)
    }

    private fun volumeUpDown(event: KeyEvent) {
        val action: Int = event.action
        when (event.keyCode) {
            KeyEvent.KEYCODE_VOLUME_UP -> {
                if (action == KeyEvent.ACTION_DOWN) {
                    audioManager.adjustVolume(AudioManager.ADJUST_RAISE,
                        AudioManager.FLAG_PLAY_SOUND)
                    val currVolume = audioManager.getStreamVolume(AudioManager.STREAM_MUSIC)
                    binding.seekbar.progress = currVolume
                    Log.e("TAG",
                        "dispatchKeyEvent: ${dialogMainBinding?.seekBarAdaptersseven?.progress}")
                    dialogMainBinding?.seekBarAdaptersseven?.progress = currVolume
                }
            }
            KeyEvent.KEYCODE_VOLUME_DOWN -> {
                if (action == KeyEvent.ACTION_DOWN) {
                    //TODO
                    audioManager.adjustVolume(AudioManager.ADJUST_LOWER,
                        AudioManager.FLAG_PLAY_SOUND)
                    val currVolume = audioManager.getStreamVolume(AudioManager.STREAM_MUSIC)
                    binding.seekbar.progress = currVolume
                    Log.e("TAG",
                        "dispatchKeyEvent: ${dialogMainBinding?.seekBarAdaptersseven?.progress}")
                    dialogMainBinding?.seekBarAdaptersseven?.progress = currVolume
                }
            }
        }
    }


}