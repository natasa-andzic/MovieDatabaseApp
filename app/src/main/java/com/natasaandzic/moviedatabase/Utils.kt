package com.natasaandzic.moviedatabase

import android.os.Build
import androidx.annotation.RequiresApi
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.Locale

@RequiresApi(Build.VERSION_CODES.O)
fun formatReleaseDate(rawDate: String?): String {
    return try {
        val inputFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd", Locale.ENGLISH)
        val outputFormatter = DateTimeFormatter.ofPattern("dd. MMMM yyyy", Locale.ENGLISH)
        val date = LocalDate.parse(rawDate, inputFormatter)
        date.format(outputFormatter)
    } catch (e: Exception) {
        rawDate ?: ""
    }
}
