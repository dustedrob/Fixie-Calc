package me.roberto.kitso.database

import android.content.SharedPreferences
import dagger.Module
import dagger.Provides
import me.roberto.fixiecalc.di.ApplicationClass
import me.roberto.fixiecalc.di.ViewModelFactory
import me.roberto.fixiecalc.ui.BottomActivity
import javax.inject.Singleton

@Module
class  AppModule (private val application: ApplicationClass) {

    val PREFS="me.roberto.track.prefs"

    @Singleton
    @Provides
    fun provideViewModelFactory(): ViewModelFactory {
        val database = GearDatabase.getInstance(application)
        return ViewModelFactory(database.GearDatabase_Impl())
    }

    @Singleton
    @Provides
    fun provideSharedPrefs() = application.getSharedPreferences(PREFS, 0)



}