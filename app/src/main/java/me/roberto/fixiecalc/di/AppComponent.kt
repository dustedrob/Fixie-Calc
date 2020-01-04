package me.roberto.fixiecalc.di

import dagger.Component
import me.roberto.fixiecalc.ui.FavoritesFragment
import me.roberto.fixiecalc.ui.GearRecyclerViewAdapter
import me.roberto.fixiecalc.ui.PickerFragment
import me.roberto.fixiecalc.ui.SpeedCadenceFragment
import javax.inject.Singleton

@Singleton
@Component(modules = [AppModule::class])
interface AppComponent{

    fun inject(fragment: PickerFragment)
    fun inject(fragment: FavoritesFragment)
    fun inject(fragment: SpeedCadenceFragment)
    fun inject(adapter: GearRecyclerViewAdapter)
}
