package id.aaaabima.catfactsapp.ui.model

import android.os.Parcelable
import id.aaaabima.catfactsapp.domain.model.Joke
import kotlinx.parcelize.Parcelize

@Parcelize
data class JokeModel(
    val fact: String,
    val length: Int
) : Parcelable

fun Joke.toModel() = JokeModel(
    fact, length
)