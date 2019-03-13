package com.example.bmi

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.widget.EditText
import android.widget.Toast
import com.example.bmi.logic.BmiForKgCm
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    private val bmiCalculator = BmiForKgCm(0, 0)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        massET.addTextChangedListener(object : TextWatcher {
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}

            override fun afterTextChanged(s: Editable?) {
                resultTV.text = ""
                categoryTV.text = ""
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
        })

        heightET.addTextChangedListener(object : TextWatcher {
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}

            override fun afterTextChanged(s: Editable?) {
                resultTV.text = ""
                categoryTV.text = ""
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
        })

    }

    fun onCountClicked(view: View) {
        val mass = getValidBmiParameterForCalculations(massET, "mass") ?: return
        val height = getValidBmiParameterForCalculations(heightET, "height") ?: return

        bmiCalculator.mass = mass
        bmiCalculator.height = height

        try {
            val bmiResult = bmiCalculator.countBmi()
            displayBmiResult(bmiResult)
            setCategory(bmiResult)
        } catch (exc: IllegalArgumentException) {
            Toast.makeText(this, "Provide valid parameters!", Toast.LENGTH_SHORT).show()

        }
    }

    private fun getValidBmiParameterForCalculations(src: EditText, paramType: String): Int? {
        if (src.text.isEmpty()) {
            Toast.makeText(this, "Provide $paramType!", Toast.LENGTH_SHORT).show()
            return null
        }
        return src.text.toString().toInt()
    }

    private fun displayBmiResult(bmiResult: Double) {
        resultTV.text = String.format("%.2f", bmiResult)
    }

    private fun setColor() {

    }

    private fun setCategory(bmiResult: Double?) {
        categoryTV.text = when {
            bmiResult == null -> ""
            bmiResult < 18.5 -> "UNDERWEIGHT"
            bmiResult < 25 -> "HEALTHY"
            bmiResult < 30 -> "OVERWEIGHT"
            bmiResult < 35 -> "OBESITY"
            else -> "SEVERE OBESITY"
        }
    }
}
