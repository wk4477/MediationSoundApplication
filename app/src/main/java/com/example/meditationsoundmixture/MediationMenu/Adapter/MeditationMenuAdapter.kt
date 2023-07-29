package com.project.meditationsoundmixture.MediationMenu.Adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.project.meditationsoundmixture.R
import com.project.meditationsoundmixture.ViewModel.MainViewModel
import com.project.meditationsoundmixture.model.MusicItems
import org.koin.java.KoinJavaComponent.inject

class MeditationMenuAdapter(val context:Context,val arraydataone:ArrayList<MusicItems>):RecyclerView.Adapter<MeditationMenuAdapter.MenuViewHolder>() {
    private val mainViewModel:MainViewModel by inject(MainViewModel::class.java)
    interface OnItemClickListener {
        fun onItemClick(position: Int, get: MusicItems)

    }
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): MeditationMenuAdapter.MenuViewHolder {
       val root=LayoutInflater.from(context).inflate(R.layout.dataitem,parent,false)
        return MenuViewHolder(root)
    }


    override fun onBindViewHolder(holder: MeditationMenuAdapter.MenuViewHolder, position: Int) {
        holder.imagedata.setImageResource(arraydataone.get(position).musicimage)
        holder.textdata.text= arraydataone[position].musicname
        holder.itemView.setOnClickListener {
           /* with(mlistener) { onItemClick(position, arraydataone[position]) }*/

        }


    }

    override fun getItemCount(): Int {
        return arraydataone.size
    }
    inner class MenuViewHolder(items: View):RecyclerView.ViewHolder(items)
    {
       val imagedata=items.findViewById<ImageView>(R.id.imagUltra)
        val textdata=items.findViewById<TextView>(R.id.mediationsongs)
    }
}