package me.roberto.gear.domain

import androidx.annotation.Keep
import androidx.room.Entity
import androidx.room.PrimaryKey

@Keep
@Entity
class Gear(val chainRing: Int, val cog: Int, val wheelSize: Int) {



    @PrimaryKey
    var id="$chainRing$cog$wheelSize"


    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as Gear

        if (id != other.id) return false

        return true
    }


    override fun toString(): String {
        return "$chainRing x $cog"
    }

    override fun hashCode(): Int {
        return id.hashCode()
    }


}