package de.ct.nutria

import android.arch.lifecycle.LiveData
import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.ViewModel
import foodSelector.FoodItem

class LoggedFoodViewModel :ViewModel() {
    private val loggedFood :MutableLiveData<List<FoodItem>> by lazy {
        MutableLiveData<List<FoodItem>>().also {
            loadLoggedFood()
        }
    }

    fun getloggedFood() :LiveData<List<FoodItem>> {
        return loggedFood
    }

    private fun loadLoggedFood() {
        // TODO: Load food from Google Fit
    }
}