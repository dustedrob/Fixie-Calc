package me.roberto.fixiecalc.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import me.roberto.gear.domain.Gear

@Database(entities = [(Gear::class)],version = 1,exportSchema = false)
abstract class GearDatabase: RoomDatabase() {

    abstract fun GearDatabase_Impl(): GearDao


    companion object {


        @Volatile
        private var INSTANCE: GearDatabase? = null

        fun getInstance(context: Context): GearDatabase =
                INSTANCE ?: synchronized(this) {
                    INSTANCE ?: buildDatabase(context).also { INSTANCE = it }
                }

        private fun buildDatabase(context: Context) =
                Room.databaseBuilder(context.applicationContext,
                        GearDatabase::class.java, "Data.db")
                        .fallbackToDestructiveMigrationOnDowngrade()
                        .build()
    }

}