@file:Suppress("unused")

package com.suein1209.common.kotlinsupportlib

import android.app.Activity
import android.content.Context
import android.content.res.Configuration
import android.content.res.Resources
import android.graphics.Rect
import android.util.DisplayMetrics
import android.util.Log
import android.view.WindowManager
import androidx.fragment.app.Fragment
import com.suein1209.common.kotlinsupportlib.common.CommonUtilsConst
import kotlin.math.ceil

/**
 * 스크린(Display) 사이즈 관련 Utils
 *
 * Created by suein1209
 */
object ScreenUtils {

    /**
     * 단말 가로 사이즈
     */
    @JvmStatic
    @Suppress("DEPRECATION")
    fun getScreenWidth(context: Context?): Int {
        context ?: return 0
        val width: Int = try {
            context.resources.displayMetrics.widthPixels
        } catch (e: Exception) {
            val displayMetrics = DisplayMetrics()
            val wm = context.getSystemService(Context.WINDOW_SERVICE) as WindowManager?
            wm?.defaultDisplay?.getMetrics(displayMetrics)
            displayMetrics.widthPixels
        }
        return width
    }

    /**
     * 단말 세로 사이즈
     */
    @JvmStatic
    @Suppress("DEPRECATION")
    fun getScreenHeight(context: Context): Int {
        val height: Int = try {
            context.resources.displayMetrics.heightPixels
        } catch (e: java.lang.Exception) {
            val displayMetrics = DisplayMetrics()
            val wm = context.getSystemService(Context.WINDOW_SERVICE) as WindowManager?
            wm?.defaultDisplay?.getMetrics(displayMetrics)
            displayMetrics.heightPixels
        }
        return height
    }

    /**
     * 단말 실제 전체 높이 사이즈(인디케이터 영역에 카메라 있는 경우 일반높이구하기는 차이가 벌어짐)
     * https://black-jin0427.tistory.com/230
     */
    @JvmStatic
    @Suppress("DEPRECATION")
    fun getScreenRealHeight(context: Context): Int {
        return try {
            val displayMetrics = DisplayMetrics()
            val wm = context.getSystemService(Context.WINDOW_SERVICE) as WindowManager?
            wm?.defaultDisplay?.getRealMetrics(displayMetrics)
            displayMetrics.heightPixels
        } catch (e: Exception) {
            getScreenHeight(context)
        }
    }

    /**
     * 단말 세로(클라이언트) 사이즈
     * 인디케이터 영역 뺀 화면 높이
     */
    @JvmStatic
    fun getClientHeight(context: Context?): Int {
        context ?: return -1
        try {
            return if (context is Activity) {
                val rect = Rect()
                context.window.decorView.getWindowVisibleDisplayFrame(rect)
                // 인디케이터 영역 높이
                getScreenHeight(context) - rect.top
            } else {
                getScreenHeight(context)
            }
        } catch (e: java.lang.Exception) {
            if (BuildConfig.DEBUG) {
                Log.e(CommonUtilsConst.LOG_TAG, e.stackTraceToString())
            }
        }
        return -1
    }

    /**
     * StatusBar 영역 높이
     */
    @JvmStatic
    fun getStatusBarHeight(context: Context): Int {
        val resources = context.resources
        val resourceId = resources.getIdentifier("status_bar_height", "dimen", "android")
        return if (resourceId > 0) {
            resources.getDimensionPixelSize(resourceId)
        } else {
            ceil((24) * resources.displayMetrics.density.toDouble()).toInt()
        }
    }

    /**
     * StatusBar 영역 높이
     */
    @JvmStatic
    fun getNavigationBarHeight(context: Context): Int {
        return try {
            val resources = context.resources
            val resourceId = resources.getIdentifier("navigation_bar_height", "dimen", "android")
            if (resourceId > 0 && hasNavigationBar(resources)) {
                resources.getDimensionPixelSize(resourceId)
            } else {
                0
            }
        } catch (e: Exception) {
            0
        }
    }

    @JvmStatic
    fun hasNavigationBar(resource: Resources): Boolean {
        val hasNavBarId: Int = resource.getIdentifier("config_showNavigationBar", "bool", "android")
        return hasNavBarId > 0 && resource.getBoolean(hasNavBarId);
    }

    /**
     * Tablet 체크
     */
    fun isTablet(context: Context?): Boolean {
        return if (context == null) {
            false
        } else (context.resources.configuration.screenLayout and Configuration.SCREENLAYOUT_SIZE_MASK) >= Configuration.SCREENLAYOUT_SIZE_LARGE
    }
}

/**
 * 단말 가로 사이즈
 */
val Context.screenWidth: Int get() = ScreenUtils.getScreenWidth(this)

/**
 * 단말 세로 사이즈
 */
val Context.screenHeight: Int get() = ScreenUtils.getScreenHeight(this)

/**
 * 인디케이터 영역 뺀 단말 세로(클라이언트) 높이
 */
val Context.clientHeight: Int get() = ScreenUtils.getClientHeight(this)

/**
 * StatusBar 영역 높이
 */
val Context.statusBarHeight: Int get() = ScreenUtils.getStatusBarHeight(this)

/**
 * StatusBar 영역 높이
 */
val Fragment.statusBarHeight: Int get() = ScreenUtils.getStatusBarHeight(requireContext())

/**
 * 단말 가로 사이즈
 */
val Fragment.screenWidth: Int get() = ScreenUtils.getScreenWidth(requireContext())

/**
 * 단말 세로 사이즈
 */
val Fragment.screenHeight: Int get() = ScreenUtils.getScreenHeight(requireContext())

/**
 * 인디케이터 영역 뺀 단말 세로(클라이언트) 높이
 */
val Fragment.clientHeight: Int get() = ScreenUtils.getClientHeight(requireContext())