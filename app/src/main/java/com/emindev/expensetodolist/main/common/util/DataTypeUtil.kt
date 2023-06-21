package com.emindev.expensetodolist.main.common.util

import java.lang.Exception

val String.toFloatOrZero: Float
    get() {
        return try {
            this.toFloat()
        } catch (e: Exception) {
            0f
        }
    }

val String.toIntOrZero: Int
    get() {
        return try {
            this.toInt()
        } catch (e: Exception) {
            0
        }
    }

val String.cleanBlanks: String
    get() = this.replace(" ", "")
val Float.clearZeroAfterDat: String
    get() {
        val splitValue = this.toString().split(".")
        return if (splitValue[1] == "0") {
            splitValue[0]
        }
        else this.toString()
    }