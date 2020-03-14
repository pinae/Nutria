package de.ct.nutria

import android.app.Application
import android.content.Context
import android.content.SharedPreferences
import androidx.room.Room

class SharedPreferencesWrapper(context: Context) {
    private val prefsFilename = "net.pinae.nutria.prefs"
    private val calirieBudgetKey = "calorieBudget"
    private val prefs: SharedPreferences = context.getSharedPreferences(prefsFilename, 0)

    var calorieBudget: Float
        get() = prefs.getFloat(calirieBudgetKey, 2000f)
        set(value) = prefs.edit().putFloat(calirieBudgetKey, value).apply()
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