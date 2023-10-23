@file:Suppress("unused")

package com.suein1209.common.kotlinsupportlib.ext

import android.app.Activity
import android.graphics.BitmapFactory
import android.graphics.Color
import android.graphics.Paint
import android.graphics.PointF
import android.view.View
import android.view.ViewGroup
import android.view.animation.Animation
import android.widget.TextView
import androidx.annotation.IdRes
import androidx.recyclerview.widget.RecyclerView
import java.io.File
import java.util.*
import kotlin.contracts.ExperimentalContracts
import kotlin.contracts.contract

/**
 *  val tempBool: Boolean = false
 *  tempBool then {
 *      // tempBool == true
 *  } elze {
 *      // tempBool == false
 *  }
 */
infix fun Boolean.then(action: () -> Unit): Boolean {
    if (this)
        action.invoke()
    return this
}

/**
 *  val tempBool: Boolean = false
 *  tempBool then {
 *      // tempBool == true
 *  } elze {
 *      // tempBool == false
 *  }
 */
infix fun Boolean.elze(action: () -> Unit): Boolean {
    if (!this)
        action.invoke()
    return this
}

/**
 *  val tempString : String? = null
 *  tempString ifNull {
 *      // tempString == null
 *  } ifNotNull {
 *      // tempString != null
 *  }
 */
infix fun <T : Any> T?.ifNull(action: () -> Unit): T? {
    if (this == null)
        action.invoke()
    return this
}

/**
 *  val tempString : String? = null
 *  tempString ifNull {
 *      // tempString == null
 *  } ifNotNull {
 *      // tempString != null
 *  }
 */
infix fun <T : Any> T?.ifNotNull(action: (T) -> Unit): T? {
    if (this != null) {
        action(this)
    }
    return this
}

/**
 *  checkNotNullSafety(tempString){
 *      // tempString == null
 *  }?.let {
 *      // tempString(it) != null
 *  }
 */
inline fun <T : Any> checkNotNullSafety(value: T?, nullCallBack: () -> Unit): T? {
    return if (value == null) {
        nullCallBack.invoke()
        null
    } else {
        value
    }
}

/**
 * String이 Empty 가 아닌지 체크 한다.
 */
fun String.isNotEmpty(action: (String) -> Unit) {
    if (this.isNotEmpty()) {
        action.invoke(this)
    }
}

/**
 * String이 Null or Empty 가 아닌지 체크 한다.
 * - contract 적용
 */
@OptIn(ExperimentalContracts::class)
fun String?.isNotNullEmpty(): Boolean {
    contract {
        returns(true) implies (this@isNotNullEmpty != null)
    }
    return this != null && length > 0
}

/**
 * List가 Null이 아니고 List의 갯수가 0개 이상 인지 체크한다.
 * - contract 적용
 */
@OptIn(ExperimentalContracts::class)
fun <T> List<T>?.isNotNullEmpty(): Boolean {
    contract {
        returns(true) implies (this@isNotNullEmpty != null)
    }
    return this != null && size > 0
}

/**
 * String이 null이 아니고 Empty가 아니라면 callback block의 처리를 한다.
 *
 *  val tempString : String? = null
 *  tempString.isNotNullEmpty { notNullOrEmptyString ->
 *      // notNullOrEmptyString != null or notNullOrEmptyString.length > 0
 *  }
 */
inline fun String?.isNotNullEmpty(action: (String) -> Unit) {
    if (!this.isNullOrEmpty()) {
        action.invoke(this)
    }
}

/**
 * String이 null이거나 Empty 라면 callBack return 값으로 대체 한다.
 *
 *  val tempDealId : String = npDeal.dealId?.ifNullEmpty { "0"}
 */
inline fun String?.ifNullOrEmpty(action: () -> String): String {
    return if (this.isNullOrEmpty()) {
        action.invoke()
    } else {
        this
    }
}

/**
 * String이 null이 아니고 Empty가 아니라면 callback block의 처리를 한다.
 *
 *  val tempList : List<String>? = listOf()
 *  tempList.isNotNullEmpty { notEmptyList ->
 *      // notEmptyList != null or notEmptyList.size > 0
 *  }
 */
inline fun <T : Collection<*>> T?.isNotNullEmpty(action: (T) -> Unit) {
    if (!this.isNullOrEmpty()) {
        action.invoke(this)
    }
}

/**
 * MutableMap null이 아니고 Empty가 아니라면 callback block의 처리를 한다.
 */
inline fun <K, V> MutableMap<K, V>?.isNotNullEmpty(action: (MutableMap<K, V>) -> Unit) {
    if (!this.isNullOrEmpty()) {
        action.invoke(this)
    }
}

/**
 * 이미지의 가로 / 세로 이미지 픽셀 을 반환한다.
 */
fun File.getImagePixelSize(): PointF {
    val fileName = this.name.lowercase(Locale.getDefault())
    return if (fileName.contains(".jpeg") || fileName.contains(".jpg") || fileName.contains(".png")) {
        val options = BitmapFactory.Options().also { it.inJustDecodeBounds = true }
        BitmapFactory.decodeFile(path, options)
        PointF(options.outWidth.toFloat(), options.outHeight.toFloat())
    } else {
        PointF(0F, 0F)
    }
}

/**
 *  Color String을 Integer 값으로 변환한다.
 *
 *  val colorHex = "#010203"
 *  val color = colorHex.asColor // -16711165
 *  val nonColorHex = "abcdef"
 *  val nonColor = nonColorHex.asColor // null
 */
val String.asColor: Int?
    get() = runCatching { Color.parseColor(this) }.getOrNull()

/**
 * HashMap 체크
 */
inline fun <K, V> MutableMap<K, V>?.isCheckOnce(k: K, v: V, action: () -> Unit) {
    if (this?.containsKey(k) == false) {
        this[k] = v
        action.invoke()
    }
}

/**
 * TextView 취소선 처리
 */
fun TextView.strikeThrough() {
    this.paintFlags = this.paintFlags or Paint.STRIKE_THRU_TEXT_FLAG
}

/**
 * 찾게된 첫번째 인스턴스를 반환한다.
 */
inline fun <reified R> Iterable<*>.findIsInstance(): R? {
    return this.find { it is R } as? R
}

/**
 * 넘겨받은 인스턴스를 제외한 인스턴스 리스트를 반환한다.
 */
inline fun <reified R> Iterable<*>.filterIsNotInstance(): List<R> {
    @Suppress("UNCHECKED_CAST")
    return this.filter { it !is R }.toList() as List<R>
}

/**
 * setAnimationListener KTX
 */
inline fun Animation.setAnimationListener(
    crossinline animationStart: (
        animation: Animation?
    ) -> Unit = { _ -> },
    crossinline animationEnd: (
        animation: Animation?
    ) -> Unit = { _ -> },
    crossinline animationRepeat: (
        animation: Animation?
    ) -> Unit = { _ -> }
): Animation.AnimationListener {
    val listener = object : Animation.AnimationListener {
        override fun onAnimationStart(animation: Animation?) {
            animationStart.invoke(animation)
        }

        override fun onAnimationEnd(animation: Animation?) {
            animationEnd.invoke(animation)
        }

        override fun onAnimationRepeat(animation: Animation?) {
            animationRepeat.invoke(animation)
        }
    }
    this.setAnimationListener(listener)
    return listener
}

/**
 * 가로 리스트 UI를 구현하다 보면 첫번째 혹은 마지막에 끝 마진의 값을 다르게 진행하는 경우가 있다.
 * 이 부분을 커버하기 위한 메소드이다.
 */
fun View.setRecyclerViewHorizontalItemMargin(edgeMargin: Int, betweenMargin: Int, position: Int, itemCount: Int) {
    if (layoutParams is RecyclerView.LayoutParams) {
        (layoutParams as RecyclerView.LayoutParams).apply {
            when (position) {
                0 -> {
                    marginStart = edgeMargin
                    marginEnd = betweenMargin
                }

                (itemCount - 1) -> {
                    marginStart = betweenMargin
                    marginEnd = edgeMargin
                }

                else -> {
                    marginStart = betweenMargin
                    marginEnd = betweenMargin
                }
            }
        }
    } else {
        throw IllegalAccessException("Parent 가 RecyclerView가 아니에요.")
    }
}

fun View.setRecyclerViewVerticalItemMargin(edgeMargin: Int, betweenMargin: Int, position: Int, itemCount: Int) {
    if (layoutParams is RecyclerView.LayoutParams) {
        val marginLayoutParam = ViewGroup.MarginLayoutParams(layoutParams)
        val margins = when (position) {
            0 -> Pair(edgeMargin, betweenMargin)
            (itemCount - 1) -> Pair(betweenMargin, edgeMargin)
            else -> Pair(betweenMargin, betweenMargin)
        }
        marginLayoutParam.setMargins(marginLayoutParam.leftMargin, margins.first, marginLayoutParam.rightMargin, margins.second)
        this.layoutParams = marginLayoutParam
    } else {
        throw IllegalAccessException("Parent 가 RecyclerView가 아니에요.")
    }
}

/**
 * 전체값의 몇 퍼센트는 얼마?
 * value -> 전체 몇 중에
 * Int -> 몇은
 * return -> 몇 프로
 *
 * 그러니깐 value 중의 Int 는 value 의 몇 프로 인지를 반환한다.
 */
fun Int.percentOf(value: Int): Int {
    return (this.toFloat() / value.toFloat() * 100f).toInt()
}

fun Double.percentOf(value: Double): Int {
    return (this / value * (100).toDouble()).toInt()
}

/**
 * 체값에서 일부값은 몇 퍼센트?
 * value -> 전체 몇 중에
 * Int -> 몇은
 * return -> 몇 프로
 *
 * 그러니깐 전체값 value 중의 Int 퍼센트가 몇인지 반환한다.
 */
fun Int.ofPercentValueFrom(value: Int): Float {
    return (value.toFloat() * this.toFloat() / 100)
}

@JvmInline
value class Minutes(@Suppress("MemberVisibilityCanBePrivate") val minute: Int) {
    fun toMillis(): Millis = Millis(minute.toLong() * 60L * 1000L)
}

@JvmInline
value class Millis(@Suppress("MemberVisibilityCanBePrivate") val millisecond: Long) {
    fun toMillis(): Long = millisecond
}

@JvmInline
value class Hour(@Suppress("MemberVisibilityCanBePrivate") val hour: Int) {
    fun toMinute(): Minutes = Minutes(hour * 60)
    fun toMillis(): Millis = Millis(hour.toLong() * 60L * 60L * 1000L)
}

val Int.min get() = Minutes(this)
val Int.hour get() = Hour(this)
val Minutes.ms get() = this.toMillis().toMillis()
val Hour.ms get() = this.toMillis()
val Hour.min get() = this.toMinute()

inline fun <reified T : View> View.find(@IdRes id: Int): T = findViewById(id)
inline fun <reified T : View> Activity.find(@IdRes id: Int): T = findViewById(id)