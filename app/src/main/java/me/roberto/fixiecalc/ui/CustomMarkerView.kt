package me.roberto.fixiecalc.ui

import android.content.Context
import com.github.mikephil.charting.components.MarkerView
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.highlight.Highlight
import com.github.mikephil.charting.utils.MPPointF
import kotlinx.android.synthetic.main.marker_layout.view.*
import me.roberto.fixiecalc.SystemType


class CustomMarkerView(context: Context?, layoutResource: Int,val speedSystem: SystemType) : MarkerView(context, layoutResource) {


    // runs every time the MarkerView is redrawn, can be used to update the
    // content (user-interface)
    override fun refreshContent(e: Entry, highlight: Highlight) {

        cadence.text = e.y.toInt().toString()+" rpm"
        speed.text = e.x.toString() + " "+ speedSystem.label
        super.refreshContent(e, highlight)
    }

    override fun getOffset(): MPPointF {
        return MPPointF((-(width / 2)).toFloat(), (-height).toFloat())
    }
}