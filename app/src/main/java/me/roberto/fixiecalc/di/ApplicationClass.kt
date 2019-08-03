package me.roberto.fixiecalc.di

import android.app.Application
import me.roberto.kitso.database.AppModule

class ApplicationClass: Application(){

    companion object {
    lateinit var appComponent: AppComponent
}

override fun onCreate() {
    super.onCreate()
    appComponent = DaggerAppComponent.builder().appModule(AppModule(this)).build()
}
}