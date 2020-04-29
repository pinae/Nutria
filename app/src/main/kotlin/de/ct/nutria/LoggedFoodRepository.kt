package de.ct.nutria

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
            val detailedFoodJson = URL(
                    "https://nutria.db.pinae.net/json/food/$type$foodId").readText()
            val food = FoodItem.fromJSONObject(JSONObject(detailedFoodJson))
            food?.let { if (it.type == 1) it.referenceAmount = 100f }
            roomId?.let { food?.roomId = it }
            uiThread {
                requestOngoing = false
                food?.let {
                    replaceOrAppend(it)
                }
            }
        }
    }

    private fun loadDetailedFoodFromRoom(type: Int, foodId: Int, roomId: Int? = null) {
        doAsync {
            val loggedFoodDao = cacheDb.loggedFoodDao()
            val result = loggedFoodDao.getFoods(type, foodId)
            val food: FoodItem? = if (roomId == null) { if (loggedFoodDao.getFoodCount(type, foodId) > 0)
                loggedFoodDao.getFood(type, foodId) else null } else loggedFoodDao.getFood(roomId)
            uiThread {
                food?.let { replaceOrAppend(it) }
            }
        }
    }

    fun loadLoggedFoodForDay(someTime: OffsetDateTime) {
        doAsync {
            val loggedFoodDao = cacheDb.loggedFoodDao()
            val timeConverter = IsoTimeConverter()
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
            uiThread {
                it.foodArray.clear()
                foodOfTheDay.forEach {
                    item -> it.foodArray.add(item)
                }
                listener.onFoodUpdate()
            }
        }
    }

    fun saveToRoom(food: FoodItem) {
        doAsync {
            val loggedFoodDao = cacheDb.loggedFoodDao()
            loggedFoodDao.insertAll(food)
        }
    }

    fun updateInRoom(food: FoodItem) {
        doAsync {
            val loggedFoodDao = cacheDb.loggedFoodDao()
            loggedFoodDao.updateAll(food)
        }
    }

    fun updateDetailsForAllFoods() {
        foodArray.forEach {
            loadDetailedFoodFromRoom(it.type, it.foodId, it.roomId)
            loadDetailedFoodFromServer(it.type, it.foodId, it.roomId)
        }
    }
}