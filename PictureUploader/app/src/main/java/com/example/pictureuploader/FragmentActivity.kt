package com.example.pictureuploader

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import com.example.pictureuploader.fragments.DetailFragment
import com.example.pictureuploader.fragments.PhotoFragment
import com.example.pictureuploader.model.PictureRecord
import com.example.pictureuploader.recycle_view.PictureComponentAdapter

class FragmentActivity : AppCompatActivity() {

    private lateinit var fragmentManager: FragmentManager
    private var fullPhotoMode = true
    private lateinit var photoFragment: Fragment
    private lateinit var detailFragment: Fragment

    companion object {
        private const val PHOTO_TAG = "photo"
        private const val DETAILS_TAG = "details"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_fragment)
        fragmentManager = supportFragmentManager
        val dataSet = intent.getParcelableArrayListExtra<PictureRecord>(PictureComponentAdapter.DATA_SET)
        val chosenPosition = intent.getIntExtra(PictureComponentAdapter.INDEX, -1)
        photoFragment = PhotoFragment.newInstance(dataSet[chosenPosition].url)
        detailFragment = DetailFragment.newInstance(dataSet[chosenPosition])
        launchFragments(savedInstanceState)
    }

    private fun launchFragments(savedInstanceState: Bundle?) {
        if(savedInstanceState == null) {
            fragmentManager.beginTransaction().add(R.id.fragment_activity_main_layout, photoFragment, PHOTO_TAG)
                .commit()
            fragmentManager.beginTransaction().add(R.id.fragment_activity_main_layout, detailFragment, DETAILS_TAG)
                .commit()
            hideFragment(detailFragment)
        }
    }

    fun onChangeCardClicked(view: View) {
        fullPhotoMode = if(fullPhotoMode) {
            hideFragment(photoFragment)
            showFragment(detailFragment)
            false
        } else {
            hideFragment(detailFragment)
            showFragment(photoFragment)
            true
        }
    }

    private fun hideFragment(fragment: Fragment) {
        fragmentManager.beginTransaction().hide(fragment).commit()
    }

    private fun showFragment(fragment:Fragment) {
        fragmentManager.beginTransaction().show(fragment).commit()
    }
}
