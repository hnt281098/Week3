package com.example.week3.modal

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class From(
    val name: String?,
    val picture: Picture?,
    val id: String?
): Parcelable