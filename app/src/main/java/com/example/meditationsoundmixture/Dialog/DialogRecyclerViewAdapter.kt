package com.project.meditationsoundmixture.Dialog


import android.content.Context
import android.media.ToneGenerator.MAX_VOLUME
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.SeekBar
import android.widget.SeekBar.OnSeekBarChangeListener
import androidx.recyclerview.widget.RecyclerView
import com.project.meditationsoundmixture.ViewModel.MainViewModel
import com.project.meditationsoundmixture.model.MusicItems
import org.koin.java.KoinJavaComponent.inject
import kotlin.math.ln


class DialogRecyclerViewAdapter(
    val context: Context,
    var ar: ArrayList<MusicItems>,
) : RecyclerView.Adapter<DialogRecyclerViewAdapter.ViewHolder>() {
    private val mainViewModel: MainViewModel by inject(MainViewModel::class.java)

    interface OnItemClickListener {
        fun onItemClickss(position: Int)
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): DialogRecyclerViewAdapter.ViewHolder {
        val root = LayoutInflater.from(context).inflate(com.project.meditationsoundmixture.R.layout.rvdialoglayout, parent, false)
        return ViewHolder(root)
    }

    override fun onBindViewHolder(holder: DialogRecyclerViewAdapter.ViewHolder, position: Int) {
        holder.image.setImageResource(ar[position].musicimage)
        holder.seekbarone.setOnSeekBarChangeListener(object : OnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: SeekBar, progress: Int, fromUser: Boolean) {
                if(position==0)
                {
                    val volume2 =
                        (1 - ln((MAX_VOLUME - progress).toDouble()) / ln(MAX_VOLUME.toDouble())).toFloat()
                    mainViewModel.getInstancemediaplayertwo().setVolume(volume2, volume2)
                }
                else if (position==1)
                {
                    val volume2 =
                        (1 - ln((MAX_VOLUME - progress).toDouble()) / ln(MAX_VOLUME.toDouble())).toFloat()
                    mainViewModel.getInstancemediaplayertwo().setVolume(volume2, volume2)
                }





            }

            override fun onStartTrackingTouch(seekBar: SeekBar) {

            }

            override fun onStopTrackingTouch(seekBar: SeekBar) {}
        })

    }

    override fun getItemCount(): Int {
        return ar.size
    }

    inner class ViewHolder(item: View) : RecyclerView.ViewHolder(item) {
        val image: ImageView = item.findViewById<ImageView>(com.project.meditationsoundmixture.R.id.imageView3)
        var seekbarone: SeekBar = item.findViewById<SeekBar>(com.project.meditationsoundmixture.R.id.seekBar_adapter)

    }
}