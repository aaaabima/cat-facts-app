package id.aaaabima.catfactsapp

import android.app.Application
import id.aaaabima.catfactsapp.di.component.ApplicationComponent
import id.aaaabima.catfactsapp.di.component.DaggerApplicationComponent
import id.aaaabima.catfactsapp.di.module.ApplicationModule

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