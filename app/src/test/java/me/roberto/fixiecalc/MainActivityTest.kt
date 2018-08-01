package me.roberto.fixiecalc

import me.roberto.fixiecalc.calculations.Calculations
import org.junit.Test

import org.junit.Assert.*

/**
 * Created by roberto on 6/08/17.
 */
class MainActivityTest {



    @Test
    fun calculateGear() {

        val gear = Calculations.calculateGear(2096, 50, 15, Measure.METERS)
        assertEquals(6.99,gear,0.1)
    }

}