package de.ct.nutria

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.Embedded
import androidx.room.Relation

@Entity
data class RoomFoodCategory(
        @PrimaryKey val uid: Long,
        @ColumnInfo(name = "name") val name: String?
)

@Entity
data class RoomFoodItem(
        @PrimaryKey val uid: Long,
        val categoryId: Long,
        @ColumnInfo(name = "nameAddition") val nameAddition: String?,
        @ColumnInfo(name = "calories") val calories: Float?,
        @ColumnInfo(name = "manufacturer") val manufacturer: String?,
        @ColumnInfo(name = "ean") val ean: Long?,
        @ColumnInfo(name = "referenceAmount") val referenceAmount: Float?
)

data class RoomFoodItemWithCategory(
        @Embedded val category: RoomFoodCategory,
        @Relation(
                parentColumn = "uid",
                entityColumn = "categoryId"
        )
        val foods: List<RoomFoodItem>
)