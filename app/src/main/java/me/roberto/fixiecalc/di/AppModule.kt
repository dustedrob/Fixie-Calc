package me.roberto.fixiecalc.di

import dagger.Module
import dagger.Provides
import me.roberto.fixiecalc.database.GearDatabase
import javax.inject.Singleton

@Module
class  AppModule (private val application: ApplicationClass) {

    private val PREFS="me.roberto.track.prefs"

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