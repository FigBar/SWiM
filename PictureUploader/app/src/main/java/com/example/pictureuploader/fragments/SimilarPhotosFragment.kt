package com.example.pictureuploader.fragments


import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import com.example.pictureuploader.R
import com.example.pictureuploader.model.PictureRecord
import com.squareup.picasso.Picasso


class SimilarPhotosFragment : Fragment() {

    companion object {
        private const val PICTURE_RECORD_LIST = "picture_record_list"
        private const val ITEM = "item"

        fun newInstance(dataSet: ArrayList<PictureRecord>, item: PictureRecord) =
            SimilarPhotosFragment().apply {
                arguments = Bundle().apply {
                    putParcelableArrayList(PICTURE_RECORD_LIST, dataSet)
                    putParcelable(ITEM, item)
                }
            }

    }


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_similar_photos, container, false)
        val photoDisplayOne = view.findViewById<ImageView>(R.id.photoView1)
        val photoDisplayTwo = view.findViewById<ImageView>(R.id.photoView2)
        val photoDisplayThree = view.findViewById<ImageView>(R.id.photoView3)
        val photoDisplayFour = view.findViewById<ImageView>(R.id.photoView4)
        val photoDisplayFive = view.findViewById<ImageView>(R.id.photoView5)
        val photoDisplaySix = view.findViewById<ImageView>(R.id.photoView6)

        val imageViewList: ArrayList<ImageView> = arrayListOf(
            photoDisplayOne,
            photoDisplayTwo, photoDisplayThree, photoDisplayFour, photoDisplayFive, photoDisplaySix
        )
        val similarPhotos = findSimilarPhotos()
        for (i in 0 until similarPhotos.size) {
            Picasso.get().load(similarPhotos[i].url).placeholder(R.drawable.loading).error(R.drawable.error_image)
                .into(imageViewList[i])
        }
        return view
    }

    private fun findSimilarPhotos(): MutableList<PictureRecord> {
        val dataSet = arguments!!.getParcelableArrayList<PictureRecord>(PICTURE_RECORD_LIST)
        val item = arguments!!.getParcelable<PictureRecord>(ITEM)
        val index = dataSet?.indexOf(item)
        val chosenPicture = dataSet?.removeAt(index!!)
        dataSet!!.sortWith(compareBy { it.assessSimilarity(chosenPicture!!) })
        return dataSet.takeLast(6).filter { it.assessSimilarity(chosenPicture!!) != 0 }.toMutableList()

    }


}
