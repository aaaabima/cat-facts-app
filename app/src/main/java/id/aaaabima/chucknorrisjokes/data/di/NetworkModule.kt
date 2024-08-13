package id.aaaabima.chucknorrisjokes.data.di

import dagger.Provides
import id.aaaabima.chucknorrisjokes.BuildConfig
import id.aaaabima.chucknorrisjokes.data.repository.source.network.ChuckNorrisJokesApi
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit
import javax.inject.Singleton

class NetworkModule {
    @Provides
    @Singleton
    fun provideHttpLoggingInterceptor(): HttpLoggingInterceptor {
        return HttpLoggingInterceptor().apply {
            level = if (BuildConfig.DEBUG) {
                HttpLoggingInterceptor.Level.BODY
            } else {
                HttpLoggingInterceptor.Level.NONE
            }
        }
    }

    @Provides
    @Singleton
    fun provideOkHttpClientBuilder(
        httpLoggingInterceptor: HttpLoggingInterceptor
    ): OkHttpClient.Builder =
        OkHttpClient.Builder()
            .writeTimeout(DEFAULT_TIMEOUT, TimeUnit.MILLISECONDS)
            .connectTimeout(DEFAULT_TIMEOUT, TimeUnit.MILLISECONDS)
            .readTimeout(DEFAULT_TIMEOUT, TimeUnit.MILLISECONDS)

    @Provides
    @Singleton
    fun provideRetrofitBuilder(
        baseUrl: String
    ): Retrofit.Builder =
        Retrofit.Builder()
            .baseUrl(baseUrl)
            .addConverterFactory(GsonConverterFactory.create())

    @Provides
    @Singleton
    fun provideChuckNorrisJokesApi(
        okHttpClientBuilder: OkHttpClient.Builder,
        retrofitBuilder: Retrofit.Builder
    ): ChuckNorrisJokesApi =
        provideService(ChuckNorrisJokesApi::class.java, okHttpClientBuilder, retrofitBuilder)

    private fun <T> provideService(
        service: Class<T>,
        okHttpClientBuilder: OkHttpClient.Builder,
        retrofitBuilder: Retrofit.Builder
    ): T {
        retrofitBuilder.client(okHttpClientBuilder.build())
        return retrofitBuilder.build().create(service)
    }

    companion object {
        private const val DEFAULT_TIMEOUT = 10L * 1000
    }
}