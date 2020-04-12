package de.ct.nutria

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.bottomnavigation.BottomNavigationView
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity(), LoggedFooditemFragment.OnLoggedFoodListInteractionListener {
    private val navigationItemSelectedListener =
            BottomNavigationView.OnNavigationItemSelectedListener { item ->
        when (item.itemId) {
            R.id.navigation_loggedFood -> {
                supportFragmentManager.beginTransaction().replace(
                        R.id.mainActivityFragmentContainer,
                        LoggedFooditemFragment.newInstance()
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
                    LoggedFooditemFragment.newInstance()).commit()
        }
        addButtonAddFood()
        if (intent.hasExtra("foodItem"))
            intent.getParcelableExtra<FoodItem>("foodItem")?.let { food ->
                supportFragmentManager.fragments.forEach {
                    if (it is LoggedFooditemFragment) it.populateFoodArrayInRepository(food)
                }
            }
        navigation.setOnNavigationItemSelectedListener(navigationItemSelectedListener)
    }

    override fun onFoodDetilsRequested(food: FoodItem) {
        val intent = Intent(this, FoodDetailsActivity::class.java)
        intent.putExtra("foodItem", food)
        startActivity(intent)
    }
}
