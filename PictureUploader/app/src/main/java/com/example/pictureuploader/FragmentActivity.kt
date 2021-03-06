package com.example.pictureuploader

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import com.example.pictureuploader.fragments.DetailFragment
import com.example.pictureuploader.fragments.PhotoFragment
import com.example.pictureuploader.fragments.SimilarPhotosFragment
import com.example.pictureuploader.model.PictureRecord
import kotlinx.android.synthetic.main.activity_fragment.*

class FragmentActivity : AppCompatActivity() {

    private lateinit var fragmentManager: FragmentManager
    private var fullPhotoMode = true
    private lateinit var photoFragment: Fragment
    private lateinit var detailFragment: Fragment
    private lateinit var similarPhotosFragment: Fragment

    companion object {
        private const val PHOTO_TAG = "photo"
        private const val DETAILS_TAG = "details"
        private const val SIMILARITIES_TAG = "similarities"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_fragment)
        fragmentManager = supportFragmentManager
        val dataSet = intent.extras?.getParcelableArrayList<PictureRecord>(MainActivity.DATA_SET)
        val chosenItem = intent.extras?.getParcelable<PictureRecord>(MainActivity.ITEM)
        photoFragment = PhotoFragment.newInstance(chosenItem!!.url)
        detailFragment = DetailFragment.newInstance(chosenItem)
        similarPhotosFragment = SimilarPhotosFragment.newInstance(dataSet!!, chosenItem)
        launchFragments(savedInstanceState)
    }

    private fun launchFragments(savedInstanceState: Bundle?) {
        if (savedInstanceState == null) {
            fragmentManager.beginTransaction().add(R.id.fragment_activity_main_layout, photoFragment, PHOTO_TAG)
                .commit()
            fragmentManager.beginTransaction().add(R.id.fragment_activity_main_layout, detailFragment, DETAILS_TAG)
                .commit()
            fragmentManager.beginTransaction()
                .add(R.id.fragment_activity_main_layout, similarPhotosFragment, SIMILARITIES_TAG)
                .commit()
            hideFragment(detailFragment)
            hideFragment(similarPhotosFragment)
        }
    }

    fun onChangeCardClicked(view: View) {
        fullPhotoMode = if (fullPhotoMode) {
            hideFragment(photoFragment)
            showFragment(detailFragment)
            showFragment(similarPhotosFragment)
            switch_button.text = getString(R.string.fullscreen_button)
            false
        } else {
            hideFragment(detailFragment)
            hideFragment(similarPhotosFragment)
            showFragment(photoFragment)
            switch_button.text = getString(R.string.details_button)
            true
        }
    }

    private fun hideFragment(fragment: Fragment) {
        fragmentManager.beginTransaction().hide(fragment).commit()
    }

    private fun showFragment(fragment: Fragment) {
        fragmentManager.beginTransaction().show(fragment).commit()
    }
}
