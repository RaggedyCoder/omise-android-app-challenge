package co.omise.android.charity.util

import java.text.NumberFormat
import java.util.*

fun Int.toFormattedCurrency(locale: Locale = Locale.getDefault()): String {
    val numberFormat = NumberFormat.getCurrencyInstance(locale)
    return numberFormat.format(this)
}

fun Float.toFormattedCurrency(locale: Locale = Locale.getDefault()): String {
    val numberFormat = NumberFormat.getCurrencyInstance(locale)
    return numberFormat.format(this)
}

fun Double.toFormattedCurrency(locale: Locale = Locale.getDefault()): String {
    val numberFormat = NumberFormat.getCurrencyInstance(locale)
    return numberFormat.format(this)
}

fun String.toFormattedCurrency(locale: Locale = Locale.getDefault()): String {
    val numberFormat = NumberFormat.getCurrencyInstance(locale)
    return numberFormat.format(this)
}

