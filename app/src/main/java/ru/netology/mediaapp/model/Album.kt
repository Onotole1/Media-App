package ru.netology.mediaapp.model

import com.google.gson.annotations.SerializedName

data class Album(
    @SerializedName("artist")
    val artist: String,
    @SerializedName("genre")
    val genre: String,
    @SerializedName("id")
    val id: Int,
    @SerializedName("published")
    val published: String,
    @SerializedName("subtitle")
    val subtitle: String,
    @SerializedName("title")
    val title: String,
    @SerializedName("tracks")
    val tracks: List<Track>
)