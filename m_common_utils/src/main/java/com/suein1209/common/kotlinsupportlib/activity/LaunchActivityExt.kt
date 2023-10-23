@file:Suppress("unused")

package com.suein1209.common.kotlinsupportlib.activity

import android.content.Context
import android.content.Intent
import androidx.activity.result.ActivityResult
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.Fragment


interface SimpleLaunchActivityForResult {
    var simpleDummyActivityForResult: ActivityResultLauncher<Intent>
    var callbackActivityForResult: ((ActivityResult) -> Unit)?
    fun Fragment.getInitSimpleResultHub(): ActivityResultLauncher<Intent> {
        return registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result: ActivityResult ->
            callbackActivityForResult?.invoke(result)
        }
    }
}

/**
 * Intent 생성 없이 바로 Start Activity for result
 */
inline fun <reified T : Any> SimpleLaunchActivityForResult.launchSimpleActivity(context: Context, vararg params: Pair<String, Any?>, isSingleTop: Boolean = false, isClearTop: Boolean = false, crossinline callback: (ActivityResult) -> Unit) {
    callbackActivityForResult = { rest ->
        callback.invoke(rest)
        callbackActivityForResult = null
    }
    val tempIntent = AnkoStartActivityInternals.createIntent(context, T::class.java, params)
    if (isSingleTop) tempIntent.singleTop()
    if (isClearTop) tempIntent.clearTop()
    simpleDummyActivityForResult.launch(tempIntent)
}

/**
 * Intent를 통해 Start Activity for result
 * - context가 불필요 하다.
 */
inline fun SimpleLaunchActivityForResult.launchSimpleActivity(intent: Intent, crossinline callback: (ActivityResult) -> Unit) {
    callbackActivityForResult = { rest ->
        callback.invoke(rest)
        callbackActivityForResult = null
    }
    simpleDummyActivityForResult.launch(intent)
}