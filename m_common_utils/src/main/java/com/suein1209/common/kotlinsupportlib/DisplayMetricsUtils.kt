@file:Suppress("unused")

package com.suein1209.common.kotlinsupportlib

import android.content.Context
import android.content.res.Resources
import android.util.DisplayMetrics
import android.util.TypedValue
import kotlin.math.roundToInt

/**
 *
 */
object DisplayMetricsUtils {
    /**
     * 현재 디스플레이 화면에 비례한 DP단위를 픽셀 크기로 반환합니다.
     */
    @JvmStatic
    @JvmOverloads
    fun getPixelFromDip(dip: Float, isRound: Boolean = true): Int {
        val px = TypedValue.applyDimension(
            TypedValue.COMPLEX_UNIT_DIP,
            dip,
            Resources.getSystem().displayMetrics
        )
        return if (isRound) {
            px.roundToInt()
        } else {
            px.toInt()
        }
    }

    /**
     * 현재 디스플레이 화면에 비례한 픽셀단위를 DP단위로 반환합니다.
     */
    @JvmStatic
    fun getDipFromPixel(px: Int) = (px / Resources.getSystem().displayMetrics.density).roundToInt()

    /**
     * Deprecated
     * - Java & Kotlin
     * -- DisplayMetricsUtils.getPixelFromDip()
     *
     *
     * - Kotlin(Ext)
     * -- 123.px
     * -- 123F.px
     *
     *
     * dp -> 픽셀 변환. 위와의 차이는 소수점일 경우 올림해준다.(갤럭시9에서 1dp가 1px로 나오는 이슈) http://jira.wemakeprice.com/browse/QA-12600
     *
     * @param context
     * @param dp
     * @return
     */
    @JvmStatic
    @Deprecated("")
    fun convertDpToPixel(context: Context, dp: Float): Int {
        val resources = context.resources
        val metrics = resources.displayMetrics
        return (dp * (metrics.densityDpi.toFloat() / DisplayMetrics.DENSITY_DEFAULT) + 0.5f).toInt()
    }
}

/**
 * pixel to dp
 */
val Int.dp: Int get() = (this.toFloat() / Resources.getSystem().displayMetrics.density).roundToInt()

/**
 * dp to pixel
 */
val Int.px: Int get() = (this.toFloat() * Resources.getSystem().displayMetrics.density).roundToInt()

/**
 * dp(float) to pixel
 */
val Float.px: Int get() = (this * Resources.getSystem().displayMetrics.density).roundToInt()