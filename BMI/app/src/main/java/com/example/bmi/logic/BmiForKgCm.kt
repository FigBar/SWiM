package com.example.bmi.logic

class BmiForKgCm(mass: Int, height: Int) : Bmi(mass, height) {

    override fun countBmi(): Double {
        require(mass in 20..200 && height in 50..230) { "invalid data" }
        return (mass * 10000.0) / (height * height)

    }

}