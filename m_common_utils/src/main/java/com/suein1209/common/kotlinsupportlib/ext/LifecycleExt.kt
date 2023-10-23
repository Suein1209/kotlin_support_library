@file:Suppress("unused")

package com.suein1209.common.kotlinsupportlib.ext

import androidx.fragment.app.Fragment
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.LiveData
import androidx.lifecycle.Observer
import androidx.lifecycle.lifecycleScope

fun <T> LifecycleOwner.observe(liveData: LiveData<T>?, observer: (T) -> Unit) =
    liveData?.observe(this, Observer(observer))

@Suppress("DEPRECATION")
fun Fragment.launchOnLifecycleScope(execute: suspend () -> Unit) {
    viewLifecycleOwner.lifecycleScope.launchWhenCreated {
        execute()
    }
}