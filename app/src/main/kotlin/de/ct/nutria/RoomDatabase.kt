package de.ct.nutria

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(entities = [RoomFoodCategory::class, RoomFoodItem::class], version = 1)
abstract class CachedFoodDatabase : RoomDatabase() {
    abstract fun categoryDao(): CategoryDao
    abstract fun foodDao(): FoodDao
}