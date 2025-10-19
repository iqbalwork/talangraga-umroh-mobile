package com.talangraga.umrohmobile.util

fun Int.formatToIDR(): String {
    val amountString = this.toString()
    val reversedAmount = amountString.reversed()
    val stringBuilder = StringBuilder()

    for (i in reversedAmount.indices) {
        stringBuilder.append(reversedAmount[i])
        if ((i + 1) % 3 == 0 && (i + 1) != reversedAmount.length) {
            stringBuilder.append('.')
        }
    }
    return "Rp ${stringBuilder.reverse()}"
}