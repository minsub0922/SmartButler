package com.kau.smartbutler.model

import android.os.Parcel
import android.os.Parcelable
import androidx.databinding.ObservableField

data class Device(
        var type:Int,
        var name:String,
        var path: String = "",
        var stateInHome:Boolean = false,
        var stateOutOfHome: Boolean = false,
        var stringType: String = ""

    //type 0: 기기 추가 / 1:서재조명 / 2:tv / 3:에어컨
): Parcelable {
    constructor(parcel: Parcel) : this(
            parcel.readInt(),
            parcel.readString(),
            parcel.readString(),
            parcel.readByte() != 0.toByte(),
            parcel.readByte() != 0.toByte(),
            parcel.readString()) {
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeInt(type)
        parcel.writeString(name)
        parcel.writeString(path)
        parcel.writeByte(if (stateInHome) 1 else 0)
        parcel.writeByte(if (stateOutOfHome) 1 else 0)
        parcel.writeString(stringType)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<Device> {
        override fun createFromParcel(parcel: Parcel): Device {
            return Device(parcel)
        }

        override fun newArray(size: Int): Array<Device?> {
            return arrayOfNulls(size)
        }
    }
}