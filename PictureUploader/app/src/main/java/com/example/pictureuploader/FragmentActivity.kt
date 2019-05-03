package com.example.pictureuploader

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.fragment.app.FragmentManager
import com.example.pictureuploader.fragments.PhotoFragment
import com.example.pictureuploader.model.PictureRecord
import com.example.pictureuploader.recycle_view.PictureComponentAdapter

class FragmentActivity : AppCompatActivity() {

    private lateinit var fragmentManager: FragmentManager
    private var fullPhotoMode = true

    companion object {
        private const val PHOTO_TAG = "photo"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_fragment)
        fragmentManager = supportFragmentManager
        val dataSet = intent.getParcelableArrayListExtra<PictureRecord>(PictureComponentAdapter.DATA_SET)
        val chosenPosition = intent.getIntExtra(PictureComponentAdapter.INDEX, -1)
        val photoFragment = PhotoFragment.newInstance(dataSet[chosenPosition].url)
        launchFragments(savedInstanceState, photoFragment)
    }

    private fun launchFragments(savedInstanceState: Bundle?, photoFragment: PhotoFragment) {
        if(savedInstanceState == null) {
            fragmentManager.beginTransaction().add(R.id.fragment_activity_main_layout, photoFragment, PHOTO_TAG)
                .commit()
        }
    }
}
