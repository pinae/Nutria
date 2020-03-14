package de.ct.nutria

import android.util.Log
import org.jetbrains.anko.doAsync
import org.jetbrains.anko.uiThread
import org.json.JSONArray
import org.json.JSONObject
import java.net.URL
import java.util.ArrayList

interface FoodItemRepositoryListener {
    fun onFoodItemRepositoryUpdate()
}

class FoodItemRepository(var listener: FoodItemRepositoryListener) {
    var foodArray: ArrayList<FoodItem> = ArrayList()
    var requestOngoing = false
    var manSt = ManufacturerDescriptionStrings()

    private fun cleanFoodArray(query: String) {
        var i = 0
        while (i < foodArray.size) {
            if (!foodArray[i].name.contains(query)) {
                foodArray.removeAt(i)
                i--
            }
            i++
        }
    }

    fun query(query: String) {
        queryServer(query)
        cleanFoodArray(query)
        queryRoomDB(query)
    }

    fun queryServer(query: String) {
        Log.i(this.toString(), "Querying Nutria DB: $query")
        requestOngoing = true;
        doAsync {
            val result = JSONObject(URL(
                    "https://nutria.db.pinae.net/json/find?name=$query").readText())
            val foundFoods: JSONArray = result["food"] as JSONArray
            for (i in 0 until foundFoods.length()) {
                val newFood = FoodItem.fromJSONArray(foundFoods[i] as JSONArray)
                newFood.manSt = manSt
                foodArray.add(i, newFood)
                addToRoomDB(newFood.toRoomFoodItem())
                Log.i("  food", foundFoods[i].toString())
            }
            uiThread {
                requestOngoing = false;
                Log.i("Request", result.toString())

                listener.onFoodItemRepositoryUpdate()
            }
        }
    }

    fun queryRoomDB(query: String) {
        Log.d("querying RoomDB", query)
        val foodDao : FoodDao = cacheDb.foodDao()
        val categoryDao: CategoryDao = cacheDb.categoryDao()
        doAsync {
            for ((foundItemCounter, roomFoodItem) in foodDao.queryFood(query).withIndex()) {
                val roomFoodCategory = categoryDao.getById(roomFoodItem.categoryId)
                val item = FoodItem.fromRoom(roomFoodItem, roomFoodCategory)
                item.manSt = manSt
                foodArray.add(foundItemCounter, item)
            }
            uiThread {
                Log.d("foodArray", foodArray.toString())
                listener.onFoodItemRepositoryUpdate()
            }
        }
    }

    fun addToRoomDB(vararg foods: RoomFoodItem) {
        val foodDao : FoodDao = cacheDb.foodDao()
        doAsync {
            foodDao.insertAll(*foods)
        }
    }
}