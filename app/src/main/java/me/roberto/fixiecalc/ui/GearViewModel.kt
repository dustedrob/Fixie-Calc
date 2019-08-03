package me.roberto.kitso.ui

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import io.reactivex.Completable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import me.roberto.fixiecalc.di.DaggerAppComponent
import me.roberto.fixiecalc.ui.Gear
import me.roberto.kitso.database.AppModule
import me.roberto.kitso.database.GearDao
import javax.inject.Inject

/**
 * Created by roberto on 6/07/17.
 */
class GearViewModel(private val gearDao: GearDao) : ViewModel() {

    var gears: MutableLiveData<List<Gear>> = MutableLiveData()

    private val TAG: String? = "gear_model"

    fun loadFavoriteGears() {


        gearDao.loadGears().subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe{
                    list-> gears.value=list
                    Log.i(TAG, "getting new gears: ")
                }
    }




    fun deleteFavoriteGear(gear:Gear)
    {
        Completable.fromAction{gearDao.delete(gear)}
                .subscribeOn(Schedulers.newThread()).observeOn(AndroidSchedulers.mainThread())
                .subscribe{loadFavoriteGears()}
    }

    fun insertFavoriteGear(gear:Gear)
    {

        Completable.fromAction{gearDao.save(gear)}
        .subscribeOn(Schedulers.newThread()).observeOn(AndroidSchedulers.mainThread())
                .subscribe{loadFavoriteGears()}
    }

}