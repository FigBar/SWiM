package com.example.bmi

import com.example.bmi.logic.BmiForKgCm
import io.kotlintest.matchers.startWith
import io.kotlintest.should
import io.kotlintest.shouldThrow
import io.kotlintest.specs.StringSpec
import org.junit.Assert
import java.lang.IllegalArgumentException

class BmiForKgCmTest : StringSpec() {
    init {
        "for valid mass and height return correct bmi"{
            val bmi = BmiForKgCm(65, 170)
            bmi.countBmi() shouldBeAround 22.491
        }

        "for lower mass and height constraint failed throw an exception" {
            val bmi = BmiForKgCm(19, 49)
            val exception = shouldThrow<IllegalArgumentException> {
                bmi.countBmi()
            }

            exception.message should startWith("invalid data")
        }

        "for upper edge mass and height constraint failed throw an exception" {
            val bmi = BmiForKgCm(201, 231)
            val exception = shouldThrow<IllegalArgumentException> {
                bmi.countBmi()
            }

            exception.message should startWith("invalid data")
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