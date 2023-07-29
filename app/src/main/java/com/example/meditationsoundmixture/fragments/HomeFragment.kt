package com.project.meditationsoundmixture.fragments

import android.content.ComponentName
import android.content.Intent
import android.content.ServiceConnection
import android.os.Bundle
import android.os.Handler
import android.os.IBinder
import android.os.Looper
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.SearchView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager2.widget.CompositePageTransformer
import androidx.viewpager2.widget.MarginPageTransformer
import androidx.viewpager2.widget.ViewPager2
import com.project.meditationsoundmixture.Constants.isBackPressedCallback
import com.project.meditationsoundmixture.Extension.SharedPref
import com.project.meditationsoundmixture.Extension.setDarkLightMode
import com.project.meditationsoundmixture.MeditatioinSoundServices.MusicPlayerService
import com.project.meditationsoundmixture.R
import com.project.meditationsoundmixture.ViewModel.MainViewModel
import com.project.meditationsoundmixture.adapter.ImageAdapter
import com.project.meditationsoundmixture.adapter.playMediaPlayer
import com.project.meditationsoundmixture.databinding.FragmentHomeBinding
import com.example.meditationsoundmixture.room.model.MostPlayedModel
import com.example.meditationsoundmixture.room.viewmodel.MostPlayedViewModel
import com.project.meditationsoundmixture.ui.MediationSound
import com.project.meditationsoundmixture.ui.MediationSound.Companion.arraynew
import com.project.meditationsoundmixture.ui.MediationSound.Companion.soundAdapter
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.koin.android.ext.android.inject
import kotlin.math.abs


class HomeFragment : Fragment(), ImageAdapter.OnItemClickListener, ServiceConnection {
    lateinit var binding: FragmentHomeBinding
    private lateinit var viewPager2: ViewPager2
    private lateinit var handler: Handler
    var imageList: ArrayList<Homeitemss> = ArrayList()
    var getAllMostPlayed: ArrayList<MostPlayedModel> = ArrayList()
    private lateinit var adapter: ImageAdapter
    var lst: List<Homeitemss>? = null
    private var totalCounts = 0
    lateinit var intent :Intent
    var countone = 0
    private val sharedViewModel: MainViewModel by inject()
    private val mostPlayedViewModel: MostPlayedViewModel by inject()
    private var sharedPref: SharedPref? = null
    //lateinit var intentService :Intent


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        imageList = ArrayList()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        Log.e("TAG", "onCreateView: call")
        requireActivity().setDarkLightMode()
        binding = FragmentHomeBinding.inflate(layoutInflater)
        sharedPref = SharedPref(requireContext())
        viewPager2 = binding.viewPager2
        addSongList()
        observeListValues()
        init(imageList)
        setUpTransformer()
        return binding.root
    }

    private fun addSongList() {
        imageList.clear()
        with(activity){
            imageList.add(
                Homeitemss(
                    R.drawable.bgone,
                    getString(R.string.gentletitle),
                    getString(R.string.gentlebody),
                    R.raw.slowmorning
                )
            )
            imageList.add(
                Homeitemss(
                    R.drawable.bgtwo,
                    getString(R.string.peacelaketitle),
                    getString(R.string.peacelakebody),
                    R.raw.lake
                )
            )
            imageList.add(
                Homeitemss(
                    R.drawable.bgthree,
                    getString(R.string.sunrisetitle),
                    getString(R.string.sunrisebody),
                    R.raw.sunriseambient
                )
            )
            imageList.add(
                Homeitemss(
                    R.drawable.bgfour,
                    getString(R.string.softtitle),
                    getString(R.string.softbody),
                    R.raw.piano_1
                )
            )
            imageList.add(
                Homeitemss(
                    R.drawable.bgfive,
                    getString(R.string.heaventitle),
                    getString(R.string.heavenbody),
                    R.raw.heaven
                )
            )
            imageList.add(
                Homeitemss(
                    R.drawable.bgsix,
                    getString(R.string.raintitle),
                    getString(R.string.rainbody),
                    R.raw.perfect_rain
                )
            )
            imageList.add(
                Homeitemss(
                    R.drawable.bgseven,
                    getString(R.string.inspirationtitle),
                    getString(R.string.inspirationbody),
                    R.raw.insp
                )
            )
            imageList.add(
                Homeitemss(
                    R.drawable.bgeight,
                    getString(R.string.autumntitle),
                    getString(R.string.autumnbody),
                    R.raw.autumn_forest
                )
            )
            imageList.add(
                Homeitemss(
                    R.drawable.bgnine,
                    getString(R.string.conventtitle),
                    getString(R.string.conventbody),
                    R.raw.convent
                )
            )
            imageList.add(
                Homeitemss(
                    R.drawable.bgten,
                    getString(R.string.seatitle),
                    getString(R.string.seabody),
                    R.raw.bells
                )
            )
            imageList.add(
                Homeitemss(
                    R.drawable.bgeleven,
                    getString(R.string.templetitle),
                    getString(R.string.templebody),
                    R.raw.temple_in_the_hills
                )
            )
            imageList.add(
                Homeitemss(
                    R.drawable.bgtwelve,
                    getString(R.string.mystictitle),
                    getString(R.string.mysticbody),
                    R.raw.mystic_temple
                )
            )
            imageList.add(
                Homeitemss(
                    R.drawable.bgthirteen,
                    getString(R.string.scratchingtitle),
                    getString(R.string.scratchingbody),
                    R.raw.asmr_scratching
                )
            )
            imageList.add(
                Homeitemss(
                    R.drawable.bgfouteen,
                    getString(R.string.pagetitle),
                    getString(R.string.pagebody),
                    R.raw.asmr_page_turning
                )
            )
            imageList.add(
                Homeitemss(
                    R.drawable.bgfifteen,
                    getString(R.string.chewingtitle),
                    getString(R.string.chewingbody),
                    R.raw.asmr_chewing
                )
            )
            imageList.add(
                Homeitemss(
                    R.drawable.bgsixteen,
                    getString(R.string.whisperingtitle),
                    getString(R.string.whisperingbody),
                    R.raw.asmr_whispering
                )
            )
            imageList.add(
                Homeitemss(
                    R.drawable.bgseven,
                    getString(R.string.breathingtitle),
                    getString(R.string.breathingbody),
                    R.raw.asmr_breathing
                )
            )
            imageList.add(
                Homeitemss(
                    R.drawable.bgeighteen,
                    getString(R.string.cracklingtitle),
                    getString(R.string.cracklingbody),
                    R.raw.asmr_crackling
                )
            )
            imageList.add(
                Homeitemss(
                    R.drawable.bgnineteen,
                    getString(R.string.cattitle),
                    getString(R.string.catbody),
                    R.raw.asmr_cat_purring
                )
            )
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        // binding = FragmentHomeBinding.bind(view)
        setClickListener()
        Log.e("TAG", "onViewCreated: imagelistcall")
        isBackPressedCallback = 1

    }

    override fun onResume() {
        super.onResume()
//        init(imageList)
//        setUpTransformer()
    }

    private fun observeListValues() {
//        sharedViewModel.musiclistitem.observe(viewLifecycleOwner) {
//            imageList.clear()
//            imageList.addAll(it)
//            Log.d("_mac", "init: " + imageList)
//        }
        mostPlayedViewModel.getAllmostPlayed.observe(viewLifecycleOwner) {
            getAllMostPlayed.clear()
            getAllMostPlayed = ArrayList(it)
        }
        Log.e("TAG", "onCreateView: onViewCreated")
//        Constants.isHomeSelected = {
//            if (it) {
//                Log.e("TAG", "onCreateView: ${imageList.size}", )
//                init(imageList)
//                setUpTransformer()
//            }
//        }

        mostPlayedViewModel.mostPlayedModel.observe(viewLifecycleOwner) { model ->
            Log.e("TAG", "observeListValues:${model} ")
            if (model != null) {
                lst = imageList.filter {
                    it.title == model.title
                }
            }

        }
//        mostPlayedViewModel.mostPlayedModel.observe(viewLifecycleOwner) {
//
//        }
    }

    private fun setClickListener() {

        binding.apply {
            mostlisten.setOnClickListener {
                binding.mostlisten.setTextColor(resources.getColor(R.color.convert_black_white))
                if (!lst.isNullOrEmpty()) {
                    init(lst as ArrayList<Homeitemss>)
                    setUpTransformer()
                } else {
                   // init(arrayListOf())
                   // setUpTransformer()
                }
            }
            txtNew.setOnClickListener {
                binding.mostlisten.setTextColor(resources.getColor(R.color.lightcolor))
                init(imageList)
                setUpTransformer()
            }
            searchView.setOnSearchClickListener {
                binding.txtNew.visibility = View.GONE
                binding.mostlisten.visibility = View.GONE

            }
            searchView.setOnCloseListener {
                binding.txtNew.visibility = View.VISIBLE
                binding.mostlisten.visibility = View.VISIBLE
                binding.searchView.setIconifiedByDefault(true)
                false
            }

        }
        searching(binding.searchView)
    }

    private fun searching(search: SearchView) {
        search.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                // Log.i(TAG,"Llego al querysubmit")
                return false
            }

            override fun onQueryTextChange(newText: String): Boolean {
                Log.i("TAG", "onQueryTextChange  ${newText}")
                val abc = imageList.filter {
                    it.title.lowercase().contains(newText)
                }
                init(abc as ArrayList)
                setUpTransformer()
                return true
            }
        })
    }

    private fun setUpTransformer() {
        val transformer = CompositePageTransformer()
        transformer.addTransformer(MarginPageTransformer(40))
        transformer.addTransformer { page, position ->
            val r = 1 - abs(position)
            page.scaleY = 0.85f + r * 0.14f
        }

        viewPager2.setPageTransformer(transformer)
    }

    private fun init(imageList: ArrayList<Homeitemss>) {
        CoroutineScope(Dispatchers.Main).launch {
            handler = Handler(Looper.myLooper()!!)
            adapter = ImageAdapter(requireActivity(), imageList, viewPager2, this@HomeFragment)
            viewPager2.adapter = adapter
            viewPager2.offscreenPageLimit = 3
            viewPager2.clipToPadding = false
            viewPager2.clipChildren = false
            viewPager2.getChildAt(0).overScrollMode = RecyclerView.OVER_SCROLL_NEVER
        }.invokeOnCompletion {

        }

    }

    override fun onItemClickss(position: Int, homeit: Homeitemss?) {

        val isItemExist = mostPlayedViewModel.isExist(homeit!!.title)
        if (isItemExist) {
            getAllMostPlayed.forEach { mostPlayedModel ->
                Log.e("TAG", "onItem homeit: ${homeit.title}")
                Log.e("TAG", "onItem homeit: ${homeit.title}")
                Log.e("TAG", "onItem model: ${mostPlayedModel.title}")
                Log.e("TAG", "onItem homeit: ${homeit.title.length}")
                Log.e("TAG", "onItem model: ${mostPlayedModel.title.length}")
                if (mostPlayedModel.title == homeit.title) {
                    var totalCounts = mostPlayedModel.counterPosition
                    totalCounts++
                    mostPlayedViewModel.updateMostPlayed(
                        MostPlayedModel(mostPlayedModel.id,
                        mostPlayedModel.title, totalCounts)
                    )
                }
            }
        }
        else {
            mostPlayedViewModel.insertMostPlayed(mostPlayedModel = MostPlayedModel(0,
                homeit.title,
                1)
            )
        }

        if (homeit != null) {
            sharedViewModel.homeviewpageritems.add(homeit)
            sharedViewModel.positonview = position
        }
        if (sharedViewModel.positonview == 0 || sharedViewModel.homeviewpageritems.size == 1) {
            if (sharedViewModel.getInstancemediaplayerone().isPlaying ||
                sharedViewModel.getInstancemediaplayertwo().isPlaying ||
                sharedViewModel.getInstancemediaplayerthree().isPlaying ||
                sharedViewModel.getInstancemediaplayerfour().isPlaying ||
                sharedViewModel.getInstancemediaplayerfive().isPlaying ||
                sharedViewModel.getInstancemediaplayersix().isPlaying
            ) {

                startForegroundService(homeit)
                sharedViewModel.getInstancemediaplayerone().reset()
                resetAllSongs()
                setMediationSoundIntent(position,homeit)
//                val intent = Intent(context, MediationSound::class.java)
//                intent.putExtra("GentleMorning", 0)
//                intent.putExtra("gone", homeit.title)
//                intent.putExtra("vone", homeit.description)
//                homeit.raws.let {
//                    if (it != null) {
//                        sharedViewModel.getInstancemediaplayerone()
//                            .playMediaPlayer(requireContext(), it) {
//                                context?.shortToast(it)
//                            }
//                    }
//                }
//                sharedViewModel.getInstancemediaplayerone().start()
//                sharedViewModel.getInstancemediaplayerone().isLooping = true
                if (arraynew.size > 0) {
                    arraynew.clear()
                    sharedViewModel.musicchange.clear()
                    soundAdapter.notifyDataSetChanged()
                }
                context?.startActivity(intent)

            } else {
//                intentService = Intent(context, MusicPlayerService::class.java)
//                activity?.bindService(intentService, this, AppCompatActivity.BIND_AUTO_CREATE)
//                intentService.putExtra("modelList",homeit)
//                ContextCompat.startForegroundService(requireContext(), intentService)
                startForegroundService(homeit)
                setMediationSoundIntent(position,homeit)
//                val intent = Intent(context, MediationSound::class.java)
//                intent.putExtra("GentleMorning", 0)
//                intent.putExtra("gone", homeit.title)
//                intent.putExtra("vone", homeit.description)
//                homeit.raws.let {
//                    if (it != null) {
//                        sharedViewModel.getInstancemediaplayerone()
//                            .playMediaPlayer(requireContext(), it) {
//                                context?.shortToast(it)
//                            }
//                    }
//                }
//                sharedViewModel.getInstancemediaplayerone().start()
//                sharedViewModel.getInstancemediaplayerone().isLooping = true
                context?.startActivity(intent)

            }

        }
        else if (sharedViewModel.positonview == 1 || sharedViewModel.homeviewpageritems.size == 2) {
            sharedViewModel.getInstancemediaplayerone().reset()
            if (sharedViewModel.getInstancemediaplayerone().isPlaying || sharedViewModel.getInstancemediaplayertwo().isPlaying || sharedViewModel.getInstancemediaplayerthree().isPlaying ||
                sharedViewModel.getInstancemediaplayerfour().isPlaying ||
                sharedViewModel.getInstancemediaplayerfive().isPlaying ||
                sharedViewModel.getInstancemediaplayersix().isPlaying) {
               // val intentse = Intent(context, MusicPlayerService::class.java)
                startForegroundService(homeit)
                resetAllSongs()
            }
            if (arraynew.size > 0) {
                arraynew.clear()
                sharedViewModel.musicchange.clear()
                soundAdapter.notifyDataSetChanged()
            }

            setMediationSoundIntent(position,homeit)
//            val intent = Intent(context, MediationSound::class.java)
//            intent.putExtra("GentleMorning", 1)
//            intent.putExtra("gtwo", "Peacefull Lake")
//            intent.putExtra("vtwo", "Peacefull Song With Multiple Musics")
//            homeit.raws.let {
//                if (it != null) {
//                    sharedViewModel.getInstancemediaplayerone()
//                        .playMediaPlayer(requireContext(), it) {
//                            context?.shortToast(it)
//                        }
//                }
//            }
//            sharedViewModel.getInstancemediaplayerone().start()
//            sharedViewModel.getInstancemediaplayerone().isLooping = true
            context?.startActivity(intent)
        }
        else if (sharedViewModel.positonview == 2 || sharedViewModel.homeviewpageritems.size == 3) {
            sharedViewModel.getInstancemediaplayerone().reset()
            if (sharedViewModel.getInstancemediaplayerone().isPlaying || sharedViewModel.getInstancemediaplayertwo().isPlaying || sharedViewModel.getInstancemediaplayerthree().isPlaying || sharedViewModel.getInstancemediaplayerfour().isPlaying || sharedViewModel.getInstancemediaplayerfive().isPlaying || sharedViewModel.getInstancemediaplayersix().isPlaying) {
                resetAllSongs()
            }
            if (arraynew.size > 0) {
                arraynew.clear()
                sharedViewModel.musicchange.clear()
                soundAdapter.notifyDataSetChanged()
            }
            //intentService.putExtra("modelList",homeit)
            startForegroundService(homeit)
            setMediationSoundIntent(position,homeit)
//            val intent = Intent(context, MediationSound::class.java)
//            intent.putExtra("gthree", "Sunrise")
//            intent.putExtra("vthree", "Sunrise Sound with Multiple Musics")
//            intent.putExtra("GentleMorning", 2)
//            homeit.raws.let {
//                if (it != null) {
//                    sharedViewModel.getInstancemediaplayerone()
//                        .playMediaPlayer(requireContext(), it) {
//                            context?.shortToast(it)
//                        }
//                }
//            }
//            sharedViewModel.getInstancemediaplayerone().start()
//            sharedViewModel.getInstancemediaplayerone().isLooping = true
            context?.startActivity(intent)
        }
        else if (sharedViewModel.positonview == 3 || sharedViewModel.homeviewpageritems.size == 4) {
            sharedViewModel.getInstancemediaplayerone().reset()
            if (sharedViewModel.getInstancemediaplayerone().isPlaying || sharedViewModel.getInstancemediaplayertwo().isPlaying || sharedViewModel.getInstancemediaplayerthree().isPlaying || sharedViewModel.getInstancemediaplayerfour().isPlaying || sharedViewModel.getInstancemediaplayerfive().isPlaying || sharedViewModel.getInstancemediaplayersix().isPlaying) {

                resetAllSongs()
            }
            if (arraynew.size > 0) {
                arraynew.clear()
                sharedViewModel.musicchange.clear()
                soundAdapter.notifyDataSetChanged()
            }
           // intentService.putExtra("modelList",homeit)
            startForegroundService(homeit)
            setMediationSoundIntent(position,homeit)
//            val intent = Intent(context, MediationSound::class.java)
//            intent.putExtra("GentleMorning", 3)
//            intent.putExtra("gfour", "Soft Piano")
//            intent.putExtra("vfour", "Soft Piano with Multiple Musics")
//            homeit.raws.let {
//                if (it != null) {
//                    sharedViewModel.getInstancemediaplayerone()
//                        .playMediaPlayer(requireContext(), it) {
//                            context?.shortToast(it)
//                        }
//                }
//            }
//            sharedViewModel.getInstancemediaplayerone().start()
//            sharedViewModel.getInstancemediaplayerone().isLooping = true
            context?.startActivity(intent)
        }
        else if (sharedViewModel.positonview == 4 || sharedViewModel.homeviewpageritems.size == 5) {
            sharedViewModel.getInstancemediaplayerone().reset()
            if (sharedViewModel.getInstancemediaplayerone().isPlaying || sharedViewModel.getInstancemediaplayertwo().isPlaying || sharedViewModel.getInstancemediaplayerthree().isPlaying || sharedViewModel.getInstancemediaplayerfour().isPlaying || sharedViewModel.getInstancemediaplayerfive().isPlaying || sharedViewModel.getInstancemediaplayersix().isPlaying) {
                resetAllSongs()
            }
            if (arraynew.size > 0) {
                arraynew.clear()
                sharedViewModel.musicchange.clear()
                soundAdapter.notifyDataSetChanged()
            }
          //  intentService.putExtra("modelList",homeit)
            startForegroundService(homeit)
            setMediationSoundIntent(position,homeit)
//            val intent = Intent(context, MediationSound::class.java)
//            intent.putExtra("GentleMorning", 4)
//            intent.putExtra("gfive", "Heaven Sound")
//            intent.putExtra("vfive", "Heaven Sound with Multiple Musics")
//            homeit.raws.let {
//                if (it != null) {
//                    sharedViewModel.getInstancemediaplayerone()
//                        .playMediaPlayer(requireContext(), it) {
//                            context?.shortToast(it)
//                        }
//                }
//            }
//            sharedViewModel.getInstancemediaplayerone().start()
//            sharedViewModel.getInstancemediaplayerone().isLooping = true
            context?.startActivity(intent)
        }
        else if (sharedViewModel.positonview == 5 || sharedViewModel.homeviewpageritems.size == 6) {
            sharedViewModel.getInstancemediaplayerone().reset()
            if (sharedViewModel.getInstancemediaplayerone().isPlaying || sharedViewModel.getInstancemediaplayertwo().isPlaying || sharedViewModel.getInstancemediaplayerthree().isPlaying || sharedViewModel.getInstancemediaplayerfour().isPlaying || sharedViewModel.getInstancemediaplayerfive().isPlaying || sharedViewModel.getInstancemediaplayersix().isPlaying) {

                resetAllSongs()
            }
            if (arraynew.size > 0) {
                arraynew.clear()
                sharedViewModel.musicchange.clear()
                soundAdapter.notifyDataSetChanged()
            }
           // intentService.putExtra("modelList",homeit)
            startForegroundService(homeit)
            setMediationSoundIntent(position,homeit)
//            val intent = Intent(context, MediationSound::class.java)
//            intent.putExtra("GentleMorning", 5)
//            intent.putExtra("gsix", "Perfect Rain")
//            intent.putExtra("vsix", "Multiple Sound with Rain Effects")
//            homeit.raws.let {
//                if (it != null) {
//                    sharedViewModel.getInstancemediaplayerone()
//                        .playMediaPlayer(requireContext(), it) {
//                            context?.shortToast(it)
//                        }
//                }
//            }
//            sharedViewModel.getInstancemediaplayerone().start()
//            sharedViewModel.getInstancemediaplayerone().isLooping = true
            context?.startActivity(intent)
        }
        else if (sharedViewModel.positonview == 6 || sharedViewModel.homeviewpageritems.size == 7) {
            sharedViewModel.getInstancemediaplayerone().reset()
            if (sharedViewModel.getInstancemediaplayerone().isPlaying || sharedViewModel.getInstancemediaplayertwo().isPlaying || sharedViewModel.getInstancemediaplayerthree().isPlaying || sharedViewModel.getInstancemediaplayerfour().isPlaying || sharedViewModel.getInstancemediaplayerfive().isPlaying || sharedViewModel.getInstancemediaplayersix().isPlaying) {

                resetAllSongs()
            }
            if (arraynew.size > 0) {
                arraynew.clear()
                sharedViewModel.musicchange.clear()
                soundAdapter.notifyDataSetChanged()
            }
           // intentService.putExtra("modelList",homeit)
            startForegroundService(homeit)
            setMediationSoundIntent(position,homeit)
//            val intent = Intent(context, MediationSound::class.java)
//            intent.putExtra("GentleMorning", 6)
//            intent.putExtra("gseven", "Inspiration")
//            intent.putExtra("vseven", "Multiple Sound with Rain Effects")
//            homeit.raws.let {
//                if (it != null) {
//                    sharedViewModel.getInstancemediaplayerone()
//                        .playMediaPlayer(requireContext(), it) {
//                            context?.shortToast(it)
//                        }
//                }
//            }
//            sharedViewModel.getInstancemediaplayerone().start()
//            sharedViewModel.getInstancemediaplayerone().isLooping = true
            context?.startActivity(intent)
        }
        else if (sharedViewModel.positonview == 7 || sharedViewModel.homeviewpageritems.size == 8) {
            sharedViewModel.getInstancemediaplayerone().reset()
            if (sharedViewModel.getInstancemediaplayerone().isPlaying || sharedViewModel.getInstancemediaplayertwo().isPlaying || sharedViewModel.getInstancemediaplayerthree().isPlaying || sharedViewModel.getInstancemediaplayerfour().isPlaying || sharedViewModel.getInstancemediaplayerfive().isPlaying || sharedViewModel.getInstancemediaplayersix().isPlaying) {

                resetAllSongs()
            }
            if (arraynew.size > 0) {
                arraynew.clear()
                sharedViewModel.musicchange.clear()
                soundAdapter.notifyDataSetChanged()
            }
            //intentService.putExtra("modelList",homeit)
            startForegroundService(homeit)
            setMediationSoundIntent(position,homeit)
//            val intent = Intent(context, MediationSound::class.java)
//            intent.putExtra("GentleMorning", 7)
//            intent.putExtra("geight", "Autumn Forest")
//            intent.putExtra("veight", "Forest Sound with Rain Effects")
//            homeit.raws.let {
//                if (it != null) {
//                    sharedViewModel.getInstancemediaplayerone()
//                        .playMediaPlayer(requireContext(), it) {
//                            context?.shortToast(it)
//                        }
//                }
//            }
//            sharedViewModel.getInstancemediaplayerone().start()
//            sharedViewModel.getInstancemediaplayerone().isLooping = true
            context?.startActivity(intent)
        }
        else if (sharedViewModel.positonview == 8 || sharedViewModel.homeviewpageritems.size == 9) {
            sharedViewModel.getInstancemediaplayerone().reset()
            if (sharedViewModel.getInstancemediaplayerone().isPlaying || sharedViewModel.getInstancemediaplayertwo().isPlaying || sharedViewModel.getInstancemediaplayerthree().isPlaying || sharedViewModel.getInstancemediaplayerfour().isPlaying || sharedViewModel.getInstancemediaplayerfive().isPlaying || sharedViewModel.getInstancemediaplayersix().isPlaying) {

                resetAllSongs()
            }
            if (arraynew.size > 0) {
                arraynew.clear()
                sharedViewModel.musicchange.clear()
                soundAdapter.notifyDataSetChanged()
            }
           // intentService.putExtra("modelList",homeit)
            startForegroundService(homeit)
            setMediationSoundIntent(position,homeit)
//            val intent = Intent(context, MediationSound::class.java)
//            intent.putExtra("GentleMorning", 8)
//            intent.putExtra("gnine", "Convent")
//            intent.putExtra("vnine", "Convent Areas Sound")
//            homeit.raws.let {
//                if (it != null) {
//                    sharedViewModel.getInstancemediaplayerone()
//                        .playMediaPlayer(requireContext(), it) {
//                            context?.shortToast(it)
//                        }
//                }
//            }
//            sharedViewModel.getInstancemediaplayerone().start()
//            sharedViewModel.getInstancemediaplayerone().isLooping = true
            context?.startActivity(intent)
        }
        else if (sharedViewModel.positonview == 9 || sharedViewModel.homeviewpageritems.size == 10) {
            sharedViewModel.getInstancemediaplayerone().reset()
            if (sharedViewModel.getInstancemediaplayerone().isPlaying || sharedViewModel.getInstancemediaplayertwo().isPlaying || sharedViewModel.getInstancemediaplayerthree().isPlaying || sharedViewModel.getInstancemediaplayerfour().isPlaying || sharedViewModel.getInstancemediaplayerfive().isPlaying || sharedViewModel.getInstancemediaplayersix().isPlaying) {

                resetAllSongs()
            }
            if (arraynew.size > 0) {
                arraynew.clear()
                sharedViewModel.musicchange.clear()
                soundAdapter.notifyDataSetChanged()
            }
            //intentService.putExtra("modelList",homeit)
            startForegroundService(homeit)
            setMediationSoundIntent(position,homeit)
//            val intent = Intent(context, MediationSound::class.java)
//            intent.putExtra("GentleMorning", 9)
//            intent.putExtra("gten", "Sea Side Relaxation")
//            intent.putExtra("vten", "Best Relaxation Sounds")
//
//            homeit.raws.let {
//                if (it != null) {
//                    sharedViewModel.getInstancemediaplayerone()
//                        .playMediaPlayer(requireContext(), it) {
//                            context?.shortToast(it)
//                        }
//                }
//            }
//            sharedViewModel.getInstancemediaplayerone().start()
//            sharedViewModel.getInstancemediaplayerone().isLooping = true
            context?.startActivity(intent)
        }
        else if (sharedViewModel.positonview == 10 || sharedViewModel.homeviewpageritems.size == 11) {
            sharedViewModel.getInstancemediaplayerone().reset()
            if (sharedViewModel.getInstancemediaplayerone().isPlaying || sharedViewModel.getInstancemediaplayertwo().isPlaying || sharedViewModel.getInstancemediaplayerthree().isPlaying || sharedViewModel.getInstancemediaplayerfour().isPlaying || sharedViewModel.getInstancemediaplayerfive().isPlaying || sharedViewModel.getInstancemediaplayersix().isPlaying) {

                resetAllSongs()
            }
            if (arraynew.size > 0) {
                arraynew.clear()
                sharedViewModel.musicchange.clear()
                soundAdapter.notifyDataSetChanged()
            }
            //intentService.putExtra("modelList",homeit)
            startForegroundService(homeit)
            setMediationSoundIntent(position,homeit)
//            val intent = Intent(context, MediationSound::class.java)
//            intent.putExtra("GentleMorning", 10)
//            intent.putExtra("geleven", "Temple In Hills")
//            intent.putExtra("veleven", "Temple Area Sound With Hills")
//            homeit.raws.let {
//                if (it != null) {
//                    sharedViewModel.getInstancemediaplayerone()
//                        .playMediaPlayer(requireContext(), it) {
//                            context?.shortToast(it)
//                        }
//                }
//            }
//            sharedViewModel.getInstancemediaplayerone().start()
//            sharedViewModel.getInstancemediaplayerone().isLooping = true
            context?.startActivity(intent)
        }
        else if (sharedViewModel.positonview == 11 || sharedViewModel.homeviewpageritems.size == 12) {
            sharedViewModel.getInstancemediaplayerone().reset()
            if (sharedViewModel.getInstancemediaplayerone().isPlaying || sharedViewModel.getInstancemediaplayertwo().isPlaying || sharedViewModel.getInstancemediaplayerthree().isPlaying || sharedViewModel.getInstancemediaplayerfour().isPlaying || sharedViewModel.getInstancemediaplayerfive().isPlaying || sharedViewModel.getInstancemediaplayersix().isPlaying) {

                resetAllSongs()
            }
            if (arraynew.size > 0) {
                arraynew.clear()
                sharedViewModel.musicchange.clear()
                soundAdapter.notifyDataSetChanged()
            }
            //intentService.putExtra("modelList",homeit)
            startForegroundService(homeit)
            setMediationSoundIntent(position,homeit)
//            val intent = Intent(context, MediationSound::class.java)
//            intent.putExtra("GentleMorning", 11)
//            intent.putExtra("gtwelve", "Mystic Temple")
//            intent.putExtra("vtwelve", "Temple Area Sound With Hills")
//            homeit.raws.let {
//                if (it != null) {
//                    sharedViewModel.getInstancemediaplayerone()
//                        .playMediaPlayer(requireContext(), it) {
//                            context?.shortToast(it)
//                        }
//                }
//            }
//            sharedViewModel.getInstancemediaplayerone().start()
//            sharedViewModel.getInstancemediaplayerone().isLooping = true
            context?.startActivity(intent)
        }
        else if (sharedViewModel.positonview == 12 || sharedViewModel.homeviewpageritems.size == 13) {
            sharedViewModel.getInstancemediaplayerone().reset()
            if (sharedViewModel.getInstancemediaplayerone().isPlaying || sharedViewModel.getInstancemediaplayertwo().isPlaying || sharedViewModel.getInstancemediaplayerthree().isPlaying || sharedViewModel.getInstancemediaplayerfour().isPlaying || sharedViewModel.getInstancemediaplayerfive().isPlaying || sharedViewModel.getInstancemediaplayersix().isPlaying) {

                resetAllSongs()
            }
            if (arraynew.size > 0) {
                arraynew.clear()
                sharedViewModel.musicchange.clear()
                soundAdapter.notifyDataSetChanged()
            }
            //intentService.putExtra("modelList",homeit)
            startForegroundService(homeit)
            setMediationSoundIntent(position,homeit)
//            val intent = Intent(context, MediationSound::class.java)
//            intent.putExtra("GentleMorning", 12)
//            intent.putExtra("gthirteen", "Scratching")
//            intent.putExtra("vthirteen", "Scratching Sound With Effects")
//            homeit.raws.let {
//                if (it != null) {
//                    sharedViewModel.getInstancemediaplayerone()
//                        .playMediaPlayer(requireContext(), it) {
//                            context?.shortToast(it)
//                        }
//                }
//            }
//            sharedViewModel.getInstancemediaplayerone().start()
//            sharedViewModel.getInstancemediaplayerone().isLooping = true
            context?.startActivity(intent)
        }
        else if (sharedViewModel.positonview == 13 || sharedViewModel.homeviewpageritems.size == 14) {
            sharedViewModel.getInstancemediaplayerone().reset()
            if (sharedViewModel.getInstancemediaplayerone().isPlaying || sharedViewModel.getInstancemediaplayertwo().isPlaying || sharedViewModel.getInstancemediaplayerthree().isPlaying || sharedViewModel.getInstancemediaplayerfour().isPlaying || sharedViewModel.getInstancemediaplayerfive().isPlaying || sharedViewModel.getInstancemediaplayersix().isPlaying) {

                resetAllSongs()
            }
            if (arraynew.size > 0) {
                arraynew.clear()
                sharedViewModel.musicchange.clear()
                soundAdapter.notifyDataSetChanged()
            }
            //intentService.putExtra("modelList",homeit)
            startForegroundService(homeit)
            setMediationSoundIntent(position,homeit)
//            val intent = Intent(context, MediationSound::class.java)
//            intent.putExtra("GentleMorning", 13)
//            intent.putExtra("gfourteen", "Page Turning")
//            intent.putExtra("vfourteen", "Page Turning With Effects")
//            homeit.raws.let {
//                if (it != null) {
//                    sharedViewModel.getInstancemediaplayerone()
//                        .playMediaPlayer(requireContext(), it) {
//                            context?.shortToast(it)
//                        }
//                }
//            }
//            sharedViewModel.getInstancemediaplayerone().start()
//            sharedViewModel.getInstancemediaplayerone().isLooping = true
            context?.startActivity(intent)
        }
        else if (sharedViewModel.positonview == 14 || sharedViewModel.homeviewpageritems.size == 15) {
            sharedViewModel.getInstancemediaplayerone().reset()
            if (sharedViewModel.getInstancemediaplayerone().isPlaying || sharedViewModel.getInstancemediaplayertwo().isPlaying || sharedViewModel.getInstancemediaplayerthree().isPlaying || sharedViewModel.getInstancemediaplayerfour().isPlaying || sharedViewModel.getInstancemediaplayerfive().isPlaying || sharedViewModel.getInstancemediaplayersix().isPlaying) {

                resetAllSongs()
            }
            if (arraynew.size > 0) {
                arraynew.clear()
                sharedViewModel.musicchange.clear()
                soundAdapter.notifyDataSetChanged()
            }
           // intentService.putExtra("modelList",homeit)
            startForegroundService(homeit)
            setMediationSoundIntent(position,homeit)
//            val intent = Intent(context, MediationSound::class.java)
//            intent.putExtra("GentleMorning", 14)
//            intent.putExtra("gfiveteen", "Chewing")
//            intent.putExtra("vfiveteen", "Chewing Sound Effects")
//            homeit.raws.let {
//                if (it != null) {
//                    sharedViewModel.getInstancemediaplayerone()
//                        .playMediaPlayer(requireContext(), it) {
//                            context?.shortToast(it)
//                        }
//                }
//            }
//            sharedViewModel.getInstancemediaplayerone().start()
//            sharedViewModel.getInstancemediaplayerone().isLooping = true
            context?.startActivity(intent)
        }
        else if (sharedViewModel.positonview == 15 || sharedViewModel.homeviewpageritems.size == 16) {
            sharedViewModel.getInstancemediaplayerone().reset()
            if (sharedViewModel.getInstancemediaplayerone().isPlaying || sharedViewModel.getInstancemediaplayertwo().isPlaying || sharedViewModel.getInstancemediaplayerthree().isPlaying || sharedViewModel.getInstancemediaplayerfour().isPlaying || sharedViewModel.getInstancemediaplayerfive().isPlaying || sharedViewModel.getInstancemediaplayersix().isPlaying) {

                resetAllSongs()
            }
            if (arraynew.size > 0) {
                arraynew.clear()
                sharedViewModel.musicchange.clear()
                soundAdapter.notifyDataSetChanged()
            }
           // intentService.putExtra("modelList",homeit)
            startForegroundService(homeit)
            setMediationSoundIntent(position,homeit)
//            val intent = Intent(context, MediationSound::class.java)
//            intent.putExtra("GentleMorning", 15)
//            intent.putExtra("gsixteen", "Whispering")
//            intent.putExtra("vsixteen", "Whispering Sound Effects")
//            homeit.raws.let {
//                if (it != null) {
//                    sharedViewModel.getInstancemediaplayerone()
//                        .playMediaPlayer(requireContext(), it) {
//                            context?.shortToast(it)
//                        }
//                }
//            }
//            sharedViewModel.getInstancemediaplayerone().start()
//            sharedViewModel.getInstancemediaplayerone().isLooping = true
            context?.startActivity(intent)
        }
        else if (sharedViewModel.positonview == 16 || sharedViewModel.homeviewpageritems.size == 17) {
            sharedViewModel.getInstancemediaplayerone().reset()
            if (sharedViewModel.getInstancemediaplayerone().isPlaying || sharedViewModel.getInstancemediaplayertwo().isPlaying || sharedViewModel.getInstancemediaplayerthree().isPlaying || sharedViewModel.getInstancemediaplayerfour().isPlaying || sharedViewModel.getInstancemediaplayerfive().isPlaying || sharedViewModel.getInstancemediaplayersix().isPlaying) {

                resetAllSongs()
            }
            if (arraynew.size > 0) {
                arraynew.clear()
                sharedViewModel.musicchange.clear()
                soundAdapter.notifyDataSetChanged()
            }
           // intentService.putExtra("modelList",homeit)
            startForegroundService(homeit)
            setMediationSoundIntent(position,homeit)
//            val intent = Intent(context, MediationSound::class.java)
//            intent.putExtra("GentleMorning", 16)
//            intent.putExtra("gseventeen", "Breathing")
//            intent.putExtra("vseventeen", "Breathing Sound Effects")
//            homeit.raws.let {
//                if (it != null) {
//                    sharedViewModel.getInstancemediaplayerone()
//                        .playMediaPlayer(requireContext(), it) {
//                            context?.shortToast(it)
//                        }
//                }
//            }
//            sharedViewModel.getInstancemediaplayerone().start()
//            sharedViewModel.getInstancemediaplayerone().isLooping = true
            context?.startActivity(intent)
        }
        else if (sharedViewModel.positonview == 17 || sharedViewModel.homeviewpageritems.size == 18) {
            sharedViewModel.getInstancemediaplayerone().reset()
            if (sharedViewModel.getInstancemediaplayerone().isPlaying || sharedViewModel.getInstancemediaplayertwo().isPlaying || sharedViewModel.getInstancemediaplayerthree().isPlaying || sharedViewModel.getInstancemediaplayerfour().isPlaying || sharedViewModel.getInstancemediaplayerfive().isPlaying || sharedViewModel.getInstancemediaplayersix().isPlaying) {

                resetAllSongs()
            }
            if (arraynew.size > 0) {
                arraynew.clear()
                sharedViewModel.musicchange.clear()
                soundAdapter.notifyDataSetChanged()
            }
           // intentService.putExtra("modelList",homeit)
            startForegroundService(homeit)
            setMediationSoundIntent(position,homeit)
//            val intent = Intent(context, MediationSound::class.java)
//            intent.putExtra("GentleMorning", 17)
//            intent.putExtra("geighteen", "Crackling")
//            intent.putExtra("veighteen", "Crackling Sound Effect")
//            homeit.raws.let {
//                if (it != null) {
//                    sharedViewModel.getInstancemediaplayerone()
//                        .playMediaPlayer(requireContext(), it) {
//                            context?.shortToast(it)
//                        }
//                }
//            }
//            sharedViewModel.getInstancemediaplayerone().start()
//            sharedViewModel.getInstancemediaplayerone().isLooping = true
            context?.startActivity(intent)
        }
        else if (sharedViewModel.positonview == 18 || sharedViewModel.homeviewpageritems.size == 19) {
            sharedViewModel.getInstancemediaplayerone().reset()
            if (sharedViewModel.getInstancemediaplayerone().isPlaying || sharedViewModel.getInstancemediaplayertwo().isPlaying || sharedViewModel.getInstancemediaplayerthree().isPlaying || sharedViewModel.getInstancemediaplayerfour().isPlaying || sharedViewModel.getInstancemediaplayerfive().isPlaying || sharedViewModel.getInstancemediaplayersix().isPlaying) {

                resetAllSongs()
            }
            if (arraynew.size > 0) {
                arraynew.clear()
                sharedViewModel.musicchange.clear()
                soundAdapter.notifyDataSetChanged()
            }
          //  intentService.putExtra("modelList",homeit)
            startForegroundService(homeit)
//            val intent = Intent(context, MediationSound::class.java)
//            intent.putExtra("GentleMorning", 18)
//            intent.putExtra("gnineteen", "Cat Purring")
//            intent.putExtra("vnineteen", "Cat Purring with Effects")
//            homeit.raws.let {
//                if (it != null) {
//                    sharedViewModel.getInstancemediaplayerone()
//                        .playMediaPlayer(requireContext(), it) {
//                            context?.shortToast(it)
//                        }
//                }
//            }
//            sharedViewModel.getInstancemediaplayerone().start()
//            sharedViewModel.getInstancemediaplayerone().isLooping = true
            context?.startActivity(intent)
        }

    }

    private fun startForegroundService(homeit: Homeitemss?) {
        val intentService = Intent(context, MusicPlayerService::class.java)
        intentService.putExtra("modelList", homeit)
        activity?.bindService(intentService,this, AppCompatActivity.BIND_AUTO_CREATE)
        ContextCompat.startForegroundService(requireContext(), intentService)

    }

    private fun setMediationSoundIntent(position: Int, homeit: Homeitemss) {
         intent = Intent(context, MediationSound::class.java)
        intent.putExtra("GentleMorning", position)
        intent.putExtra("gone", homeit.title)
        intent.putExtra("vone", homeit.description)
        homeit.raws.let {
            if (it != null) {
                sharedViewModel.getInstancemediaplayerone()
                    .playMediaPlayer(requireContext(), it) {
                        Log.e("TAG", "setMediationSoundIntent: ${it}", )
                        //context?.shortToast(it)
                    }
            }
        }
        sharedViewModel.getInstancemediaplayerone().start()
        sharedViewModel.getInstancemediaplayerone().isLooping = true
    }

    private fun resetAllSongs() {
        sharedViewModel.getInstancemediaplayertwo().reset()
        sharedViewModel.getInstancemediaplayerthree().reset()
        sharedViewModel.getInstancemediaplayerfour().reset()
        sharedViewModel.getInstancemediaplayerfive().reset()
        sharedViewModel.getInstancemediaplayersix().reset()
    }


    private val viewModel: MainViewModel by inject()
    override fun onServiceConnected(p0: ComponentName?, service: IBinder?) {
        //Toast.makeText(context, "kdkdkdk", Toast.LENGTH_SHORT).show()
        val binder = service as MusicPlayerService.MyBinder
        viewModel.musicService = binder.currentService()

    }

    override fun onServiceDisconnected(p0: ComponentName?) {
        //myMusicService = null
      //  context?.unbindService(this)
    }


}