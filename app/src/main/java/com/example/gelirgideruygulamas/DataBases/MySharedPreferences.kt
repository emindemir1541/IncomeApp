package com.example.gelirgideruygulamas.DataBases

import android.content.Context
import android.content.Context.MODE_PRIVATE
import android.content.SharedPreferences

class MySharedPreferences(private val mContext: Context) {

    //bottom navigation sayfa ID
    fun saveBottomNavigationLocation(locationID: Int) {
        val editor: SharedPreferences.Editor =
            mContext.getSharedPreferences("BOTTOM_NAVIGATION_LOCATION", MODE_PRIVATE).edit()
        editor.putInt("locationID", locationID)
        editor.apply()
    }

    fun getBottomNavigationLocation(): Int {
        val prefs: SharedPreferences = mContext.getSharedPreferences("BOTTOM_NAVIGATION_LOCATION", MODE_PRIVATE)
        return prefs.getInt("locationID", 1)
    }

}