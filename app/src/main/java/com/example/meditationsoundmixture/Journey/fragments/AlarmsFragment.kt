package com.project.meditationsoundmixture.Journey.fragments

import android.app.Application
import android.app.Dialog
import android.content.Context
import android.os.Build
import android.os.Bundle
import android.os.VibrationEffect
import android.os.Vibrator
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.widget.LinearLayout
import android.widget.PopupMenu
import android.widget.TextView
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.datastore.preferences.core.preferencesKey
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import com.project.meditationsoundmixture.Constants
import com.project.meditationsoundmixture.Extension.SharedPref
import com.project.meditationsoundmixture.Extension.setDarkLightMode
import com.project.meditationsoundmixture.Extension.shortToast
import com.project.meditationsoundmixture.R
import com.project.meditationsoundmixture.adapters.AlarmAdapter
import com.project.meditationsoundmixture.database.AlarmDatabase
import com.project.meditationsoundmixture.databinding.FragmentAlarmsBinding
import com.project.meditationsoundmixture.databinding.ReminderseekbarBinding
import com.project.meditationsoundmixture.databinding.WeekdayCardBinding
import com.project.meditationsoundmixture.datashare.DataShare
import com.project.meditationsoundmixture.util.hideSoftKeyboard
import com.project.meditationsoundmixture.util.showBasicMessageDialog
import com.project.meditationsoundmixture.viewmodels.AddAlarmViewModel
import com.project.meditationsoundmixture.viewmodels.AddAlarmViewModelFactory
import com.project.meditationsoundmixture.viewmodels.AlarmsViewModel
import com.project.meditationsoundmixture.viewmodels.AlarmsViewModelFactory
import kotlinx.coroutines.launch
import java.time.LocalDate
import java.util.*


class AlarmsFragment : Fragment() {
    private lateinit var viewModel: AlarmsViewModel
    private var sharedPref: SharedPref? = null
    var dialogMainBinding: ReminderseekbarBinding? = null
    lateinit var binding:FragmentAlarmsBinding
    private lateinit var viewModels: AddAlarmViewModel
    var dialogreminder: Dialog? = null
    var ampm:Int?=null
    var isSelected = false
    var textstr=""
    lateinit var textdays:TextView
    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
         binding = DataBindingUtil.inflate(
            layoutInflater,
            R.layout.fragment_alarms,
            container,
            false
        )
        (activity as AppCompatActivity).supportActionBar?.hide()
        val application: Application = requireNotNull(this.activity).application
        val alarmDao = AlarmDatabase.getInstance(application).alarmDao
        val viewModelFactory = AlarmsViewModelFactory(application, alarmDao)
        viewModel = ViewModelProvider(this, viewModelFactory).get(AlarmsViewModel::class.java)
        binding.viewModel = viewModel
        val adapter = AlarmAdapter(alarmDao)
        activity?.let { sharedPref = SharedPref(it) }
        binding.alarmsRecyclerView.adapter = adapter
        val calendar: Calendar = Calendar.getInstance()
        val day: Int = calendar.get(Calendar.DAY_OF_WEEK)
        val totalCount = sharedPref?.getInteger("TotalCount")!!
         ampm=calendar.get(Calendar.AM_PM)


        binding.totalweeks.text = totalCount.toString()
        if (sharedPref?.getInteger("Monday") == 1) {
            binding.iconone.setImageResource(R.drawable.ic_elicon)
        }
        if (sharedPref?.getInteger("Tuesday") == 1) {
            binding.icontwo.setImageResource(R.drawable.ic_elicon)
        }
        if (sharedPref?.getInteger("Wednesday") == 1) {
            binding.iconthree.setImageResource(R.drawable.ic_elicon)
        }
        if (sharedPref?.getInteger("Thursday") == 1) {
            binding.iconfour.setImageResource(R.drawable.ic_elicon)
        }
        if (sharedPref?.getInteger("Friday") == 1) {
            binding.iconfive.setImageResource(R.drawable.ic_elicon)
        }
        if (sharedPref?.getInteger("Saturday") == 1) {
            binding.iconsix.setImageResource(R.drawable.ic_elicon)
        }
        if (sharedPref?.getInteger("Sunday") == 1) {
            binding.iconseven.setImageResource(R.drawable.ic_elicon)
        }
        /*  if (day == Calendar.SUNDAY) {
              sharedPref?.getInteger("Sunday")
              binding.iconseven.setImageResource(R.drawable.ic_elicon)

          }
          if (day == Calendar.MONDAY) {
              sharedPref?.getInteger("Monday")
              binding.iconone.setImageResource(R.drawable.ic_elicon)

          }
          if (day == Calendar.TUESDAY) {
              sharedPref?.getInteger("Tuesday")
              binding.icontwo.setImageResource(R.drawable.ic_elicon)

          }
          if (day == Calendar.WEDNESDAY) {
              sharedPref?.getInteger("Wednesday")
              binding.iconthree.setImageResource(R.drawable.ic_elicon)


          }
          if (day == Calendar.THURSDAY) {
              sharedPref?.getInteger("Thursday")
              binding.iconfour.setImageResource(R.drawable.ic_elicon)

          }
          if (day == Calendar.FRIDAY) {
              sharedPref?.getInteger("Friday")
              binding.iconfive.setImageResource(R.drawable.ic_elicon)

          }
          if (day == Calendar.SATURDAY) {
              sharedPref?.getInteger("Saturday")
              binding.iconsix.setImageResource(R.drawable.ic_elicon)

          }
  */
        viewModel.alarms.observe(viewLifecycleOwner) {list ->
            list?.let {
                    Log.e("alarms", "onCreateView: ${list}", )
                    adapter.submitList(it)
            }
        }
        viewModel.alarmdays.observe(viewLifecycleOwner) {list ->
                val strs: StringBuilder = StringBuilder()
                list?.let {
                    if (list.isAM){
                        strs.append(list.hour.toString() + " : "+list.min.toString()+" "+"AM")
                    }else{
                        strs.append(list.hour.toString() + " : "+list.min.toString()+" "+"PM")
                    }
                    binding.Remainder.text=strs
                    binding.days.text = list.repeatDays.toString().replace("[", "").replace("]", "")
                    Log.e("TAG", "onCreateView: ${list}")
            }
        }
        setonclickListener()
        binding.lifecycleOwner = this
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        requireActivity().setDarkLightMode()
        lifecycleScope.launch {
            DataShare.getInstance(requireContext()).getAllValue(
                preferencesKey("SetAlarmTime"),false
            ).collect{
                isSelected = it
                if (it){
                   binding.reminderSwitch.isChecked = true
                }
            }
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun setonclickListener() {
        binding.addAlarmButton.setOnClickListener {
          //  binding.days.text = ""
            if (isSelected){
                requireContext().shortToast("already reminder time selected")
            }else{
                reminderDialog()
            }
        }
        binding.reminderSwitch.setOnClickListener {
            lifecycleScope.launch {
                DataShare.getInstance(requireContext()).getAllValue(
                    preferencesKey("SetAlarmTime"),false
                ).collect{
                    if (!it){
                        requireContext().shortToast("First add reminder timer")
                        binding.reminderSwitch.isChecked = false

                    }
                }
            }
        }
        binding.reminderSwitch.setOnCheckedChangeListener { buttonView, isChecked ->
            if (!isChecked){
                lifecycleScope.launch{
                    DataShare.getInstance(requireContext()).setAllValue(
                        false, preferencesKey("SetAlarmTime")
                    )
                }
                binding.apply {
                    Remainder.text = resources.getString(R.string.add_timer)
                    days.text = resources.getString(R.string.days)
                }
                if (this::viewModels.isInitialized){
                    viewModels.days.clear()
                }
                viewModel.deleteAlarm()
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Constants.isBackPressedCallback = 0
    }
    @RequiresApi(Build.VERSION_CODES.O)
    fun reminderDialog() {
        dialogMainBinding = ReminderseekbarBinding.inflate(layoutInflater)
        dialogreminder = context?.let { Dialog(it, R.style.CustomAlertDialog) }
        dialogMainBinding?.root?.let { dialogreminder?.setContentView(it) }
        val application: Application = requireNotNull(this.activity).application
        val alarmDao = AlarmDatabase.getInstance(application).alarmDao
        val viewModelFactory = AddAlarmViewModelFactory(application, alarmDao)
        viewModels = ViewModelProvider(this, viewModelFactory)[AddAlarmViewModel::class.java]
        dialogMainBinding?.viewModel = viewModels

        dialogMainBinding?.addAlarmContainer?.setOnClickListener {
            hideSoftKeyboard(requireContext(), it)
        }
        val calendarInstance = Calendar.getInstance()
        val hour = calendarInstance.get(Calendar.HOUR)
        val minute = calendarInstance.get(Calendar.MINUTE)
        val ampm = if(calendarInstance.get(Calendar.AM_PM)==0) "AM " else "PM "
        val er: StringBuilder = StringBuilder()
        er.append(hour.toString() + " : "+minute.toString()+" "+ampm)
        dialogMainBinding?.totaltime?.text=er

        dialogMainBinding?.saveAlarmButton?.setOnClickListener {
            lifecycleScope.launch {
                DataShare.getInstance(requireContext()).setAllValue(
                    true, preferencesKey("SetAlarmTime")
                )
                viewModels.saveAlarm(dialogMainBinding?.alarmTitle?.text.toString())
               // val saved = viewModels.saveAlarm(dialogMainBinding?.alarmTitle?.text.toString())
                dialogreminder?.dismiss()
//                if (!saved) {
//                    showBasicMessageDialog("Error saving alarm", requireActivity())
//                } else {
//                    dialogreminder?.dismiss()
//                }
            }
        }

        dialogMainBinding?.cancelButton?.setOnClickListener {
            dialogreminder?.dismiss()
        }

        val alarmSelectListener = View.OnClickListener {
            val popupMenu = PopupMenu(requireContext(), it)
            popupMenu.menuInflater.inflate(R.menu.alarm_types_menu, popupMenu.menu)
            popupMenu.setOnMenuItemClickListener {
                viewModels.setAlarmSound(it.title.toString())
                true
            }
            popupMenu.show()
        }

        dialogMainBinding?.selectAlarmButton?.setOnClickListener(alarmSelectListener)
        dialogMainBinding?.alarmSoundText?.setOnClickListener(alarmSelectListener)

        dialogMainBinding?.previewButton?.setOnClickListener {
            if (viewModels.mediaPlaying.value == true) {
                viewModels.stopAlarmSound()
            } else {
                viewModels.playSelectedAlarmSound()
            }
        }

        dialogMainBinding?.vibrationSwitch?.setOnCheckedChangeListener { p0, p1 ->
            viewModels.addVibration.value = p1
            if (p1) {
                val vibrator = application.getSystemService(Context.VIBRATOR_SERVICE) as Vibrator
                vibrator.vibrate(
                    VibrationEffect.createOneShot(
                        200,
                        VibrationEffect.DEFAULT_AMPLITUDE
                    )
                )
            }
        }

        dialogMainBinding?.volumeSlider?.value = 0.5F
        dialogMainBinding?.volumeSlider?.addOnChangeListener { _, value, _ ->
            viewModels.changeAlarmVolume(value)
        }

        dialogMainBinding?.hourPicker?.minValue = 1
        dialogMainBinding?.hourPicker?.maxValue = 12
        dialogMainBinding?.hourPicker?.value = viewModels.hour.toInt()

        val hourRage = 1..12
        dialogMainBinding?.hourPicker?.displayedValues = (hourRage.map {
            it.toString().padStart(2, '0')
        }).toTypedArray()
        dialogMainBinding?.hourPicker?.setOnValueChangedListener { _, _, i ->
            viewModels.hour = i
        }

        dialogMainBinding?.minutePicker?.minValue = 0
        dialogMainBinding?.minutePicker?.maxValue = 59
        dialogMainBinding?.minutePicker?.value = viewModels.minute.toInt()
        val minRange = 0..59
        dialogMainBinding?.minutePicker?.displayedValues = (minRange.map {
            it.toString().padStart(2, '0')
        }).toTypedArray()
        dialogMainBinding?.minutePicker?.setOnValueChangedListener { _, _, i ->
            viewModels.minute = i
        }

        val days = arrayListOf("Sun", "Mon", "Tue", "Wed", "Thu", "Fri", "Sat")
        days.forEach {
            val day = it
            val weekdayBinding =
                WeekdayCardBinding.inflate(layoutInflater, dialogMainBinding?.weekdaysHolder, false)
            weekdayBinding.selected = false
            weekdayBinding.weekDay = it
            val layoutParams = LinearLayout.LayoutParams(
                50,
                (50 * requireContext().resources.displayMetrics.density).toInt(),
                1f
            )
            if (it != "Sunday") {
                layoutParams.marginEnd = 10
            }
            if(viewModels.sd!=null)
            {
                viewModels.sd.clear()
            }

            weekdayBinding.button.setOnClickListener {
                if(viewModels.isAM.value ==true)
                {
                      textstr="AM"
                }
                else
                {
                    textstr="PM"
                }
                val strs: StringBuilder = StringBuilder()
                strs.append(viewModels.hour.toString() + " : "+viewModels.minute.toString()+" "+textstr)
                binding.Remainder.text=strs
                weekdayBinding.selected = !weekdayBinding.selected!!
                if (weekdayBinding.selected == true) {
                    viewModels.sd.add(day)
                    viewModels.addToSelectedDays(day)
                    val str: StringBuilder = StringBuilder()
                    for (i in viewModels.sd.indices) {
                        str.append(viewModels.sd[i] + " , ")
                        binding.days.text= str
                    }

                } else {
                    viewModels.removeFromSelectedDays(day)
                }
            }

            weekdayBinding.root.layoutParams = layoutParams
            dialogMainBinding?.weekdaysHolder?.addView(weekdayBinding.root)
        }

        dialogMainBinding?.lifecycleOwner = this

        dialogreminder?.show()
        requireActivity().setDarkLightMode()
    }
}