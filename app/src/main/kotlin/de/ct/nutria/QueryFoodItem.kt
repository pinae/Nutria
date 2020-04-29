package de.ct.nutria

import androidx.room.*
import java.time.OffsetDateTime

@TypeConverters(IsoTimeConverter::class)
@Entity(tableName = "food", indices = [Index(value = ["combinedName"])])
data class QueryFoodItem(
        @PrimaryKey @ColumnInfo(name = "idInNutriaDB") val foodId: Int,
        val isRecipe: Boolean,
        val categoryId: Int,
        @ColumnInfo(name = "combinedName") val name: String?,
        val calories: Float?,
        val source: String?,
        val ean: Long?,
        val referenceAmount: Float?,
        val relevance: Float = 1f,
        val lastLogged: OffsetDateTime?
)