package me.roberto.fixiecalc.ui

import android.content.SharedPreferences
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import kotlinx.android.synthetic.main.fragment_gear_list.empty_list
import kotlinx.android.synthetic.main.fragment_gear_list.recycler_list
import me.roberto.fixiecalc.R
import me.roberto.gear.domain.Rollout
import me.roberto.gear.domain.calculations.Calculations
import me.roberto.fixiecalc.di.ApplicationClass
import me.roberto.fixiecalc.di.ViewModelFactory
import me.roberto.gear.domain.Gear
import javax.inject.Inject


class FavoritesFragment : Fragment(R.layout.fragment_gear_list) {


    private val viewModel by activityViewModels<GearViewModel>{viewModelFactory}
    @Inject
    lateinit var viewModelFactory: ViewModelFactory
    @Inject
    lateinit var prefs: SharedPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        ApplicationClass.appComponent.inject(this)
        viewModel.gears.observe(this, observer)
    }

    private val observer: Observer<List<Gear>> = Observer { list ->

        if (list.isEmpty()) {
            recycler_list.visibility = View.GONE
            empty_list.visibility = View.VISIBLE
        } else {
            empty_list.visibility = View.GONE
            recycler_list.visibility = View.VISIBLE
            gearAdapter.gears.clear()
            gearAdapter.addAll(list)
        }

    }

    private lateinit var gearAdapter: GearRecyclerViewAdapter

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        gearAdapter = GearRecyclerViewAdapter()
        recycler_list.apply {
            addItemDecoration(DividerItemDecoration(activity, DividerItemDecoration.VERTICAL))
            layoutManager = LinearLayoutManager(activity)
            adapter = gearAdapter
        }
    }
}
