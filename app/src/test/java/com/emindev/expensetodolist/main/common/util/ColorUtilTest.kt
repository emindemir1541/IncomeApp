package com.emindev.expensetodolist.main.common.util

import androidx.compose.ui.graphics.Color
import org.junit.Assert.*
import org.junit.Test
import kotlin.random.Random

class ColorUtilTest{
    @Test
    fun `getColorBetweenRedAndYellow returns red`() {
        assertEquals(Color.Red, ColorUtil.getColorBetweenRedAndYellow(0))
    }

    @Test
    fun `getColorBetweenRedAndYellow returns yellow`() {
        assertEquals(Color.Yellow, ColorUtil.getColorBetweenRedAndYellow(100))
    }

    @Test
    fun `getColorBetweenRedAndYellow random percentage without 0 and 100 returns between yellow and red`() {
        val color = ColorUtil.getColorBetweenRedAndYellow(Random.nextInt(1,99))
        assert(color.red == 1f && color.blue == 0f)
    }

}