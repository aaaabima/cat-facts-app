package id.aaaabima.chucknorrisjokes.ui.model

import android.os.Parcelable
import id.aaaabima.chucknorrisjokes.domain.model.Joke
import kotlinx.parcelize.Parcelize

@Parcelize
data class JokeModel(
    val id: String,
    val url: String,
    val value: String,
    val iconUrl: String
) : Parcelable

fun Joke.toModel() = JokeModel(
    id = id,
    url = url,
    value = value,
    iconUrl = iconUrl
)