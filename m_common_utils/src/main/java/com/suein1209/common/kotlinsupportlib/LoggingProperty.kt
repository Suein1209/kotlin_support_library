@file:Suppress("unused")

package com.suein1209.common.kotlinsupportlib

import android.util.Log
import kotlin.reflect.KProperty

/**
 * kotlin property 의 쓰기/읽기를 로깅한다.
 * - 이펙티브 코틀린 참고
 * by suein1209
 */
class LoggingProperty<T>(private var value: T) {
    operator fun getValue(thisRef: Any?, prop: KProperty<*>): T {
        if (BuildConfig.DEBUG) {
            Log.d("LOGGING_PROPERTY", "${prop.name} returned value [$value]")
        }
        return value
    }

    operator fun setValue(thisRef: Any?, prop: KProperty<*>, newValue: T) {
        val name = prop.name
        if (BuildConfig.DEBUG) {
            Log.d("LOGGING_PROPERTY", "$name changed from [$value] to [$newValue]")
        }
        value = newValue
    }
}