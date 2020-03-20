package de.ct.nutria

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(entities = [QueryFoodItem::class, FoodItem::class], version = 1)
abstract class CachedFoodDatabase : RoomDatabase() {
    abstract fun queryFoodItemDao(): QueryFoodItemDao
    abstract fun loggedFoodDao(): LoggedFoodDao
}