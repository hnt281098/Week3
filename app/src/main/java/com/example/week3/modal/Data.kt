package com.example.week3.modal

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Data(
    @SerializedName("created_time")
    val createdTime: Long?,
    val id: String?,
    val message: String?,
    val from: From?,
    val likes: Likes?,
    val attachments: Attachments?,
    @SerializedName("object_id")
    val objectId: String?
): Parcelable