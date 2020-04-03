package de.ct.nutria

import android.os.Parcel
import android.os.Parcelable
import android.util.Log
import org.json.JSONArray
import java.time.OffsetDateTime
import java.util.Locale
import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.ColumnInfo
import androidx.room.Ignore
import androidx.room.TypeConverters
import org.json.JSONObject
import kotlin.reflect.KMutableProperty

data class ManufacturerDescriptionStrings(
        var recipeBy: String = "recipe by",
        var selfmade: String = "selfmade",
        var harvested: String = "harvested")

@TypeConverters(IsoTimeConverter::class)
@Entity(tableName = "loggedFood")
data class FoodItem (
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "roomId") var roomId: Int? = null,
    @ColumnInfo(name = "foodId") var foodId: Int = -1,
    @ColumnInfo(name = "type") var type: Int = 0,
    @ColumnInfo(name = "nameAddition") var nameAddition: String? = null,
    @ColumnInfo(name = "author") var author: String? = null,
    @ColumnInfo(name = "categoryId") var categoryId: Int = 0,
    @ColumnInfo(name = "categoryName") var categoryName: String? = null,
    @ColumnInfo(name = "date") var date: OffsetDateTime? = null,
    @ColumnInfo(name = "ean") var ean: Long = -1,
    @ColumnInfo(name = "reference_amount") var reference_amount: Float = java.lang.Float.NaN,
    @ColumnInfo(name = "calories") var calories: Float = java.lang.Float.NaN,
    @ColumnInfo(name = "manufacturer") var manufacturer: String? = null,
    @ColumnInfo(name = "total_fat") var total_fat: Float = java.lang.Float.NaN,
    @ColumnInfo(name = "saturated_fat") var saturated_fat: Float = java.lang.Float.NaN,
    @ColumnInfo(name = "cholesterol") var cholesterol: Float = java.lang.Float.NaN,
    @ColumnInfo(name = "protein") var protein: Float = java.lang.Float.NaN,
    @ColumnInfo(name = "total_carbs") var total_carbs: Float = java.lang.Float.NaN,
    @ColumnInfo(name = "sugar") var sugar: Float = java.lang.Float.NaN,
    @ColumnInfo(name = "dietary_fiber") var dietary_fiber: Float = java.lang.Float.NaN,
    @ColumnInfo(name = "salt") var salt: Float = java.lang.Float.NaN,
    @ColumnInfo(name = "sodium") var sodium: Float = java.lang.Float.NaN,
    @ColumnInfo(name = "potassium") var potassium: Float = java.lang.Float.NaN,
    @ColumnInfo(name = "copper") var copper: Float = java.lang.Float.NaN,
    @ColumnInfo(name = "iron") var iron: Float = java.lang.Float.NaN,
    @ColumnInfo(name = "magnesium") var magnesium: Float = java.lang.Float.NaN,
    @ColumnInfo(name = "manganese") var manganese: Float = java.lang.Float.NaN,
    @ColumnInfo(name = "zinc") var zinc: Float = java.lang.Float.NaN,
    @ColumnInfo(name = "phosphorous") var phosphorous: Float = java.lang.Float.NaN,
    @ColumnInfo(name = "sulphur") var sulphur: Float = java.lang.Float.NaN,
    @ColumnInfo(name = "chloro") var chloro: Float = java.lang.Float.NaN,
    @ColumnInfo(name = "fluoric") var fluoric: Float = java.lang.Float.NaN,
    @ColumnInfo(name = "vitamin_b1") var vitamin_b1: Float = java.lang.Float.NaN,
    @ColumnInfo(name = "vitamin_b12") var vitamin_b12: Float = java.lang.Float.NaN,
    @ColumnInfo(name = "vitamin_b6") var vitamin_b6: Float = java.lang.Float.NaN,
    @ColumnInfo(name = "vitamin_c") var vitamin_c: Float = java.lang.Float.NaN,
    @ColumnInfo(name = "vitamin_d") var vitamin_d: Float = java.lang.Float.NaN,
    @ColumnInfo(name = "vitamin_e") var vitamin_e: Float = java.lang.Float.NaN,
    @ColumnInfo(name = "relevance") var relevance: Float = 1f,
    @ColumnInfo(name = "lastLogged") var lastLogged: OffsetDateTime? = null,
    @Ignore var manSt: ManufacturerDescriptionStrings = ManufacturerDescriptionStrings()
) : Parcelable {
    var name: String
        get() = "$categoryName: $nameAddition"
        set(value) {
            categoryName = value.split(": ")[0]
            nameAddition = value.split(": ")[1]
        }

    val caloriesString: String
        get() = if (java.lang.Float.isNaN(calories)) {
            ""
        } else {
            String.format(Locale.getDefault(), "%.0f", calories)
        }

    val referenceAmountString: String
        get() = if (java.lang.Float.isNaN(reference_amount)) {
            "-"
        } else {
            String.format(Locale.getDefault(), "%.1f g", reference_amount)
        }

    @Ignore
    internal constructor(nameAddition: String) : this() {
        this.nameAddition = nameAddition
        this.date = OffsetDateTime.now()
        this.categoryId = -1
        this.type = 0
        this.reference_amount = 100.0f
    }

    @Ignore
    private constructor(p: Parcel) : this() {
        roomId = p.readInt()
        type = p.readInt()
        foodId = p.readInt()
        nameAddition = p.readString()
        author = p.readString()
        categoryId = p.readInt()
        categoryName = p.readString()
        date = IsoTimeConverter().stringToOffsetDateTime(p.readString())
        ean = p.readLong()
        reference_amount = p.readFloat()
        calories = p.readFloat()
        manufacturer = p.readString()
        total_fat = p.readFloat()
        saturated_fat = p.readFloat()
        cholesterol = p.readFloat()
        protein = p.readFloat()
        total_carbs = p.readFloat()
        sugar = p.readFloat()
        dietary_fiber = p.readFloat()
        salt = p.readFloat()
        sodium = p.readFloat()
        potassium = p.readFloat()
        copper = p.readFloat()
        iron = p.readFloat()
        magnesium = p.readFloat()
        manganese = p.readFloat()
        zinc = p.readFloat()
        phosphorous = p.readFloat()
        sulphur = p.readFloat()
        chloro = p.readFloat()
        fluoric = p.readFloat()
        vitamin_b1 = p.readFloat()
        vitamin_b12 = p.readFloat()
        vitamin_b6 = p.readFloat()
        vitamin_c = p.readFloat()
        vitamin_d = p.readFloat()
        vitamin_e = p.readFloat()
        relevance = p.readFloat()
        lastLogged = IsoTimeConverter().stringToOffsetDateTime(p.readString())
    }

    fun setCategory(id: Int, categoryName: String) {
        this.categoryId = id
        this.categoryName = categoryName
    }

    fun setCreationDate(newDate: OffsetDateTime) {
        this.date = newDate
    }

    fun describeManufacturer(): String {
        return when(type) {
            0 -> {
                if (manufacturer != null) manufacturer!!
                else manSt.harvested
            }
            1 -> {
                if (author != null && author!!.isNotEmpty()) manSt.recipeBy + author!!
                else manSt.selfmade
            }
            else -> ""
        }
    }

    override fun describeContents(): Int {
        return 0
    }

    override fun writeToParcel(out: Parcel, flags: Int) {
        if (roomId != null) out.writeInt(roomId!!)
        else out.writeValue(roomId)
        out.writeInt(type)
        out.writeInt(foodId)
        out.writeString(nameAddition)
        out.writeString(author)
        out.writeInt(categoryId)
        out.writeString(categoryName)
        out.writeString(IsoTimeConverter().offsetDateTimeToString(date))
        out.writeLong(ean)
        out.writeFloat(reference_amount)
        out.writeFloat(calories)
        out.writeString(manufacturer)
        out.writeFloat(total_fat)
        out.writeFloat(saturated_fat)
        out.writeFloat(cholesterol)
        out.writeFloat(protein)
        out.writeFloat(total_carbs)
        out.writeFloat(sugar)
        out.writeFloat(dietary_fiber)
        out.writeFloat(salt)
        out.writeFloat(sodium)
        out.writeFloat(potassium)
        out.writeFloat(copper)
        out.writeFloat(iron)
        out.writeFloat(magnesium)
        out.writeFloat(manganese)
        out.writeFloat(zinc)
        out.writeFloat(phosphorous)
        out.writeFloat(sulphur)
        out.writeFloat(chloro)
        out.writeFloat(fluoric)
        out.writeFloat(vitamin_b1)
        out.writeFloat(vitamin_b12)
        out.writeFloat(vitamin_b6)
        out.writeFloat(vitamin_c)
        out.writeFloat(vitamin_d)
        out.writeFloat(vitamin_e)
        out.writeFloat(relevance)
        out.writeString(IsoTimeConverter().offsetDateTimeToString(lastLogged))
    }

    fun toQueryFoodItem(): QueryFoodItem {
        return QueryFoodItem(
                foodId = foodId,
                isRecipe = type == 1,
                categoryId = categoryId,
                name = name,
                calories = calories,
                source = describeManufacturer(),
                ean = ean,
                referenceAmount = reference_amount,
                relevance = relevance,
                lastLogged = lastLogged
        )
    }

    fun setFromJSON(property: KMutableProperty<*>, json: JSONObject, check: (Any) -> Boolean) {
        if (json.isNull(property.name)) return
        if (check(json.get(property.name))) property.setter.call(json.get(property.name))
    }

    fun setFloatFromJSON(property: KMutableProperty<Float>, json: JSONObject) {
        if (json.isNull(property.name)) return
        val doubleValue: Double = json.getDouble(property.name)
        if (doubleValue.isNaN()) return
        property.setter.call(doubleValue.toFloat())
    }

    companion object {
        @JvmField
        val CREATOR: Parcelable.Creator<FoodItem> = object : Parcelable.Creator<FoodItem> {
            override fun createFromParcel(`in`: Parcel): FoodItem {
                return FoodItem(`in`)
            }

            override fun newArray(size: Int): Array<FoodItem?> {
                return arrayOfNulls(size)
            }
        }

        fun fromQueryFoodItem(queryFoodItem: QueryFoodItem): FoodItem {
            var nameAddition = ""
            queryFoodItem.name?.let { nameAddition = it.split(": ")[1] }
            val food = FoodItem(nameAddition)
            food.type = 0
            if (queryFoodItem.isRecipe) food.type = 1
            food.foodId = queryFoodItem.foodId
            if (queryFoodItem.categoryId >= 0) {
                food.categoryId = queryFoodItem.categoryId
                if (queryFoodItem.name != null)
                    food.setCategory(food.categoryId, queryFoodItem.name.split(": ")[0])
            }
            if (queryFoodItem.isRecipe) food.author = queryFoodItem.source
            else food.manufacturer = queryFoodItem.source
            queryFoodItem.ean?.let { food.ean = it }
            queryFoodItem.calories?.let { food.calories = it }
            queryFoodItem.referenceAmount?.let { food.reference_amount = it }
            food.relevance = queryFoodItem.relevance
            food.lastLogged = queryFoodItem.lastLogged
            return food
        }

        fun fromJSONObject(detailedFood: JSONObject, existingFood: FoodItem? = null): FoodItem? {
            Log.i("fromJSONObject", existingFood.toString())
            if (detailedFood.isNull("name")) {
                Log.e("Unable to create FoodItem from JSON",
                        "The property 'name' is missing.")
                return null
            }
            val loadedName = detailedFood.getString("name")
            if (!loadedName.contains(": ")) {
                Log.e("Unable to create FoodItem from JSON",
                        "The property 'name' did not contain ': '.")
                return null
            }
            val nameAddition: String = loadedName.split(": ")[1]
            var food = FoodItem(nameAddition)
            existingFood?.let { food = it }
            food.nameAddition = nameAddition
            food.nameAddition?.let{Log.i("nameAddition", it)}
            food.categoryName = loadedName.split(": ")[0]
            arrayOf(
                    food::type,
                    food::foodId,
                    food::categoryId,
                    food::ean
            ).map { food.setFromJSON(it, detailedFood) { true } }
            arrayOf(
                    food::manufacturer,
                    food::author
            ).map { food.setFromJSON(it, detailedFood) {
                name -> (name as String?).isNullOrBlank() } }
            food.author?.let { if (it.removeSurrounding(" ").isEmpty()) null; else it }
            arrayOf(
                    food::reference_amount,
                    food::calories,
                    food::total_fat,
                    food::saturated_fat,
                    food::cholesterol,
                    food::protein,
                    food::total_carbs,
                    food::sugar,
                    food::dietary_fiber,
                    food::salt,
                    food::sodium,
                    food::potassium,
                    food::copper,
                    food::iron,
                    food::magnesium,
                    food::manganese,
                    food::zinc,
                    food::phosphorous,
                    food::sulphur,
                    food::chloro,
                    food::vitamin_b1,
                    food::vitamin_b12,
                    food::vitamin_b6,
                    food::vitamin_c,
                    food::vitamin_d,
                    food::vitamin_e
            ).map { food.setFloatFromJSON(it, detailedFood) }
            if (detailedFood.has("date") &&
                    !detailedFood.getString("date").isNullOrBlank())
                food.date = IsoTimeConverter().stringToOffsetDateTime(
                    detailedFood.getString("date"))
            return food
        }

        fun fromQueryJSONArray(jsonFood: JSONArray): FoodItem {
            val food = FoodItem(jsonFood[2] as String)
            val typeIdCategory = jsonFood[0] as String
            food.type = typeIdCategory.substring(0, 1).toInt()
            food.foodId = typeIdCategory.substring(1).split(":")[0].toInt()
            food.setCategory(
                    typeIdCategory.substring(1).split(":")[1].toInt(),
                    jsonFood[1] as String)
            val manufacturerOrAuthor = jsonFood[3] as String
            if (manufacturerOrAuthor.isNotEmpty()) when (food.type) {
                0 -> food.manufacturer = manufacturerOrAuthor
                1 -> food.author = manufacturerOrAuthor
            }
            food.reference_amount = (jsonFood[4] as String).toFloat()
            food.calories = (jsonFood[5] as String).toFloat()
            return food
        }
    }
}
