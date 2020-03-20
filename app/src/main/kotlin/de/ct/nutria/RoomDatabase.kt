package de.ct.nutria

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(entities = [RoomFoodItem::class], version = 1)
abstract class CachedFoodDatabase : RoomDatabase() {
    abstract fun foodDao(): FoodDao
}