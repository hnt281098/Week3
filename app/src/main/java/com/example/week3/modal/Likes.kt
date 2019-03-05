package com.example.week3.modal

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Likes(
    val `data`: ArrayList<DataXXX>?,
    val pagingX: PagingX?,
    val summary: Summary?
): Parcelable