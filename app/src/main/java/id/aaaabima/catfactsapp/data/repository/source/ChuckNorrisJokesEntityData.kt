package id.aaaabima.catfactsapp.data.repository.source

import id.aaaabima.catfactsapp.data.model.JokeEntity
import io.reactivex.rxjava3.core.Observable

interface ChuckNorrisJokesEntityData {
    fun searchJokes(): Observable<JokeEntity>

}