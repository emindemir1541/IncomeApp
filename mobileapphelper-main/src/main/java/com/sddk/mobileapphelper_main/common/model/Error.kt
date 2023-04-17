package com.sddk.mobileapphelper_main.common.model

import com.google.gson.Gson


data class Error(
    val message: String,
    val localizedMessage: String,
    val cause:String,
    val errorLocation: String,
    val time: Long,
    val description: String?,
    ) {

    val json: String = Gson().toJson(this)

    

}