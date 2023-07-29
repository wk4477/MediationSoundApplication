package com.project.meditationsoundmixture.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.project.meditationsoundmixture.R
import com.project.meditationsoundmixture.model.datamusicitems

class MusicDataAdapter(
    private val courseList: ArrayList<datamusicitems>,
    private val context: Context
) : RecyclerView.Adapter<MusicDataAdapter.CourseViewHolder>() {
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): MusicDataAdapter.CourseViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(
            R.layout.songdatalayout,
            parent, false
        )
        return CourseViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: MusicDataAdapter.CourseViewHolder, position: Int) {
        holder.courseNameTV.text = courseList.get(position).courseName
        holder.courseIV.setImageResource(courseList.get(position).courseImg)
    }

    override fun getItemCount(): Int {
        return courseList.size
    }

    class CourseViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val courseNameTV: TextView = itemView.findViewById(R.id.idTVCourse)
        val courseIV: ImageView = itemView.findViewById(R.id.idIVCourse)

    }
}