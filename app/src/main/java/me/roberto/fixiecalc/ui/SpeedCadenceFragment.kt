package me.roberto.fixiecalc.ui


import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import android.view.*
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import kotlinx.android.synthetic.main.fragment_speed_cadence.*
import me.roberto.fixiecalc.R
import me.roberto.gear.domain.SystemType
import me.roberto.gear.domain.calculations.Calculations
import me.roberto.fixiecalc.di.ApplicationClass
import me.roberto.fixiecalc.di.ViewModelFactory
import me.roberto.fixiecalc.ui.BottomActivity.Companion.PREFS_SYSTEM
import me.roberto.gear.domain.Gear
import javax.inject.Inject


class SpeedCadenceFragment : Fragment(){


    private var minRange: Int = 20
    private var maxRange: Int = 80


    @Inject
    lateinit var viewModelFactory: ViewModelFactory
    @Inject
    lateinit var prefs: SharedPreferences
    private lateinit var viewModel: GearViewModel
    private lateinit var systemType: SystemType
    private var gearList: List<Gear> = emptyList()
    private val observer: Observer<List<Gear>> = Observer { dataList->

           gearList = dataList
        setupChart(gearList,minRange,maxRange)
    }


    private fun setupChart(dataList: List<Gear>, minThumbValue: Int, maxThumbValue: Int)
    {
        if (dataList.isEmpty())
        {
            chart_layout.visibility=View.GONE
            empty_chart.visibility=View.VISIBLE
        }


        else {

            empty_chart.visibility=View.GONE
            chart_layout.visibility=View.VISIBLE


            dataList.forEach { value ->
                for (i in minThumbValue..maxThumbValue step 5) {
                    val cadence = Calculations.calculateCadence(value, i,systemType)
                    Log.i("Cadence", "$cadence")
                }

            }

        }
    }


    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)

        inflater.inflate(R.menu.units,menu)
        when(systemType)
        {
            SystemType.METRIC -> menu.findItem(R.id.system_metric).isChecked = true
            SystemType.IMPERIAL -> menu.findItem(R.id.system_imperial).isChecked = true
        }

    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {

        systemType =
        when (item.itemId)
        {
            R.id.system_metric -> SystemType.METRIC
            else -> SystemType.IMPERIAL
        }

        prefs.edit()?.putInt(PREFS_SYSTEM, systemType.ordinal)?.commit()

        item.isChecked = true
        setupChart(gearList,minRange,maxRange)
        return super.onOptionsItemSelected(item)
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.gears.observe(viewLifecycleOwner,observer)
        setHasOptionsMenu(true)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        ApplicationClass.appComponent.inject(this)
        activity?.let {
            viewModel = ViewModelProviders.of(it,viewModelFactory).get(GearViewModel::class.java)
        }
        systemType= SystemType.values()[prefs.getInt(PREFS_SYSTEM,0)]


    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_speed_cadence, container, false)
    }


    companion object {
        @JvmStatic
        fun newInstance() = SpeedCadenceFragment()
    }

}
