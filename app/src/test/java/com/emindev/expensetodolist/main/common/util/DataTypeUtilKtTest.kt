package com.emindev.expensetodolist.main.common.util

import org.junit.Assert.*
import org.junit.Test

class DataTypeUtilKtTest {

    @Test
    fun `float or zero turns zero`() {
        assertEquals("54f54".toFloatOrZero,0f)
    }

    @Test
    fun `float or zero turns float`() {
        val value = "57245".toFloatOrZero
        assertNotEquals(value,0f)
    }
    @Test
    fun `int or zero turns zero`() {
        assertEquals("54f54".toIntOrZero,0)
    }

    @Test
    fun `int or zero turns int`() {
        val value = "57245".toIntOrZero
        assertNotEquals(value,0)
    }

    @Test
    fun `clear blanks return no blanks`() {
            assertEquals("val u e".cleanBlanks,"value")
    }

    @Test
    fun `clear zero after dat`() {
        assertEquals(214.0f.clearZeroAfterDat,"214")
    }

}