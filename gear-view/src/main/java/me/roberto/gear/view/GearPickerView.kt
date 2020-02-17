package me.roberto.gear.view

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import androidx.cardview.widget.CardView
import kotlinx.android.synthetic.main.gear_picker.view.cog_bar
import kotlinx.android.synthetic.main.gear_picker.view.ring_bar
import kotlinx.android.synthetic.main.gear_picker.view.wheel_spinner
import me.roberto.OnEditableSeekBarChangeListener
import me.roberto.gear.domain.Gear
import me.roberto.gear.domain.calculations.Calculations

class GearPickerView(context: Context, attrs: AttributeSet?) : CardView(context, attrs), AdapterView.OnItemSelectedListener {

    var onGearSelected: ((Gear) -> Unit)? = null
    var currentGear: Gear? = null


    private val gearListener = object : OnEditableSeekBarChangeListener() {

        override fun onEditableSeekBarValueChanged(id: Int, value: Int) {

            super.onEditableSeekBarValueChanged(id, value)

            currentGear = getGear(ring_bar.value, cog_bar.value, Calculations.wheelSizes[wheel_spinner.selectedItemPosition]).also {
                onGearSelected?.invoke(it)
            }
        }
    }


    init {

        val layoutInflater = context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        layoutInflater.inflate(R.layout.gear_picker, this)

        val wheelAdapter = ArrayAdapter.createFromResource(context, R.array.wheel_values, android.R.layout.simple_spinner_dropdown_item)
        wheel_spinner.adapter = wheelAdapter
        wheel_spinner.onItemSelectedListener = this

        ring_bar.setOnEditableSeekBarChangeListener(gearListener)
        cog_bar.setOnEditableSeekBarChangeListener(gearListener)
        currentGear = getGear(ring_bar.value, cog_bar.value, Calculations.wheelSizes[wheel_spinner.selectedItemPosition])

    }

    private fun getGear(chainRingValue: Int, cogValue: Int, wheelSize: Int) = Gear(chainRingValue, cogValue, wheelSize)

    override fun onNothingSelected(p0: AdapterView<*>?) {}

    override fun onItemSelected(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {

        onGearSelected?.invoke(getGear(ring_bar.value, cog_bar.value, Calculations.wheelSizes[wheel_spinner.selectedItemPosition]))
    }

}