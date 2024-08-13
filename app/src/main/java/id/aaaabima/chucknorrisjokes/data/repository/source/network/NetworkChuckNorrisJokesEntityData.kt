package id.aaaabima.chucknorrisjokes.data.repository.source.network

import id.aaaabima.chucknorrisjokes.data.mapper.toJokeEntity
import id.aaaabima.chucknorrisjokes.data.model.JokeEntity
import id.aaaabima.chucknorrisjokes.data.repository.source.ChuckNorrisJokesEntityData
import io.reactivex.rxjava3.core.Observable
import javax.inject.Inject

class NetworkChuckNorrisJokesEntityData @Inject constructor(
    private val chuckNorrisJokesApi: ChuckNorrisJokesApi
) : ChuckNorrisJokesEntityData {
    override fun searchJokes(query: String): Observable<List<JokeEntity>> {
        return chuckNorrisJokesApi.searchJokes(query).map { jokesResponse ->
            jokesResponse.result.map { it.toJokeEntity() }
        }
    }
}