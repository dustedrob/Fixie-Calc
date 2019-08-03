package me.roberto.fixiecalc.calculations

import me.roberto.fixiecalc.Rollout
import me.roberto.fixiecalc.SystemType
import me.roberto.fixiecalc.ui.Gear
import kotlin.math.roundToInt

object Calculations {


    private val milesFactor = 1.609344
    var wheelSizes = intArrayOf(2070, 2080, 2086, 2096, 2105, 2136, 2146, 2155, 2168)


    fun calculateCadence(gear: Gear, speed: Int, systemType: SystemType): Double {

        return when (systemType) {
            SystemType.METRIC -> {

                val speedFactor: Double = speed.toDouble().div(60).times(1000)
                val rollout = calculateGear(gear.wheelSize, gear.chainRing, gear.cog, Rollout.METERS)
                speedFactor / rollout

            }

            SystemType.IMPERIAL -> {

                val speedFactor: Double = speed.toDouble().div(60).times(63360)
                val rollout = calculateGear(gear.wheelSize, gear.chainRing, gear.cog, Rollout.INCHES)
                speedFactor / rollout

            }
        }


    }


    fun calculateGear(wheelSize: Int, ring: Int, cog: Int, type: Rollout): Double {

        val development = ring.toFloat() / cog.toFloat()
        return when (type) {
            //convert the circumference in mm to meters
            Rollout.METERS -> development * (wheelSize * 0.001)

            //first get the diameter of the circumference and then convert it to inches
            Rollout.INCHES -> development * (wheelSize / Math.PI / 25.4)
        }
    }

    fun fromMilesToKm(miles: Int) = miles * milesFactor.roundToInt()
    fun fromKmToMiles(km: Int) = km / milesFactor.roundToInt()


}