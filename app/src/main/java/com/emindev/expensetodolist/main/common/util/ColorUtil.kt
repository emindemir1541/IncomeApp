package com.emindev.expensetodolist.main.common.util

import androidx.compose.ui.graphics.Color
import kotlin.random.Random

object ColorUtil {

    fun getColorBetweenRedAndYellow(percentage: Int): Color {
        val red = (255).toInt()
        val green = (255f * (percentage / 100f)).toInt()
        val blue = 0

        return Color(red, green, blue)
    }

    val randomColor: Color
        get() {
            return Color(Random.nextLong())
        }
}