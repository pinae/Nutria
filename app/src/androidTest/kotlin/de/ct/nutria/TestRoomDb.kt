package de.ct.nutria

import android.database.sqlite.SQLiteConstraintException
import androidx.room.Room
import androidx.test.platform.app.InstrumentationRegistry
import org.junit.Before
import org.junit.Test
import java.time.OffsetDateTime
import java.time.ZoneOffset
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith
import kotlin.test.assertNotNull

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
                queryFoodItemDao.getFood("Backzutat: foo"),
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
        assertEquals("UNIQUE constraint failed: food.idInNutriaDB " +
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

    @Test
    fun shortenCache() {
        val queryFoodItemDao = cachedFoodDatabase.queryFoodItemDao()
        val exampleFoodFoo = get_example_food_foo()
        val exampleFoodBar = get_example_food_bar()
        queryFoodItemDao.insertAll(exampleFoodFoo, exampleFoodBar)
        assertEquals(
                queryFoodItemDao.queryFood(""),
                listOf(exampleFoodFoo, exampleFoodBar))
        queryFoodItemDao.deleteIrrelevant(1)
        assertEquals(
                queryFoodItemDao.queryFood(""),
                listOf(exampleFoodFoo))
    }

    @Test
    fun insertLoggedFood() {
        val logTime = OffsetDateTime.now()
        val food = FoodItem(
                type = 0,
                foodId = 5,
                categoryId = 5,
                categoryName = "Ei",
                nameAddition = "Hühnerei",
                amount = 57.0f,
                calories = 78.09f,
                referenceAmount = 100.0f,
                lastLogged = logTime
        )
        assertEquals(Float.NaN, food.total_fat)
        val loggedFoodDao = cachedFoodDatabase.loggedFoodDao()
        assertEquals(null, loggedFoodDao.getFood(0, 5))
        loggedFoodDao.insertAll(food)
        val loadedFood = loggedFoodDao.getFood(0, 5)
        assertNotNull(loadedFood)
        for (property in arrayOf(
                FoodItem::type,
                FoodItem::foodId,
                FoodItem::categoryId,
                FoodItem::categoryName,
                FoodItem::nameAddition,
                FoodItem::amount,
                FoodItem::calories,
                FoodItem::referenceAmount,
                FoodItem::lastLogged
        )) {
            assertEquals(property.get(food), property.get(loadedFood))
        }
    }

    @Test
    fun loadLogsForDay() {
        val food1 = FoodItem(
                type = 0,
                foodId = 5,
                categoryId = 5,
                categoryName = "Ei",
                nameAddition = "Hühnerei",
                amount = 57.0f,
                calories = 78.09f,
                referenceAmount = 100.0f,
                lastLogged = OffsetDateTime.of(2020, 4, 12,
                        12, 3, 23, 123, ZoneOffset.UTC)
        )
        val food2 = FoodItem(
                type = 0,
                foodId = 1,
                categoryId = 1,
                categoryName = "Mehl",
                nameAddition = "Weizenmehl Type 550",
                amount = 100.0f,
                calories = 352.0f,
                referenceAmount = 100.0f,
                lastLogged = OffsetDateTime.of(2020, 4, 12,
                        23, 59, 59, 123, ZoneOffset.UTC)
        )
        val food3 = FoodItem(
                type = 0,
                foodId = 19,
                categoryId = 12,
                categoryName = "Süßigkeit",
                nameAddition = "Sahnekaramellen",
                amount = 100.0f,
                calories = 357.0f,
                referenceAmount = 100.0f,
                lastLogged = OffsetDateTime.of(2020, 4, 11,
                        23, 59, 59, 123, ZoneOffset.UTC)
        )
        val loggedFoodDao = cachedFoodDatabase.loggedFoodDao()
        loggedFoodDao.insertAll(food1, food2, food3)
        val tc = IsoTimeConverter()
        val foodOfToday = loggedFoodDao.loadTimeRange(
                tc.offsetDateTimeToString(
                        OffsetDateTime.of(2020, 4, 12,
                        0, 0, 0, 0, ZoneOffset.UTC)),
                tc.offsetDateTimeToString(
                        OffsetDateTime.of(2020, 4, 13,
                        0, 0, 0, 0, ZoneOffset.UTC))
        )
        listOf(food1, food2).forEachIndexed { index, expected ->
            val actual = foodOfToday[index]
            arrayOf(
                    FoodItem::type,
                    FoodItem::foodId,
                    FoodItem::categoryId,
                    FoodItem::categoryName,
                    FoodItem::nameAddition,
                    FoodItem::amount,
                    FoodItem::calories,
                    FoodItem::referenceAmount,
                    FoodItem::lastLogged
            ).forEach {property ->
                assertEquals(property.get(expected), property.get(actual))
            }
        }
    }
}