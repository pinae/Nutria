package de.ct.nutria

fun getIcon(categoryId :Int) :Int {
    return when (categoryId) {
        1 -> R.drawable.ic_category_1
        2 -> R.drawable.ic_category_2
        3 -> R.drawable.ic_category_3
        4 -> R.drawable.ic_category_4
        5 -> R.drawable.ic_category_5
        6 -> R.drawable.ic_category_6
        7 -> R.drawable.ic_category_7
        else -> R.drawable.ic_tupperdose
    }
}