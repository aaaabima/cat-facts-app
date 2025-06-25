package id.aaaabima.catfactsapp.domain

import id.aaaabima.catfactsapp.domain.model.Joke
import io.reactivex.rxjava3.core.Observable

interface ChuckNorrisJokesRepository {

    fun searchJokes(): Observable<Joke>
}