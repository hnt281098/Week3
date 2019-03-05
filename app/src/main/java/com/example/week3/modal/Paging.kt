package com.example.week3.modal

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Paging(
    val previous: String?,
    val next: String?
): Parcelable