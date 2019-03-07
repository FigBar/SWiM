package com.example.bmi.logic

class BmiForKgCm(var mass: Int, var height: Int) : Bmi {

    //override fun countBmi(): Double = (mass * 10000.0) / (height*height)

    override fun countBmi(): Double {
        require(mass in 20..150 && height in 50..230) {"invalid data"} //how to test it !!!
        return (mass * 10000.0) / (height*height)

    }
}