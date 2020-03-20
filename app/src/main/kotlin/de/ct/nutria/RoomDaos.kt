package de.ct.nutria

import androidx.room.*
import java.time.OffsetDateTime
import java.time.format.DateTimeFormatter

@Dao
interface QueryFoodItemDao {
    @Query("SELECT * FROM food WHERE name=:name LIMIT 1")
    fun getFood(name: String): QueryFoodItem

    @Query("SELECT * FROM food WHERE name LIKE '%' || :queryString || '%'")
    fun queryFood(queryString: String): List<QueryFoodItem>

    @Query("SELECT * FROM food WHERE categoryId=:categoryId")
    fun getAllFoodsOfCategory(categoryId: Long): List<QueryFoodItem>

    @Insert
    fun insertAll(vararg foods: QueryFoodItem)

    @Update
    fun updateAll(vararg foods: QueryFoodItem)

    @Delete
    fun delete(food: QueryFoodItem)
}

@Dao
interface LoggedFoodDao {
    @Query("SELECT * FROM loggedFood WHERE foodId=:id LIMIT 1")
    fun getFood(id: Int): FoodItem

    @Insert
    fun insertAll(vararg foods: FoodItem)

    @Update
    fun updateAll(vararg foods: FoodItem)

    @Delete
    fun delete(food: FoodItem)
}

class IsoTimeConverter {
    @TypeConverter
    fun stringToOffsetDateTime(value: String?): OffsetDateTime? {
        if (value == null) return null
        return OffsetDateTime.parse(value, DateTimeFormatter.ISO_OFFSET_DATE_TIME)
    }

    @TypeConverter
    fun offsetDateTimeToString(value: OffsetDateTime?): String? {
        if (value == null) return null
        return value.format(DateTimeFormatter.ISO_OFFSET_DATE_TIME)
    }
}