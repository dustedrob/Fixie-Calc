package me.roberto.kitso.ui

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import me.roberto.fixiecalc.ui.Gear
import me.roberto.kitso.database.GearDao

/**
 * Created by roberto on 6/07/17.
 */
class GearViewModel(private val dataSource: GearDao) : ViewModel() {
    var gears: MutableLiveData<List<Gear>> = MutableLiveData()

    private val TAG: String? = "gear_model"

    fun loadFavoriteGears() {


        dataSource.load().subscribeOn(Schedulers.newThread()).doOnSuccess { list-> gears.value=list }
                .observeOn(AndroidSchedulers.mainThread()).subscribe()
    }




    fun deleteFavoriteGear(gear:Gear)
    {
        dataSource.delete(gear)
    }

    fun insertFavoriteGear(gear:Gear)
    {
        dataSource.save(gear)
    }

}