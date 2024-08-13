package id.aaaabima.chucknorrisjokes.data.repository

import id.aaaabima.chucknorrisjokes.data.mapper.toJoke
import id.aaaabima.chucknorrisjokes.data.repository.source.ChuckNorrisJokesEntityDataFactory
import id.aaaabima.chucknorrisjokes.domain.ChuckNorrisJokesRepository
import id.aaaabima.chucknorrisjokes.domain.model.Joke
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class ChuckNorrisJokesEntityRepository @Inject constructor(
    private val chuckNorrisJokesEntityDataFactory: ChuckNorrisJokesEntityDataFactory
) : ChuckNorrisJokesRepository {
    private fun getRepository() = chuckNorrisJokesEntityDataFactory.createEntityData()

    override fun searchJokes(query: String): Flow<List<Joke>> {
        return getRepository().searchJokes(query).map { jokesResult ->
            jokesResult.map { it.toJoke() }
        }
    }
}