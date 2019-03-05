package com.example.week3.modal

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Summary(
    @SerializedName("total_count")
    val totalCount: Int?,
    @SerializedName("can_like")
    val canLike: Boolean?,
    @SerializedName("has_liked")
    val hasLiked: Boolean?
): Parcelable