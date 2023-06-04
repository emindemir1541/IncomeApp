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