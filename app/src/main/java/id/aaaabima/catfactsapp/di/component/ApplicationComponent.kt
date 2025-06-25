package id.aaaabima.catfactsapp.di.component

import dagger.Component
import id.aaaabima.catfactsapp.data.di.NetworkModule
import id.aaaabima.catfactsapp.di.module.ApplicationModule
import id.aaaabima.catfactsapp.di.module.MainViewModelModule
import id.aaaabima.catfactsapp.domain.ChuckNorrisJokesRepository
import id.aaaabima.catfactsapp.ui.main.MainActivity
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