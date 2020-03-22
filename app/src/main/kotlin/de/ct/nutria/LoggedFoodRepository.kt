package de.ct.nutria

import android.util.Log
import org.jetbrains.anko.doAsync
import org.jetbrains.anko.uiThread
import org.json.JSONObject
import java.net.URL
import java.util.ArrayList

interface LoggedFoodRepositoryListener {
    fun onFoodUpdate()
}

class LoggedFoodRepository(var listener: LoggedFoodRepositoryListener) {
    var foodArray: ArrayList<FoodItem> = ArrayList()
    var requestOngoing = false

    fun clear() {
        foodArray.clear()
    }

    fun updateFood(food: FoodItem) {
        var updated = false
        foodArray.forEachIndexed { i, _ -> foodArray[i] = food; updated = true }
        if (!updated) foodArray.add(food)
        listener.onFoodUpdate()
    }

    private fun loadDetailedFoodFromServer(type: Int, id: Int) {
        requestOngoing = true
        doAsync {
            Log.i("request to", "https://nutria.db.pinae.net/json/food/$type$id/100")
            val detailedFoodJson = URL(
                    "https://nutria.db.pinae.net/json/food/$type$id").readText()
            Log.i("rceived", detailedFoodJson)
            Log.i("__json", JSONObject(detailedFoodJson).toString())
            val food = FoodItem.fromJSONObject(JSONObject(detailedFoodJson))
            food?.let { it.reference_amount = 100f }
            Log.i("received food", food.toString())
            uiThread {
                requestOngoing = false
                Log.i("Food details from server", food.toString())
                food?.let { updateFood(it) }
            }
        }
    }

    private fun loadDetailedFoodFromRoom(type: Int, id: Int) {

    }

    fun updateDetailsForAllFoods() {
        foodArray.forEach {
            loadDetailedFoodFromRoom(it.type, it.id)
            loadDetailedFoodFromServer(it.type, it.id)
        }
    }
}