package id.aaaabima.chucknorrisjokes.data.repository.source.network

import id.aaaabima.chucknorrisjokes.data.model.JokesResponse
import kotlinx.coroutines.flow.Flow
import retrofit2.http.GET
import retrofit2.http.Path

interface ChuckNorrisJokesApi {

    @GET("jokes/search/{query}")
    fun searchJokes(
        @Path("query") query: String
    ): Flow<JokesResponse>
}