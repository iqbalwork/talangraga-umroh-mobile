package com.talangraga.shared

import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.withStyle

//fun Double.formatToIDR(): String {
//    val amountString = this.toString()
//    val reversedAmount = amountString.reversed()
//    val stringBuilder = StringBuilder()
//
//    for (i in reversedAmount.indices) {
//        stringBuilder.append(reversedAmount[i])
//        if ((i + 1) % 3 == 0 && (i + 1) != reversedAmount.length) {
//            stringBuilder.append('.')
//        }
//    }
//    return "Rp ${stringBuilder.reverse()}"
//}

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

fun String.mandatory(): AnnotatedString {
    return buildAnnotatedString {
        append(this@mandatory)
        withStyle(style = SpanStyle(color = Red)) {
            append("*")
        }
    }
}
