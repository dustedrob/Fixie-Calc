package me.roberto.kitso.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import me.roberto.fixiecalc.ui.Gear

@Database(entities = [(Gear::class)],version = 1)
abstract class GearDatabase: RoomDatabase() {

    abstract fun GearDatabase_Impl():GearDao


    companion object {

        @Volatile
        private var INSTANCE:GearDatabase? = null

        fun getInstance(context: Context): GearDatabase =
                INSTANCE ?: synchronized(this) {
                    INSTANCE ?: buildDatabase(context).also { INSTANCE = it }
                }

        private fun buildDatabase(context: Context) =
                Room.databaseBuilder(context.applicationContext,
                        GearDatabase::class.java, "Data.db")
                        .build()
    }

}