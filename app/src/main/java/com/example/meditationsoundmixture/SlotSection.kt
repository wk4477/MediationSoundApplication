package com.project.meditationsoundmixture

import android.content.Context
import android.view.View
import com.project.meditationsoundmixture.model.MusicItems
import io.github.luizgrp.sectionedrecyclerviewadapter.SectionParameters
import androidx.recyclerview.widget.RecyclerView
import com.project.meditationsoundmixture.Extension.onSingleClick
import io.github.luizgrp.sectionedrecyclerviewadapter.Section

internal class SlotSection(
    private val title: String,
    private val sessionNumber: Int,
    private val slotModelList: List<MusicItems>,
    private val clickListener: ClickListener,
    private val context: Context
) : Section(
    SectionParameters.builder()
        .itemResourceId(R.layout.section_items)
        .headerResourceId(R.layout.section_header)
        .build()
) {
    private val headerPosition = 0
    override fun getContentItemsTotal(): Int {
        return slotModelList.size
    }

    override fun getItemViewHolder(view: View): RecyclerView.ViewHolder {
        return ItemViewHolder(view)
    }


    override fun onBindItemViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val itemHolder = holder as ItemViewHolder
        val slotModel = slotModelList[position]
        itemHolder.textViewSessionSlot.setImageResource(slotModelList[position].musicimage)
        itemHolder.text.text = slotModelList[position].musicname
        itemHolder.rootView.onSingleClick{ v: View? ->
            clickListener.onItemRootViewClicked(
                v,
                slotModel,
                sessionNumber,position
            )

        }
    }

    override fun getHeaderViewHolder(view: View): RecyclerView.ViewHolder {
        return HeaderViewHolder(view)
    }

    override fun onBindHeaderViewHolder(holder: RecyclerView.ViewHolder) {
        val headerHolder = holder as HeaderViewHolder
        headerHolder.textViewSessionHeader.text = title
    }

    internal interface ClickListener {
        fun onItemRootViewClicked(
            v: View?,
            slotModel: MusicItems?,
            sessionNumber: Int,
            position: Int
        )
    }
}