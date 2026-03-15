package com.talangraga.umrohmobile.ui.utils

fun Double.formatCurrency(): String {
    val text = this.toLong().toString()
    if (text.isEmpty()) return "Rp 0"
    val reversed = text.reversed()
    val formatted = StringBuilder()
    for (i in reversed.indices) {
        formatted.append(reversed[i])
        if ((i + 1) % 3 == 0 && i != reversed.lastIndex) {
            formatted.append(".")
        }
    }
    return "Rp " + formatted.reverse().toString()
}
