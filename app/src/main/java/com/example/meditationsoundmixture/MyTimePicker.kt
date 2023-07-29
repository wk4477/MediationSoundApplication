package com.project.meditationsoundmixture

import android.app.AlertDialog
import android.app.Dialog
import android.content.Context
import android.content.SharedPreferences
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.provider.ContactsContract.Data
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
import android.widget.Button
import android.widget.LinearLayout
import android.widget.NumberPicker
import android.widget.TextView
import androidx.appcompat.widget.SwitchCompat
import androidx.datastore.preferences.core.preferencesKey
import androidx.fragment.app.DialogFragment
import androidx.lifecycle.lifecycleScope
import com.project.meditationsoundmixture.Extension.shortToast
import com.project.meditationsoundmixture.datastore.DataShare
import kotlinx.coroutines.launch
import java.util.*

class MyTimePicker : DialogFragment() {

    private lateinit var timePickerLayout: View
    private lateinit var hourPicker: NumberPicker
    private lateinit var minPicker: NumberPicker
    private lateinit var secPicker: NumberPicker
    private lateinit var sharedPreferences : SharedPreferences

    private lateinit var mContext :Context

    private var onTimeSetOption:
                (minute: Int, second: Int) -> Unit = { _, _ -> }
    private var timeSetText: String = "Ok"

    private var onCancelOption: () -> Unit = {}
    private var cancelText: String = "Cancel"

    /**
     * Which value will appear a the start of
     * the Dialog for the Hour picker.
     * Default value is 0.
     */
    var initialHour: Int = 0

    /**
     * Which value will appear a the start of
     * the Dialog for the Minute picker.
     * Default value is 0.
     */
    var initialMinute: Int = 0

    /**
     * Which value will appear a the start of
     * the Dialog for the Second picker.
     * Default value is 0.
     */
    var initialSeconds: Int = 0

    /**
     * Max value for the Hour picker.
     * Default value is 23.
     */
    var maxValueHour: Int = 23

    /**
     * Max value for the Minute picker.
     * Default value is 59.
     */
    var maxValueMinute: Int = 59

    /**
     * Max value for the Second picker.
     * Default value is 59.
     */
    var maxValueSeconds: Int = 59

    /**
     * Min value for the Hour picker.
     * Default value is 0.
     */
    var minValueHour: Int = 0

    /**
     * Min value for the Minute picker.
     * Default value is 0.
     */
    var minValueMinute: Int = 0

    /**
     * Min value for the Second picker.
     * Default value is 0.
     */
    var minValueSecond: Int = 0

    /**
     * Default value is true.
     * If set to false the hour picker is not
     * visible in the Dialog
     */
    var includeHours: Boolean = true

    private var title: String? = null

    override fun onStart() {
        super.onStart()
        val width = (resources.displayMetrics.widthPixels * 0.90).toInt()
        dialog!!.window?.setLayout(width, ViewGroup.LayoutParams.WRAP_CONTENT)
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        mContext = context
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        sharedPreferences = mContext.getSharedPreferences("Mypref", Context.MODE_PRIVATE)
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        return activity?.let { it ->
            // Use the Builder class for convenient dialog construction
            val builder = AlertDialog.Builder(it)
            timePickerLayout = requireActivity()
                .layoutInflater.inflate(R.layout.my_time_picker_content, null)
            setupTimePickerLayout()
            builder.setView(timePickerLayout)
            val button = timePickerLayout.findViewById<Button>(R.id.addtimer)
            val textview = timePickerLayout.findViewById<LinearLayout>(R.id.canceltxt)
            val timecl = timePickerLayout.findViewById<TextView>(R.id.time)
            val switch = timePickerLayout.findViewById<SwitchCompat>(R.id.switch_Timer)
            val calendarInstance = Calendar.getInstance()
            val hour = calendarInstance.get(Calendar.HOUR)
            val second = calendarInstance.get(Calendar.SECOND)
            val minute = calendarInstance.get(Calendar.MINUTE)
            val er: StringBuilder = StringBuilder()
            secPicker.setOnValueChangedListener { picker, oldVal, newVal ->
                if (newVal >0){
                    button.setBackgroundResource(R.drawable.add_timer_background_selected)
                }else{
                    button.setBackgroundResource(R.drawable.add_timer_background_unselected)
                }
            }
            minPicker.setOnValueChangedListener { picker, oldVal, newVal ->
                if (newVal >0){
                    button.setBackgroundResource(R.drawable.add_timer_background_selected)
                }else{
                    button.setBackgroundResource(R.drawable.add_timer_background_unselected)
                }
            }

            lifecycleScope.launch {
                DataShare.getInstance(mContext).getAllValue(preferencesKey("switchValue"),false)
                    .collect{
                       if (it){
                           switch.isChecked = true
                       }
                 }
            }

//            lifecycleScope.launch {
//                DataShare.getInstance(requireActivity()).getAllValue(
//                    preferencesKey("addTimer"),"0:0"
//                ).collect {
//                    timecl.text = it
//                    switch.isChecked = !(it.isEmpty() || it == "0:0")
//                    if (it.isEmpty() || it == "0:0"){
//                        button.setBackgroundResource(R.drawable.add_timer_background_unselected)
//                    }else{
//                        button.setBackgroundResource(R.drawable.add_timer_background_selected)
//                    }
//                }
//            }
            lifecycleScope.launch {
                DataShare.getInstance(requireActivity()).getAllValue(
                    preferencesKey("addTimer"),"0:0"
                ).collect {
                    if (it.isEmpty() || it == "0:0")
                    {
                        requireContext().shortToast("Select time first")
                        switch.isChecked = false
                        // dismiss()
                    }
                }
            }
            switch.setOnClickListener {
            lifecycleScope.launch {
                DataShare.getInstance(requireActivity()).getAllValue(
                    preferencesKey("addTimer"),"0:0"
                ).collect {
                    if (it.isEmpty() || it == "0:0"){
                       // button.setBackgroundResource(R.drawable.add_timer_background_unselected)
                    }else{
                        switch.isChecked = true
                      //  button.setBackgroundResource(R.drawable.add_timer_background_selected)
                    }
                }
            }
            }
            switch.setOnCheckedChangeListener { buttonView, isChecked ->

                    val editor: SharedPreferences.Editor = sharedPreferences.edit()
                    editor.putBoolean("switchValue",isChecked)
                    editor.apply()

//                if (isChecked) {
//                    lifecycleScope.launch {
//                        DataShare.getInstance(mContext).setAllValue(
//                            true, preferencesKey("switchValue")
//                        )
//                    }
//                }
//                else {
//                    lifecycleScope.launch {
//                        DataShare.getInstance(requireActivity()).setAllValue(
//                            "0:0",
//                            preferencesKey("addTimer")
//                        )
//                    }
//                }
            }
            button.setOnClickListener {

             //   if (!switch.isChecked){
                    var hour = hourPicker.value
                    Log.e("addTimer", "onCreateDialog: ${minPicker.value}", )
                    Log.e("addTimer", "onCreateDialog: ${secPicker.value}", )
                    if (minPicker.value == 0 && secPicker.value == 0){
                        requireContext().shortToast("select time again")
                        lifecycleScope.launch {
                            DataShare.getInstance(requireActivity()).setAllValue(
                                er.toString(),
                                preferencesKey("addTimer")
                            )
                        }
                    }
                    else{
                        if (!includeHours) hour = 0
                        onTimeSetOption(minPicker.value, secPicker.value)
                        Log.e("TAG", "setOnClickListener:${onTimeSetOption(minPicker.value, secPicker.value)} ", )
                        //  timecl.text = onTimeSetOption(minPicker.value, secPicker.value).toString()
                        er.append(minPicker.value.toString() + ":" + secPicker.value.toString())
                        lifecycleScope.launch {
                            DataShare.getInstance(requireActivity()).setAllValue(
                                er.toString(),
                                preferencesKey("addTimer")
                            )
                        }
                        //  switch.isChecked = true
                        button.setBackgroundResource(R.drawable.add_timer_background_selected)
                        er.clear()
                    }
            //    }
            //    else{
                  //  mContext.shortToast("please on the ")
             //   }



//                if (!switch.isChecked){
//                    var hour = hourPicker.value
//                    Log.e("addTimer", "onCreateDialog: ${minPicker.value}", )
//                    Log.e("addTimer", "onCreateDialog: ${secPicker.value}", )
//                    if (minPicker.value == 0 && secPicker.value == 0){
//                        requireContext().shortToast("select time again")
//                        lifecycleScope.launch {
//                            DataShare.getInstance(requireActivity()).setAllValue(
//                                er.toString(),
//                                preferencesKey("addTimer")
//                            )
//                        }
//                    }
//                    else{
//                        if (!includeHours) hour = 0
//                        onTimeSetOption(minPicker.value, secPicker.value)
//                        Log.e("TAG", "setOnClickListener:${onTimeSetOption(minPicker.value, secPicker.value)} ", )
//                        //  timecl.text = onTimeSetOption(minPicker.value, secPicker.value).toString()
//                        er.append(minPicker.value.toString() + ":" + secPicker.value.toString())
//                        lifecycleScope.launch {
//                            DataShare.getInstance(requireActivity()).setAllValue(
//                                er.toString(),
//                                preferencesKey("addTimer")
//                            )
//                        }
//                        switch.isChecked = true
//                        button.setBackgroundResource(R.drawable.add_timer_background_selected)
//                        er.clear()
//                    }
//                }
//                else{
//                   requireContext().shortToast( "Already Time selected")
//                }
              //  dismiss()

            }
            textview.setOnClickListener {
                dismiss()
            }

            // Create the AlertDialog object and return it
            builder.create()
        } ?: throw IllegalStateException("Activity cannot be null")
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        dialog?.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        dialog?.window?.requestFeature(Window.FEATURE_NO_TITLE)
        return super.onCreateView(inflater, container, savedInstanceState)
    }

    fun setTitle(title: String) {
        this.title = title
    }

    fun setOnTimeSetOption(text: String, onTimeSet: (minute: Int, second: Int) -> Unit) {
        onTimeSetOption = onTimeSet
        timeSetText = text
    }

    fun setOnCancelOption(text: String, onCancelOption: () -> Unit) {
        this.onCancelOption = onCancelOption
        cancelText = text
    }

    private fun setupTimePickerLayout() {
        bindViews()

        setupMaxValues()
        setupMinValues()
        setupInitialValues()

        if (!includeHours) {
            timePickerLayout.findViewById<LinearLayout>(R.id.hours_container).visibility = View.GONE
        }
    }

    private fun bindViews() {
        hourPicker = timePickerLayout.findViewById(R.id.hours)
        minPicker = timePickerLayout.findViewById(R.id.minutes)
        secPicker = timePickerLayout.findViewById(R.id.seconds)
    }

    private fun setupMaxValues() {
        hourPicker.maxValue = maxValueHour
        minPicker.maxValue = maxValueMinute
        secPicker.maxValue = maxValueSeconds
    }

    private fun setupMinValues() {
        hourPicker.minValue = minValueHour
        minPicker.minValue = minValueMinute
        secPicker.minValue = minValueSecond
    }

    private fun setupInitialValues() {
        hourPicker.value = initialHour
        minPicker.value = initialMinute
        secPicker.value = initialSeconds
    }
}
