package me.roberto.fixiecalc.ui

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import io.reactivex.Completable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import me.roberto.gear.domain.Rollout
import me.roberto.gear.domain.calculations.Calculations
import me.roberto.fixiecalc.database.GearDao
import me.roberto.gear.domain.Gear
import java.util.HashSet

/**
 * Created by roberto on 6/07/17.
 */
class GearViewModel(private val gearDao: GearDao) : ViewModel() {

    private val favoriteGears: HashSet<Gear> = HashSet()
    var gears: MutableLiveData<List<Gear>> = MutableLiveData()
    var favorite: MutableLiveData<Boolean> = MutableLiveData()
    var rolloutLiveData :MutableLiveData<RolloutValue> = MutableLiveData()
    val compositeDisposable = CompositeDisposable()

    override fun onCleared() {
        super.onCleared()
        compositeDisposable.clear()

    }
    fun loadFavoriteGears() {
        gearDao.loadGears().subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe{ list->
                    val sortedlist = list.sortedWith(Comparator { p0, p1 ->
                        val gear0 = Calculations.calculateGear(p0!!.wheelSize, p0.chainRing, p0.cog, Rollout.METERS)
                        val gear1 = Calculations.calculateGear(p1!!.wheelSize, p1.chainRing, p1.cog, Rollout.METERS)
                        when {
                            gear0 < gear1 -> -1
                            gear0 == gear1 -> 0
                            else -> 1
                        }
                    })
                    favoriteGears.clear()
                    favoriteGears.addAll(sortedlist)
                    gears.postValue(sortedlist)
                }
                .also { compositeDisposable.add(it) }
    }

    private fun deleteFavoriteGear(gear: Gear)
    {
        Completable.fromAction{gearDao.delete(gear)}
                .subscribeOn(Schedulers.newThread()).observeOn(AndroidSchedulers.mainThread())
                .subscribe{loadFavoriteGears()}
                .also { compositeDisposable.add(it) }
    }

    private fun insertFavoriteGear(gear: Gear)
    {

        Completable.fromAction{gearDao.save(gear)}
                .subscribeOn(Schedulers.newThread()).observeOn(AndroidSchedulers.mainThread())
                .subscribe{loadFavoriteGears()}
                .also { compositeDisposable.add(it) }
    }

    fun changeFavoriteStatus(gear: Gear?, checked: Boolean) {
        gear?.let {
                if (checked) {
                    insertFavoriteGear(it)
                } else {
                    deleteFavoriteGear(it)
                }
            }
        }

    fun calculateGear(gear: Gear) {
        val gearMeters = Calculations.calculateGear(gear.wheelSize, gear.chainRing, gear.cog, Rollout.METERS)
        rolloutLiveData.value = RolloutValue(Rollout.METERS,gearMeters)
        val gearInches = Calculations.calculateGear(gear.wheelSize, gear.chainRing, gear.cog, Rollout.INCHES)
        rolloutLiveData.value = RolloutValue(Rollout.INCHES,gearInches)
    }

    fun isFavorite(gear: Gear) = favorite.postValue(favoriteGears.contains(gear))


    class RolloutValue(val rollout: Rollout, val rollValue: Double)


}