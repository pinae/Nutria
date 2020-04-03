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

    fun replaceOrAppend(food: FoodItem) {
        var updated = false
        foodArray.forEachIndexed {
            i, af -> if (af.type == food.type && af.foodId == food.foodId) {
            foodArray[i] = food
            updated = true
        } }
        if (!updated) foodArray.add(food)
        listener.onFoodUpdate()
    }

    private fun loadDetailedFoodFromServer(type: Int, foodId: Int) {
        requestOngoing = true
        doAsync {
            Log.i("request to", "https://nutria.db.pinae.net/json/food/$type$foodId/100")
            val detailedFoodJson = URL(
                    "https://nutria.db.pinae.net/json/food/$type$foodId").readText()
            Log.i("rceived", detailedFoodJson)
            Log.i("__json", JSONObject(detailedFoodJson).toString())
            val food = FoodItem.fromJSONObject(JSONObject(detailedFoodJson))
            food?.let { if (it.type == 1) it.reference_amount = 100f }
            Log.i("received food", food.toString())
            uiThread {
                requestOngoing = false
                Log.i("Food details from server", food.toString())
                food?.let {
                    replaceOrAppend(it)
                    saveToRoom(it)
                }
            }
        }
    }

    private fun loadDetailedFoodFromRoom(type: Int, foodId: Int) {
        val loggedFoodDao = cacheDb.loggedFoodDao()
        doAsync {
            Log.i("ROOM query", type.toString() + foodId.toString())
            Log.i("ROOM allFoods",loggedFoodDao.getAllFoods().toString())
            val result = loggedFoodDao.getFoods(type, foodId)
            Log.i("ROOM result", result.toString())
            Log.i("  count", loggedFoodDao.getFoodCount(type, foodId).toString())
            val food: FoodItem? = if (loggedFoodDao.getFoodCount(type, foodId) > 0)
                loggedFoodDao.getFood(type, foodId) else null
            Log.i("  food from roomDB", food.toString())
            uiThread {
                Log.i("food from roomDB", food.toString())
                food?.let { replaceOrAppend(it) }
            }
        }
    }

    private fun saveToRoom(food: FoodItem) {
        val loggedFoodDao = cacheDb.loggedFoodDao()
        doAsync {
            loggedFoodDao.insertAll(food)
        }
    }

    fun updateDetailsForAllFoods() {
        foodArray.forEach {
            loadDetailedFoodFromRoom(it.type, it.foodId)
            loadDetailedFoodFromServer(it.type, it.foodId)
        }
    }
}