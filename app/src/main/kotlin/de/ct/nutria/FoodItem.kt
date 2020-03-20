package de.ct.nutria

import android.os.Parcel
import android.os.Parcelable
import org.json.JSONArray
import java.time.OffsetDateTime
import java.util.Calendar
import java.util.Date
import java.util.Locale
import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.ColumnInfo
import androidx.room.Ignore
import androidx.room.TypeConverters
import org.json.JSONObject

data class ManufacturerDescriptionStrings(
        var recipeBy: String = "recipe by",
        var selfmade: String = "selfmade",
        var harvested: String = "harvested")

@TypeConverters(IsoTimeConverter::class)
@Entity(tableName = "loggedFood")
data class FoodItem (
    @PrimaryKey
    @ColumnInfo(name = "foodId") var id: Int = -1,
    @ColumnInfo(name = "type") var type: Int = 0,
    @ColumnInfo(name = "nameAddition") var nameAddition: String? = null,
    @ColumnInfo(name = "autorName") var authorName: String? = null,
    @ColumnInfo(name = "categoryId") var categoryId: Int = 0,
    @ColumnInfo(name = "categoryName") var categoryName: String? = null,
    @ColumnInfo(name = "date") var date: OffsetDateTime? = null,
    @ColumnInfo(name = "ean") var ean: Long = -1,
    @ColumnInfo(name = "referenceAmount") var referenceAmount: Float = java.lang.Float.NaN,
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
    @ColumnInfo(name = "vitaminB1") var vitaminB1: Float = java.lang.Float.NaN,
    @ColumnInfo(name = "vitaminB12") var vitaminB12: Float = java.lang.Float.NaN,
    @ColumnInfo(name = "vitaminB6") var vitaminB6: Float = java.lang.Float.NaN,
    @ColumnInfo(name = "vitaminC") var vitaminC: Float = java.lang.Float.NaN,
    @ColumnInfo(name = "vitaminD") var vitaminD: Float = java.lang.Float.NaN,
    @ColumnInfo(name = "vitaminE") var vitaminE: Float = java.lang.Float.NaN,
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
        get() = if (java.lang.Float.isNaN(referenceAmount)) {
            "-"
        } else {
            String.format(Locale.getDefault(), "%.1f g", referenceAmount)
        }

    @Ignore
    internal constructor(nameAddition: String) : this() {
        this.nameAddition = nameAddition
        this.date = OffsetDateTime.now()
        this.categoryId = -1
        this.type = 0
        this.referenceAmount = 100.0f
    }

    @Ignore
    private constructor(p: Parcel) : this() {
        type = p.readInt()
        id = p.readInt()
        nameAddition = p.readString()
        authorName = p.readString()
        categoryId = p.readInt()
        categoryName = p.readString()
        date = IsoTimeConverter().stringToOffsetDateTime(p.readString())
        ean = p.readLong()
        referenceAmount = p.readFloat()
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
        vitaminB1 = p.readFloat()
        vitaminB12 = p.readFloat()
        vitaminB6 = p.readFloat()
        vitaminC = p.readFloat()
        vitaminD = p.readFloat()
        vitaminE = p.readFloat()
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
                if (authorName != null && authorName!!.isNotEmpty()) manSt.recipeBy + authorName!!
                else manSt.selfmade
            }
            else -> ""
        }
    }

    override fun describeContents(): Int {
        return 0
    }

    override fun writeToParcel(out: Parcel, flags: Int) {
        out.writeInt(type)
        out.writeInt(id)
        out.writeString(nameAddition)
        out.writeString(authorName)
        out.writeInt(categoryId)
        out.writeString(categoryName)
        out.writeString(IsoTimeConverter().offsetDateTimeToString(date))
        out.writeLong(ean)
        out.writeFloat(referenceAmount)
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
        out.writeFloat(vitaminB1)
        out.writeFloat(vitaminB12)
        out.writeFloat(vitaminB6)
        out.writeFloat(vitaminC)
        out.writeFloat(vitaminD)
        out.writeFloat(vitaminE)
        out.writeFloat(relevance)
        out.writeString(IsoTimeConverter().offsetDateTimeToString(lastLogged))
    }

    fun toQueryFoodItem(): QueryFoodItem {
        return QueryFoodItem(
                foodId = id,
                isRecipe = type == 1,
                categoryId = categoryId,
                name = name,
                calories = calories,
                source = describeManufacturer(),
                ean = ean,
                referenceAmount = referenceAmount,
                relevance = relevance,
                lastLogged = lastLogged
        )
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
            food.id = queryFoodItem.foodId
            if (queryFoodItem.categoryId >= 0) {
                food.categoryId = queryFoodItem.categoryId
                if (queryFoodItem.name != null)
                    food.setCategory(food.categoryId, queryFoodItem.name.split(": ")[0])
            }
            if (queryFoodItem.isRecipe) food.authorName = queryFoodItem.source
            else food.manufacturer = queryFoodItem.source
            queryFoodItem.ean?.let { food.ean = it }
            queryFoodItem.calories?.let { food.calories = it }
            queryFoodItem.referenceAmount?.let { food.referenceAmount = it }
            food.relevance = queryFoodItem.relevance
            food.lastLogged = queryFoodItem.lastLogged
            return food
        }

        fun fromJSONObject(detailedFood: JSONObject, existingFood: FoodItem? = null): FoodItem {
            var food = FoodItem(detailedFood["nameAddition"] as String)
            existingFood?.let { food = it }
            food.nameAddition = detailedFood["nameAddition"] as String
            food.type = detailedFood["type"] as Int
            food.id = detailedFood["id"] as Int
            if ((detailedFood["authorName"] as String).isNotEmpty()) {
                food.authorName = detailedFood["authorName"] as String?
            }
            food.categoryId = detailedFood["categoryId"] as Int
            food.categoryName = detailedFood["categoryName"] as String?
            food.date = IsoTimeConverter().stringToOffsetDateTime(detailedFood["date"] as String)
            food.ean = detailedFood["ean"] as Long
            food.referenceAmount = detailedFood["referenceAmount"] as Float
            food.calories = detailedFood["calories"] as Float
            food.manufacturer = detailedFood["manufacturer"] as String?
            food.total_fat = detailedFood["total_fat"] as Float
            food.saturated_fat = detailedFood["saturated_fat"] as Float
            food.cholesterol = detailedFood["cholesterol"] as Float
            food.protein = detailedFood["protein"] as Float
            food.total_carbs = detailedFood["total_carbs"] as Float
            food.sugar = detailedFood["sugar"] as Float
            food.dietary_fiber = detailedFood["dietary_fiber"] as Float
            food.salt = detailedFood["salt"] as Float
            food.sodium = detailedFood["sodium"] as Float
            food.potassium = detailedFood["potassium"] as Float
            food.copper = detailedFood["copper"] as Float
            food.iron = detailedFood["iron"] as Float
            food.magnesium = detailedFood["magnesium"] as Float
            food.manganese = detailedFood["manganese"] as Float
            food.zinc = detailedFood["zinc"] as Float
            food.phosphorous = detailedFood["phosphorous"] as Float
            food.sulphur = detailedFood["sulphur"] as Float
            food.chloro = detailedFood["chloro"] as Float
            food.fluoric = detailedFood["fluoric"] as Float
            food.vitaminB1 = detailedFood["vitaminB1"] as Float
            food.vitaminB12 = detailedFood["vitaminB12"] as Float
            food.vitaminB6 = detailedFood["vitaminB6"] as Float
            food.vitaminC = detailedFood["vitaminC"] as Float
            food.vitaminD = detailedFood["vitaminD"] as Float
            food.vitaminE = detailedFood["vitaminE"] as Float
            return food
        }

        fun fromQueryJSONArray(jsonFood: JSONArray): FoodItem {
            val food = FoodItem(jsonFood[2] as String)
            val typeIdCategory = jsonFood[0] as String
            food.type = typeIdCategory[0].toInt()
            food.id = typeIdCategory.substring(1).split(":")[0].toInt()
            food.setCategory(
                    typeIdCategory.substring(1).split(":")[1].toInt(),
                    jsonFood[1] as String)
            val manufacturerOrAuthor = jsonFood[3] as String
            if (manufacturerOrAuthor.isNotEmpty()) when (food.type) {
                0 -> food.manufacturer = manufacturerOrAuthor
                1 -> food.authorName = manufacturerOrAuthor
            }
            food.referenceAmount = (jsonFood[4] as String).toFloat()
            food.calories = (jsonFood[5] as String).toFloat()
            return food
        }
    }
}
