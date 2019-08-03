package me.roberto.kitso.database

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase.CONFLICT_NONE
import android.graphics.Color
import android.util.Log
import androidx.room.Database
import androidx.room.OnConflictStrategy
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase
import androidx.sqlite.db.SupportSQLiteQuery
import me.roberto.fixiecalc.ui.Gear
import java.util.*

@Database(entities = [(Gear::class)],version = 2,exportSchema = false)
abstract class GearDatabase: RoomDatabase() {

    abstract fun GearDatabase_Impl():GearDao


    companion object {

        val MIGRATION_1_2 = object :Migration(1,2){
            override fun migrate(database: SupportSQLiteDatabase) {


                Log.w("database", "running migration: " )
                database.execSQL("ALTER TABLE gear "
                        + " ADD COLUMN color INTEGER")

                val cursor = database.query("Select id from gear")

                val rnd = Random()
                database.beginTransaction()
                while (cursor.moveToNext())
                {

                    val color = Color.argb(255, rnd.nextInt(256), rnd.nextInt(256), rnd.nextInt(256))
                    val id=cursor.getString(0)
                    val values=ContentValues()
                    values.put("color",color)
                    database.update("gear",CONFLICT_NONE,values,"id = ?", arrayOf(id))

                }
                database.endTransaction()

            }

        }

        @Volatile
        private var INSTANCE:GearDatabase? = null

        fun getInstance(context: Context): GearDatabase =
                INSTANCE ?: synchronized(this) {
                    INSTANCE ?: buildDatabase(context).also { INSTANCE = it }
                }

        private fun buildDatabase(context: Context) =
                Room.databaseBuilder(context.applicationContext,
                        GearDatabase::class.java, "Data.db")
                        .addMigrations(MIGRATION_1_2)
                        .build()
    }

}