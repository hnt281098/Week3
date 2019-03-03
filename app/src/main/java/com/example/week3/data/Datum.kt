package com.example.week3.data

data class Datum(
    val created_time : Long,
    val id : String?,
    val message : String?,
    val object_id : String?,
    val likes : Likes?,
    val from : From?,
    val attachments : Attachments?
)
