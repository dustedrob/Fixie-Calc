package me.roberto.fixiecalc.ui

import android.content.SharedPreferences
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import kotlinx.android.synthetic.main.fragment_gear_list.*
import me.roberto.fixiecalc.R
import me.roberto.fixiecalc.Rollout
import me.roberto.fixiecalc.calculations.Calculations
import me.roberto.fixiecalc.di.ApplicationClass
import me.roberto.fixiecalc.di.ViewModelFactory
import javax.inject.Inject


class FavoritesFragment : Fragment() {

    @Inject
    lateinit var viewModelFactory:ViewModelFactory
    @Inject
    lateinit var prefs: SharedPreferences
    private lateinit var viewModel: GearViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        ApplicationClass.appComponent.inject(this)
        activity?.let {
            viewModel = ViewModelProviders.of(it,viewModelFactory).get(GearViewModel::class.java)
        }
        viewModel.gears.observe(this, observer)
    }


    private val observer: Observer<List<Gear>> = Observer { list ->


        if (list.isEmpty()) {
            recycler_list.visibility = View.GONE
            empty_list.visibility = View.VISIBLE
        } else {
            empty_list.visibility = View.GONE
            recycler_list.visibility = View.VISIBLE

            val sortedlist = list.sortedWith(Comparator { p0, p1 ->
                val gear0 = Calculations.calculateGear(p0!!.wheelSize, p0.chainRing, p0.cog, Rollout.METERS)
                val gear1 = Calculations.calculateGear(p1!!.wheelSize, p1.chainRing, p1.cog, Rollout.METERS)
                when {
                    gear0 < gear1 -> -1
                    gear0 == gear1 -> 0
                    else -> 1
                }
            })
            gearAdapter.gears.clear()
            gearAdapter.addAll(sortedlist)
        }

    }

    private lateinit var gearAdapter: GearRecyclerViewAdapter


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_gear_list, container, false)
        gearAdapter = GearRecyclerViewAdapter()
        return view
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        recycler_list.apply {
            addItemDecoration(DividerItemDecoration(activity, DividerItemDecoration.VERTICAL))
            layoutManager = LinearLayoutManager(activity)
            adapter = gearAdapter
        }
    }
}
