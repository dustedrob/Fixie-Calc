package me.roberto.fixiecalc.ui

import android.os.Bundle
import com.google.android.material.bottomnavigation.BottomNavigationView
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import kotlinx.android.synthetic.main.activity_bottom.*
import me.roberto.fixiecalc.R
import me.roberto.kitso.database.Injection
import me.roberto.kitso.ui.GearViewModel
import me.roberto.kitso.ui.ViewModelFactory

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

    private lateinit var viewModelFactory: ViewModelFactory

    private lateinit var viewModel: GearViewModel

    private lateinit var bookObserver: Observer<List<Gear>>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_bottom)

        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener)

        supportActionBar?.title=getString(R.string.calculator)
        switchFragment(PickerFragment.newInstance(),"picker")

        viewModelFactory= Injection.provideViewModelFactory(this)
        viewModel = ViewModelProviders.of(this,viewModelFactory).get(GearViewModel::class.java)
        viewModel.loadFavoriteGears()

    }


    fun switchFragment(fragment: Fragment, tag: String): Boolean {


        supportFragmentManager
                .beginTransaction()
                .replace(R.id.fragment_container, fragment,tag)
                .commit()

        return true
    }

}
