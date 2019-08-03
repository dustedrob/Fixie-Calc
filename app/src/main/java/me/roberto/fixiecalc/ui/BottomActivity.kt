package me.roberto.fixiecalc.ui

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.findNavController
import androidx.navigation.ui.setupWithNavController
import kotlinx.android.synthetic.main.activity_bottom.*
import me.roberto.fixiecalc.R

class BottomActivity : AppCompatActivity(), OnFragmentInteractionListener {
    override fun onFragmentInteraction() {
    }



    companion object {
        val  PREFS_ROLLOUT="me.roberto.tracks.prefs.rollout"
        val  PREFS_SYSTEM="me.roberto.tracks.prefs.system"

    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_bottom)
        val navController = findNavController(R.id.nav_host_fragment)
        navigation.setupWithNavController(navController)
    }

    override fun onSupportNavigateUp(): Boolean {
        return findNavController(R.id.nav_host_fragment).navigateUp()
    }


}
