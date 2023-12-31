package com.project.meditationsoundmixture.fragments

import android.app.Application
import android.content.Context
import android.os.Build
import android.os.Bundle
import android.os.VibrationEffect
import android.os.Vibrator
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.PopupMenu
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.navigation.findNavController
import com.project.meditationsoundmixture.R
import com.project.meditationsoundmixture.database.AlarmDatabase
import com.project.meditationsoundmixture.databinding.FragmentAddAlarmBinding
import com.project.meditationsoundmixture.databinding.WeekdayCardBinding
import com.project.meditationsoundmixture.util.hideSoftKeyboard
import com.project.meditationsoundmixture.util.showBasicMessageDialog
import com.project.meditationsoundmixture.viewmodels.AddAlarmViewModel
import com.project.meditationsoundmixture.viewmodels.AddAlarmViewModelFactory
import kotlinx.coroutines.launch

class AddAlarmFragment : Fragment() {
    private lateinit var viewModel: AddAlarmViewModel
    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        (activity as AppCompatActivity).supportActionBar?.hide()
        val binding: FragmentAddAlarmBinding = DataBindingUtil.inflate(
            inflater,
            R.layout.fragment_add_alarm, container, false
        )

        val application: Application = requireNotNull(this.activity).application
        val alarmDao = AlarmDatabase.getInstance(application).alarmDao
        val viewModelFactory = AddAlarmViewModelFactory(application, alarmDao)
        viewModel = ViewModelProvider(this, viewModelFactory).get(AddAlarmViewModel::class.java)
        binding.viewModel = viewModel

        binding.addAlarmContainer.setOnClickListener {
            hideSoftKeyboard(requireContext(), it)
        }

        binding.saveAlarmButton.setOnClickListener {
            lifecycleScope.launch {
                val saved = viewModel.saveAlarm(binding.alarmTitle.text.toString())
                if (!saved) {
                    showBasicMessageDialog("Error saving alarm", requireActivity())
                } else {
                    it.findNavController().navigateUp()
                }
            }
        }

        binding.cancelButton.setOnClickListener {
            it.findNavController().navigateUp()
        }

        val alarmSelectListener = View.OnClickListener {
            val popupMenu = PopupMenu(requireContext(), it)
            popupMenu.menuInflater.inflate(R.menu.alarm_types_menu, popupMenu.menu)
            popupMenu.setOnMenuItemClickListener {
                viewModel.setAlarmSound(it.title.toString())
                true
            }
            popupMenu.show()
        }

        binding.selectAlarmButton.setOnClickListener(alarmSelectListener)
        binding.alarmSoundText.setOnClickListener(alarmSelectListener)

        binding.previewButton.setOnClickListener {
            if (viewModel.mediaPlaying.value == true) {
                viewModel.stopAlarmSound()
            } else {
                viewModel.playSelectedAlarmSound()
            }
        }

        binding.vibrationSwitch.setOnCheckedChangeListener { p0, p1 ->
            viewModel.addVibration.value = p1
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

        binding.volumeSlider.value = 0.5F
        binding.volumeSlider.addOnChangeListener { _, value, _ ->
            viewModel.changeAlarmVolume(value)
        }

        binding.hourPicker.minValue = 1
        binding.hourPicker.maxValue = 12
        binding.hourPicker.value = viewModel.hour.toInt()
        val hourRage = 1..12
        binding.hourPicker.displayedValues = (hourRage.map {
            it.toString().padStart(2, '0')
        }).toTypedArray()
        binding.hourPicker.setOnValueChangedListener { _, _, i ->
            viewModel.hour = i
        }

        binding.minutePicker.minValue = 0
        binding.minutePicker.maxValue = 59
        binding.minutePicker.value = viewModel.minute.toInt()
        val minRange = 0..59
        binding.minutePicker.displayedValues = (minRange.map {
            it.toString().padStart(2, '0')
        }).toTypedArray()
        binding.minutePicker.setOnValueChangedListener { _, _, i ->
            viewModel.minute = i
        }


        val days = arrayListOf("M", "T", "W", "T", "F", "S", "S")
        days.forEach {
            var day = it
            val weekdayBinding =
                WeekdayCardBinding.inflate(layoutInflater, binding.weekdaysHolder, false)
            weekdayBinding.selected = false
            weekdayBinding.weekDay = it
            val layoutParams = LinearLayout.LayoutParams(
                0,
                (80 * requireContext().resources.displayMetrics.density).toInt(),
                1f
            )
            if (it != "S") {
                layoutParams.marginEnd = 10
            }

            weekdayBinding.button.setOnClickListener {
                weekdayBinding.selected = !weekdayBinding.selected!!
                if (weekdayBinding.selected == true) {
                    viewModel.addToSelectedDays(day)
                } else {
                    viewModel.removeFromSelectedDays(day)
                }
            }

            weekdayBinding.root.layoutParams = layoutParams
            binding.weekdaysHolder.addView(weekdayBinding.root)
        }

        binding.lifecycleOwner = this
        return binding.root
    }
}