package id.aaaabima.chucknorrisjokes.data.repository.source

import id.aaaabima.chucknorrisjokes.data.model.JokeEntity
import kotlinx.coroutines.flow.Flow

interface ChuckNorrisJokesEntityData {
    fun searchJokes(query: String): Flow<List<JokeEntity>>

}