package id.aaaabima.chucknorrisjokes.data.repository.source.network

import id.aaaabima.chucknorrisjokes.data.model.JokesResponse
import io.reactivex.rxjava3.core.Observable
import retrofit2.http.GET
import retrofit2.http.Query

interface ChuckNorrisJokesApi {

    @GET("search")
    fun searchJokes(
        @Query("query") query: String
    ): Observable<JokesResponse>
}