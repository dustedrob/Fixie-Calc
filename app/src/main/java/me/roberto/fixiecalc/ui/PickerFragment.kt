package me.roberto.fixiecalc.ui

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
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
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
import me.roberto.fixiecalc.ui.BottomActivity.Companion.PREFS
import me.roberto.fixiecalc.ui.BottomActivity.Companion.PREFS_ROLLOUT
import me.roberto.kitso.ui.GearViewModel
import java.util.*
import javax.inject.Inject


class PickerFragment : Fragment(), AdapterView.OnItemSelectedListener {




    @Inject
    lateinit var viewModelFactory: ViewModelFactory
    private lateinit var viewModel:GearViewModel
    val TAG = "gear_picker"
    var cog = 11
    var chainRing = 44
    var wheelSize = 0

    val favoriteGears: HashSet<Gear> = HashSet()
    lateinit var gearListener: OnEditableSeekBarChangeListener


    init {
        gearListener = object : OnEditableSeekBarChangeListener() {

            override fun onEditableSeekBarValueChanged(id: Int, value: Int) {
                super.onEditableSeekBarValueChanged(id, value)
                updateUI(wheelSizes[wheel_spinner.selectedItemPosition], ring_bar.value, cog_bar.value)
            }

        }


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

    fun changeFavoriteState(button: CompoundButton, checked: Boolean) {

        if (checked) {

            viewModel.insertFavoriteGear(getGear())
            favorite_text.setTypeface(null, Typeface.BOLD)
        } else {
            viewModel.deleteFavoriteGear(getGear())
            favorite_text.setTypeface(null, Typeface.NORMAL)
        }

        button.startAnimation(getAnimation())


    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        button_favorite.setOnCheckedChangeListener(checkedChangeListener)
        val prefs = activity?.getSharedPreferences(PREFS, 0)
        val wheelAdapter = ArrayAdapter.createFromResource(context!!, R.array.wheel_values, android.R.layout.simple_spinner_dropdown_item)
        wheel_spinner.adapter = wheelAdapter
        wheel_spinner.onItemSelectedListener = this

        when (prefs?.getInt(PREFS_ROLLOUT, Rollout.METERS.ordinal)) {
            Rollout.METERS.ordinal -> radioGroup.check(R.id.metersRadio)
            Rollout.INCHES.ordinal -> radioGroup.check(R.id.inchesRadio)
        }

        radioGroup.setOnCheckedChangeListener { radioGroup, i ->
            updateUI(wheelSizes[wheel_spinner.selectedItemPosition], ring_bar.value, cog_bar.value)
        }

        ring_bar.setOnEditableSeekBarChangeListener(gearListener)
        cog_bar.setOnEditableSeekBarChangeListener(gearListener)


    }

    private val checkedChangeListener = CompoundButton.OnCheckedChangeListener { compoundButton, checked ->
        changeFavoriteState(compoundButton, checked)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_picker, container, false)
    }


    override fun onNothingSelected(p0: AdapterView<*>?) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun onItemSelected(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {
        Log.i(TAG, "selected : " + p3)
        updateUI(wheelSizes[wheel_spinner.selectedItemPosition], ring_bar.value, cog_bar.value)

    }


    fun getGear(): Gear {
        //We'll use the color for the cadence chart
        val rnd = Random()
        val color = Color.argb(255, rnd.nextInt(256), rnd.nextInt(256), rnd.nextInt(256))

        return Gear(ring_bar.value, cog_bar.value, wheelSizes[wheel_spinner.selectedItemPosition], color)

    }

    fun updateUI(selectedWheelSize: Int, selectedRing: Int, selectedCog: Int) {

        val prefs = context?.getSharedPreferences(PREFS, 0)
        var roll: Rollout? = null

        if (radioGroup.checkedRadioButtonId == metersRadio.id) {
            unitText.text = "m"
            roll = Rollout.METERS
        } else {
            unitText.text = "in"
            roll = Rollout.INCHES

        }
        prefs?.edit()?.putInt(PREFS_ROLLOUT, roll.ordinal)?.commit()

        rollout.startAnimation(getAnimation())
        unitText.startAnimation(getAnimation())
        rollout.text = "%.2f".format(Calculations.calculateGear(selectedWheelSize, selectedRing, selectedCog, roll))

        button_favorite.setOnCheckedChangeListener(null)
        //we have to disable the listener while changing the state to avoid unnecessary calls to de db
        button_favorite.isChecked = favoriteGears.contains(getGear())
        button_favorite.setOnCheckedChangeListener(checkedChangeListener)


    }


    fun getAnimation(): ScaleAnimation {
        val scaleAnimation = ScaleAnimation(0.7f, 1.0f, 0.7f, 1.0f, Animation.RELATIVE_TO_SELF, 0.7f, Animation.RELATIVE_TO_SELF, 0.7f)
        scaleAnimation.duration = 500
        val bounceInterpolator = BounceInterpolator()
        scaleAnimation.interpolator = bounceInterpolator

        return scaleAnimation
    }


}
