package com.project.meditationsoundmixture.adapter

import android.content.Context
import android.media.MediaPlayer
import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager2.widget.ViewPager2
import com.bumptech.glide.Glide
import com.project.meditationsoundmixture.R
import com.project.meditationsoundmixture.ViewModel.MainViewModel
import com.project.meditationsoundmixture.fragments.Homeitemss
import org.koin.java.KoinJavaComponent.inject

class ImageAdapter(
    private val context: Context,
    private val imageList: ArrayList<Homeitemss>,
    private val viewPager2: ViewPager2,
    val mlistener: OnItemClickListener,
) :
    RecyclerView.Adapter<ImageAdapter.ImageViewHolder>() {
    private val viewModel: MainViewModel by inject(MainViewModel::class.java)
    interface OnItemClickListener {
        fun onItemClickss(position: Int,homeit: Homeitemss?)
    }

    class ImageViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val imageView: ImageView = itemView.findViewById(R.id.cardimage)
        var texttile:TextView=itemView.findViewById(R.id.title)
        var textdescription:TextView=itemView.findViewById(R.id.descriptionview)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ImageViewHolder {
        val view =
            LayoutInflater.from(parent.context).inflate(R.layout.image_container, parent, false)
        return ImageViewHolder(view)
    }

    override fun onBindViewHolder(holder: ImageViewHolder, position: Int) {
        val arrpositon=imageList[position]

        Glide.with(context).load(imageList[position].viewimage).into(holder.imageView)

        holder.itemView.setOnClickListener {
            mlistener.onItemClickss(position,arrpositon)
        }
        holder.texttile.text=arrpositon.title
        holder.textdescription.text=arrpositon.description

    }

    override fun getItemCount(): Int {
        return imageList.size
    }

    private val runnable = Runnable {
        imageList.addAll(imageList)
        notifyDataSetChanged()
    }
}

fun Int.getUri(context: Context): Uri? {
    val mediaPath: Uri? =
        Uri.parse("android.resource://" + context.packageName.toString() + "/" + this)
    return mediaPath
}

inline fun MediaPlayer.playMediaPlayer(context: Context, songId: Int, onStart: ((String) -> Unit)) {

    this.reset()
    songId.getUri(context)?.let { setDataSource(context, it) }
    prepare()
    start().apply {
        onStart.invoke("Playing")
    }
}