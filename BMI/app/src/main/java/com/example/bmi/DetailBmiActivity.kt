package com.example.bmi
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.method.ScrollingMovementMethod
import kotlinx.android.synthetic.main.activity_detail_bmi.*

class DetailBmiActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail_bmi)

        val bundleExtras = this.intent.extras
        bmi_result_detail.text = bundleExtras?.getString("BMI_RESULT")
        bmi_category_detail.text = bundleExtras?.getString("BMI_CATEGORY")
        setDescription()
        description_text_view.movementMethod = ScrollingMovementMethod()
    }

    private fun setDescription() {
        when (this.intent.extras?.getString("BMI_CATEGORY")) {
            "UNDERWEIGHT" -> description_text_view.text = getString(R.string.underweight_description)
            "HEALTHY" -> description_text_view.text = getString(R.string.healthy_description)
            "OVERWEIGHT" -> description_text_view.text = getString(R.string.overweight_description)
            "OBESITY" -> description_text_view.text = getString(R.string.obesity_description)
            "SEVERE OBESITY" -> description_text_view.text = getString(R.string.severe_obesity_description)
            else -> description_text_view.text = ""
        }
    }
}
