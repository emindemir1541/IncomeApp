package com.example.gelirgideruygulamas.common.helper

import android.content.Context
import android.os.Message
import android.util.Log
import android.widget.Toast

fun Context.mes(message: String){
    Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    Log.e("TAG",message)
}