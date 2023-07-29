package com.project.meditationsoundmixture.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.Toast
import androidx.viewpager.widget.PagerAdapter
import com.bumptech.glide.Glide
import com.project.meditationsoundmixture.model.SliderItems
import com.project.meditationsoundmixture.R
import java.util.*
import kotlin.collections.ArrayList


class SliderAdapter(
    var context: Context,
    var images: ArrayList<SliderItems>,
) :
    PagerAdapter() {
    // Layout Inflater
    var mLayoutInflater: LayoutInflater
    override fun getCount(): Int {
        // return the number of images
        return images.size
    }

    /* interface OnViewPagerClick {
         fun onPagetItemClick(position: Int)
     }*/

    override fun isViewFromObject(view: View, `object`: Any): Boolean {
        return view === `object` as LinearLayout
    }

    override fun instantiateItem(container: ViewGroup, position: Int): Any {
        val itemView: View = mLayoutInflater.inflate(R.layout.sliderresource, container, false)
        val imageView = itemView.findViewById<View>(R.id.imageView) as ImageView
        Glide.with(context)
            .load(images[position].image)
            .into(imageView)
        //imageView.setImageResource(images[position].image)
        /*itemView.setOnClickListener {
            Toast.makeText(context, "hi", Toast.LENGTH_SHORT).show()
            onViewPagerClick.onPagetItemClick(position)
        }*/
        Objects.requireNonNull(container).addView(itemView,0)
        return itemView
    }

    override fun destroyItem(container: ViewGroup, position: Int, `object`: Any) {
        container.removeView(`object` as LinearLayout)
    }

    init {
        images = images
        mLayoutInflater =
            context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
    }
}