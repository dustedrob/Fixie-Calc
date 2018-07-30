package me.roberto.kitso.database

import android.content.Context
import me.roberto.kitso.ui.ViewModelFactory

object Injection {

    fun provideUserDataSource(context: Context): GearDao {
        val database = GearDatabase.getInstance(context)
        return database.GearDatabase_Impl()
    }

    fun provideViewModelFactory(context: Context): ViewModelFactory {
        val dataSource = provideUserDataSource(context)
        return ViewModelFactory(dataSource)
    }
}