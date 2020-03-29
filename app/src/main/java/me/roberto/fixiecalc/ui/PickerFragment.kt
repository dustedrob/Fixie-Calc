package me.roberto.fixiecalc.ui

import android.content.SharedPreferences
import android.graphics.Typeface
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.Animation
import android.view.animation.BounceInterpolator
import android.view.animation.ScaleAnimation
import android.widget.CompoundButton
import android.widget.TextView
import android.widget.ToggleButton
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import kotlinx.android.synthetic.main.fragment_picker.button_favorite
import kotlinx.android.synthetic.main.fragment_picker.favorite_text
import kotlinx.android.synthetic.main.fragment_picker.inchesText
import kotlinx.android.synthetic.main.fragment_picker.metersText
import kotlinx.android.synthetic.main.fragment_picker.rolloutInches
import kotlinx.android.synthetic.main.fragment_picker.rolloutMeters
import me.roberto.fixiecalc.R
import me.roberto.gear.domain.Rollout
import me.roberto.fixiecalc.di.ApplicationClass
import me.roberto.fixiecalc.di.ViewModelFactory
import me.roberto.gear.domain.Gear
import me.roberto.gear.view.GearPickerView
import javax.inject.Inject


class PickerFragment : Fragment(R.layout.fragment_picker) {

    private val viewModel by activityViewModels<GearViewModel>{viewModelFactory}
    @Inject
    lateinit var viewModelFactory: ViewModelFactory
    @Inject
    lateinit var prefs:SharedPreferences
    lateinit var gearPickerView: GearPickerView
    lateinit var buttonFavorite :ToggleButton
    private val TAG = "gear_picker"
    var cog = 11
    var chainRing = 44
    var wheelSize = 0


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        ApplicationClass.appComponent.inject(this)
        viewModel.favorite.observe(this, isFavorite)
        viewModel.rolloutLiveData.observe(this,rolloutValueObserver)
    }


    private val isFavorite: Observer<Boolean> = Observer {
        buttonFavorite.setOnCheckedChangeListener(null)
        setFavoriteChecked(it)
        setFavoriteFont(it)

        buttonFavorite.setOnCheckedChangeListener(checkedChangeListener)
    }

    private fun setFavoriteChecked(favorite: Boolean) {
        buttonFavorite.isChecked = favorite
    }

    private fun setFavoriteFont(favorite: Boolean) {
        val typeface = if (favorite) Typeface.BOLD else Typeface.NORMAL
        favorite_text.setTypeface(null, typeface)
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        gearPickerView = view.findViewById(R.id.gearPicker)
        buttonFavorite = view.findViewById(R.id.button_favorite)
        button_favorite.setOnCheckedChangeListener(checkedChangeListener)

        viewModel.loadFavoriteGears()
        gearPickerView.onGearSelected = onGearSelected
        gearPickerView.currentGear?.let{onGearSelected.invoke(it)}
    }

    private val onGearSelected : ((Gear) -> Unit) = {
        viewModel.calculateGear(it)
        viewModel.isFavorite(it)
    }



    private val checkedChangeListener = CompoundButton.OnCheckedChangeListener { compoundButton, checked ->
        setFavoriteFont(checked)
        viewModel.changeFavoriteStatus(gearPickerView.currentGear,checked)
    }

    val rolloutValueObserver: Observer<GearViewModel.RolloutValue> = Observer{

        when (it.rollout){
            Rollout.INCHES -> setText(rolloutInches,inchesText,it.rollValue)
            Rollout.METERS -> setText(rolloutMeters,metersText,it.rollValue)
        }
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
