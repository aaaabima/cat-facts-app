package id.aaaabima.chucknorrisjokes.domain

import id.aaaabima.chucknorrisjokes.domain.model.Joke
import kotlinx.coroutines.flow.Flow

interface ChuckNorrisJokesRepository {

    fun searchJokes(query: String): Flow<List<Joke>>
}