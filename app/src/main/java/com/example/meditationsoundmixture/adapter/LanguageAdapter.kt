package com.project.meditationsoundmixture.adapter

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.spanishspeakandtranslate.data.local.shared_pref.TinyDB
import com.project.meditationsoundmixture.Extension.disableClick
import com.project.meditationsoundmixture.Extension.getLanguage
import com.project.meditationsoundmixture.R
import com.project.meditationsoundmixture.databinding.LanguagesitemBinding
import com.project.meditationsoundmixture.model.languageitem

class LanguageAdapter(
    var context: Context,
    var languageitem: ArrayList<languageitem>,
    val mlistener: OnItemClickListener2
) :
    RecyclerView.Adapter<LanguageAdapter.ViewHolder>() {
    //var onClick: OnItemClickListener? = null
    var selectedItem: Int = 0
    private var langCode = ""
    private val tinyDB by lazy {
        TinyDB(context)
    }

//    interface setOnItemClickLitener {
//        fun onItemClicksser(lanuage: languageitem,position: Int)
//    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): LanguageAdapter.ViewHolder {

        val itemView =
            LanguagesitemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        langCode = itemView.tvLanguages.context.getLanguage()
        Log.e("language", "onCreateViewHolder: ${langCode}", )
        return ViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val list: languageitem = languageitem[position]
        holder.bindLanguages(list, mlistener)
    }

//    private fun defaultBg(holder: ViewHolder) {
//        holder.binding.imagetick.setImageResource(R.drawable.tick)
//        holder.binding.constlayout.setBackgroundResource(R.drawable.unchecklangshape)
//
//    }
//
//    private fun selectedBg(holder: ViewHolder) {
//        holder.binding.imagetick.setImageResource(R.drawable.sbgreen)
//        holder.binding.constlayout.setBackgroundResource(R.drawable.langshape)
//    }

    override fun getItemCount(): Int {
        return languageitem.size
    }

    interface OnItemClickListener2 {
        fun onItemClick(lanuage: languageitem, position: Int)
    }

    inner class ViewHolder(var binding: LanguagesitemBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bindLanguages(list: languageitem, listener: OnItemClickListener2) {
            binding.tvLanguages.text = list.language

             val language = tinyDB.getOutputCode("outputCodeKey")



            if (list.language.equals(binding.tvLanguages.context.getLanguage())) {
                Log.e("language", "enter:${list.language} ", )
                binding.imagetick.setImageResource(R.drawable.sbgreen)
               // binding.constlayout.setBackgroundResource(R.drawable.unchecklangshape)
            } else {
                binding.imagetick.setImageResource(R.drawable.tick)
            }

            Log.e("outputCodeKey", "bindLanguages: ${language}", )
            if (language == "en" && adapterPosition == 0){
                binding.imagetick.setImageResource(R.drawable.sbgreen)
                Log.e("enter", "bindLanguages: ${language}", )
            }

            binding.root.setOnClickListener {
                if (disableClick()) {
                    listener.onItemClick(list,adapterPosition)
                }
            }

        }
    }
}