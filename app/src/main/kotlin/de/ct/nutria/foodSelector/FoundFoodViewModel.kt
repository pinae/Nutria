package de.ct.nutria.foodSelector

import android.arch.lifecycle.LiveData
import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.ViewModel
import de.ct.nutria.FoodItem

class FoundFoodViewModel : ViewModel() {
    private val foundFood :MutableLiveData<List<FoodItem>> by lazy {
        MutableLiveData<List<FoodItem>>().also {
            searchFood("")
        }
    }

    fun getFood() :LiveData<List<FoodItem>> {
        return foundFood
    }

    private fun searchFood(query: CharSequence) {
        // TODO: Search for Food in Room DB and make a query to the cloud. Maybe do this in an extra class.
    }
}