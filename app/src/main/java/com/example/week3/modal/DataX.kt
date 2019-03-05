package com.example.week3.modal

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Parcelize
data class DataX(
    val height: Int?,
    @SerializedName("is_silhouette")
    val isSilhouette: Boolean?,
    val url: String?,
    val width: Int?
): Parcelable