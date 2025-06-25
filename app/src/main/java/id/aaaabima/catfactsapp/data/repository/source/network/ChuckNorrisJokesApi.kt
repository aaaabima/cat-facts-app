package id.aaaabima.catfactsapp.data.repository.source.network

import id.aaaabima.catfactsapp.data.model.JokesResponse
import io.reactivex.rxjava3.core.Observable
import retrofit2.http.GET

interface ChuckNorrisJokesApi {

    @GET("fact")
    fun searchJokes(): Observable<JokesResponse>
}