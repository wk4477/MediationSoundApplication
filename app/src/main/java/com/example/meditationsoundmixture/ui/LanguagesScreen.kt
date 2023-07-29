package com.project.meditationsoundmixture.ui

import android.os.Bundle
import android.util.Log
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.spanishspeakandtranslate.data.local.shared_pref.TinyDB
import com.project.meditationsoundmixture.Constants.languageSelected
import com.project.meditationsoundmixture.Extension.getLanguage
import com.project.meditationsoundmixture.Extension.setLanguage
import com.project.meditationsoundmixture.Extension.shortToast
import com.project.meditationsoundmixture.adapter.LanguageAdapter
import com.project.meditationsoundmixture.databinding.ActivityLanguagesscreenBinding
import com.project.meditationsoundmixture.model.languageitem
import com.project.meditationsoundmixture.util.BaseClass

class LanguagesScreen : BaseClass(),LanguageAdapter.OnItemClickListener2 {
    private val binding by lazy {
        ActivityLanguagesscreenBinding.inflate(layoutInflater)
    }
    lateinit var adapter: LanguageAdapter
    lateinit var layoutManager: LinearLayoutManager
    private var arraylanguages: ArrayList<languageitem> = ArrayList()

    private val tinyDB by lazy {
        TinyDB(this)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        addLanguages()
        clickListener()
        Log.e("language", "onCreate: ${this.getLanguage()}", )
        layoutManager =
            LinearLayoutManager(this@LanguagesScreen, LinearLayoutManager.VERTICAL, false)
        binding.recycleLan.layoutManager = layoutManager
        adapter = LanguageAdapter(this@LanguagesScreen, arraylanguages,this)
        binding.recycleLan.adapter = adapter
    }

    private fun clickListener() {
        binding.apply {
            backLanguage.setOnClickListener {
                onBackPressed()
            }
        }
    }

    private fun addLanguages() {
        arraylanguages.add(languageitem("English(Default)", iscick = false))
        arraylanguages.add(languageitem("Spanish", iscick = false))
        arraylanguages.add(languageitem("Hindi", iscick = false))
        arraylanguages.add(languageitem("Arabic", iscick = false))
    }

    override fun onItemClick(lanuage: languageitem, position: Int) {

        setLanguage(lanuage.language)
        if (languageSelected == lanuage.language){
            shortToast("already selected")
            return
        }
//        if (lanuage.language ==getLanguage()) {
//            shortToast("already selected")
//            return
//        }
        when (lanuage.language) {
            "English(Default)" -> {
                tinyDB.putOutputCode("outputCodeKey", "en")
                switchLocale(this@LanguagesScreen, "en")
            }
            "Spanish" -> {
                tinyDB.putOutputCode("outputCodeKey", "es")
                switchLocale(this@LanguagesScreen, "es")

            }
            "Hindi" -> {
                tinyDB.putOutputCode("outputCodeKey", "hi")
                switchLocale(this@LanguagesScreen, "hi")

            }
            "Arabic" -> {
                tinyDB.putOutputCode("outputCodeKey", "ar")
                switchLocale(this@LanguagesScreen, "ar")
            }
            else -> {}
        }
        languageSelected = lanuage.language
    }

}