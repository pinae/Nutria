package de.ct.nutria

import androidx.room.*
import java.time.OffsetDateTime

@TypeConverters(IsoTimeConverter::class)
@Entity(tableName = "food", indices = [Index(value = ["name"])])
data class QueryFoodItem(
        @PrimaryKey @ColumnInfo(name = "foodId") val foodId: Int,
        @ColumnInfo(name = "isRecipe") val isRecipe: Boolean,
        @ColumnInfo(name = "categoryId") val categoryId: Int,
        @ColumnInfo(name = "name") val name: String?,
        @ColumnInfo(name = "calories") val calories: Float?,
        @ColumnInfo(name = "source") val source: String?,
        @ColumnInfo(name = "ean") val ean: Long?,
        @ColumnInfo(name = "referenceAmount") val referenceAmount: Float?,
        @ColumnInfo(name = "relevance") val relevance: Float = 1f,
        @ColumnInfo(name = "lastLogged") val lastLogged: OffsetDateTime?
)