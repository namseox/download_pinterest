package com.kma.travel.data.model

import com.google.gson.annotations.SerializedName

data class Video(
    @SerializedName("@context")
    val context: String,
    @SerializedName("@id")
    val id: String,
    @SerializedName("@type")
    val type: String,
    val contentUrl: String,
    val description: String,
    val duration: String,
    val name: String,
    val thumbnailUrl: String,
    val uploadDate: String

)