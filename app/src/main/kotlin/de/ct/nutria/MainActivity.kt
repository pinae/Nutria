package de.ct.nutria

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.bottomnavigation.BottomNavigationView

import de.ct.nutria.LoggedFooditemFragment
import de.ct.nutria.dummy.DummyContent
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity(), LoggedFooditemFragment.OnListFragmentInteractionListener {

    private var mTextMessage: TextView? = null
    private val loggedFooditemFragment: LoggedFooditemFragment? = null

    private val navigationItemSelectedListener =
            BottomNavigationView.OnNavigationItemSelectedListener { item ->
        when (item.itemId) {
            R.id.navigation_loggedFood -> {
                message.setText(R.string.navigation_label_food)
                addButtonAddFood()
                return@OnNavigationItemSelectedListener true
            }
            R.id.navigation_recipes -> {
                message.setText(R.string.navigation_label_recipes)
                addButton.visibility = View.GONE
                return@OnNavigationItemSelectedListener true
            }
            R.id.navigation_preferences -> {
                message.setText(R.string.navigation_label_preferences)
                addButton.visibility = View.GONE
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
        addButtonAddFood()
        navigation.setOnNavigationItemSelectedListener(navigationItemSelectedListener)
    }

    override fun onListFragmentInteraction(item: DummyContent.DummyItem?) {
        TODO("Not yet implemented")
    }
}
