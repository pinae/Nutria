package de.ct.nutria

import androidx.room.*

@Dao
interface CategoryDao {
    @Query("SELECT * FROM roomfoodcategory")
    fun getAll(): List<RoomFoodCategory>

    @Query("SELECT * from roomfoodcategory WHERE uid=:uid LIMIT 1")
    fun getById(uid: Int): RoomFoodCategory

    @Query("SELECT * from roomfoodcategory WHERE name=:name LIMIT 1")
    fun getByName(name: String): RoomFoodCategory

    @Query("SELECT * FROM roomfoodcategory WHERE name LIKE :nameQuery")
    fun queryName(nameQuery: String): List<RoomFoodCategory>

    @Insert
    fun insertAll(vararg categories: RoomFoodCategory)

    @Delete
    fun delete(category: RoomFoodCategory)
}

@Dao
interface FoodDao {
    @Query("SELECT * FROM roomfooditem WHERE categoryId=:categoryId AND nameAddition=:nameAddition LIMIT 1")
    fun getFood(categoryId: Long, nameAddition: String): RoomFoodItem

    @Query("SELECT * FROM roomfooditem WHERE nameAddition LIKE :queryString")
    fun queryFood(queryString: String): List<RoomFoodItem>

    @Query("SELECT * FROM roomfooditem WHERE categoryId=:categoryId")
    fun getAllFoodsOfCategory(categoryId: Long): List<RoomFoodItem>

    @Transaction
    @Query("SELECT * FROM roomfoodcategory")
    fun getCategoriesWithFoodItems(): List<RoomFoodItemWithCategory>

    @Insert
    fun insertAll(vararg foods: RoomFoodItem)

    @Delete
    fun delete(food: RoomFoodItem)
}