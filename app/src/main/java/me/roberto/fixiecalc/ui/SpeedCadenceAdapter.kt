package me.roberto.fixiecalc.ui

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import me.roberto.fixiecalc.R


private const val HEADER_TYPE = 0
private const val CADENCE_TYPE = 0

class SpeedCadenceAdapter(private val cadenceList : MutableList<SpeedCadenceRowItem> = mutableListOf()): RecyclerView.Adapter<RecyclerView.ViewHolder>() {


    fun addItem(item: SpeedCadenceRowItem) {
        cadenceList.add(item)
        notifyItemInserted(cadenceList.size-1)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {

        val inflater = LayoutInflater.from(parent.context)

        return when (viewType)
        {
            HEADER_TYPE -> HeaderViewHolder(inflater.inflate(R.layout.cadence_gear, parent, false))
            else -> CadenceViewHolder(inflater.inflate(R.layout.fragment_gear, parent, false))
        }
    }

    override fun getItemCount() = cadenceList.size

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {

        val item = cadenceList[position]

        when (holder)
        {
            is HeaderViewHolder -> {
                holder.headerTextView.text = item.headerText
            }
            is CadenceViewHolder ->{
                holder.cadenceText.text = item.cadence.toString()
                holder.gearText.text = item.gear.toString()

            }
        }
    }


}

class HeaderViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

    val headerTextView: TextView = itemView.findViewById(R.id.speed_header)

}


class CadenceViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

    val cadenceText : TextView = itemView.findViewById(R.id.cadence_rpm)
    val gearText: TextView = itemView.findViewById(R.id.gear_value)

}