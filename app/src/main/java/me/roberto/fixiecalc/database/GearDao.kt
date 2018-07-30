package me.roberto.kitso.database

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy.REPLACE
import androidx.room.Query
import io.reactivex.Single
import me.roberto.fixiecalc.ui.Gear


@Dao
interface GearDao {


    @Insert(onConflict = REPLACE)
    fun save(item: Gear)

    @Query("SELECT * FROM gear")
    fun load(): Single<List<Gear>>



}
