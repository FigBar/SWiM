package com.example.bmi.logic

class BmiForLbIn(mass: Int, height: Int) : Bmi(mass, height) {

    override fun countBmi(): Double {
        require(mass in 44..440 && height in 19..90) {"invalid data"}
        return ((mass * 1.0) / (height * height)) * 703
    }

}