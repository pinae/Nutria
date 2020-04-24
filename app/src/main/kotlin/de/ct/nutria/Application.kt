package de.ct.nutria

import android.app.Application
import android.content.Context
import android.content.SharedPreferences
import androidx.room.Room

class SharedPreferencesWrapper(context: Context) {
    private val prefsFilename = "net.pinae.nutria.prefs"
    private val calorieBudgetKey = "calorieBudget"
    private val sizeQueryCacheKey = "sizeQueryCache"
    private val prefs: SharedPreferences = context.getSharedPreferences(prefsFilename, 0)

    var calorieBudget: Float
        get() = prefs.getFloat(calorieBudgetKey, 2000f)
        set(value) = prefs.edit().putFloat(calorieBudgetKey, value).apply()

    var sizeQueryCache: Int
        get() = prefs.getInt(sizeQueryCacheKey, 5000)
        set(value) = prefs.edit().putInt(sizeQueryCacheKey, value).apply()
}

val prefs: SharedPreferencesWrapper = NutriaApplication.sharedPreferences!!
val cacheDb: CachedFoodDatabase = NutriaApplication.cachedFoodDatabase!!

class NutriaApplication: Application() {
    companion object {
        var sharedPreferences: SharedPreferencesWrapper? = null
        var cachedFoodDatabase : CachedFoodDatabase? = null
    }

    override fun onCreate() {
        sharedPreferences = SharedPreferencesWrapper(applicationContext)
        cachedFoodDatabase = Room.databaseBuilder(
                applicationContext,
                CachedFoodDatabase::class.java, "cached-food-database"
        ).build()
        super.onCreate()
    }
}