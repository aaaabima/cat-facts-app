package id.aaaabima.chucknorrisjokes.data.mapper

import id.aaaabima.chucknorrisjokes.data.model.JokeEntity
import id.aaaabima.chucknorrisjokes.data.model.JokesItem
import id.aaaabima.chucknorrisjokes.domain.model.Joke

fun JokesItem.toJokeEntity() = JokeEntity(
    iconUrl = iconUrl,
    id = id,
    url = url,
    value = value
)

fun JokeEntity.toJoke() = Joke(
    iconUrl = iconUrl,
    id = id,
    url = url,
    value = value
)