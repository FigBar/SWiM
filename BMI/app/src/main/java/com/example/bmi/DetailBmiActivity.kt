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
        bmi_result_detail.text = bundleExtras?.getString(getString(R.string.result_bundle_key))
        bmi_category_detail.text = bundleExtras?.getString(getString(R.string.category_bundle_key))
        setDescription()
        description_text_view.movementMethod = ScrollingMovementMethod()
    }

    private fun setDescription() {
        when (this.intent.extras?.getString(getString(R.string.category_bundle_key))) {
            getString(R.string.underweight) -> description_text_view.text = getString(R.string.underweight_description)
            getString(R.string.healthy)-> description_text_view.text = getString(R.string.healthy_description)
            getString(R.string.overweight)-> description_text_view.text = getString(R.string.overweight_description)
            getString(R.string.obesity) -> description_text_view.text = getString(R.string.obesity_description)
            getString(R.string.severe_obesity) -> description_text_view.text = getString(R.string.severe_obesity_description)
            else -> description_text_view.text = ""
        }
    }
}
