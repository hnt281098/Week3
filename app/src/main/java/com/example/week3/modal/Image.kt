package com.example.week3.modal

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Image(
    val height: Int?,
    val src: String?,
    val width: Int?
): Parcelable