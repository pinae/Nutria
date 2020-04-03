package de.ct.nutria
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.Arguments
import org.junit.jupiter.params.provider.MethodSource

class GetIconTest {
    @Test
    fun `returns default icon id`() {
        assertEquals(getIcon(-1), R.drawable.ic_tupperdose)
    }

    @ParameterizedTest
    @MethodSource("getIconProvider")
    fun `returns correct darwable id for a given category id`(
            category_id: Int, expected_drawable_id: Int) {
        assertEquals(getIcon(category_id), expected_drawable_id)
    }

    companion object {
        @JvmStatic
        fun getIconProvider() = listOf(
                Arguments.of(1, R.drawable.ic_category_1),
                Arguments.of(2, R.drawable.ic_category_2),
                Arguments.of(3, R.drawable.ic_category_3),
                Arguments.of(4, R.drawable.ic_category_4),
                Arguments.of(5, R.drawable.ic_category_5),
                Arguments.of(6, R.drawable.ic_category_6),
                Arguments.of(7, R.drawable.ic_category_7)
        )
    }
}