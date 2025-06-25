package id.aaaabima.catfactsapp.data.repository

import id.aaaabima.catfactsapp.data.mapper.toJoke
import id.aaaabima.catfactsapp.data.repository.source.ChuckNorrisJokesEntityDataFactory
import id.aaaabima.catfactsapp.domain.ChuckNorrisJokesRepository
import id.aaaabima.catfactsapp.domain.model.Joke
import io.reactivex.rxjava3.core.Observable
import javax.inject.Inject

class ChuckNorrisJokesEntityRepository @Inject constructor(
    private val chuckNorrisJokesEntityDataFactory: ChuckNorrisJokesEntityDataFactory
) : ChuckNorrisJokesRepository {
    private fun getRepository() = chuckNorrisJokesEntityDataFactory.createEntityData()

    override fun searchJokes(): Observable<Joke> {
        return getRepository().searchJokes().map {
            it.toJoke()
        }
    }
}