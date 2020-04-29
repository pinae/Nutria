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
    @ColumnInfo(name = "categoryId") var categoryId: Int = 0,
    @ColumnInfo(name = "categoryName") var categoryName: String? = null,
    @ColumnInfo(name = "nameAddition") var nameAddition: String? = null,
    @ColumnInfo(name = "author") var author: String? = null,
    @ColumnInfo(name = "manufacturer") var manufacturer: String? = null,
    @ColumnInfo(name = "ean") var ean: Long = -1,
    @ColumnInfo(name = "date") var date: OffsetDateTime? = null,
    @ColumnInfo(name = "lastLogged") var lastLogged: OffsetDateTime? = null,
    @ColumnInfo(name = "amount") var amount: Float? = java.lang.Float.NaN,
    @ColumnInfo(name = "referenceAmount") var referenceAmount: Float? = java.lang.Float.NaN,
    @ColumnInfo(name = "calories") var calories: Float? = java.lang.Float.NaN,
    @ColumnInfo(name = "total_fat") var total_fat: Float? = java.lang.Float.NaN,
    @ColumnInfo(name = "saturated_fat") var saturated_fat: Float? = java.lang.Float.NaN,
    @ColumnInfo(name = "cholesterol") var cholesterol: Float? = java.lang.Float.NaN,
    @ColumnInfo(name = "protein") var protein: Float? = java.lang.Float.NaN,
    @ColumnInfo(name = "total_carbs") var total_carbs: Float? = java.lang.Float.NaN,
    @ColumnInfo(name = "sugar") var sugar: Float? = java.lang.Float.NaN,
    @ColumnInfo(name = "dietary_fiber") var dietary_fiber: Float? = java.lang.Float.NaN,
    @ColumnInfo(name = "salt") var salt: Float? = java.lang.Float.NaN,
    @ColumnInfo(name = "sodium") var sodium: Float? = java.lang.Float.NaN,
    @ColumnInfo(name = "potassium") var potassium: Float? = java.lang.Float.NaN,
    @ColumnInfo(name = "copper") var copper: Float? = java.lang.Float.NaN,
    @ColumnInfo(name = "iron") var iron: Float? = java.lang.Float.NaN,
    @ColumnInfo(name = "magnesium") var magnesium: Float? = java.lang.Float.NaN,
    @ColumnInfo(name = "manganese") var manganese: Float? = java.lang.Float.NaN,
    @ColumnInfo(name = "zinc") var zinc: Float? = java.lang.Float.NaN,
    @ColumnInfo(name = "phosphorous") var phosphorous: Float? = java.lang.Float.NaN,
    @ColumnInfo(name = "sulphur") var sulphur: Float? = java.lang.Float.NaN,
    @ColumnInfo(name = "chloro") var chloro: Float? = java.lang.Float.NaN,
    @ColumnInfo(name = "fluoric") var fluoric: Float? = java.lang.Float.NaN,
    @ColumnInfo(name = "vitamin_b1") var vitamin_b1: Float? = java.lang.Float.NaN,
    @ColumnInfo(name = "vitamin_b12") var vitamin_b12: Float? = java.lang.Float.NaN,
    @ColumnInfo(name = "vitamin_b6") var vitamin_b6: Float? = java.lang.Float.NaN,
    @ColumnInfo(name = "vitamin_c") var vitamin_c: Float? = java.lang.Float.NaN,
    @ColumnInfo(name = "vitamin_d") var vitamin_d: Float? = java.lang.Float.NaN,
    @ColumnInfo(name = "vitamin_e") var vitamin_e: Float? = java.lang.Float.NaN,
    @ColumnInfo(name = "relevance") var relevance: Float = 1f,
    @Ignore var manSt: ManufacturerDescriptionStrings = ManufacturerDescriptionStrings()
) : Parcelable {
    var name: String
        get() = "$categoryName: $nameAddition"
        set(value) {
            categoryName = value.split(": ")[0]
            nameAddition = value.split(": ")[1]
        }

    val caloriesString: String
        get() = if (calories?.isNaN() == false) {
            ""
        } else  {
            String.format(Locale.getDefault(), "%.0f", calories)
        }

    val referenceAmountString: String
        get() = if (referenceAmount?.isNaN() == false) {
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
        roomId = p.readInt()
        foodId = p.readInt()
        type = p.readInt()
        categoryId = p.readInt()
        categoryName = p.readString()
        nameAddition = p.readString()
        sourceProperties().forEach { property ->
            property.setter.call(this, p.readString())
        }
        ean = p.readLong()
        date = IsoTimeConverter().stringToOffsetDateTime(p.readString())
        lastLogged = IsoTimeConverter().stringToOffsetDateTime(p.readString())
        amount = p.readFloat()
        referenceAmount = p.readFloat()
        scalableProperties().forEach { property ->
            property.setter.call(this, p.readFloat())
        }
        relevance = p.readFloat()
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

    fun getCsvLine(): String {
        var csvLine = FoodItem::roomId.get(this).toString()
        csvLine += csvSep() + FoodItem::foodId.get(this).toString()
        csvLine += csvSep() + FoodItem::type.get(this).toString()
        csvLine += csvSep() + FoodItem::categoryId.get(this).toString()
        csvLine += csvSep() + FoodItem::categoryName.get(this)
        csvLine += csvSep() + FoodItem::nameAddition.get(this)
        sourceProperties().forEach { csvLine += csvSep() + it.getter.call(this) }
        csvLine += csvSep() + FoodItem::ean.get(this).toString()
        csvLine += csvSep() + IsoTimeConverter().offsetDateTimeToString(
                FoodItem::date.get(this))
        csvLine += csvSep() + IsoTimeConverter().offsetDateTimeToString(
                FoodItem::lastLogged.get(this))
        csvLine += csvSep() + String.format(
                Locale.ENGLISH,
                "%.5f", FoodItem::amount.get(this))
        csvLine += csvSep() + String.format(
                Locale.ENGLISH,
                "%.5f", FoodItem::referenceAmount.get(this))
        scalableProperties().forEach {
            csvLine += csvSep() + String.format(
                    Locale.ENGLISH,
                    "%.5f", it.getter.call(this))
        }
        csvLine += csvSep() + String.format(
                Locale.ENGLISH,
                "%.5f", FoodItem::relevance.get(this))
        return csvLine
    }

    fun copyToScaled(scaledAmount: Float): FoodItem {
        Log.i("amount", amount.toString())
        Log.i("reference_amount", referenceAmount.toString())
        val curentAmount: Float? = if (amount?.isNaN() == false) amount else referenceAmount
        Log.i("curentAmount", curentAmount.toString())
        Log.i("scaledAmount", scaledAmount.toString())
        val scaledFood = FoodItem(
                roomId = roomId,
                foodId = foodId,
                type = type,
                nameAddition = nameAddition,
                author = author,
                categoryId = categoryId,
                categoryName = categoryName,
                date = date,
                ean = ean,
                amount = scaledAmount,
                referenceAmount = referenceAmount,
                manufacturer = manufacturer,
                lastLogged = lastLogged,
                relevance = relevance + 1.0f
        )
        if (curentAmount?.isNaN() == false) for (property in scalableProperties()) {
            if (property.getter.call(this)?.isNaN() == false) {
                property.setter.call(scaledFood,
                        property.getter.call(this)!! / curentAmount * scaledAmount)
            }
        }
        Log.i("scaledFood.calories", scaledFood.calories.toString())
        return scaledFood
    }

    private fun nanIfNull(f: Float?): Float {
        return f ?: Float.NaN
    }

    override fun describeContents(): Int {
        return 0
    }

    override fun writeToParcel(out: Parcel, flags: Int) {
        if (roomId != null) out.writeInt(roomId!!)
        else out.writeValue(roomId)
        out.writeInt(foodId)
        out.writeInt(type)
        out.writeInt(categoryId)
        out.writeString(categoryName)
        out.writeString(nameAddition)
        sourceProperties().forEach {property ->
            out.writeString(property.getter.call(this))
        }
        out.writeLong(ean)
        out.writeString(IsoTimeConverter().offsetDateTimeToString(date))
        out.writeString(IsoTimeConverter().offsetDateTimeToString(lastLogged))
        out.writeFloat(nanIfNull(amount))
        out.writeFloat(nanIfNull(referenceAmount))
        scalableProperties().forEach { property ->
            out.writeFloat(nanIfNull(property.getter.call(this)))
        }
        out.writeFloat(relevance)
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
                referenceAmount = referenceAmount,
                relevance = relevance,
                lastLogged = lastLogged
        )
    }

    private fun setFromJSON(property: KMutableProperty<*>, json: JSONObject, check: (Any) -> Boolean) {
        if (json.isNull(property.name)) return
        if (check(json.get(property.name)))
            property.setter.call(this, json.get(property.name))
    }

    private fun setIntFromJSON(property: KMutableProperty<Int>, json: JSONObject, check: (Any) -> Boolean) {
        if (json.isNull(property.name)) return
        if (check(json.getInt(property.name)))
            property.setter.call(this, json.getInt(property.name))
    }

    private fun setFloatFromJSON(property: KMutableProperty<Float?>, json: JSONObject) {
        if (json.isNull(property.name)) return
        val doubleValue: Double = json.getDouble(property.name)
        if (doubleValue.isNaN()) return
        property.setter.call(this, doubleValue.toFloat())
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
            queryFoodItem.referenceAmount?.let { food.referenceAmount = it }
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
            if (!detailedFood.isNull("foodId"))
                food.foodId = detailedFood.getInt("foodId")
            if (!detailedFood.isNull("type"))
                food.type = detailedFood.getInt("type")
            if (!detailedFood.isNull("categoryId"))
                food.categoryId = detailedFood.getInt("categoryId")
            if (!detailedFood.isNull("ean"))
                food.ean = detailedFood.getLong("ean")
            sourceProperties().map { food.setFromJSON(it, detailedFood) {
                name -> (name as String?).isNullOrBlank() } }
            food.author?.let { if (it.removeSurrounding(" ").isEmpty()) null; else it }
            scalableProperties().map { food.setFloatFromJSON(it, detailedFood) }
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
            food.referenceAmount = (jsonFood[4] as String).toFloat()
            food.calories = (jsonFood[5] as String).toFloat()
            return food
        }

        fun scalableProperties() = arrayOf(
                FoodItem::calories,
                FoodItem::total_fat,
                FoodItem::saturated_fat,
                FoodItem::cholesterol,
                FoodItem::protein,
                FoodItem::total_carbs,
                FoodItem::sugar,
                FoodItem::dietary_fiber,
                FoodItem::salt,
                FoodItem::sodium,
                FoodItem::potassium,
                FoodItem::copper,
                FoodItem::iron,
                FoodItem::magnesium,
                FoodItem::manganese,
                FoodItem::zinc,
                FoodItem::phosphorous,
                FoodItem::sulphur,
                FoodItem::chloro,
                FoodItem::fluoric,
                FoodItem::vitamin_b1,
                FoodItem::vitamin_b12,
                FoodItem::vitamin_b6,
                FoodItem::vitamin_c,
                FoodItem::vitamin_d,
                FoodItem::vitamin_e)

        fun sourceProperties() = arrayOf(
                FoodItem::author,
                FoodItem::manufacturer)

        private fun csvSep() = ", "

        fun getCsvHeader(): String {
            var csvHeader = FoodItem::roomId.name
            csvHeader += csvSep() + FoodItem::foodId.name
            csvHeader += csvSep() + FoodItem::type.name
            csvHeader += csvSep() + FoodItem::categoryId.name
            csvHeader += csvSep() + FoodItem::categoryName.name
            csvHeader += csvSep() + FoodItem::nameAddition.name
            sourceProperties().forEach { csvHeader += csvSep() + it.name }
            csvHeader += csvSep() + FoodItem::ean.name
            csvHeader += csvSep() + FoodItem::date.name
            csvHeader += csvSep() + FoodItem::lastLogged.name
            csvHeader += csvSep() + FoodItem::amount.name
            csvHeader += csvSep() + FoodItem::referenceAmount.name
            scalableProperties().forEach { csvHeader += csvSep() + it.name }
            csvHeader += csvSep() + FoodItem::relevance.name
            return csvHeader
        }
    }
}
