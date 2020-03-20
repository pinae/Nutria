package de.ct.nutria

import androidx.room.*
import java.time.OffsetDateTime
import java.time.format.DateTimeFormatter

class IsoTimeConverter {
    @TypeConverter
    fun stringToOffsetDateTime(value: String?): OffsetDateTime? {
            if (value == null) return null
            return OffsetDateTime.parse(value, DateTimeFormatter.ISO_OFFSET_DATE_TIME)
    }

    @TypeConverter
    fun offsetDateTimeToString(value: OffsetDateTime?): String? {
        if (value == null) return null
        return value.format(DateTimeFormatter.ISO_OFFSET_DATE_TIME)
    }
}

@TypeConverters(IsoTimeConverter::class)
@Fts4
@Entity(tableName = "food")
data class RoomFoodItem(
        @PrimaryKey @ColumnInfo(name = "rowid") val id: Int,
        @ColumnInfo(name = "isRecipe") val isRecipe: Boolean,
        @ColumnInfo(name = "categoryId") val categoryId: Long,
        @ColumnInfo(name = "name") val name: String?,
        @ColumnInfo(name = "calories") val calories: Float?,
        @ColumnInfo(name = "source") val source: String?,
        @ColumnInfo(name = "ean") val ean: Long?,
        @ColumnInfo(name = "referenceAmount") val referenceAmount: Float?,
        @ColumnInfo(name = "relevance") val relevance: Float = 1f,
        @ColumnInfo(name = "lastLogged") val lastLogged: OffsetDateTime?
)