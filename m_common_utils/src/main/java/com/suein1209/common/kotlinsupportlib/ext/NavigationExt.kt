@file:Suppress("unused")

package com.suein1209.common.kotlinsupportlib.ext

import androidx.activity.OnBackPressedCallback
import androidx.fragment.app.Fragment
import androidx.navigation.NavDirections
import androidx.navigation.fragment.findNavController

fun Fragment.clearCurrentStackSavedStateValues() {
    val savedStateHandle = this.findNavController().currentBackStackEntry?.savedStateHandle
    savedStateHandle ?: return
    savedStateHandle.keys().forEach { key ->
        savedStateHandle[key] = null
    }
}

fun Fragment.removeCurrentStackSavedStateValue(key: String) {
    this.findNavController().currentBackStackEntry?.savedStateHandle?.set(key, null)
}

fun Fragment.setBackStackSavedStateValue(key: String, value: Any?, popStack: Boolean = false) {
    this.findNavController().previousBackStackEntry?.savedStateHandle?.set(key, value)
    if (popStack) {
        this.findNavController().popBackStack()
    }
}

fun <DATA_TYPE> Fragment.getCurrentStackSavedSateValue(key: String): DATA_TYPE? {
    return this.findNavController().currentBackStackEntry?.savedStateHandle?.get<DATA_TYPE>(key)
}

fun <DATA_TYPE> Fragment.getCurrentStackSavedSateValueAndRemoveKey(key: String): DATA_TYPE? {
    val retValue = this.findNavController().currentBackStackEntry?.savedStateHandle?.get<DATA_TYPE>(key)
    removeCurrentStackSavedStateValue(key)
    return retValue
}

fun Fragment.setOnBackPressDispatcher(callback: OnBackPressedCallback) {
    requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner, callback)
}

fun NavDirections.goWith(fragment: Fragment) {
    fragment.findNavController().navigate(this)
}