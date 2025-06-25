package id.aaaabima.catfactsapp.data.di

import dagger.Module
import dagger.Provides
import id.aaaabima.catfactsapp.BuildConfig
import id.aaaabima.catfactsapp.data.repository.source.network.ChuckNorrisJokesApi
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.adapter.rxjava3.RxJava3CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit
import javax.inject.Singleton

@Module
class NetworkModule {
    @Provides
    @Singleton
    fun provideBaseURL() = BuildConfig.BASE_URL

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
        httpsLoggingInterceptor: HttpLoggingInterceptor
    ): OkHttpClient.Builder =
        OkHttpClient.Builder()
            .addNetworkInterceptor(httpsLoggingInterceptor)
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
            .addCallAdapterFactory(RxJava3CallAdapterFactory.create())
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