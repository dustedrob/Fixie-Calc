package me.roberto.fixiecalc.ui


import android.content.SharedPreferences
import android.os.Bundle
import android.view.*
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.github.mikephil.charting.components.Description
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.components.YAxis
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.data.LineData
import com.github.mikephil.charting.data.LineDataSet
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet
import com.innovattic.rangeseekbar.RangeSeekBar
import kotlinx.android.synthetic.main.fragment_speed_cadence.*
import me.roberto.fixiecalc.R
import me.roberto.fixiecalc.Rollout
import me.roberto.fixiecalc.SystemType
import me.roberto.fixiecalc.calculations.Calculations
import me.roberto.fixiecalc.di.ApplicationClass
import me.roberto.fixiecalc.di.ViewModelFactory
import me.roberto.fixiecalc.ui.BottomActivity.Companion.PREFS_SYSTEM
import me.roberto.kitso.ui.GearViewModel
import java.util.*
import javax.inject.Inject


/**
 * A simple [Fragment] subclass.
 * Use the [SpeedCadenceFragment.newInstance] factory method to
 * create an instance of this fragment.
 *
 */
class SpeedCadenceFragment : Fragment(), RangeSeekBar.SeekBarChangeListener {
    override fun onStartedSeeking() {}
    override fun onStoppedSeeking() = setupChart(gearList,minRange,maxRange)

    private var minRange: Int = 0
    private var maxRange: Int = 0

    override fun onValueChanged(minThumbValue: Int, maxThumbValue: Int) {
        minRange = minThumbValue
        maxRange = maxThumbValue
        min_value_text.text = minRange.toString()
        max_value_text.text = maxRange.toString()
    }


    @Inject
    lateinit var viewModelFactory: ViewModelFactory
    @Inject
    lateinit var prefs: SharedPreferences
    private lateinit var viewModel:GearViewModel
    private lateinit var rollout: Rollout
    private lateinit var systemType: SystemType
    private var gearList: List<Gear> = emptyList()
    private val observer: Observer<List<Gear>> = Observer { dataList->

           gearList = dataList
        setupChart(gearList,minRange,maxRange)
    }


    fun setupChart(dataList: List<Gear>, minThumbValue: Int, maxThumbValue: Int)
    {
        if (dataList.isEmpty())
        {
            chart_layout.visibility=View.GONE
            empty_chart.visibility=View.VISIBLE
        }


        else {

            empty_chart.visibility=View.GONE
            chart_layout.visibility=View.VISIBLE

            val lineDataSets = ArrayList<ILineDataSet>()

            dataList.forEach { value ->
                val lineEntries = ArrayList<Entry>()
                for (i in minThumbValue..maxThumbValue) {
                    val cadence = Calculations.calculateCadence(value, i,systemType)
                    lineEntries.add(Entry(i.toFloat(), cadence.toFloat()))
                }
                val dataSet = LineDataSet(lineEntries, value.toString())
                setDataSetVisual(dataSet, value.color)
                lineDataSets.add(dataSet)

            }
            linear_chart.data = LineData(lineDataSets)

            linear_chart.xAxis.position = XAxis.XAxisPosition.BOTTOM
            linear_chart.axisRight.setDrawLabels(true)
            linear_chart.legend.textSize = 20f
            linear_chart.legend.xEntrySpace = 20f
            linear_chart.legend.isWordWrapEnabled = true
            linear_chart.axisLeft.textSize = 12f
            linear_chart.axisRight.textSize = 12f
            linear_chart.xAxis.textSize = 12f
            linear_chart.axisLeft.granularity = .5f
            linear_chart.xAxis.setAvoidFirstLastClipping(true)
            val description = Description()
            description.text = "RPM"
            description.textSize=15f
            linear_chart.description = description

            // create marker to display box when values are selected
            val mv = CustomMarkerView(this@SpeedCadenceFragment.context, R.layout.marker_layout,systemType)
            linear_chart.marker = mv
            linear_chart.invalidate()

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

        prefs?.edit()?.putInt(PREFS_SYSTEM, systemType.ordinal)?.commit()

        setRanges(systemType)
        item.isChecked = true
        setSystemLabel(systemType)
        setupChart(gearList,minRange,maxRange)
        return super.onOptionsItemSelected(item)
    }


    fun setSystemLabel(systemType:SystemType)
    {

        start_speed_text.text=systemType.label
        top_speed_text.text=systemType.label
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setHasOptionsMenu(true)
        rangebar.seekBarChangeListener= this
        setRanges(systemType)

        setSystemLabel(systemType)

    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        ApplicationClass.appComponent.inject(this)
        activity?.let {
            viewModel = ViewModelProviders.of(it,viewModelFactory).get(GearViewModel::class.java)
        }
        viewModel.gears.observe(this,observer)
        systemType= SystemType.values()[prefs!!.getInt(PREFS_SYSTEM,0)]


    }

    private fun setRanges(systemType: SystemType) {

        when (systemType) {

            SystemType.METRIC -> {
                minRange = 0
                maxRange = 60
            }

            SystemType.IMPERIAL -> {
                minRange = 0
                maxRange = 50
            }
        }
        rangebar.minRange = minRange
        rangebar.max = maxRange

        min_value_text.text = minRange.toString()
        max_value_text.text = maxRange.toString()
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




    fun setDataSetVisual(dataSet: LineDataSet, color: Int) {
        dataSet.fillColor = ContextCompat.getColor(activity!!, R.color.colorPrimary)

        dataSet.color = color
        dataSet.mode = LineDataSet.Mode.LINEAR
        dataSet.highLightColor = ContextCompat.getColor(activity!!, R.color.colorPrimaryDark)
        dataSet.axisDependency = YAxis.AxisDependency.LEFT
        dataSet.lineWidth = 2f
        dataSet.setCircleColor(color)
        dataSet.circleRadius=3f
        dataSet.setCircleColorHole(color)
        dataSet.fillColor=color
        dataSet.setDrawCircleHole(true)
        dataSet.setDrawValues(false)

    }



}
