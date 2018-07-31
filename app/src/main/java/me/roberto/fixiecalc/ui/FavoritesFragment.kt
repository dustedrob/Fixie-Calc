package me.roberto.fixiecalc.ui

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import me.roberto.fixiecalc.R

import me.roberto.kitso.database.Injection
import me.roberto.kitso.ui.GearViewModel
import me.roberto.kitso.ui.ViewModelFactory

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
    }


    private lateinit var viewModelFactory: ViewModelFactory
    private lateinit var viewModel: GearViewModel
    private val observer: Observer<List<Gear>> = Observer { list->

        gearAdapter.gears.clear()
        gearAdapter.addAll(list)

        }

    private lateinit var gearAdapter:GearRecyclerViewAdapter


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_gear_list, container, false)


        gearAdapter= GearRecyclerViewAdapter()
        // Set the gearAdapter
        if (view is RecyclerView) {
            with(view) {
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
