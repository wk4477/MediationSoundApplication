package com.project.meditationsoundmixture
import com.project.meditationsoundmixture.model.MusicItems
import java.util.ArrayList

internal class LoadSlotsUseCase {
    fun execute(stringArray: ArrayList<MusicItems>): List<MusicItems> {
        val slotModelList: MutableList<MusicItems> = ArrayList()
        for ((musicimage, musicname, raw) in stringArray) {
            slotModelList.add(MusicItems(musicimage, musicname, raw))
        }
        return slotModelList
    }
}