package me.roberto.kitso.ui

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import me.roberto.fixiecalc.ui.Gear
import me.roberto.kitso.database.GearDao

/**
 * Created by roberto on 6/07/17.
 */
class GearViewModel(private val dataSource: GearDao) : ViewModel() {
    var gears: MutableLiveData<List<Gear>> = MutableLiveData()

    private val TAG: String? = "gear_model"

    fun loadFavoriteGears() {


        dataSource.load()
    }



}