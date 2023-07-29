package com.project.meditationsoundmixture.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.switchmaterial.SwitchMaterial
import com.project.meditationsoundmixture.R
import com.project.meditationsoundmixture.database.AlarmDao
import com.project.meditationsoundmixture.databinding.AlarmListItemBinding
import com.project.meditationsoundmixture.models.Alarm

import kotlinx.coroutines.runBlocking

class AlarmAdapter(private val alarmDao: AlarmDao) :
    ListAdapter<Alarm, AlarmAdapter.ViewHolder>(AlarmDiffCallback()) {
    class ViewHolder(private val binding: AlarmListItemBinding, private val alarmDao: AlarmDao) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(alarm: Alarm) {
            binding.alarm = alarm
            binding.disableSwitch.isChecked = !alarm.disabled

            binding.disableSwitch.setOnCheckedChangeListener { switch, b ->
                binding.alarm?.let {
                    it.disabled = !b
                    (switch as SwitchMaterial).apply {
                        if (it.disabled) {
                            trackDrawable.setTint(resources.getColor(R.color.lightGrey))
                        } else {
                            trackDrawable.setTint(resources.getColor(R.color.primaryColorLight))
                        }
                    }
                }
                runBlocking { alarmDao.update(requireNotNull(binding.alarm)) }
            }

            binding.deleteIcon.setOnClickListener {
                //runBlocking { alarmDao.delete(alarm) }
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val binding = AlarmListItemBinding.inflate(layoutInflater, parent, false)
        return ViewHolder(binding, alarmDao)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val alarm = getItem(position)
        holder.bind(alarm)
    }
}

class AlarmDiffCallback : DiffUtil.ItemCallback<Alarm>() {
    override fun areItemsTheSame(oldItem: Alarm, newItem: Alarm): Boolean {
        return oldItem.dbId == newItem.dbId
    }

    override fun areContentsTheSame(oldItem: Alarm, newItem: Alarm): Boolean {
        return oldItem == newItem
    }
}