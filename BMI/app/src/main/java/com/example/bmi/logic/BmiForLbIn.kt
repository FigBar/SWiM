package com.example.bmi.logic

class BmiForLbIn(var mass: Int, var height: Int) : Bmi {

    override fun countBmi(): Double {
        require(mass in 44..440 && height in 19..90) {"invalid data"}
        return ((mass * 1.0) / (height * height)) * 703
    }

    override fun adjustWeight(mass: Int) {
        this.mass = mass
    }

    override fun adjustHeight(height: Int) {
        this.height = height
    }
}