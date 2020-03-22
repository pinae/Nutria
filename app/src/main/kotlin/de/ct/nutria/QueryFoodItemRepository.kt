package de.ct.nutria

import org.jetbrains.anko.doAsync
import org.jetbrains.anko.uiThread
import org.json.JSONArray
import org.json.JSONObject
import java.net.URL
import java.util.ArrayList

interface FoodItemRepositoryListener {
    fun onFoodItemRepositoryUpdate()
}

class QueryFoodItemRepository(var listener: FoodItemRepositoryListener) {
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
        requestOngoing = true;
        doAsync {
            val result = JSONObject(URL(
                    "https://nutria.db.pinae.net/json/find?name=$query").readText())
            if (result.isNull("food")) return@doAsync
            val foundFoods: JSONArray = result["food"] as JSONArray
            uiThread {
                for (i in 0 until foundFoods.length()) {
                    val newFood = FoodItem.fromQueryJSONArray(foundFoods[i] as JSONArray)
                    newFood.manSt = manSt
                    foodArray.add(i, newFood)
                    addToRoomDB(newFood.toQueryFoodItem())
                }
                requestOngoing = false
                listener.onFoodItemRepositoryUpdate()
            }
        }
    }

    fun queryRoomDB(query: String) {
        val queryFoodItemDao : QueryFoodItemDao = cacheDb.queryFoodItemDao()
        doAsync {
            for ((foundItemCounter, roomFoodItem) in queryFoodItemDao.queryFood(query).withIndex()) {
                val item = FoodItem.fromQueryFoodItem(roomFoodItem)
                item.manSt = manSt
                foodArray.add(foundItemCounter, item)
            }
            uiThread {
                listener.onFoodItemRepositoryUpdate()
            }
        }
    }

    fun addToRoomDB(vararg foods: QueryFoodItem) {
        val queryFoodItemDao : QueryFoodItemDao = cacheDb.queryFoodItemDao()
        doAsync {
            queryFoodItemDao.insertAll(*foods)
        }
    }
}