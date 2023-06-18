package com.emindev.expensetodolist.main.common.util

import java.lang.Exception

fun String.toFloatOrZero(): Float {
    return try {
        this.toFloat()
    } catch (e: Exception) {
        0f
    }
}

fun String.toIntOrZero(): Int {
    return try {
        this.toInt()
    } catch (e: Exception) {
        0
    }
}

fun String.cleanBlanks(): String = this.replace(" ", "")
fun Float.clearZero(): String {
    val splitValue = this.toString().split(".")
    return if (splitValue[1] == "0") {
        splitValue[0]
    }
    else this.toString()
}