package de.ct.nutria

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.bottomnavigation.BottomNavigationView

import de.ct.nutria.LoggedFooditemFragment
import de.ct.nutria.dummy.DummyContent
import kotlinx.android.synthetic.main.activity_main.*
import kotlin.concurrent.fixedRateTimer

class MainActivity : AppCompatActivity(), LoggedFooditemFragment.OnListFragmentInteractionListener {
    private val navigationItemSelectedListener =
            BottomNavigationView.OnNavigationItemSelectedListener { item ->
        when (item.itemId) {
            R.id.navigation_loggedFood -> {
                supportFragmentManager.beginTransaction().replace(
                        R.id.mainActivityFragmentContainer,
                        LoggedFooditemFragment.newInstance(1)
                ).addToBackStack(null).commit()
                addButtonAddFood()
                return@OnNavigationItemSelectedListener true
            }
            R.id.navigation_recipes -> {
                addButton.visibility = View.GONE
                return@OnNavigationItemSelectedListener true
            }
            R.id.navigation_preferences -> {
                addButton.visibility = View.GONE
                supportFragmentManager.beginTransaction().replace(
                        R.id.mainActivityFragmentContainer,
                        PreferencesFragment.newInstance()
                ).addToBackStack(null).commit()
                return@OnNavigationItemSelectedListener true
            }
        }
        false
    }

    private fun addButtonAddFood() {
        addButton.visibility = View.VISIBLE
        addButton.setOnClickListener {
            val intent = Intent(this, SearchFoodActivity::class.java)
            startActivity(intent)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        if (savedInstanceState == null) {
            supportFragmentManager.beginTransaction().add(
                    R.id.mainActivityFragmentContainer,
                    LoggedFooditemFragment.newInstance(1)).commit()
        }
        addButtonAddFood()
        navigation.setOnNavigationItemSelectedListener(navigationItemSelectedListener)
    }

    override fun onListFragmentInteraction(item: DummyContent.DummyItem?) {
        TODO("Not yet implemented")
    }
}
