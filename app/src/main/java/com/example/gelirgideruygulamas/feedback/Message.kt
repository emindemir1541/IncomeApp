package com.example.gelirgideruygulamas.feedback

import android.content.Context
import android.widget.Toast
import com.example.gelirgideruygulamas.R

class Message(private val context: Context) {

    fun emptyWarning() {
        Toast.makeText(context, context.getString(R.string.warning_empty), Toast.LENGTH_SHORT).show()
    }

    fun muchCharacterWarning(characterLength: Int) {
        Toast.makeText(context, characterLength.toString() + " " + context.getString(R.string.warning_character_much), Toast.LENGTH_SHORT).show()
    }

    fun lessCharacterWarning(characterLength: Int) {
        Toast.makeText(context, characterLength.toString() + " " + context.getString(R.string.warning_character_less), Toast.LENGTH_SHORT).show()
    }
}