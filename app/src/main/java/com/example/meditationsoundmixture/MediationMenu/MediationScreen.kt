package com.project.meditationsoundmixture.MediationMenu

import android.content.Intent
import android.media.MediaPlayer
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.GridLayoutManager
import com.project.meditationsoundmixture.Extension.shortToast
import com.project.meditationsoundmixture.MediationMenu.Adapter.MeditationMenuAdapter
import com.project.meditationsoundmixture.ViewModel.MainViewModel
import com.project.meditationsoundmixture.databinding.ActivityMediationScreenBinding
import com.project.meditationsoundmixture.model.MusicItems
import com.project.meditationsoundmixture.model.datamusicitems
import com.project.meditationsoundmixture.ui.MediationSound
import org.koin.android.ext.android.inject

class MediationScreen : AppCompatActivity() {
    private val binding by lazy {
        ActivityMediationScreenBinding.inflate(layoutInflater)
    }

    var ar: ArrayList<MusicItems> = ArrayList()
    var arone: ArrayList<MusicItems> = ArrayList()
    private lateinit var adapter: MeditationMenuAdapter
    private val mainViewModel: MainViewModel by inject()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        mainViewModel.muiclist.observe(this, Observer {
            ar.addAll(it)
           // shortToast(ar.size.toString())

        })
        recyclerdata()



    }
    fun recyclerdata()
    {

        val layoutManager = GridLayoutManager(this, 4)
        binding.recycler.layoutManager = layoutManager
        adapter = MeditationMenuAdapter(this, ar)
        binding.recycler.adapter = adapter
    }

    /*override fun onItemClick(position: Int, musicItemObj: MusicItems) {
        mainViewModel.position = position
        mainViewModel.musicItem = musicItemObj
        mainViewModel.musicchange.add(musicItemObj)

       *//* MediationSound.arrayd.add(musicItemObj)*//*
        val intent = Intent(this, MediationSound::class.java)
        startActivity(intent)
        finish()


    }*/

}