package com.natasaandzic.moviedatabase.data

import android.os.Build
import androidx.annotation.RequiresApi
import java.time.LocalDate
import java.time.format.TextStyle
import java.util.Locale

enum class Filter {
    ALL,
    CURRENT_MONTH,
    NEXT_MONTH;

    @RequiresApi(Build.VERSION_CODES.O)
    fun getLabel(today: LocalDate = LocalDate.now()): String {
        return when (this) {
            ALL -> "All"
            CURRENT_MONTH -> today.month.getDisplayName(TextStyle.FULL, Locale.getDefault())
            NEXT_MONTH -> today.plusMonths(1).month.getDisplayName(TextStyle.FULL, Locale.getDefault())
        }
    }
}

enum class RatingFilter(val label: String, val minRating: Float) {
    ALL("All", 0f),
    STAR_8("⭐ 8", 8f),
    STAR_7("⭐ 7", 7f),
    STAR_6("⭐ 6", 6f),
    STAR_5("⭐ 5", 5f),
    STAR_4("⭐ 4", 4f),
    STAR_3("⭐ 3", 3f),
    STAR_2("⭐ 2", 2f),
    STAR_1("⭐ 1", 1f),
}

