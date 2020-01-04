package me.roberto.fixiecalc.ui

import android.content.SharedPreferences
import android.graphics.Color
import android.graphics.Typeface
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.Animation
import android.view.animation.BounceInterpolator
import android.view.animation.ScaleAnimation
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.CompoundButton
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import kotlinx.android.synthetic.main.fragment_picker.*
import kotlinx.android.synthetic.main.gear_picker.*
import me.roberto.OnEditableSeekBarChangeListener
import me.roberto.fixiecalc.R
import me.roberto.fixiecalc.Rollout
import me.roberto.fixiecalc.calculations.Calculations
import me.roberto.fixiecalc.calculations.Calculations.wheelSizes
import me.roberto.fixiecalc.di.ApplicationClass
import me.roberto.fixiecalc.di.ViewModelFactory
import java.util.*
import javax.inject.Inject


class PickerFragment : Fragment() {




    @Inject
    lateinit var viewModelFactory: ViewModelFactory
    @Inject
    lateinit var prefs:SharedPreferences
    private lateinit var viewModel: GearViewModel
    private val TAG = "gear_picker"
    var cog = 11
    var chainRing = 44
    var wheelSize = 0

    private val favoriteGears: HashSet<Gear> = HashSet()


    init {


    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        ApplicationClass.appComponent.inject(this)
        activity?.let {
            viewModel = ViewModelProviders.of(it,viewModelFactory).get(GearViewModel::class.java)
        }
        viewModel.gears.observe(this, observer)
        viewModel.loadFavoriteGears()
    }



    private val observer: Observer<List<Gear>> = Observer { list ->
        favoriteGears.clear()
        favoriteGears.addAll(list)

    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
    }


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_picker, container, false)
    }



    private fun getGear(): Gear {
        //We'll use the color for the cadence chart
        val rnd = Random()
        val color = Color.argb(255, rnd.nextInt(256), rnd.nextInt(256), rnd.nextInt(256))

        return Gear(ring_bar.value, cog_bar.value, wheelSizes[wheel_spinner.selectedItemPosition], color)

    }

    fun updateUI(selectedWheelSize: Int, selectedRing: Int, selectedCog: Int) {

        button_favorite.setOnCheckedChangeListener(null)
        //we have to disable the listener while changing the state to avoid unnecessary calls to the db
        button_favorite.isChecked = favoriteGears.contains(getGear())
        button_favorite.setOnCheckedChangeListener(checkedChangeListener)

        val gearMeters = Calculations.calculateGear(selectedWheelSize, selectedRing, selectedCog, Rollout.METERS)
        setText(rolloutMeters,metersText,gearMeters)
        val gearInches = Calculations.calculateGear(selectedWheelSize, selectedRing, selectedCog, Rollout.INCHES)
        setText(rolloutInches,inchesText,gearInches)
    }


    private fun setText(rolloutText: TextView, unitText: TextView, gear: Double){

        rolloutText.startAnimation(getAnimation())
        unitText.startAnimation(getAnimation())
        rolloutText.text = "%.2f".format(gear)
    }


    private fun getAnimation(): ScaleAnimation {
        val scaleAnimation = ScaleAnimation(0.7f, 1.0f, 0.7f, 1.0f, Animation.RELATIVE_TO_SELF, 0.7f, Animation.RELATIVE_TO_SELF, 0.7f)
        scaleAnimation.duration = 500
        val bounceInterpolator = BounceInterpolator()
        scaleAnimation.interpolator = bounceInterpolator

        return scaleAnimation
    }


}
