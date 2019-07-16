package com.kau.smartbutler.model

import android.os.Parcel
import android.os.Parcelable
import androidx.databinding.ObservableField

data class CCTV(
        var type:Int,
        var name:String
):Parcelable {
    constructor(parcel: Parcel) : this(
            parcel.readInt(),
            parcel.readString()) {
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeInt(type)
        parcel.writeString(name)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<CCTV> {
        override fun createFromParcel(parcel: Parcel): CCTV {
            return CCTV(parcel)
        }

        override fun newArray(size: Int): Array<CCTV?> {
            return arrayOfNulls(size)
        }
    }
}