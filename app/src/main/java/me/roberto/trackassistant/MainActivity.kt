package me.roberto.trackassistant

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_main.*
import me.roberto.OnEditableSeekBarChangeListener

class MainActivity : AppCompatActivity(){


    val PREFS="me.roberto.track.prefs"
    val TAG="MAIN"

    private val  PREFS_SYSTEM="me.roberto.tracks.prefs.system"

    private val METRIC =0
    private val IMPERIAL =1
    var gear:Double=0.0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val prefs=this.getSharedPreferences(PREFS,0)

        radioGroup . setOnCheckedChangeListener { group, checkedId ->
            when (checkedId) {
                metricRadio.id ->{prefs.edit().putInt(PREFS_SYSTEM, METRIC).commit()
                gear=calculateMeters()
                    rollout.text="%.2f".format(gear)
                    unitText.text="m"}

                imperialRadio.id -> {
                    prefs.edit().putInt(PREFS_SYSTEM, IMPERIAL).commit()
                gear=calculateInches()
                    rollout.text="%.2f".format(gear)
                    unitText.text="in"
                }
            }
        }


        when (prefs.getInt(PREFS_SYSTEM, METRIC))
        {
            METRIC->radioGroup.check(R.id.metricRadio)
            IMPERIAL->radioGroup.check(R.id.imperialRadio)
        }


        ring_bar.setOnEditableSeekBarChangeListener(gearListener)
        cog_bar.setOnEditableSeekBarChangeListener(gearListener)

    }


    fun calculateMeters():Double
    {
        val development=ring_bar.value.toFloat()/cog_bar.value
        return development*2.1
    }

    fun calculateInches():Double
    {
        val development=ring_bar.value.toFloat()/cog_bar.value
        return development*26.3

    }


    var gearListener:OnEditableSeekBarChangeListener =object: OnEditableSeekBarChangeListener()
    {

        override fun onEditableSeekBarValueChanged(id: Int, value: Int) {
            super.onEditableSeekBarValueChanged(id, value)


            if (radioGroup.checkedRadioButtonId==metricRadio.id)
            {
                unitText.text="m"
                gear= calculateMeters()

            }
            else
            {

                unitText.text="in"
                gear=calculateInches()
            }
            rollout.text="%.2f".format(gear)


        }

    }

}
