package me.roberto.fixiecalc.di

import android.app.Activity
import androidx.fragment.app.Fragment
import javax.inject.Singleton

import dagger.Component
import me.roberto.fixiecalc.ui.FavoritesFragment
import me.roberto.fixiecalc.ui.PickerFragment
import me.roberto.fixiecalc.ui.SpeedCadenceFragment
import me.roberto.kitso.database.AppModule
import me.roberto.kitso.ui.GearViewModel

@Singleton
@Component(modules = [AppModule::class])
interface AppComponent{

    fun inject(fragment: PickerFragment)
    fun inject(fragment: FavoritesFragment)
    fun inject(fragment: SpeedCadenceFragment)
}
