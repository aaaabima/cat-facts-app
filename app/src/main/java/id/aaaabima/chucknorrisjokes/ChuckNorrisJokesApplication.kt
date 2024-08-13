package id.aaaabima.chucknorrisjokes

import android.app.Application
import id.aaaabima.chucknorrisjokes.di.component.ApplicationComponent
import id.aaaabima.chucknorrisjokes.di.component.DaggerApplicationComponent
import id.aaaabima.chucknorrisjokes.di.module.ApplicationModule

class ChuckNorrisJokesApplication : Application() {

    private lateinit var applicationComponent: ApplicationComponent

    fun getApplicationComponent() = applicationComponent

    override fun onCreate() {
        super.onCreate()
        applicationComponent = DaggerApplicationComponent.builder()
            .applicationModule(ApplicationModule(this))
            .build()
    }
}