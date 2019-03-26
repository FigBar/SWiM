package com.example.bmi

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.core.content.ContextCompat
import com.example.bmi.logic.Bmi
import com.example.bmi.logic.BmiForKgCm
import com.example.bmi.logic.BmiForLbIn
import kotlinx.android.synthetic.main.activity_main.*
import android.content.Intent
import android.content.SharedPreferences
import com.example.bmi.logic.HistoryElement
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import java.lang.Exception
import java.lang.IllegalArgumentException
import java.lang.NumberFormatException
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList


class MainActivity : AppCompatActivity() {

    companion object {
        const val PREFS_FILENAME = "com.example.bmi.prefs"
        private const val KEY = "history_list"

    }

    private var prefs: SharedPreferences? = null


    private var areUnitsSwitched: Boolean = false
    private var currentBmiCalculator: Bmi? = null
    private val bmiKgCm = BmiForKgCm(0, 0)
    private val bmiLbIn = BmiForLbIn(0, 0)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        prefs = this.getSharedPreferences(PREFS_FILENAME, 0)
        currentBmiCalculator = bmiKgCm

        massET.addTextChangedListener(object : TextWatcher {
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}

            override fun afterTextChanged(s: Editable?) {
                resultTV.text = ""
                categoryTV.text = ""
                results_segment.visibility = View.INVISIBLE
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
        })

        heightET.addTextChangedListener(object : TextWatcher {
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}

            override fun afterTextChanged(s: Editable?) {
                resultTV.text = ""
                categoryTV.text = ""
                results_segment.visibility = View.INVISIBLE
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
        })

    }

    private fun readHistoryListFromPrefs(): List<HistoryElement?> {
        val jsonHistory = prefs!!.getString(KEY, "[]")

        class Token : TypeToken<List<HistoryElement?>>()
        return Gson().fromJson<List<HistoryElement?>>(jsonHistory, Token().type)
    }

    private fun saveHistoryListToPrefs(elem: HistoryElement?) {
        val previousHistoryList = readHistoryListFromPrefs()
        val currentHistoryList = listOf(elem) + previousHistoryList.take(9)
        val jsonHistory = Gson().toJson(currentHistoryList)
        with(prefs!!.edit()) {
            remove(KEY)
            putString(KEY, jsonHistory)
            apply()
        }

    }

    private fun createHistoryRecord(): HistoryElement? {
        val mass = massET.text.toString() + if (areUnitsSwitched) "lb" else "kg"
        val height = heightET.text.toString() + if (areUnitsSwitched) "in" else "cm"
        val bmiResult = resultTV.text.toString()
        val date = SimpleDateFormat("dd-MM-yyyy").format(Date())
        return HistoryElement(mass, height, bmiResult, date)
    }

    override fun onSaveInstanceState(outState: Bundle?) {

        outState?.putString(getString(R.string.result_bundle_key), resultTV.text.toString())
        outState?.putString(getString(R.string.category_bundle_key), categoryTV.text.toString())
        outState?.putBoolean(getString(R.string.units_flag_key), areUnitsSwitched)
        super.onSaveInstanceState(outState)

    }

    override fun onRestoreInstanceState(savedInstanceState: Bundle?) {
        super.onRestoreInstanceState(savedInstanceState)

        if (savedInstanceState?.getString(getString(R.string.result_bundle_key)) != "") {
            resultTV.text = savedInstanceState?.getString(getString(R.string.result_bundle_key))
            categoryTV.text = savedInstanceState?.getString(getString(R.string.category_bundle_key))
            setColor(resultTV, categoryTV.text.toString())
            results_segment.visibility = View.VISIBLE
        }

        areUnitsSwitched = savedInstanceState!!.getBoolean(getString(R.string.units_flag_key))
        if (areUnitsSwitched) {
            currentBmiCalculator = bmiLbIn
            massLabel.text = getString(R.string.lb_label)
            heightLabel.text = getString(R.string.in_label)
            invalidateOptionsMenu()
        } else {
            currentBmiCalculator = bmiKgCm
            massLabel.text = getString(R.string.mass_kg)
            heightLabel.text = getString(R.string.height_cm)
        }
    }

    override fun onPrepareOptionsMenu(menu: Menu?): Boolean {
        menu?.findItem(R.id.unit_change)?.isChecked = areUnitsSwitched
        if (!readHistoryListFromPrefs().isEmpty()) menu?.findItem(R.id.history)?.isEnabled = true
        return super.onPrepareOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {

        return when (item?.itemId) {
            R.id.unit_change -> onUnitChanged(item)
            R.id.history -> onHistorySelected()
            R.id.about -> onAboutSelected()
            else -> super.onOptionsItemSelected(item)

        }
    }


    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        val inflater: MenuInflater = menuInflater
        inflater.inflate(R.menu.main, menu)
        return true

    }

    private fun onHistorySelected(): Boolean {
        val historyIntent = Intent(this, BmiHistoryActivity::class.java)
        startActivity(historyIntent)
        return true
    }

    private fun onAboutSelected(): Boolean {
        val aboutIntent = Intent(this, AboutActivity::class.java)
        startActivity(aboutIntent)
        return true
    }

    fun goToInfoActivity(view: View) {
        val infoIntent = Intent(this, DetailBmiActivity::class.java)
        val bmiBundle = Bundle()
        bmiBundle.putString(getString(R.string.result_bundle_key), resultTV.text.toString())
        bmiBundle.putString(getString(R.string.category_bundle_key), categoryTV.text.toString())
        infoIntent.putExtras(bmiBundle)
        startActivity(infoIntent)
    }


    private fun onUnitChanged(menuItem: MenuItem): Boolean {
        areUnitsSwitched = !areUnitsSwitched
        menuItem.isChecked = areUnitsSwitched
        changeUnits()
        return true
    }

    private fun changeUnits() {
        if (areUnitsSwitched) {
            currentBmiCalculator = bmiLbIn
            massLabel.text = getString(R.string.lb_label)
            heightLabel.text = getString(R.string.in_label)

        } else {
            currentBmiCalculator = bmiKgCm
            massLabel.text = getString(R.string.mass_kg)
            heightLabel.text = getString(R.string.height_cm)
        }
        resultTV.text = ""
        categoryTV.text = ""
        massET.text.clear()
        heightET.text.clear()
        results_segment.visibility = View.INVISIBLE
    }

    fun onCountClicked(view: View) {
        try {
            val mass = getValidBmiParameterForCalculations(massET, "mass") ?: return
            val height = getValidBmiParameterForCalculations(heightET, "height") ?: return
            currentBmiCalculator?.mass = mass
            currentBmiCalculator?.height = height
            val bmiResult = currentBmiCalculator!!.countBmi()
            displayBmiResult(bmiResult)
            setCategory(bmiResult)
            setColor(resultTV, categoryTV.text.toString())
            results_segment.visibility = View.VISIBLE
            val newRecord = createHistoryRecord()
            saveHistoryListToPrefs(newRecord)
            invalidateOptionsMenu()
        } catch (exc: Exception) {
            when (exc) {
                is IllegalArgumentException,
                is NumberFormatException -> {
                    Toast.makeText(this, getString(R.string.valid_parameters_request), Toast.LENGTH_SHORT).show()
                    results_segment.visibility = View.INVISIBLE
                }
            }
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

    private fun setColor(compToModify: TextView, bmiCategory: String) {
        when (bmiCategory) {
            getString(R.string.underweight) -> compToModify.setTextColor(
                ContextCompat.getColor(
                    this,
                    R.color.lapisLazuli
                )
            )
            getString(R.string.healthy) -> compToModify.setTextColor(ContextCompat.getColor(this, R.color.verdigris))
            getString(R.string.overweight) -> compToModify.setTextColor(
                ContextCompat.getColor(
                    this,
                    R.color.overweightOrange
                )
            )
            getString(R.string.obesity) -> compToModify.setTextColor(
                ContextCompat.getColor(
                    this,
                    R.color.obesityDarkOrange
                )
            )
            else -> compToModify.setTextColor(ContextCompat.getColor(this, R.color.pompeianRose))
        }
    }

    private fun setCategory(bmiResult: Double?) {
        categoryTV.text = when {
            bmiResult == null -> ""
            bmiResult < 18.5 -> getString(R.string.underweight)
            bmiResult < 25 -> getString(R.string.healthy)
            bmiResult < 30 -> getString(R.string.overweight)
            bmiResult < 35 -> getString(R.string.obesity)
            else -> getString(R.string.severe_obesity)
        }
    }

}
