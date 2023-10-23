package com.suein1209.common.kotlinsupportlib

@Suppress("unused")
fun Float.toFloatRound(roundPosition: Int): Float {
    return kotlin.runCatching {
        String.format("%.${roundPosition}f", this).toFloat()
    }.getOrNull() ?: this
}