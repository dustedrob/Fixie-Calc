package me.roberto.fixiecalc.ui

import androidx.annotation.Keep
import androidx.room.Entity
import androidx.room.PrimaryKey

@Keep
@Entity
class Gear(chainRing:Int, cog:Int, wheelSize:Int ) {


    @PrimaryKey
    var id="$chainRing$cog$wheelSize"

    var chainRing=chainRing
    var cog=cog
    var wheelSize=wheelSize



    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as Gear

        if (id != other.id) return false

        return true
    }

    override fun hashCode(): Int {
        return id.hashCode()
    }


}