package com.project.meditationsoundmixture.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class MusicItems(var musicimage:Int,var musicname:String,var raw:Int) : Parcelable
