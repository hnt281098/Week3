package com.example.week3.modal

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class PagingX(
    val cursors: Cursors?
): Parcelable