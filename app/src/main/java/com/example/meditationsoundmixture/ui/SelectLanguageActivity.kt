package com.project.meditationsoundmixture.ui

import android.annotation.SuppressLint
import android.content.Context
import android.content.res.Configuration
import android.content.res.Resources
import android.os.Bundle
import android.util.DisplayMetrics
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.LinearLayoutManager

import com.project.meditationsoundmixture.Extension.getLanguage
import com.project.meditationsoundmixture.Extension.setLanguage
import com.project.meditationsoundmixture.Extension.shortToast
import com.project.meditationsoundmixture.adapter.ChangeLanguageAdapter
import com.project.meditationsoundmixture.databinding.ActivityLanguagesscreenBinding
import com.project.meditationsoundmixture.model.ChangeLanguage
import com.project.meditationsoundmixture.model.languageitem
import com.project.meditationsoundmixture.util.BaseClass
import java.util.*

class SelectLanguageActivity : BaseClass(), ChangeLanguageAdapter.OnItemClickListener {

    val mBinding: ActivityLanguagesscreenBinding by lazy {
        ActivityLanguagesscreenBinding.inflate(layoutInflater)
    }
    private var arraylanguages: ArrayList<ChangeLanguage> = ArrayList()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(mBinding.root)

        setChangeLanguageAdapter()
    }


    @SuppressLint("NotifyDataSetChanged")
    private fun setChangeLanguageAdapter() {

        arraylanguages.add(ChangeLanguage("English(Default)","en"))
        arraylanguages.add(ChangeLanguage("Spanish", "es"))
        arraylanguages.add(ChangeLanguage("Hindi", "hi"))
        arraylanguages.add(ChangeLanguage("Arabic", "ar"))
        val adapter =
            ChangeLanguageAdapter(arraylanguages, this)
        val layoutManager = LinearLayoutManager(applicationContext)
        mBinding.recycleLan.layoutManager = layoutManager
        mBinding.recycleLan.itemAnimator = DefaultItemAnimator()
        mBinding.recycleLan.adapter = adapter
    }

    override fun onItemClick(langCode: String, langName: String) {
//        setLanguage(langName)
        if (langCode ==getLanguage()) {
            shortToast("already selected")
            return
        }
        setLanguage(langCode)
        changeLocale(this, langCode)
    }

    fun changeLocale(context: Context, code: String) {
        val myLocale = Locale(code)
        val res: Resources = context.resources
        val dm: DisplayMetrics = res.displayMetrics
        val conf: Configuration = res.configuration
        conf.locale = myLocale
        res.updateConfiguration(conf, dm)
    }

}