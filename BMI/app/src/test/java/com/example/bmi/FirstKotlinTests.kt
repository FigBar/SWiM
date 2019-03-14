package com.example.bmi

import com.example.bmi.logic.BmiForKgCm
import io.kotlintest.shouldThrow
import io.kotlintest.specs.StringSpec
import org.junit.Assert
import java.lang.IllegalArgumentException

class FirstKotlinTests : StringSpec() {
    init {
        "for valid mass and height return bmi"{
            val bmi = BmiForKgCm(65, 170)
            bmi.countBmi() shouldBeAround 22.491
        }

        "for mass and height lower than 0 should throw exception" {
            val bmi = BmiForKgCm(0, -1)
            shouldThrow<IllegalArgumentException> {
                bmi.countBmi()
            }
        }
    }

    infix fun Double.shouldBeAround(expected: Double) {
        Assert.assertEquals(expected, this, 0.001)
    }
}