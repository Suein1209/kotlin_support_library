@file:Suppress("unused")

package com.suein1209.common.kotlinsupportlib

import android.app.Activity
import android.content.Context
import androidx.fragment.app.Fragment

/**
 * 현재 Activity 혹은 Fragment가 active 상태인지 체크한다.
 */
fun <T> isContextAlive(context: T?): Boolean {
    return when (context) {
        null -> false
        is Activity -> !context.isFinishing
        is Fragment -> context.isAdded
        else -> true
    }
}

/**
 * Activity 혹은 Fragment가 active 상태 일대 실행 되는 action block을 반환한다.
 */
inline fun <T> T.runFunctionIfAlive(crossinline action: () -> Unit): () -> Unit {
    return { runIfAlive(action) }
}

/**
 * Activity 혹은 Fragment가 active 상태 일대 실행 되는 action block 실행한다.
 */
inline fun <T> T.runIfAlive(crossinline action: () -> Unit) {
    if (isContextAlive(this)) {
        action()
    }
}

/**
 * Navigation 용
 */
inline fun Fragment.runIfAliveFragment(crossinline elze: () -> Unit = {}, crossinline action: () -> Unit) {
    if (isAliveFragment()) {
        action()
    } else elze.invoke()
}

fun Fragment.isAliveFragment() = isAdded

fun Int.asColor(context: Context) =
    kotlin.runCatching {
        context.resources.getColor(this, null)
    }.getOrNull() ?: -1