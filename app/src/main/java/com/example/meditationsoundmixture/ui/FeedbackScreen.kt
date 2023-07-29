package com.project.meditationsoundmixture.ui

import android.content.ActivityNotFoundException
import android.content.Intent
import android.graphics.Color
import android.net.Uri
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.provider.CalendarContract.Colors
import android.util.Log
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.hsalf.smilerating.BaseRating
import com.hsalf.smilerating.SmileRating
import com.project.meditationsoundmixture.Extension.shortToast
import com.project.meditationsoundmixture.R
import com.project.meditationsoundmixture.databinding.ActivityFeedbackScreenBinding
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class FeedbackScreen : AppCompatActivity() {
    private val binding by lazy {
        ActivityFeedbackScreenBinding.inflate(layoutInflater)
    }
    var rating = 0
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
//        with(binding)
//        {
//            leftrocketAnim.playAnimation()
//            Handler(Looper.getMainLooper()).postDelayed({
//                binding.leftrocketAnim.pauseAnimation()
//                binding.constlayoutanimation.visibility= View.GONE
//                binding.emojislayout.visibility=View.VISIBLE
//
//            }, 3000)
//            emojiimageone.setOnClickListener {
//                emojiimageone.setImageResource(R.drawable.angrycolor)
//                emojiimgtwo.setImageResource(R.drawable.colorsurprised)
//                emojiimgthree.setImageResource(R.drawable.smilecolor)
//                emojiimgfour.setImageResource(R.drawable.laughcolor)
//                emojiimgfive.setImageResource(R.drawable.lovecolor)
//                feedbackconst.visibility=View.VISIBLE
//
//            }
//        }
        lifecycleScope.launch {
            binding.apply {
                smileRating.selectedSmile = SmileRating.TERRIBLE
                delay(200)
                smileRating.selectedSmile = SmileRating.BAD
                delay(200)
                smileRating.selectedSmile = SmileRating.OKAY
                delay(200)
                smileRating.selectedSmile = SmileRating.GOOD
                delay(200)
                smileRating.selectedSmile = SmileRating.GREAT
                delay(200)
                smileRating.selectedSmile = SmileRating.NONE
            }
        }

        initialiseRating()
        clickListner()

    }

    private fun rateUs() {
        try {
            val marketUri: Uri = Uri.parse("market://details?id=$packageName")
            val marketIntent = Intent(Intent.ACTION_VIEW, marketUri)
            startActivity(marketIntent)
        } catch (e: ActivityNotFoundException) {
            val marketUri: Uri = Uri.parse("https://play.google.com/store/apps/details?id=$packageName")
            val marketIntent = Intent(Intent.ACTION_VIEW, marketUri)
            startActivity(marketIntent)
        }
    }

    private fun clickListner() {
        binding.apply {
            btnnothankz.setOnClickListener {
                finish()
            }
            btnsendfeedback.setOnClickListener {
                if (rating <= 0) {
                    shortToast("Please select a rating first!")
                } else {
                    if (rating > 2) {
                        rateUs()
                    } else {
                        feedBack()
                    }
                }
            }
            imgBackArfd.setOnClickListener {
                onBackPressed()
            }

        }
    }

    private fun initialiseRating() {
        binding.apply {
            smileRating.isSelected = true
            smileRating.setNameForSmile(BaseRating.TERRIBLE, "Very Poor")
            smileRating.setNameForSmile(BaseRating.BAD, "Poor")
            smileRating.setNameForSmile(BaseRating.OKAY, "Average")
            smileRating.setNameForSmile(BaseRating.GOOD, "Good")
            smileRating.setNameForSmile(BaseRating.GREAT, "Excellent")

            smileRating.setOnSmileySelectionListener { smiley, _ ->
                when (smiley) {
                    SmileRating.TERRIBLE -> {
                        Log.i("TAG", "TERRIBLE, the user gave rating")
                        feedbackconst.visibility = View.VISIBLE
                    }
                    SmileRating.BAD -> {
                        Log.i("TAG", "BAD, the user gave rating")
                        feedbackconst.visibility = View.VISIBLE
                    }
                    SmileRating.OKAY -> {
                        Log.i("TAG", "OKAY, the user gave rating")
                        feedbackconst.visibility = View.VISIBLE
                    }
                    SmileRating.GOOD -> {
                        Log.i("TAG", "GOOD, the user gave rating")
                        feedbackconst.visibility = View.GONE
                    }
                    SmileRating.GREAT -> {
                        Log.i("TAG", "GREAT, the user gave rating")
                        feedbackconst.visibility = View.GONE
                    }
                    else -> {
                        rating = 0
                    }
                }
                rating = smiley
            }
        }
    }

    private fun feedBack() {

        if (binding.getEditText.text.isNullOrEmpty()){
            shortToast("Write feedback to continue")
        }
        else{
            try {
                val intent = Intent(Intent.ACTION_SEND)
                val recipients = arrayOf("abc@gmail.com")
                intent.putExtra(Intent.EXTRA_EMAIL, recipients)
                intent.putExtra(Intent.EXTRA_SUBJECT, "Meditation Sound Mixture")
                intent.putExtra(Intent.EXTRA_TEXT, binding.getEditText.text)
                intent.type = "text/html"
                intent.setPackage("com.google.android.gm")
                startActivity(Intent.createChooser(intent, "Send mail"))
            } catch (e: Exception) {
                e.printStackTrace()
            }
//            try {
//                val emailIntent = Intent(Intent.ACTION_SENDTO)
//                val uriText = "mailto:" + Uri.encode("abc@.com") +
//                        "?subject=" + Uri.encode("Meditation Sound Mixture")
//                val uri = Uri.parse(uriText)
//                emailIntent.data = uri
//                startActivity(Intent.createChooser(emailIntent, "${binding.getEditText.text}"))
//            } catch (e: Exception) {
//                e.printStackTrace()
//            }
        }
    }
}