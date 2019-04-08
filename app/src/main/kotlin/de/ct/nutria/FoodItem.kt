package de.ct.nutria

import android.os.Parcel
import android.os.Parcelable

import java.util.Calendar
import java.util.Date
import java.util.Locale

class FoodItem : Parcelable {
    var type: Int = 0
    var id: ByteArray? = null
    private var nameAddition: String? = null
    var authorName: String? = null
    var categoryId: Int = 0
        private set
    var categoryName: String? = null
        private set
    var date: Date? = null
        private set
    var ean: ByteArray? = null
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

    val name: String
        get() = "$categoryName: $nameAddition"

    val caloriesString: String
        get() = if (java.lang.Float.isNaN(calories)) {
            ""
        } else {
            String.format(Locale.getDefault(), "%.0f", calories)
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
        p.readByteArray(id)
        nameAddition = p.readString()
        authorName = p.readString()
        categoryId = p.readInt()
        categoryName = p.readString()
        date = Date()
        date!!.time = p.readLong()
        p.readByteArray(ean)
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

    fun setCategory(id: Int, categoryName: String) {
        this.categoryId = id
        this.categoryName = categoryName
    }

    fun setCreationDate(newDate: Date) {
        this.date = newDate
    }

    fun describeManufacturer(recipeBy: String, selfmade: String, harvested: String): String {
        return when(type) {
            0 -> {
                if (manufacturer != null && authorName != null && authorName!!.isNotEmpty()) manufacturer!!
                else harvested
            }
            1 -> {
                if (authorName != null && authorName!!.isNotEmpty()) recipeBy + authorName!!
                else selfmade
            }
            else -> ""
        }
    }

    override fun describeContents(): Int {
        return 0
    }

    override fun writeToParcel(out: Parcel, flags: Int) {
        out.writeInt(type)
        out.writeByteArray(id)
        out.writeString(nameAddition)
        out.writeString(authorName)
        out.writeInt(categoryId)
        out.writeString(categoryName)
        out.writeLong(date!!.time)
        out.writeByteArray(ean)
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
    }
}
