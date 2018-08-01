package me.roberto.fixiecalc.ui

import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import me.roberto.fixiecalc.calculations.Calculations
import me.roberto.fixiecalc.Measure
import me.roberto.fixiecalc.R

import me.roberto.kitso.database.Injection
import me.roberto.kitso.ui.GearViewModel
import me.roberto.kitso.ui.ViewModelFactory
import androidx.recyclerview.widget.DividerItemDecoration



/**
 * A fragment representing a list of Items.
 * Activities containing this fragment MUST implement the
 * [FavoritesFragment.OnFragmentInteractionListener] interface.
 */
class FavoritesFragment : Fragment() {

    // TODO: Customize parameters


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)


        viewModelFactory= Injection.provideViewModelFactory(activity!!)
        viewModel = ViewModelProviders.of(activity!!,viewModelFactory).get(GearViewModel::class.java)
        viewModel.gears.observe(this,observer)
        measure=Measure.values().get(activity!!.getSharedPreferences(BottomActivity.PREFS,0).getInt(BottomActivity.PREFS_SYSTEM,0))
    }


    private lateinit var measure: Measure
    private lateinit var viewModelFactory: ViewModelFactory
    private lateinit var viewModel: GearViewModel
    private val observer: Observer<List<Gear>> = Observer { list->


        val sortedlist=list.sortedWith(Comparator<Gear> { p0, p1 ->
            val gear0=Calculations.calculateGear(p0!!.wheelSize,p0.chainRing,p0.cog,measure)

            val gear1= Calculations.calculateGear(p1!!.wheelSize,p1.chainRing,p1.cog,measure)


            if (gear0<gear1) {
                -1
            } else if (gear0==gear1) {
                0
            } else {
                1
            }
        })
        gearAdapter.gears.clear()


        gearAdapter.addAll(sortedlist)

        }

    private lateinit var gearAdapter:GearRecyclerViewAdapter


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_gear_list, container, false)


        gearAdapter= GearRecyclerViewAdapter()
        // Set the gearAdapter
        if (view is RecyclerView) {
            with(view) {

                addItemDecoration(DividerItemDecoration(context, DividerItemDecoration.VERTICAL))
                layoutManager = LinearLayoutManager(context)

                adapter = gearAdapter
            }
        }
        return view
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
    }


    companion object {


        @JvmStatic
        fun newInstance() =
                FavoritesFragment().apply {}
    }
}
