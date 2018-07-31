package me.roberto.fixiecalc.Calculations

import me.roberto.fixiecalc.Measure

class Calculations{


    companion object {




        var wheelSizes= intArrayOf(2070,2080,2086,2096,2105,2136,2146,2155,2168)



        fun calculateGear(wheelSize: Int, ring: Int, cog:Int,type: Measure):Double
        {


            val development = ring.toFloat () / cog.toFloat()


            return when(type){
            //convert the circumference in mm to meters
                Measure.METERS -> development*(wheelSize*0.001)

            //first get the diameter of the circumference and then convert it to inches
                Measure.INCHES -> development*(wheelSize/3.1416/25.4)
            }
        }


    }


}