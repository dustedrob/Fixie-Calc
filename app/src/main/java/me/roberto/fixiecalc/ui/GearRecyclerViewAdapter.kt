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
import me.roberto.fixiecalc.Rollout
import me.roberto.fixiecalc.calculations.Calculations
import me.roberto.fixiecalc.di.ApplicationClass
import me.roberto.fixiecalc.ui.BottomActivity.Companion.PREFS_ROLLOUT
import javax.inject.Inject

class GearRecyclerViewAdapter
    : RecyclerView.Adapter<GearRecyclerViewAdapter.ViewHolder>() {

    init {
        ApplicationClass.appComponent.inject(this)
    }
    val gears=ArrayList<Gear>()

    @Inject
    lateinit var prefs:SharedPreferences
    lateinit var context: Context
    private lateinit var rollout: Rollout

    override fun onAttachedToRecyclerView(recyclerView: RecyclerView) {
        super.onAttachedToRecyclerView(recyclerView)
        context=recyclerView.context

        when (prefs.getInt(PREFS_ROLLOUT, Rollout.METERS.ordinal))
        {
            Rollout.METERS.ordinal->rollout=Rollout.METERS
            Rollout.INCHES.ordinal->rollout=Rollout.INCHES
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
                .inflate(R.layout.fragment_gear, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = gears[position]
        val unit:String

        when (rollout)
        {
            Rollout.METERS->unit="m"
            Rollout.INCHES->unit="in"
        }



        holder.chainRing.text = item.chainRing.toString()
        holder.cog.text = item.cog.toString()


        for (i in Calculations.wheelSizes.indices)
        {
            if (Calculations.wheelSizes[i]==item.wheelSize)
            holder.wheelSize.text= context.resources.getStringArray(R.array.wheel_values)[i].toString()
        }

        holder.rollout.text = "%.2f".format(Calculations.calculateGear(item.wheelSize, item.chainRing, item.cog,rollout  ))+" "+unit


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
