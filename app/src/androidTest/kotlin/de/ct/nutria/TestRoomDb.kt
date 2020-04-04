package de.ct.nutria

import android.database.sqlite.SQLiteConstraintException
import androidx.room.Room
import androidx.test.platform.app.InstrumentationRegistry
import org.junit.Before
import org.junit.Test
import java.time.OffsetDateTime
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith

class TestRoomDaos {
    lateinit var cachedFoodDatabase: CachedFoodDatabase

    @Before
    fun setUp() {
        this.cachedFoodDatabase = Room.inMemoryDatabaseBuilder(
                InstrumentationRegistry.getInstrumentation().targetContext,
                CachedFoodDatabase::class.java
        ).build()
    }

    private fun get_example_food_foo(): QueryFoodItem {
        return QueryFoodItem(
                foodId = 1,
                isRecipe = false,
                categoryId = 1,
                name = "Backzutat: foo",
                calories = 202.3f,
                source = "Max Mustermann",
                ean = null,
                referenceAmount = 100.0f,
                lastLogged = OffsetDateTime.now(),
                relevance = 1.0f
        )
    }

    private fun get_example_food_bar(): QueryFoodItem {
        return QueryFoodItem(
                foodId = 2,
                isRecipe = false,
                categoryId = 1,
                name = "Backzutat: bar",
                calories = 123.3f,
                source = "Max Mustermann",
                ean = null,
                referenceAmount = 50.0f,
                lastLogged = OffsetDateTime.now(),
                relevance = .75f
        )
    }

    @Test
    fun check_empty_database() {
        val queryFoodItemDao = cachedFoodDatabase.queryFoodItemDao()
        assertEquals(
                queryFoodItemDao.getFood("Backzutat: foo") as QueryFoodItem?,
                null)
    }

    @Test
    fun insert_and_get() {
        val queryFoodItemDao = cachedFoodDatabase.queryFoodItemDao()
        val exampleFood = get_example_food_foo()
        queryFoodItemDao.insertAll(exampleFood)
        assertEquals(
                queryFoodItemDao.getFood("Backzutat: foo") as QueryFoodItem?,
                exampleFood)
    }

    @Test
    fun double_insert() {
        val queryFoodItemDao = cachedFoodDatabase.queryFoodItemDao()
        val exampleFood = get_example_food_foo()
        queryFoodItemDao.insertAll(exampleFood)
        val ex = assertFailsWith<SQLiteConstraintException>(
                "This throws an error that the UNIQUE constraint failed."
        ) {
            queryFoodItemDao.insertAll(exampleFood)
        }
        assertEquals("UNIQUE constraint failed: food.foodId " +
                "(code 1555 SQLITE_CONSTRAINT_PRIMARYKEY[1555])", ex.message)
        assertEquals(exampleFood,
                queryFoodItemDao.getFood("Backzutat: foo") as QueryFoodItem?)
    }

    @Test
    fun insert_and_query() {
        val queryFoodItemDao = cachedFoodDatabase.queryFoodItemDao()
        val exampleFood = get_example_food_foo()
        queryFoodItemDao.insertAll(exampleFood)
        assertEquals(
                queryFoodItemDao.queryFood("foo"),
                listOf(exampleFood))
        val exampleFood2 = get_example_food_bar()
        queryFoodItemDao.insertAll(exampleFood2)
        assertEquals(
                queryFoodItemDao.queryFood("Backzutat"),
                listOf(exampleFood, exampleFood2))
        assertEquals(
                queryFoodItemDao.queryFood(""),
                listOf(exampleFood, exampleFood2))
        queryFoodItemDao.delete(exampleFood)
        assertEquals(
                queryFoodItemDao.queryFood(""),
                listOf(exampleFood2))
        queryFoodItemDao.delete(exampleFood2)
        assertEquals(
                queryFoodItemDao.queryFood(""),
                listOf())
    }

    @Test
    fun insert_and_update() {
        val queryFoodItemDao = cachedFoodDatabase.queryFoodItemDao()
        val exampleFood = get_example_food_foo()
        queryFoodItemDao.insertAll(exampleFood)
        assertEquals(
                queryFoodItemDao.getFood("Backzutat: foo") as QueryFoodItem?,
                exampleFood)
        val updatedFood = QueryFoodItem(
                foodId = 1,
                isRecipe = true,
                categoryId = 6,
                name = "Frucht: Apfel",
                calories = 32.3f,
                source = "Maria Mustermann",
                ean = null,
                referenceAmount = 250.0f,
                lastLogged = OffsetDateTime.now(),
                relevance = 1.2f
        )
        queryFoodItemDao.updateAll(updatedFood)
        assertEquals(
                queryFoodItemDao.queryFood(""),
                listOf(updatedFood))
    }
}