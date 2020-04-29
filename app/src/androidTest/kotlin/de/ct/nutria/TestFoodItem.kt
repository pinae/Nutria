package de.ct.nutria

import android.os.Parcel
import org.json.JSONObject
import java.time.OffsetDateTime
import java.time.ZoneOffset
import org.junit.Test
import kotlin.reflect.KMutableProperty
import kotlin.test.assertEquals
import kotlin.test.assertNotNull

class TestFoodItem {
    private fun getBanana(): FoodItem {
        return FoodItem(
                roomId = 0,
                foodId = 42,
                type = 0,
                categoryId = 20,
                categoryName = "Obst",
                nameAddition = "Banane",
                author = "Max Mustermann",
                manufacturer = "Dohle",
                ean = 94011,
                date = OffsetDateTime.of(2019, 5, 7,
                        16, 44, 7, 152176, ZoneOffset.UTC),
                lastLogged = OffsetDateTime.of(2020, 4, 27,
                        16, 12, 0, 0, ZoneOffset.UTC),
                amount = 85.0f,
                referenceAmount = 100f,
                calories = 79.05f,
                total_fat = 0.153f,
                saturated_fat = 0.051f,
                cholesterol = 0.0f,
                protein = 0.9775f,
                total_carbs = 17.0255f,
                sugar = 14.6795f,
                dietary_fiber = 1.7f,
                salt = 0f,
                sodium = 0.85f,
                potassium = 311.95f,
                copper = 0.0918f,
                iron = 0.2975f,
                magnesium = 25.5f,
                manganese = 0.2193f,
                zinc = 0.136f,
                phosphorous = 18.7f,
                sulphur = 11.05f,
                chloro = 92.65f,
                fluoric = 0.0119f,
                vitamin_b1 = 0.034f,
                vitamin_b12 = 0f,
                vitamin_b6 = 0.306f,
                vitamin_c = 9.35f,
                vitamin_d = 0f,
                vitamin_e = 0.2295f,
                relevance = 2.123f
        )
    }

    private fun getApple(): FoodItem {
        return FoodItem(
                foodId = 37,
                type = 0,
                categoryId = 20,
                categoryName = "Obst",
                nameAddition = "Apfel",
                author = " ",
                date = OffsetDateTime.of(2019, 5, 7,
                        16, 44, 6, 743352, ZoneOffset.UTC),
                referenceAmount = 100f,
                calories = 65f,
                total_carbs = 14.35f,
                sugar = 13.16f,
                protein = 0.34f,
                dietary_fiber = 2.01f,
                total_fat = 0.05f,
                saturated_fat = 0.02f,
                cholesterol = 0f,
                salt = 0f,
                sodium = 1f,
                fluoric = 0.009f,
                sulphur = 6f,
                magnesium = 5f,
                manganese = 0.043f,
                copper = 0.052f,
                zinc = 0.04f,
                iron = 0.25f,
                potassium = 119f,
                phosphorous = 11f,
                chloro = 2f,
                vitamin_b1 = 0.01f,
                vitamin_b6 = 0.04f,
                vitamin_b12 = 0f,
                vitamin_c = 12f,
                vitamin_d = 0f,
                vitamin_e = 0.49f
        )
    }

    private fun checkAllFoodItemProperties(expected: FoodItem, actual: FoodItem) {
        val props: ArrayList<KMutableProperty<*>> = arrayListOf(FoodItem::roomId,
                FoodItem::foodId,
                FoodItem::type,
                FoodItem::categoryId,
                FoodItem::categoryName,
                FoodItem::nameAddition)
        props.addAll(FoodItem.sourceProperties())
        props.add(FoodItem::ean)
        props.add(FoodItem::amount)
        props.add(FoodItem::referenceAmount)
        props.addAll(FoodItem.scalableProperties())
        props.forEach { property ->
            assertEquals(property.getter.call(expected), property.getter.call(actual),
                    property.name + " was wrong!")
        }
        assertEquals(expected.relevance, actual.relevance, "relevance was wrong!")
    }

    @Test
    fun savesToParcelAndRestoresCorrectly() {
        val originalFood = getBanana()
        val parcel = Parcel.obtain()
        originalFood.writeToParcel(parcel, 0)
        parcel.setDataPosition(0)
        val recreatedFood = FoodItem.CREATOR.createFromParcel(parcel)
        checkAllFoodItemProperties(originalFood, recreatedFood)
    }

    @Test
    fun createsCsvHeaderAndContent() {
        val originalFood = getBanana()
        assertEquals("roomId, foodId, type, categoryId, categoryName, nameAddition, " +
                "author, manufacturer, ean, " +
                "date, lastLogged, " +
                "amount, referenceAmount, calories, " +
                "total_fat, saturated_fat, cholesterol, protein, total_carbs, sugar, " +
                "dietary_fiber, salt, sodium, potassium, copper, iron, magnesium, manganese, " +
                "zinc, phosphorous, sulphur, chloro, fluoric, vitamin_b1, vitamin_b12, " +
                "vitamin_b6, vitamin_c, vitamin_d, vitamin_e, relevance",
                FoodItem.getCsvHeader())
        assertEquals("0, 42, 0, 20, Obst, Banane, " +
                "Max Mustermann, Dohle, 94011, " +
                "2019-05-07T16:44:07.000152176Z, 2020-04-27T16:12:00Z, " +
                "85.00000, 100.00000, 79.05000, " +
                "0.15300, 0.05100, 0.00000, 0.97750, 17.02550, 14.67950, " +
                "1.70000, 0.00000, 0.85000, 311.95001, 0.09180, 0.29750, 25.50000, 0.21930, " +
                "0.13600, 18.70000, 11.05000, 92.65000, 0.01190, 0.03400, 0.00000, " +
                "0.30600, 9.35000, 0.00000, 0.22950, 2.12300",
                originalFood.getCsvLine())
    }

    @Test
    fun createFromJson() {
        val json = "{\"sodium\":1,\"calories\":65,\"vitamin_d\":0,\"name\":\"Obst: Apfel\"," +
                "\"cholesterol\":0,\"phosphorous\":11,\"saturated_fat\":0.02," +
                "\"vitamin_b6\":0.04,\"vitamin_e\":0.49,\"salt\":0,\"total_carbs\":14.35," +
                "\"sugar\":13.16,\"servings\":[],\"chloro\":2,\"vitamin_b12\":0," +
                "\"magnesium\":5,\"copper\":0.052," +
                "\"creation_date\":\"2019-05-07 16:44:06.743352+00:00\",\"protein\":0.34," +
                "\"dietary_fiber\":2.01,\"sulphur\":6,\"reference_amount\":100," +
                "\"manganese\":0.043,\"vitamin_c\":12,\"iron\":0.25,\"potassium\":119," +
                "\"type\":0,\"fluoric\":0.009,\"categoryId\":20,\"zinc\":0.04,\"foodId\":37," +
                "\"total_fat\":0.05,\"author\":\" \",\"vitamin_b1\":0.01}"
        val food = FoodItem.fromJSONObject(JSONObject(json))
        assertNotNull(food)
        checkAllFoodItemProperties(getApple(), food!!)
    }

    @Test
    fun updateFromJson() {
        val existingApple = getApple().copyToScaled(75f)
        existingApple.protein = 0.35f
        val json = "{\"sodium\":1,\"calories\":65,\"vitamin_d\":0,\"name\":\"Obst: Apfel\"," +
                "\"cholesterol\":0,\"phosphorous\":11,\"saturated_fat\":0.02," +
                "\"vitamin_b6\":0.04,\"vitamin_e\":0.49,\"salt\":0,\"total_carbs\":14.35," +
                "\"sugar\":13.16,\"servings\":[],\"chloro\":2,\"vitamin_b12\":0," +
                "\"magnesium\":5,\"copper\":0.052," +
                "\"creation_date\":\"2019-05-07 16:44:06.743352+00:00\",\"protein\":0.34," +
                "\"dietary_fiber\":2.01,\"sulphur\":6,\"reference_amount\":100," +
                "\"manganese\":0.043,\"vitamin_c\":12,\"iron\":0.25,\"potassium\":119," +
                "\"type\":0,\"fluoric\":0.009,\"categoryId\":20,\"zinc\":0.04,\"foodId\":37," +
                "\"total_fat\":0.05,\"author\":\" \",\"vitamin_b1\":0.01}"
        val food = FoodItem.fromJSONObject(JSONObject(json), existingApple)
        assertNotNull(food)
        assertEquals(0.34f, food.protein)
        assertEquals(75f, food.amount)
        checkAllFoodItemProperties(existingApple, food)
    }
}