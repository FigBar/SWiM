package com.example.bmi

import com.example.bmi.logic.BmiForKgCm
import org.junit.Assert
import org.junit.Test

class MyFirstTests {
    @Test
    fun for_valid_data_should_return_bmi() {
        val bmi = BmiForKgCm(65, 170)
        Assert.assertEquals(22.491, bmi.countBmi(), 0.001) // one decimal place lower than planned
    }

    @Test
    fun my_bmi_is_correct() {
        val myBmi = BmiForKgCm(73, 186)
        Assert.assertEquals(21.100, myBmi.countBmi(), 0.001)
    }
}