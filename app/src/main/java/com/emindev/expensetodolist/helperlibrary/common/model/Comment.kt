package com.emindev.expensetodolist.helperlibrary.common.model

import com.google.gson.Gson

data class Comment(val nameSurname: String,val commentTitle:String, val comment: String, val time: Long) {

    val json: String = Gson().toJson(this)

}