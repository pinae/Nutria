package de.ct.nutria

import androidx.room.*

@Dao
interface FoodDao {
    @Query("SELECT * FROM food WHERE name=:name LIMIT 1")
    fun getFood(name: String): RoomFoodItem

    @Query("SELECT * FROM food WHERE name LIKE '%' || :queryString || '%'")
    fun queryFood(queryString: String): List<RoomFoodItem>

    @Query("SELECT * FROM food WHERE categoryId=:categoryId")
    fun getAllFoodsOfCategory(categoryId: Long): List<RoomFoodItem>

    @Insert
    fun insertAll(vararg foods: RoomFoodItem)

    @Delete
    fun delete(food: RoomFoodItem)
}