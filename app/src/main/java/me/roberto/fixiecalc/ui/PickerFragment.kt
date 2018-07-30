package me.roberto.fixiecalc.ui

import android.content.Context
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
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import kotlinx.android.synthetic.main.fragment_picker.*
import kotlinx.android.synthetic.main.gear_picker.*
import me.roberto.OnEditableSeekBarChangeListener
import me.roberto.fixiecalc.Measure
import me.roberto.fixiecalc.R
import me.roberto.kitso.database.Injection
import me.roberto.kitso.ui.GearViewModel
import me.roberto.kitso.ui.ViewModelFactory


class PickerFragment : Fragment(), AdapterView.OnItemSelectedListener {



    val TAG="gear_picker"
    val PREFS="me.roberto.track.prefs"
    private val  PREFS_SYSTEM="me.roberto.tracks.prefs.system"
    var cog=11
    var chainRing=44
    var wheelSize=0
    var wheelSizes= intArrayOf(2070,2080,2086,2096,2105,2136,2146,2155,2168)

    val favoriteGears:HashSet<Gear> = HashSet()
    lateinit var gearListener: OnEditableSeekBarChangeListener



    init {



        gearListener=object: OnEditableSeekBarChangeListener()
        {

            override fun onEditableSeekBarValueChanged(id: Int, value: Int) {
                super.onEditableSeekBarValueChanged(id, value)
                updateUI(wheelSizes[wheel_spinner.selectedItemPosition],ring_bar.value,cog_bar.value)
            }

        }


    }

    private var listener: OnFragmentInteractionListener? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }


    private lateinit var viewModelFactory: ViewModelFactory

    private lateinit var viewModel: GearViewModel

    private val observer: Observer<List<Gear>> = Observer { list->

        favoriteGears.clear()
        favoriteGears.addAll(list)

    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModelFactory= Injection.provideViewModelFactory(activity!!)
        viewModel = ViewModelProviders.of(this,viewModelFactory).get(GearViewModel::class.java)
        viewModel.gears.observe(this,observer)
    }


    fun changeFavoriteState(button: CompoundButton, checked:Boolean)
    {
        if (checked)
        {


            if (checked)
            {
                favorite_text.setTypeface(null,Typeface.BOLD)
            }
            else
            {
                favorite_text.setTypeface(null,Typeface.NORMAL)
            }

            button.startAnimation(getAnimation())


        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)



        button_favorite.setOnCheckedChangeListener { compoundButton, checked ->
            changeFavoriteState(compoundButton,checked) }
        val prefs=activity?.getSharedPreferences(PREFS,0)

        val wheelAdapter= ArrayAdapter.createFromResource(activity, R.array.wheel_values,android.R.layout.simple_spinner_dropdown_item)
        wheel_spinner.adapter=wheelAdapter
        wheel_spinner.onItemSelectedListener=this



        when (prefs?.getInt(PREFS_SYSTEM, Measure.METERS.ordinal))
        {
            Measure.METERS.ordinal->radioGroup.check(R.id.metersRadio)
            Measure.INCHES.ordinal->radioGroup.check(R.id.inchesRadio)
        }

        radioGroup.setOnCheckedChangeListener { radioGroup, i ->
            updateUI(wheelSizes[wheel_spinner.selectedItemPosition],ring_bar.value,cog_bar.value)
        }

        ring_bar. setOnEditableSeekBarChangeListener(gearListener)
        cog_bar.setOnEditableSeekBarChangeListener(gearListener)


    }
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment



        return inflater.inflate(R.layout.fragment_picker, container, false)
    }

    // TODO: Rename method, update argument and hook method into UI event
    fun onButtonPressed() {
        listener?.onFragmentInteraction()
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        if (context is OnFragmentInteractionListener) {
            listener = context
        } else {
            throw RuntimeException(context.toString() + " must implement OnFragmentInteractionListener")
        }
    }

    override fun onDetach() {
        super.onDetach()
        listener = null
    }


    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment PickerFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance() =
                PickerFragment().apply {}
    }

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



    override fun onNothingSelected(p0: AdapterView<*>?) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun onItemSelected(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {
        Log.i(TAG, "selected : "+p3)
        updateUI(wheelSizes[wheel_spinner.selectedItemPosition],ring_bar.value,cog_bar.value)

    }


    fun updateUI(selectedWheelSize:Int, selectedRing:Int, selectedCog:Int)
    {

        val prefs=context?.getSharedPreferences(PREFS,0)
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
        prefs?.edit()?.putInt(PREFS_SYSTEM, measure.ordinal)?.commit()

        rollout.startAnimation(getAnimation())
        unitText.startAnimation(getAnimation())
        rollout.text="%.2f".format(calculateGear(selectedWheelSize,selectedRing,selectedCog, measure))

        val gear =Gear(selectedRing,selectedCog,selectedWheelSize)


            changeFavoriteState(button_favorite,favoriteGears.contains(gear))
    }


    fun getAnimation():ScaleAnimation
    {
        val scaleAnimation = ScaleAnimation(0.7f, 1.0f, 0.7f, 1.0f, Animation.RELATIVE_TO_SELF, 0.7f, Animation.RELATIVE_TO_SELF, 0.7f)
        scaleAnimation.duration = 500
        val bounceInterpolator = BounceInterpolator()
        scaleAnimation.interpolator = bounceInterpolator

        return scaleAnimation
    }


}
