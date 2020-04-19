package de.ct.nutria

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import de.ct.nutria.foodSelector.FoodSearchFragment

import kotlinx.android.synthetic.main.activity_search_food.*

class SearchFoodActivity : AppCompatActivity(), FoodSearchFragment.OnListSelect {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_search_food)
        setSupportActionBar(toolbar)
        supportActionBar?.let{
            it.setDisplayHomeAsUpEnabled(true)
            it.setDisplayShowHomeEnabled(true)
        }
        toolbar.setNavigationOnClickListener { finish() }
    }

    override fun onListSelect(item: FoodItem) {
        item.roomId = null
        val intent = Intent(this, FoodDetailsActivity::class.java)
        intent.putExtra("foodItem", item)
        startActivity(intent)
    }
}
