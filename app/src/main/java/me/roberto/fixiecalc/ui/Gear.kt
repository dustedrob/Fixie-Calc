package me.roberto.fixiecalc.ui

import androidx.annotation.Keep
import androidx.room.Entity
import androidx.room.PrimaryKey

@Keep
@Entity
class Gear(@PrimaryKey var id:String, var chainRing:Int, var cog:Int, wheelSize:Int )