package me.roberto.fixiecalc.di

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import me.roberto.fixiecalc.database.GearDao
import me.roberto.fixiecalc.ui.GearViewModel


class ViewModelFactory(private val dataSource: GearDao) : ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(GearViewModel::class.java)) {
            return GearViewModel(dataSource) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}