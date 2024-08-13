package id.aaaabima.chucknorrisjokes.data.model

import com.google.gson.annotations.SerializedName

data class JokesResponse(
    @SerializedName("total")
    val total: Int,
    @SerializedName("result")
    val result: List<JokesItem>
)

data class JokesItem(
    val id: String,
    @SerializedName("icon_url")
    val iconUrl: String,
    val url: String,
    val value: String
)