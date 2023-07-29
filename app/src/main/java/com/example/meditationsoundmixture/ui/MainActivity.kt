package com.project.meditationsoundmixture.ui

import android.os.Bundle
import androidx.lifecycle.LifecycleObserver
import androidx.navigation.NavController
import androidx.navigation.NavDestination
import androidx.navigation.findNavController
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.setupWithNavController
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.project.meditationsoundmixture.Constants.isBackPressedCallback
import com.project.meditationsoundmixture.Extension.setDarkLightMode
import com.project.meditationsoundmixture.R
import com.project.meditationsoundmixture.databinding.ActivityMainBinding
import com.project.meditationsoundmixture.databinding.ExitBottomSheetBinding
import com.project.meditationsoundmixture.util.BaseClass

class MainActivity : BaseClass(),LifecycleObserver {
    private val binding by lazy {
        ActivityMainBinding.inflate(layoutInflater)
    }
    lateinit var navController :NavController

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        checkUpdate(binding.root)
        setLocalizationAppStart()
        navController = findNavController(R.id.nav_host_fragment)
        val navView: BottomNavigationView = findViewById(R.id.bottom_nav_view)
        navView.setupWithNavController(navController)
        navView.itemIconTintList = null
       // bottomClickListner()

    }

    override fun onResume() {
        super.onResume()
        this@MainActivity.setDarkLightMode()
    }

//    private fun bottomClickListner() {
//        navController.addOnDestinationChangedListener { controller, destination, arguments ->
//            if (destination.id == R.id.homeFragment) {
//                // isHomeSelected?.invoke(true)
//            }
//        }
//    }

    override fun onBackPressed() {
        if (navController.currentDestination?.id  == R.id.homeFragment){
              //  finishAffinity()
                bottomSheetExit()
            }else{
            super.onBackPressed()
        }

       // super.onBackPressed()
    }

    private fun bottomSheetExit() {
        val dialog = BottomSheetDialog(this,R.style.BottomSheetDialogTheme)
        val exitBottomSheetDialogBinding = ExitBottomSheetBinding.inflate(layoutInflater)
        exitBottomSheetDialogBinding.cancelBtn.setOnClickListener {
            dialog.dismiss()
        }
        exitBottomSheetDialogBinding.exitBtn.setOnClickListener {
            finishAffinity()
        }
        dialog.setCancelable(true)
        dialog.setContentView(exitBottomSheetDialogBinding.root)
        dialog.show()
    }

    fun closeActivity() {
        finish()
    }


}