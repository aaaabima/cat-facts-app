package id.aaaabima.catfactsapp.data.model

import com.google.gson.annotations.SerializedName

data class JokesResponse(
    @SerializedName("fact")
    val fact: String,
    @SerializedName("length")
    val length: Int
)

data class JokesItem(
    val id: String,
    @SerializedName("icon_url")
    val iconUrl: String,
    val url: String,
    val value: String
)