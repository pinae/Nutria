package de.ct.nutria

import android.util.Log
import org.jetbrains.anko.doAsync
import org.jetbrains.anko.uiThread
import org.json.JSONObject
import java.net.URL
import java.time.OffsetDateTime
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
                foodArray[i].lastLogged?.let { food.lastLogged = it }
                foodArray[i] = food
                updated = true
            }
        }
        if (!updated) foodArray.add(food)
        listener.onFoodUpdate()
    }

    private fun loadDetailedFoodFromServer(type: Int, foodId: Int, roomId: Int? = null) {
        requestOngoing = true
        doAsync {
            Log.i("request to", "https://nutria.db.pinae.net/json/food/$type$foodId/100")
            val detailedFoodJson = URL(
                    "https://nutria.db.pinae.net/json/food/$type$foodId").readText()
            Log.i("rceived", detailedFoodJson)
            Log.i("__json", JSONObject(detailedFoodJson).toString())
            val food = FoodItem.fromJSONObject(JSONObject(detailedFoodJson))
            food?.let { if (it.type == 1) it.reference_amount = 100f }
            roomId?.let { food?.roomId = it }
            Log.i("received food", food.toString())
            uiThread {
                requestOngoing = false
                Log.i("Food details from server", food.toString())
                food?.let {
                    replaceOrAppend(it)
                }
            }
        }
    }

    private fun loadDetailedFoodFromRoom(type: Int, foodId: Int, roomId: Int? = null) {
        doAsync {
            val loggedFoodDao = cacheDb.loggedFoodDao()
            Log.i("ROOM query", type.toString() + foodId.toString())
            Log.i("ROOM allFoods",loggedFoodDao.getAllFoods().toString())
            Log.i("ROOM getFood", loggedFoodDao.getFood(type, foodId).toString())
            val result = loggedFoodDao.getFoods(type, foodId)
            Log.i("ROOM result", result.toString())
            Log.i("  count", loggedFoodDao.getFoodCount(type, foodId).toString())
            val food: FoodItem? = if (roomId == null) { if (loggedFoodDao.getFoodCount(type, foodId) > 0)
                loggedFoodDao.getFood(type, foodId) else null } else loggedFoodDao.getFood(roomId)
            Log.i("  food from roomDB", food.toString())
            uiThread {
                Log.i("food from roomDB", food.toString())
                food?.let { replaceOrAppend(it) }
            }
        }
    }

    fun loadLoggedFoodForDay(someTime: OffsetDateTime) {
        doAsync {
            val loggedFoodDao = cacheDb.loggedFoodDao()
            val timeConverter = IsoTimeConverter()
            Log.i("loading from room", timeConverter.offsetDateTimeToString(
                    OffsetDateTime.of(someTime.year, someTime.monthValue,
                            someTime.dayOfMonth,
                            0, 0, 0, 0, someTime.offset)) + " – " +
                    timeConverter.offsetDateTimeToString(
                            OffsetDateTime.of(someTime.year, someTime.monthValue,
                                    someTime.dayOfMonth + 1,
                                    0, 0, 0, 0, someTime.offset)))
            Log.i("all logged Food", loggedFoodDao.getAllFoods().toString())
            val foodOfTheDay = loggedFoodDao.loadTimeRange(
                    timeConverter.offsetDateTimeToString(
                            OffsetDateTime.of(someTime.year, someTime.monthValue,
                            someTime.dayOfMonth,
                            0, 0, 0, 0, someTime.offset)),
                    timeConverter.offsetDateTimeToString(
                            OffsetDateTime.of(someTime.year, someTime.monthValue,
                            someTime.dayOfMonth + 1,
                            0, 0, 0, 0, someTime.offset))
            )
            Log.i("foodOfTheDay", foodOfTheDay.toString())
            uiThread {
                it.foodArray.clear()
                foodOfTheDay.forEach {
                    item -> it.foodArray.add(item)
                    Log.i("loadLoggedFoodForDay", item.toString())
                }
                listener.onFoodUpdate()
            }
        }
    }

    fun saveToRoom(food: FoodItem) {
        doAsync {
            val loggedFoodDao = cacheDb.loggedFoodDao()
            Log.i("saveToRoom", food.toString())
            loggedFoodDao.insertAll(food)
            Log.i("saveToRoom - finished", food.toString())
        }
    }

    fun updateInRoom(food: FoodItem) {
        doAsync {
            val loggedFoodDao = cacheDb.loggedFoodDao()
            Log.i("updateInRoom", food.toString())
            loggedFoodDao.updateAll(food)
            Log.i("updateInRoom - finished", food.toString())
        }
    }

    fun updateDetailsForAllFoods() {
        foodArray.forEach {
            loadDetailedFoodFromRoom(it.type, it.foodId, it.roomId)
            loadDetailedFoodFromServer(it.type, it.foodId, it.roomId)
        }
    }
}