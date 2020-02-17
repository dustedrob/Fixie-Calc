package me.roberto.fixiecalc.ui


import android.content.Context
import android.content.SharedPreferences
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.fragment_gear.view.*
import me.roberto.fixiecalc.R
import me.roberto.gear.domain.Rollout
import me.roberto.gear.domain.calculations.Calculations
import me.roberto.fixiecalc.di.ApplicationClass
import javax.inject.Inject

class GearRecyclerViewAdapter
    : RecyclerView.Adapter<GearRecyclerViewAdapter.ViewHolder>() {

    init {
        ApplicationClass.appComponent.inject(this)
    }
    val gears=ArrayList<me.roberto.gear.domain.Gear>()

    @Inject
    lateinit var prefs:SharedPreferences
    lateinit var context: Context

    override fun onAttachedToRecyclerView(recyclerView: RecyclerView) {
        super.onAttachedToRecyclerView(recyclerView)
        context=recyclerView.context
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
                .inflate(R.layout.fragment_gear, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = gears[position]
        holder.gearValue.text = item.toString()


        for (i in Calculations.wheelSizes.indices)
        {
            if (Calculations.wheelSizes[i]==item.wheelSize)
            holder.wheelSize.text= context.resources.getStringArray(R.array.wheel_values)[i].toString()
        }

        holder.rolloutMeters.text = "%.2f".format(Calculations.calculateGear(item.wheelSize, item.chainRing, item.cog, Rollout.METERS  ))+" m"
        holder.rolloutInches.text = "%.2f".format(Calculations.calculateGear(item.wheelSize, item.chainRing, item.cog, Rollout.INCHES  ))+" in"
    }



    fun addAll(list:List<me.roberto.gear.domain.Gear>)
    {
        gears.clear()
        gears.addAll(list)
        notifyDataSetChanged()
    }
    override fun getItemCount(): Int = gears.size

    inner class ViewHolder(val view: View) : RecyclerView.ViewHolder(view) {
        val gearValue: TextView = view.gear_value
        val wheelSize:TextView=view.gear_wheel
        val rolloutMeters:TextView=view.gear_rollout_meters
        val rolloutInches:TextView=view.gear_rollout_inches

        override fun toString(): String {
            return super.toString() + " '" + gearValue.text + "'"
        }
    }
}
