package com.example.pictureuploader

import android.app.Activity
import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.DatePicker
import android.widget.EditText
import android.widget.Toast
import com.example.pictureuploader.model.PictureRecord
import kotlinx.android.synthetic.main.activity_add_picture.*
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
        val date = date_picker.checkIfDateNotFromFuture(this) ?: return

        val replyIntent = Intent()
        replyIntent.putExtra(REPLY_KEY, PictureRecord(url, title, date))
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

    private fun DatePicker.checkIfDateNotFromFuture(context: Context): Calendar? {
        val date = Calendar.getInstance()
        date.set(year, month, dayOfMonth)
        if (date <= Calendar.getInstance()) {
            return date
        } else {
            Toast.makeText(context, "Chosen date is invalid!", Toast.LENGTH_SHORT).show()
            return null
        }
    }
}
