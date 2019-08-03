package me.roberto.fixiecalc.di

import android.app.Application

class ApplicationClass: Application(){

    companion object {
    lateinit var appComponent: AppComponent
}

override fun onCreate() {
    super.onCreate()
    appComponent = DaggerAppComponent.builder().appModule(AppModule(this)).build()
}
}