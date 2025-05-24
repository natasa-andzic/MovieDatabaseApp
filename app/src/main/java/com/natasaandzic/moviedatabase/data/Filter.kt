package com.natasaandzic.moviedatabase.data

enum class Filter {
    ALL,
    THIS_MONTH,
    NEXT_30_DAYS
}

enum class RatingFilter(val label: String, val minRating: Float) {
    ALL("All", 0f),
    STAR_8("⭐ 8+", 8f),
    STAR_7("⭐ 7+", 7f),
    STAR_6("⭐ 6+", 6f)
}

