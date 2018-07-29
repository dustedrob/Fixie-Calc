package me.roberto.fixiecalc.ui

import android.os.Bundle
import com.google.android.material.bottomnavigation.BottomNavigationView
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import kotlinx.android.synthetic.main.activity_bottom.*
import me.roberto.fixiecalc.R

class BottomActivity : AppCompatActivity(), OnFragmentInteractionListener {
    override fun onFragmentInteraction() {
    }

    private val mOnNavigationItemSelectedListener = BottomNavigationView.OnNavigationItemSelectedListener { item ->



        when (item.itemId) {
            R.id.navigation_calculator -> {

                supportActionBar?.title=getString(R.string.calculator)
                switchFragment(PickerFragment.newInstance(),"picker")
                return@OnNavigationItemSelectedListener true
            }
            R.id.navigation_favorites -> {
                supportActionBar?.title=getString(R.string.favorites)
                switchFragment(FavoritesFragment.newInstance(),"favorites")
                return@OnNavigationItemSelectedListener true
            }

        }
        false
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_bottom)

        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener)

        supportActionBar?.title=getString(R.string.calculator)
        switchFragment(PickerFragment.newInstance(),"picker")
    }


    fun switchFragment(fragment: Fragment, tag: String): Boolean {


        supportFragmentManager
                .beginTransaction()
                .replace(R.id.fragment_container, fragment,tag)
                .commit()

        return true
    }

}