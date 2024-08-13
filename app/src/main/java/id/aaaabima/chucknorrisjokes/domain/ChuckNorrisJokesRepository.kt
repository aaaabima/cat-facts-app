package id.aaaabima.chucknorrisjokes.domain

import id.aaaabima.chucknorrisjokes.domain.model.Joke
import io.reactivex.rxjava3.core.Observable

interface ChuckNorrisJokesRepository {

    fun searchJokes(query: String): Observable<List<Joke>>
}