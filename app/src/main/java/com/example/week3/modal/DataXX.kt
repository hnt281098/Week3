package com.example.week3.modal

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class DataXX(
    val description: String?,
    val media: Media?,
    val target: Target?,
    val title: String?,
    val type: String?,
    val url: String?
): Parcelable