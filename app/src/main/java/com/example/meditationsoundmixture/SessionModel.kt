package com.project.meditationsoundmixture

import com.project.meditationsoundmixture.model.MusicItems

data class SessionModel(
    val sessionNumber: Int,
    val sessionStartTime: String,
    val sessionEndTime: String,
    val slotModel: ArrayList<MusicItems>
)