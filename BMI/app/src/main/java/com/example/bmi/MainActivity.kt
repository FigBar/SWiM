package com.example.bmi

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.widget.Button
import android.widget.TextView
import com.example.bmi.logic.BmiForKgCm
import kotlinx.android.synthetic.main.activity_main.*
import java.lang.IllegalArgumentException
import java.math.BigDecimal
import java.math.RoundingMode

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)


        val bmi = BmiForKgCm(0, 0)
        countButton.isEnabled = false

        massET.addTextChangedListener(object : TextWatcher {
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                countButton.isEnabled = !s.toString().trim().isEmpty() && !heightET.text.isEmpty()
            }

            override fun afterTextChanged(s: Editable?) {
                resultTV.text = ""
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

            }
        })

        heightET.addTextChangedListener(object : TextWatcher {
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                countButton.isEnabled = !s.toString().trim().isEmpty() && !massET.text.isEmpty()
            }

            override fun afterTextChanged(s: Editable?) {
                resultTV.text = ""
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

            }
        })

        countButton.setOnClickListener {
            var bmiResult: Double?
            try {
                bmi.mass = massET.text.toString().toInt()
                bmi.height = heightET.text.toString().toInt()
                bmiResult = bmi.countBmi()
            } catch (e: IllegalArgumentException) {
                bmiResult = null
            }

            val resText =
                if (bmiResult != null) String.format("%.1f", bmiResult) else "The values are to small or to big"
            resultTV.text = resText
        }


    }
}
