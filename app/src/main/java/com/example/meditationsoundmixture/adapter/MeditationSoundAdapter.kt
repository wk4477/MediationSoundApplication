package com.project.meditationsoundmixture.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.project.meditationsoundmixture.R
import com.project.meditationsoundmixture.model.MusicItems

class MeditationSoundAdapter(
    var context: Context,
    var arrpositon: ArrayList<MusicItems>,
    val mlistener: OnItemClickListener
) : RecyclerView.Adapter<MeditationSoundAdapter.MyViewHolder>() {
    interface OnItemClickListener {
        fun onItemClicksser(position: Int)
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): MeditationSoundAdapter.MyViewHolder {
        val root =
            LayoutInflater.from(context).inflate(R.layout.meditationsounditems, parent, false)
        return MyViewHolder(root)
    }

    override fun onBindViewHolder(holder: MeditationSoundAdapter.MyViewHolder, position: Int) {
        holder.image.setImageResource(arrpositon[position].musicimage)
        holder.itemView.setOnClickListener {
            mlistener.onItemClicksser(position)
        }
    }

    override fun getItemCount(): Int {
        return arrpositon.size
    }

    inner class MyViewHolder(item: View) : RecyclerView.ViewHolder(item) {
        var image: ImageView = item.findViewById(R.id.imgSong)
    }

}