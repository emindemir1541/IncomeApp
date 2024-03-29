package com.emindev.expensetodolist.helperlibrary.data.sharedpreference

import android.content.Context


class SharedPreferenceRepository(mContext: Context) {

    private val base = SharedPreferenceBase(mContext)

    fun setInt(fileName: String, key: String, value: Int) {
        base.editor(fileName).putInt(key, value).apply()
    }

    fun getInt(fileName: String, key: String, defValue: Int): Int {
        return base.preferences(fileName).getInt(key, defValue)
    }

    fun setLong(fileName: String, key: String, value: Long) {
        base.editor(fileName).putLong(key, value).apply()
    }

    fun getLong(fileName: String, key: String, defValue: Long): Long {
        return base.preferences(fileName).getLong(key, defValue)
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

    fun getString(fileName: String, key: String,defaultKey:String): String {
        return base.preferences(fileName).getString(key, "") ?: defaultKey
    }

    fun setBoolean(fileName: String, key: String, value: Boolean) {
        base.editor(fileName).putBoolean(key, value).apply()
    }

    fun getBoolean(fileName: String, key: String, defValue: Boolean): Boolean {
        return base.preferences(fileName).getBoolean(key, defValue)
    }




}

/*  fun setString(fileName:String,key:String,value:String){
       SharedPreferenceBase(context).editor(fileName).putString(key,value).apply()
    }
    fun getString(fileName:String,key:String):String{
        return SharedPreferenceBase(context).preferences(fileName).getString(key,"")!!
    }*/