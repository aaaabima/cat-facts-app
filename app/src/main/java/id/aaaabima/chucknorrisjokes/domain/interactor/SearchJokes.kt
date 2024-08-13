package id.aaaabima.chucknorrisjokes.domain.interactor

import id.aaaabima.chucknorrisjokes.domain.ChuckNorrisJokesRepository
import id.aaaabima.chucknorrisjokes.domain.base.BaseUseCase
import id.aaaabima.chucknorrisjokes.domain.model.Joke
import io.reactivex.rxjava3.core.Observable
import javax.inject.Inject

class SearchJokes @Inject constructor(
    private val chuckNorrisJokesRepository: ChuckNorrisJokesRepository
) : BaseUseCase<String, List<Joke>>() {

    override fun buildUseCase(param: String): Observable<List<Joke>> =
        chuckNorrisJokesRepository.searchJokes(param)
}