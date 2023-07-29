package com.project.meditationsoundmixture.util

import android.os.Build

object VersioningHelper {

    @JvmStatic
    fun isQ() = Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q

    @JvmStatic
    fun isOreoMR1() = Build.VERSION.SDK_INT >= Build.VERSION_CODES.O_MR1

    @JvmStatic
    fun isbellowMarshmallow() = Build.VERSION.SDK_INT < Build.VERSION_CODES.M

    @JvmStatic
    fun isR() = Build.VERSION.SDK_INT >= Build.VERSION_CODES.R

    @JvmStatic
    fun isMarshmallow() = Build.VERSION.SDK_INT >= Build.VERSION_CODES.M

    @JvmStatic
    fun isKitKat() = Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT


    @JvmStatic
    fun isS() = Build.VERSION.SDK_INT >= Build.VERSION_CODES.S
}
