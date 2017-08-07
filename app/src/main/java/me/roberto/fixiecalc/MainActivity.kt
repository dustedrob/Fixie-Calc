package me.roberto.fixiecalc

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.util.Log
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import kotlinx.android.synthetic.main.activity_main.*
import me.roberto.OnEditableSeekBarChangeListener

class MainActivity : AppCompatActivity(),AdapterView.OnItemSelectedListener{

    val PREFS="me.roberto.track.prefs"
    val TAG="MAIN"
    private val  PREFS_SYSTEM="me.roberto.tracks.prefs.system"
    var wheelSizes= intArrayOf(2070,2080,2086,2096,2105,2136,2146,2155,2168)


    fun calculateGear(wheelSize: Int, ring: Int, cog:Int,type: Measure):Double
    {


        val development = ring.toFloat () / cog.toFloat()


        when(type){
            //convert the circumference in mm to meters
            Measure.METERS ->return development*(wheelSize*0.001)

            //first get the diameter of the circumference and then convert it to inches
            Measure.INCHES ->return development*(wheelSize/3.1416/25.4)
        }
    }

    fun updateUI(selectedWheelSize:Int, selectedRing:Int, selectedCog:Int)
    {

        val prefs=this.getSharedPreferences(PREFS,0)
        var measure: Measure? =null

        if (radioGroup.checkedRadioButtonId== metersRadio.id)
        {
            unitText.text="m"
            measure = Measure.METERS
        }
        else
        {
            unitText.text="in"
            measure = Measure.INCHES

        }
        prefs.edit().putInt(PREFS_SYSTEM, measure.ordinal).commit()

        rollout.text="%.2f".format(calculateGear(selectedWheelSize,selectedRing,selectedCog, measure))


    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val prefs=this.getSharedPreferences(PREFS,0)

        val wheelAdapter=ArrayAdapter.createFromResource(this,R.array.wheel_values,android.R.layout.simple_spinner_dropdown_item)
        wheel_spinner.adapter=wheelAdapter
        wheel_spinner.onItemSelectedListener=this


        when (prefs.getInt(PREFS_SYSTEM, Measure.METERS.ordinal))
        {
            Measure.METERS.ordinal->radioGroup.check(R.id.metersRadio)
            Measure.INCHES.ordinal->radioGroup.check(R.id.inchesRadio)
        }

        radioGroup . setOnCheckedChangeListener { radioGroup, i ->
            updateUI(wheelSizes[wheel_spinner.selectedItemPosition],ring_bar.value,cog_bar.value)
        }

        ring_bar.setOnEditableSeekBarChangeListener(gearListener)
        cog_bar.setOnEditableSeekBarChangeListener(gearListener)

    }

    var gearListener:OnEditableSeekBarChangeListener =object: OnEditableSeekBarChangeListener()
    {

        override fun onEditableSeekBarValueChanged(id: Int, value: Int) {
            super.onEditableSeekBarValueChanged(id, value)
            updateUI(wheelSizes[wheel_spinner.selectedItemPosition],ring_bar.value,cog_bar.value)
        }

    }


    override fun onNothingSelected(p0: AdapterView<*>?) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun onItemSelected(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {
        Log.i(TAG, "selected : "+p3)
        updateUI(wheelSizes[wheel_spinner.selectedItemPosition],ring_bar.value,cog_bar.value)

    }

}
