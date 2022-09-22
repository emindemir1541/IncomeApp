package com.example.gelirgideruygulamas.feedback

import android.content.Context
import android.widget.Toast
import com.example.gelirgideruygulamas.R
import com.example.gelirgideruygulamas.data.sharedPreference.StatedDate

class Message(private val context: Context) {

    private fun res(stringId:Int) =context.getString(stringId)
    private fun messageToast(messageText: String) = Toast.makeText(context, messageText, Toast.LENGTH_LONG).show()

    fun warningEmpty() {
       messageToast(res(R.string.warning_empty))
    }

    fun warningMuchCharacter(characterLength: Int) {
        messageToast(characterLength.toString() + " " + res(R.string.warning_character_much))
    }

    fun lessCharacterWarning(characterLength: Int) {
        messageToast(characterLength.toString() + " " + res(R.string.warning_character_less))
    }

    fun infoDeletedAfterNow() {
        messageToast(StatedDate(context).getMonth() + res(R.string.info_deleted_after_now))
    }
    fun infoDeletedCard(){
        messageToast(res(R.string.info_deleted_card))
    }
}