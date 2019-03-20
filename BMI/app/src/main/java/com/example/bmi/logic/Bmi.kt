package com.example.bmi.logic

abstract class Bmi(var mass: Int, var height: Int){

    abstract fun countBmi() : Double

}