package com.project.meditationsoundmixture.Extension

import android.content.Context
import android.content.SharedPreferences

class SharedPref(var _context: Context) {
    var editor: SharedPreferences.Editor
    var PRIVATE_MODE = 0
    var pref: SharedPreferences
    var dbVersion: Int
        get() = pref.getInt(KEY_DB, 1)
        set(version) {
            editor.putInt(KEY_DB, version)
            editor.commit()
        }

    fun getBoolean(id: String?, value: Boolean): Boolean {
        return pref.getBoolean(id, false)
    }

    fun putBoolean(id: String?, value: Boolean) {
        editor.putBoolean(id, value)
        editor.commit()
    }

    fun getString(id: String?, value: String?): String? {
        return pref.getString(id, value)
    }

    fun putString(id: String?, value: String?) {
        editor.putString(id, value)
        editor.commit()
    }

    fun getInteger(id: String?): Int? {
        return pref.getInt(id, 0)
    }

    fun putInteger(id: String?, value: Int?) {
        value?.let { editor.putInt(id, it) }
        editor.commit()
    }

    companion object {
        private const val PREF_NAME = "AppPref"
        const val KEY_DB = "db"
        const val APPINSTALLED = "appInstall"
    }

    init {
        pref = _context.getSharedPreferences(PREF_NAME, PRIVATE_MODE)
        editor = pref.edit()
    }
}