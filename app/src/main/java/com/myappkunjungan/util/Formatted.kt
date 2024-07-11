package com.myappkunjungan.util

import android.os.Build
import androidx.annotation.RequiresApi
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.time.format.TextStyle
import java.util.Locale

@RequiresApi(Build.VERSION_CODES.O)
fun convertDate(inputDate: String): String {
    val localDate = LocalDate.parse(inputDate)
    val dayOfWeek = localDate.dayOfWeek.getDisplayName(TextStyle.FULL, Locale("id", "ID"))
    val formatter = DateTimeFormatter.ofPattern("dd MMMM yyyy", Locale("id", "ID"))
    val formattedDate = localDate.format(formatter)

    return "$dayOfWeek, $formattedDate"
}
