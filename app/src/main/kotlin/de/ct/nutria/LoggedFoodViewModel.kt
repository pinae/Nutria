package de.ct.nutria

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class LoggedFoodViewModel : ViewModel() {
    private val loggedFood : MutableLiveData<List<FoodItem>> by lazy {
        MutableLiveData<List<FoodItem>>().also {
            loadLoggedFood()
        }
    }

    fun getloggedFood() : LiveData<List<FoodItem>> {
        return loggedFood
    }

    private fun loadLoggedFood() {
        // TODO: Load food from Google Fit
    }
}