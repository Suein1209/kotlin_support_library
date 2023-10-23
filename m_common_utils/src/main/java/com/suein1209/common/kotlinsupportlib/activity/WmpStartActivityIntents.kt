@file:Suppress("NOTHING_TO_INLINE", "unused")

package com.suein1209.common.kotlinsupportlib.activity

import android.app.Activity
import android.content.Context
import android.content.Intent
import androidx.fragment.app.Fragment

inline fun <reified T : Activity> Context.startActivity(vararg params: Pair<String, Any?>) = AnkoStartActivityInternals.internalStartActivity(this, T::class.java, params)
inline fun <reified T : Activity> Fragment.startActivity(vararg params: Pair<String, Any?>) = AnkoStartActivityInternals.internalStartActivity(requireActivity(), T::class.java, params)
inline fun <reified T : Any> Context.intentFor(vararg params: Pair<String, Any?>) = AnkoStartActivityInternals.createIntent(this, T::class.java, params)
inline fun <reified T : Any> Fragment.intentFor(vararg params: Pair<String, Any?>) = AnkoStartActivityInternals.createIntent(requireActivity(), T::class.java, params)

/**
 * Add the [Intent.FLAG_ACTIVITY_CLEAR_TASK] flag to the [Intent].
 *
 * @return the same intent with the flag applied.
 */
inline fun Intent.clearTask(): Intent = apply { addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK) }

/**
 * Add the [Intent.FLAG_ACTIVITY_CLEAR_TOP] flag to the [Intent].
 *
 * @return the same intent with the flag applied.
 */
inline fun Intent.clearTop(): Intent = apply { addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP) }

/**
 * Add the [Intent.FLAG_ACTIVITY_CLEAR_WHEN_TASK_RESET] flag to the [Intent].
 *
 * @return the same intent with the flag applied.
 */
@Deprecated(message = "Deprecated in Android", replaceWith = ReplaceWith("org.jetbrains.anko.newDocument"))
inline fun Intent.clearWhenTaskReset(): Intent = apply {
    @Suppress("DEPRECATION")
    addFlags(Intent.FLAG_ACTIVITY_CLEAR_WHEN_TASK_RESET)
}

/**
 * Add the [Intent.FLAG_ACTIVITY_NEW_DOCUMENT] flag to the [Intent].
 *
 * @return the same intent with the flag applied.
 */
inline fun Intent.newDocument(): Intent = apply {
    addFlags(Intent.FLAG_ACTIVITY_NEW_DOCUMENT)
}

/**
 * Add the [Intent.FLAG_ACTIVITY_EXCLUDE_FROM_RECENTS] flag to the [Intent].
 *
 * @return the same intent with the flag applied.
 */
inline fun Intent.excludeFromRecents(): Intent = apply { addFlags(Intent.FLAG_ACTIVITY_EXCLUDE_FROM_RECENTS) }

/**
 * Add the [Intent.FLAG_ACTIVITY_MULTIPLE_TASK] flag to the [Intent].
 *
 * @return the same intent with the flag applied.
 */
inline fun Intent.multipleTask(): Intent = apply { addFlags(Intent.FLAG_ACTIVITY_MULTIPLE_TASK) }

/**
 * Add the [Intent.FLAG_ACTIVITY_NEW_TASK] flag to the [Intent].
 *
 * @return the same intent with the flag applied.
 */
inline fun Intent.newTask(): Intent = apply { addFlags(Intent.FLAG_ACTIVITY_NEW_TASK) }

/**
 * Add the [Intent.FLAG_ACTIVITY_NO_ANIMATION] flag to the [Intent].
 *
 * @return the same intent with the flag applied.
 */
inline fun Intent.noAnimation(): Intent = apply { addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION) }

/**
 * Add the [Intent.FLAG_ACTIVITY_NO_HISTORY] flag to the [Intent].
 *
 * @return the same intent with the flag applied.
 */
inline fun Intent.noHistory(): Intent = apply { addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY) }

/**
 * Add the [Intent.FLAG_ACTIVITY_SINGLE_TOP] flag to the [Intent].
 *
 * @return the same intent with the flag applied.
 */
inline fun Intent.singleTop(): Intent = apply { addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP) }