package com.example.gelirgideruygulamas.data.sharedPreference

import android.content.Context


class SharedPreferenceRepository(private val mContext: Context) {

    private val base = SharedPreferenceBase(mContext)

    fun setInt(fileName: String, key: String, value: Int) {
        base.editor(fileName).putInt(key, value).apply()
    }

    fun getInt(fileName: String, key: String): Int {
        return base.preferences(fileName).getInt(key, 0)
    }

    fun setLong(fileName: String, key: String, value: Long) {
        base.editor(fileName).putLong(key, value).apply()
    }

    fun getLong(fileName: String, key: String): Long {
        return base.preferences(fileName).getLong(key, 0)
    }

    fun setFloat(fileName: String, key: String, value: Float) {
        base.editor(fileName).putFloat(key, value)
    }

    fun getFloat(fileName: String, key: String): Float {
        return base.preferences(fileName).getFloat(key, 0f)
    }

    fun setString(fileName: String,key: String,value:String){
         base.editor(fileName).putString(key,value)
    }

    fun getString(fileName: String, key: String): String {
        //todo düzelt
        val aa = base.preferences(fileName).getString(key, "")
        return if(aa==null) "" else aa
    }




}

/*  fun setString(fileName:String,key:String,value:String){
       SharedPreferenceBase(context).editor(fileName).putString(key,value).apply()
    }
    fun getString(fileName:String,key:String):String{
        return SharedPreferenceBase(context).preferences(fileName).getString(key,"")!!
    }*/