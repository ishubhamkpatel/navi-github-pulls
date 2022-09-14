package com.navi.utils

import android.icu.text.SimpleDateFormat
import androidx.annotation.StringDef
import java.time.OffsetDateTime
import java.time.format.DateTimeFormatter
import java.util.*

@Retention(AnnotationRetention.RUNTIME)
@Target(
    AnnotationTarget.CLASS,
    AnnotationTarget.FUNCTION,
    AnnotationTarget.FIELD,
    AnnotationTarget.LOCAL_VARIABLE,
    AnnotationTarget.VALUE_PARAMETER,
    AnnotationTarget.TYPE_PARAMETER,
    AnnotationTarget.TYPE
)
@StringDef(DateFormat.yyyy_MM_dd_T_HH_mm_ss_Z, DateFormat.MMM_dd_yyyy_HH_mm)
annotation class DateFormat {
    companion object {
        const val yyyy_MM_dd_T_HH_mm_ss_Z = "yyyy-MM-dd'T'HH:mm:ss'Z'"
        const val MMM_dd_yyyy_HH_mm = "MMM dd, yyyy HH:mm"
    }
}

fun String.parseDateAndTime(
    @DateFormat inputFormat: String,
    @DateFormat outputFormat: String
): String = kotlin.runCatching {
    val dateTime = OffsetDateTime.parse(this).toZonedDateTime()
    val formatter = DateTimeFormatter.ofPattern(outputFormat)
    return@runCatching dateTime.format(formatter)
}.getOrDefault(parse(inputFormat, outputFormat))

private fun String.parse(
    @DateFormat inputFormat: String,
    @DateFormat outputFormat: String
): String = try {
    val locale = Locale.getDefault()
    val sourceSdf = SimpleDateFormat(inputFormat, locale)
    val requiredSdf = SimpleDateFormat(outputFormat, locale)
    requiredSdf.format(sourceSdf.parse(this))
} catch (e: Exception) {
    this
}