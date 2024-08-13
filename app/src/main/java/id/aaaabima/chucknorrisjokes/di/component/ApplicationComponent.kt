package id.aaaabima.chucknorrisjokes.di.component

import dagger.Component
import id.aaaabima.chucknorrisjokes.data.di.NetworkModule
import id.aaaabima.chucknorrisjokes.di.module.ApplicationModule
import id.aaaabima.chucknorrisjokes.di.module.MainViewModelModule
import id.aaaabima.chucknorrisjokes.domain.ChuckNorrisJokesRepository
import id.aaaabima.chucknorrisjokes.ui.main.MainActivity
import javax.inject.Singleton

@Singleton
@Component(
    modules = [
        ApplicationModule::class,
        NetworkModule::class,
        MainViewModelModule::class
    ]
)
interface ApplicationComponent {

    fun chuckNorrisJokesRepository(): ChuckNorrisJokesRepository

    fun inject(activity: MainActivity)
}