package me.roberto.kitso.database

import dagger.Module
import dagger.Provides
import me.roberto.fixiecalc.di.ApplicationClass
import me.roberto.fixiecalc.di.ViewModelFactory
import javax.inject.Singleton

@Module
class  AppModule (private val application: ApplicationClass) {

    @Singleton
    @Provides
    fun provideViewModelFactory(): ViewModelFactory {
        val database = GearDatabase.getInstance(application)
        return ViewModelFactory(database.GearDatabase_Impl())
    }


}