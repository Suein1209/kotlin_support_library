@file:Suppress("unused")

package com.suein1209.common.kotlinsupportlib

import android.os.Build

/**
 * instead of if (Build.VERSION.SDK_INT < 16) { /* handle devices using older APIs */ }
 * - toApi(16) {
 *      handle devices running older APIs
 * }
 *
 * instead of if (Build.VERSION.SDK_INT <= 16) { /* handle devices using older APIs */ }
 * - toApi(16, inclusive = true) {
 *      handle devices running older APIs
 *  }
 */
inline fun toApi(toVersion: Int, inclusive: Boolean = false, action: () -> Unit) {
    if (Build.VERSION.SDK_INT < toVersion || (inclusive && Build.VERSION.SDK_INT == toVersion)) action()
}

/**
 * instead of if (Build.VERSION.SDK_INT >= 21) { /* run methods available since API 21 */ }
 * - fromApi(21) {
 *      run methods available since API 21
 *  }
 */
inline fun fromApi(fromVersion: Int, inclusive: Boolean = true, action: () -> Unit) {
    if (Build.VERSION.SDK_INT > fromVersion || (inclusive && Build.VERSION.SDK_INT == fromVersion)) action()
}