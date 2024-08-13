package id.aaaabima.chucknorrisjokes.data.repository.source

import id.aaaabima.chucknorrisjokes.data.model.JokeEntity
import io.reactivex.rxjava3.core.Observable

interface ChuckNorrisJokesEntityData {
    fun searchJokes(query: String): Observable<List<JokeEntity>>

}