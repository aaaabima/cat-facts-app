package id.aaaabima.chucknorrisjokes.data.repository.source.network

import id.aaaabima.chucknorrisjokes.data.mapper.toJokeEntity
import id.aaaabima.chucknorrisjokes.data.model.JokeEntity
import id.aaaabima.chucknorrisjokes.data.repository.source.ChuckNorrisJokesEntityData
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class NetworkChuckNorrisJokesEntityData @Inject constructor(
    private val chuckNorrisJokesApi: ChuckNorrisJokesApi
) : ChuckNorrisJokesEntityData {
    override fun searchJokes(query: String): Flow<List<JokeEntity>> {
        return chuckNorrisJokesApi.searchJokes(query).map { jokesResponse ->
            jokesResponse.result.map { it.toJokeEntity() }
        }
    }
}