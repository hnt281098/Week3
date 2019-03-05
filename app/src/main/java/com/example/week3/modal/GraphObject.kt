package com.example.week3.modal

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class GraphObject(
    val `data`: ArrayList<Data>?,
    val paging: Paging?
): Parcelable