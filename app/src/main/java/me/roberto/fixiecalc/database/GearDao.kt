package me.roberto.fixiecalc.database

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy.REPLACE
import androidx.room.Query
import io.reactivex.Maybe
import me.roberto.fixiecalc.ui.Gear


@Dao
interface GearDao {


    @Insert(onConflict = REPLACE)
    fun save(item: Gear)


    @Delete
    fun delete(item: Gear)


    @Query("SELECT * FROM gear")
    fun loadGears():Maybe<List<Gear>>



}
