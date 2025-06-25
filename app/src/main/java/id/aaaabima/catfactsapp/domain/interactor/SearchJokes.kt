package id.aaaabima.catfactsapp.domain.interactor

import id.aaaabima.catfactsapp.domain.ChuckNorrisJokesRepository
import id.aaaabima.catfactsapp.domain.base.BaseUseCase
import id.aaaabima.catfactsapp.domain.base.NoParam
import id.aaaabima.catfactsapp.domain.model.Joke
import io.reactivex.rxjava3.core.Observable
import javax.inject.Inject

class SearchJokes @Inject constructor(
    private val chuckNorrisJokesRepository: ChuckNorrisJokesRepository
) : BaseUseCase<NoParam, Joke>() {

    override fun buildUseCase(param: NoParam): Observable<Joke> =
        chuckNorrisJokesRepository.searchJokes()
}