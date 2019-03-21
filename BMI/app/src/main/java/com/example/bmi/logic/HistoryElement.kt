package com.example.bmi.logic

import android.os.Parcel
import android.os.Parcelable


data class HistoryElement(val mass: String, val height: String, val bmiResult: String, val date: String) : Parcelable {

    constructor (parcel: Parcel) : this(
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString())

    override fun writeToParcel(dest: Parcel, flags: Int) {
        dest.writeString(mass)
        dest.writeString(height)
        dest.writeString(bmiResult)
        dest.writeString(date)

    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<HistoryElement>{
        override fun createFromParcel(source: Parcel): HistoryElement {
            return HistoryElement(source)
        }

        override fun newArray(size: Int): Array<HistoryElement?> {
            return arrayOfNulls(size)
        }
    }

}