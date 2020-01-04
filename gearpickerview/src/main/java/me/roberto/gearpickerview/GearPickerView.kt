package me.roberto.gearpickerview

import android.content.Context
import android.graphics.Typeface
import android.util.AttributeSet
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.CompoundButton
import androidx.cardview.widget.CardView
import kotlinx.android.synthetic.main.gear_picker.view.*
import me.roberto.OnEditableSeekBarChangeListener

class GearPickerView(context: Context, attrs: AttributeSet?) : CardView(context, attrs), AdapterView.OnItemSelectedListener {

    val gearListener = object : OnEditableSeekBarChangeListener() {

        override fun onEditableSeekBarValueChanged(id: Int, value: Int) {
            super.onEditableSeekBarValueChanged(id, value)
            updateUI(wheelSizes[wheel_spinner.selectedItemPosition], ring_bar.value, cog_bar.value)
        }

    }
    private val checkedChangeListener = CompoundButton.OnCheckedChangeListener { compoundButton, checked ->
        changeFavoriteState(compoundButton, checked)
    }


    init {


        button_favorite.setOnCheckedChangeListener(checkedChangeListener)
        val wheelAdapter = ArrayAdapter.createFromResource(context, R.array.wheel_values, android.R.layout.simple_spinner_dropdown_item)
        wheel_spinner.adapter = wheelAdapter
        wheel_spinner.onItemSelectedListener = this

        ring_bar.setOnEditableSeekBarChangeListener(gearListener)
        cog_bar.setOnEditableSeekBarChangeListener(gearListener)
    }

    private fun changeFavoriteState(button: CompoundButton, checked: Boolean) {

        if (checked) {
            viewModel.insertFavoriteGear(getGear())
            favorite_text.setTypeface(null, Typeface.BOLD)
        } else {
            viewModel.deleteFavoriteGear(getGear())
            favorite_text.setTypeface(null, Typeface.NORMAL)
        }
        button.startAnimation(getAnimation())
    }





    override fun onNothingSelected(p0: AdapterView<*>?) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun onItemSelected(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {
        updateUI(wheelSizes[wheel_spinner.selectedItemPosition], ring_bar.value, cog_bar.value)

    }

}