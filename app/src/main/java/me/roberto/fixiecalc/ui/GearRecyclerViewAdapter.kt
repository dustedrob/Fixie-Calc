package me.roberto.fixiecalc.ui

import android.content.Context
import android.content.SharedPreferences
import android.view.ContextMenu
import androidx.recyclerview.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView


import me.roberto.fixiecalc.dummy.DummyContent.DummyItem

import kotlinx.android.synthetic.main.fragment_gear.view.*
import me.roberto.fixiecalc.Calculations.Calculations
import me.roberto.fixiecalc.Measure
import me.roberto.fixiecalc.R
import me.roberto.fixiecalc.ui.BottomActivity.Companion.PREFS_SYSTEM

/**
 * [RecyclerView.Adapter] that can display a [DummyItem] and makes a call to the
 * specified [OnListFragmentInteractionListener].
 * TODO: Replace the implementation with code for your data type.
 */
class GearRecyclerViewAdapter
    : RecyclerView.Adapter<GearRecyclerViewAdapter.ViewHolder>() {

    val gears=ArrayList<Gear>()

    lateinit var context: Context
    lateinit var prefs:SharedPreferences
    lateinit var measure: Measure

    override fun onAttachedToRecyclerView(recyclerView: RecyclerView) {
        super.onAttachedToRecyclerView(recyclerView)
        context=recyclerView.context
        prefs=context.getSharedPreferences(BottomActivity.PREFS,0)

        when (prefs?.getInt(PREFS_SYSTEM, Measure.METERS.ordinal))
        {
            Measure.METERS.ordinal->measure=Measure.METERS
            Measure.INCHES.ordinal->measure=Measure.INCHES
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
                .inflate(R.layout.fragment_gear, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = gears[position]

        holder.chainRing.text = item.chainRing.toString()
        holder.cog.text = item.cog.toString()
        holder.wheelSize.text = item.wheelSize.toString()
        holder.rollout.text = "%.2f".format(Calculations.calculateGear(item.wheelSize, item.chainRing, item.cog,measure  ))
    }



    fun addAll(list:List<Gear>)
    {
        gears.clear()
        gears.addAll(list)
        notifyDataSetChanged()

    }
    override fun getItemCount(): Int = gears.size

    inner class ViewHolder(val view: View) : RecyclerView.ViewHolder(view) {
        val chainRing: TextView = view.gear_chainring
        val cog: TextView = view.gear_cog
        val wheelSize:TextView=view.gear_wheel
        val rollout:TextView=view.gear_rollout

        override fun toString(): String {
            return super.toString() + " '" + cog.text + "'"
        }
    }
}
