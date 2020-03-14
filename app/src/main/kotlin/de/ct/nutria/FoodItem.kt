package de.ct.nutria

import android.os.Parcel
import android.os.Parcelable
import org.jetbrains.anko.doAsync
import org.jetbrains.anko.uiThread
import org.json.JSONArray

import java.util.Calendar
import java.util.Date
import java.util.Locale

data class ManufacturerDescriptionStrings(
        var recipeBy: String = "recipe by",
        var selfmade: String = "selfmade",
        var harvested: String = "harvested")

class FoodItem : Parcelable {
    var type: Int = 0
    var id: Long = -1
    private var nameAddition: String? = null
    var authorName: String? = null
    var categoryId: Long = 0
        private set
    var categoryName: String? = null
        private set
    var date: Date? = null
        private set
    var ean: String? = null
    var referenceAmount = java.lang.Float.NaN
    var calories = java.lang.Float.NaN
    var manufacturer: String? = null
    var total_fat = java.lang.Float.NaN
    var saturated_fat = java.lang.Float.NaN
    var cholesterol = java.lang.Float.NaN
    var protein = java.lang.Float.NaN
    var total_carbs = java.lang.Float.NaN
    var sugar = java.lang.Float.NaN
    var dietary_fiber = java.lang.Float.NaN
    var salt = java.lang.Float.NaN
    var sodium = java.lang.Float.NaN
    var potassium = java.lang.Float.NaN
    var copper = java.lang.Float.NaN
    var iron = java.lang.Float.NaN
    var magnesium = java.lang.Float.NaN
    var manganese = java.lang.Float.NaN
    var zinc = java.lang.Float.NaN
    var phosphorous = java.lang.Float.NaN
    var sulphur = java.lang.Float.NaN
    var chloro = java.lang.Float.NaN
    var fluoric = java.lang.Float.NaN
    var vitaminB1 = java.lang.Float.NaN
    var vitaminB12 = java.lang.Float.NaN
    var vitaminB6 = java.lang.Float.NaN
    var vitaminC = java.lang.Float.NaN
    var vitaminD = java.lang.Float.NaN
    var vitaminE = java.lang.Float.NaN
    var manSt = ManufacturerDescriptionStrings()

    val name: String
        get() = "$categoryName: $nameAddition"

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

    internal constructor(nameAddition: String) {
        this.nameAddition = nameAddition
        this.date = Calendar.getInstance().time
        this.categoryId = -1
        this.type = 0
        this.referenceAmount = 100.0f
    }

    private constructor(p: Parcel) {
        type = p.readInt()
        id = p.readLong()
        nameAddition = p.readString()
        authorName = p.readString()
        categoryId = p.readLong()
        categoryName = p.readString()
        date = Date()
        date!!.time = p.readLong()
        ean = p.readString()
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
    }

    fun setCategory(id: Long, categoryName: String) {
        this.categoryId = id
        this.categoryName = categoryName
    }

    fun setCreationDate(newDate: Date) {
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
        out.writeLong(id)
        out.writeString(nameAddition)
        out.writeString(authorName)
        out.writeLong(categoryId)
        out.writeString(categoryName)
        out.writeLong(date!!.time)
        out.writeString(ean)
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
    }

    fun toRoomFoodItem(): RoomFoodItem {
        var exportEan: Long = 0
        if (ean != null) exportEan = ean!!.toLong()
        return RoomFoodItem(
            uid = id,
            categoryId = categoryId,
            nameAddition = nameAddition,
            calories = calories,
            manufacturer = describeManufacturer(),
            ean = exportEan,
            referenceAmount = referenceAmount
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

        fun fromRoom(roomFoodItem: RoomFoodItem, roomFoodCategory: RoomFoodCategory): FoodItem {
            val food = FoodItem(roomFoodItem.nameAddition!!)
            food.id = roomFoodItem.uid
            if (roomFoodItem.categoryId >= 0) {
                food.categoryId = roomFoodItem.categoryId
                if (roomFoodCategory.name != null)
                    food.setCategory(food.categoryId, roomFoodCategory.name)
            }
            food.manufacturer = roomFoodItem.manufacturer
            food.ean = roomFoodItem.ean.toString()
            if (roomFoodItem.calories != null)
                food.calories = roomFoodItem.calories
            if (roomFoodItem.referenceAmount != null)
                food.referenceAmount = roomFoodItem.referenceAmount
            return food
        }

        fun fromJSONArray(jsonFood: JSONArray): FoodItem {
            val food = FoodItem(jsonFood[2] as String)
            val typeIdCategory = jsonFood[0] as String
            food.type = typeIdCategory[0].toInt()
            food.id = typeIdCategory.substring(1).split(":")[0].toLong()
            food.setCategory(
                    typeIdCategory.substring(1).split(":")[1].toLong(),
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
