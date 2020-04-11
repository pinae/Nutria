package de.ct.nutria

import android.database.sqlite.SQLiteConstraintException
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

class QueryFoodItemRepository(var listener: FoodItemRepositoryListener) {
    var foodArray: ArrayList<FoodItem> = ArrayList()
    var requestOngoing = false
    var manSt = ManufacturerDescriptionStrings()
    var lastQuery = ""

    private fun cleanFoodArray(query: String) {
        var i = 0
        while (i < foodArray.size) {
            if (!foodArray[i].name.toLowerCase().contains(query.toLowerCase().trim())) {
                foodArray.removeAt(i)
                i--
            }
            i++
        }
    }

    private fun insertIntoFoodArray(food: FoodItem) {
        for ((i, it) in foodArray.withIndex()) {
            if (it.foodId == food.foodId) {
                foodArray.removeAt(i)
                break
            }
        }
        foodArray.add(food)
        foodArray.sortedByDescending { it.relevance }
    }

    fun query(query: String) {
        lastQuery = query
        queryServer(query)
        queryRoomDB(query)
    }

    fun queryServer(query: String) {
        requestOngoing = true;
        doAsync {
            val result = JSONObject(URL(
                    "https://nutria.db.pinae.net/json/find?name=$query").readText())
            if (result.isNull("food")) return@doAsync
            val foundFoods: JSONArray = result["food"] as JSONArray
            insertQueryIntoRoomDB(foundFoods)
            uiThread {
                for (i in 0 until foundFoods.length()) {
                    val newFood = FoodItem.fromQueryJSONArray(foundFoods[i] as JSONArray)
                    newFood.manSt = manSt
                    insertIntoFoodArray(newFood)
                }
                cleanFoodArray(lastQuery)
                requestOngoing = false
                listener.onFoodItemRepositoryUpdate()
            }
        }
    }

    fun queryRoomDB(query: String) {
        doAsync {
            val queryFoodItemDao : QueryFoodItemDao = cacheDb.queryFoodItemDao()
            Log.i("Room query", queryFoodItemDao.queryFood(query).toString())
            val loadedFoodList = ArrayList<FoodItem>()
            queryFoodItemDao.queryFood(query.toLowerCase().trim()).forEach {
                loadedFoodList.add(FoodItem.fromQueryFoodItem(it))
            }
            uiThread {
                loadedFoodList.forEach {
                    it.manSt = manSt
                    insertIntoFoodArray(it)
                }
                cleanFoodArray(lastQuery)
                Log.i("foodArray", foodArray.toString())
                listener.onFoodItemRepositoryUpdate()
            }
        }
    }

    private fun insertQueryIntoRoomDB(foundFoods: JSONArray) {
        val queryFoodItemDao : QueryFoodItemDao = cacheDb.queryFoodItemDao()
        for (i in 0 until foundFoods.length()) {
            val jsonFood: JSONArray = foundFoods[i] as JSONArray
            val typeIdCategory = jsonFood[0] as String
            val foodName = (jsonFood[1] as String) + ": " + (jsonFood[2] as String)
            val savedFood = queryFoodItemDao.getFood(foodName)
            var eanLong: Long? = (jsonFood[6] as String).toLong()
            if (eanLong != null && eanLong == (-1).toLong()) eanLong = null
            val newFood = QueryFoodItem(
                    foodId = typeIdCategory.substring(1).split(":")[0].toInt(),
                    isRecipe = typeIdCategory.substring(0, 1).toInt() > 0,
                    categoryId = typeIdCategory.substring(1).split(":")[1].toInt(),
                    name = foodName,
                    calories = (jsonFood[5] as String).toFloat(),
                    source = (jsonFood[3] as String),
                    ean = eanLong,
                    referenceAmount = (jsonFood[4] as String).toFloat(),
                    relevance = savedFood?.relevance ?: 1.0f,
                    lastLogged = savedFood?.lastLogged
            )
            try {
                queryFoodItemDao.insertAll(newFood)
            } catch (e: SQLiteConstraintException) {
                queryFoodItemDao.updateAll(newFood)
            }
        }
    }

    private fun addToRoomDB(vararg foods: QueryFoodItem) {
        doAsync {
            val queryFoodItemDao : QueryFoodItemDao = cacheDb.queryFoodItemDao()
            foods.forEach {
                try {
                    queryFoodItemDao.insertAll(it)
                } catch (e: SQLiteConstraintException) {
                    queryFoodItemDao.updateAll(it)
                }
            }
        }
    }
}