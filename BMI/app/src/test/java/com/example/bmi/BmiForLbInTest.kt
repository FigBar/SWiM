package com.example.bmi

import com.example.bmi.logic.BmiForLbIn
import io.kotlintest.matchers.startWith
import io.kotlintest.should
import io.kotlintest.shouldThrow
import io.kotlintest.specs.StringSpec
import org.junit.Assert
import java.lang.IllegalArgumentException

class BmiForLbInTest : StringSpec() {
    init {
        "for valid mass and height return correct bmi"{
            val bmi = BmiForLbIn(250, 80)
            bmi.countBmi() shouldBeAround 27.460
        }

        "for lower mass and height constraint failed throw an exception" {
            val bmi = BmiForLbIn(43, 18)
            val exception = shouldThrow<IllegalArgumentException> {
                bmi.countBmi()
            }

            exception.message should startWith("invalid data")
        }

        "for upper edge mass and height constraint failed throw an exception" {
            val bmi = BmiForLbIn(441, 91)
            val exception = shouldThrow<IllegalArgumentException> {
                bmi.countBmi()
            }

            exception.message should startWith("invalid data")
        }

        "for mass and height lower than 0 should throw exception" {
            val bmi = BmiForLbIn(-4, -7)
            val exception = shouldThrow<IllegalArgumentException> {
                bmi.countBmi()
            }
            exception.message should startWith("invalid data")
        }
    }

    infix fun Double.shouldBeAround(expected: Double) {
        Assert.assertEquals(expected, this, 0.001)
    }
}