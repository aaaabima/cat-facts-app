package id.aaaabima.chucknorrisjokes.di.module

import android.app.Application
import android.content.Context
import dagger.Module
import dagger.Provides
import id.aaaabima.chucknorrisjokes.data.repository.ChuckNorrisJokesEntityRepository
import id.aaaabima.chucknorrisjokes.domain.ChuckNorrisJokesRepository
import javax.inject.Singleton

@Module
class ApplicationModule(
    private val application: Application
) {

    @Singleton
    @Provides
    fun provideContext(): Context = application

    @Singleton
    @Provides
    fun provideApplication(): Application = application

    @Singleton
    @Provides
    fun provideChuckNorrisJokesRepository(
        chuckNorrisJokesEntityRepository: ChuckNorrisJokesEntityRepository
    ): ChuckNorrisJokesRepository = chuckNorrisJokesEntityRepository
}