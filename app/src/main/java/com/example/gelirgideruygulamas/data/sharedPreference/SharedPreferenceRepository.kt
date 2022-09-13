package com.example.gelirgideruygulamas.data.sharedPreference

import android.content.Context

class SharedPreferenceRepository(private val context: Context) {

    fun setInt(fileName: String, key: String, value: Int) {
        SharedPreferenceBase(context).editor(fileName).putInt(key, value).apply()
    }

    fun getInt(fileName: String, key: String): Int {
        return SharedPreferenceBase(context).preferences(fileName).getInt(key, 1)
    }

    fun setLong(fileName: String, key: String, value: Long) {
        SharedPreferenceBase(context).editor(fileName).putLong(key, value).apply()
    }

    fun getLong(fileName: String, key: String): Long {
        return SharedPreferenceBase(context).preferences(fileName).getLong(key, 1)
    }

}

/*  fun setString(fileName:String,key:String,value:String){
       SharedPreferenceBase(context).editor(fileName).putString(key,value).apply()
    }
    fun getString(fileName:String,key:String):String{
        return SharedPreferenceBase(context).preferences(fileName).getString(key,"")!!
    }*/