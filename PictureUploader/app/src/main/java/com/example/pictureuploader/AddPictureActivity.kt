package com.example.pictureuploader

import android.app.Activity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.EditText
import android.widget.Toast
import com.example.pictureuploader.logic.PictureRecord
import kotlinx.android.synthetic.main.activity_add_picture.*
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*

class AddPictureActivity : AppCompatActivity() {

    companion object {
        const val REPLY_KEY = "reply_picture_record"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_picture)
    }

    fun onAddClicked(view: View) {

        val url = getValidParameter(url_input, "image url") ?: return
        val title = getValidParameter(title_input, "image title") ?: return
        val date = getValidParameter(date_input, "date") ?: return
        //val tags = tags_input.text.toString()
        //val tagArray: Array<String> = tags.split(",").map { it.trim() }.toTypedArray()


        val currentDate: Date
        val dateParser = SimpleDateFormat("dd.MM.yyyy", Locale.getDefault())

        try {
            currentDate = dateParser.parse(date)
        } catch (exc: ParseException) {
            Toast.makeText(this, "Invalid date format", Toast.LENGTH_SHORT).show()
            return
        }

        val replyIntent = Intent()
        replyIntent.putExtra(REPLY_KEY, PictureRecord(url, title, currentDate, arrayOf(" ")))
        setResult(Activity.RESULT_OK, replyIntent)
        finish()
    }


    private fun getValidParameter(src: EditText, paramType: String): String? {
        if (src.text.isEmpty()) {
            Toast.makeText(this, "Provide $paramType!", Toast.LENGTH_SHORT).show()
            return null
        }
        return src.text.toString()
    }
}
