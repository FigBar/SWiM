package com.example.bmi.logic

interface Bmi {

    fun countBmi() : Double
    fun adjustWeight(mass : Int)
    fun adjustHeight(height : Int)

}