package com.project.meditationsoundmixture.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.project.meditationsoundmixture.Extension.disableClick
import com.project.meditationsoundmixture.Extension.getLanguage
import com.project.meditationsoundmixture.R
import com.project.meditationsoundmixture.databinding.LanguagesitemBinding
import com.project.meditationsoundmixture.model.ChangeLanguage

class ChangeLanguageAdapter(
    private val changeLanguageList: MutableList<ChangeLanguage>,
    private val listener: OnItemClickListener,
) :
    RecyclerView.Adapter<ChangeLanguageAdapter.ChangeLanguageViewHolder>() {
    private var langCode = ""
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int,
    ): ChangeLanguageViewHolder {
        val binding = LanguagesitemBinding.inflate(
            LayoutInflater.from(parent.context), parent, false
        )
        langCode = binding.tvLanguages.context.getLanguage()
        return ChangeLanguageViewHolder(binding)
    }

    override fun onBindViewHolder(
        holder: ChangeLanguageViewHolder,
        position: Int,
    ) {
        val list: ChangeLanguage = changeLanguageList[position]
        holder.bindLanguages(list, listener)
    }

    override fun getItemCount(): Int {

        return changeLanguageList.size
    }

    override fun getItemViewType(position: Int): Int {
        return 0
    }

    inner class ChangeLanguageViewHolder(private val binding: LanguagesitemBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bindLanguages(list: ChangeLanguage, listener: OnItemClickListener) {
            binding.tvLanguages.text = list.name

            if (list.code.equals(binding.tvLanguages.context.getLanguage())) {
                binding.imagetick.setImageResource(R.drawable.sbgreen)
            } else {
                binding.imagetick.setImageResource(R.drawable.tick)
            }

            binding.root.setOnClickListener {
                if (disableClick()) {
                    listener.onItemClick(list.code, list.name)
                }
            }
        }

    }

    interface OnItemClickListener {
        fun onItemClick(langCode: String, langName: String)
    }
}