package id.aaaabima.catfactsapp.data.repository.source.network

import id.aaaabima.catfactsapp.data.mapper.toJokeEntity
import id.aaaabima.catfactsapp.data.model.JokeEntity
import id.aaaabima.catfactsapp.data.repository.source.ChuckNorrisJokesEntityData
import io.reactivex.rxjava3.core.Observable
import javax.inject.Inject

class NetworkChuckNorrisJokesEntityData @Inject constructor(
    private val chuckNorrisJokesApi: ChuckNorrisJokesApi
) : ChuckNorrisJokesEntityData {
    override fun searchJokes(): Observable<JokeEntity> {
        return chuckNorrisJokesApi.searchJokes().map {
            it.toJokeEntity()
        }
    }
}