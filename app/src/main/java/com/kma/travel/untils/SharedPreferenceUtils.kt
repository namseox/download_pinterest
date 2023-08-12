package com.kma.travel.utils

import android.content.Context
import android.content.SharedPreferences
import com.google.gson.Gson
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class SharedPreferenceUtils @Inject constructor(context: Context) {
    private val PINTEREST_DOWNLOADER = "PINTEREST_DOWNLOADER"
    private val sharedPreferences: SharedPreferences =
        context.getSharedPreferences(PINTEREST_DOWNLOADER, Context.MODE_PRIVATE)


    fun putAcceptPolicy(value: Boolean) {
        putBooleanValue("isAcceptPolicy", value)
    }

    fun getAcceptPolicy(): Boolean {
        return getBooleanValue("isAcceptPolicy")
    }

    fun getPermissions(): Boolean {
        return getBooleanValue("isPermissions")
    }

    fun putPermissions(value: Boolean) {
        putBooleanValue("isPermissions", value)
    }

    fun putBooleanValue(key: String, value: Boolean) {
        val editor = sharedPreferences.edit()
        editor.putBoolean(key, value).apply()
    }

    fun getBooleanValue(key: String): Boolean {
        return sharedPreferences.getBoolean(key, false)
    }

    //check download
    fun putIsSuccessFul(value: Boolean) {
        putIsSuccessFulValue("isSuccessFulValue", value)
    }

    fun getIsSuccessFul(): Boolean {
        return getIsSuccessFulValue("isSuccessFulValue")
    }

    private fun putIsSuccessFulValue(key: String, value: Boolean) {
        val editor = sharedPreferences.edit()
        editor.putBoolean(key, value).apply()
    }

    private fun getIsSuccessFulValue(key: String): Boolean {
        return sharedPreferences.getBoolean(key, false)
    }


    companion object : SingletonHolder<SharedPreferenceUtils, Context>(::SharedPreferenceUtils)


}