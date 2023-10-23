@file:Suppress("unused")

package com.suein1209.common.kotlinsupportlib.ext

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch

inline fun <T> ViewModel.launchAsync(
    crossinline execute: suspend () -> Flow<T>,
    crossinline onSuccess: (Flow<T>) -> Unit,
    crossinline onException: (e: Exception) -> Unit = { _ -> }
) {
    viewModelScope.launch {
        try {
            val result = execute()
            onSuccess(result)
        } catch (ex: Exception) {
            onException.invoke(ex)
        }
    }
}

/**
 * Delay 를 두고 coroutine 을 실행한다.
 */
fun ViewModel.launchAsyncDelay(delayMillis: Long = 300, launchFunc: suspend () -> Unit) {
    viewModelScope.launch {
        delay(delayMillis)
        launchFunc.invoke()
    }
}