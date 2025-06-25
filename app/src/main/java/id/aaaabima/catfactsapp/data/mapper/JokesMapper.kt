package id.aaaabima.catfactsapp.data.mapper

import id.aaaabima.catfactsapp.data.model.JokeEntity
import id.aaaabima.catfactsapp.data.model.JokesResponse
import id.aaaabima.catfactsapp.domain.model.Joke

fun JokesResponse.toJokeEntity() = JokeEntity(
    fact, length
)

fun JokeEntity.toJoke() = Joke(
    fact, length
)