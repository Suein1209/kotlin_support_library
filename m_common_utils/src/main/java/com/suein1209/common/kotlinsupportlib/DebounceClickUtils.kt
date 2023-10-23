@file:Suppress("unused")

package com.suein1209.common.kotlinsupportlib

import android.os.SystemClock
import android.view.View
import com.suein1209.common.kotlinsupportlib.SingleClickValues.MIN_CLICK_INTERVAL

/**
 * 중복 클릭 방지하기 위한 메소드
 *
 * Created by suein1209
 */
object DebounceClickUtils {

    /**
     * 버튼 연타(중복 클릭) 체크
     */
    @JvmStatic
    fun checkButtonTiming(limit: Long): Boolean {
        var result = false
        val currentTime = SystemClock.elapsedRealtime()
        if (currentTime - SingleClickValues.globalSingleLastClickTime > limit) {
            result = true
            SingleClickValues.globalSingleLastClickTime = currentTime
        }
        return result
    }

    /**
     * 버튼 연타(중복 클릭) 체크
     * - limit interval(ms)를 파라미터로 넣을 수 있다.
     */
    @JvmStatic
    fun checkButtonTiming() = checkButtonTiming(MIN_CLICK_INTERVAL)
}

/**
 * 중복 클릭 방지 Click Listener
 *  - listener가 적용된 하나의 아이템(혹은 버튼)에 대해서만 중복 클릭을 방지한다.
 * Created by suein1209
 *
 * @property onSingleClick
 */
class OnSingleClickListener(private val minClickInterval: Long = MIN_CLICK_INTERVAL, private val onSingleClick: (view: View) -> Unit) : View.OnClickListener {
    private var mLastClickTime: Long = 0
    override fun onClick(view: View) {
        val currentClickTime = SystemClock.elapsedRealtime()
        val elapsedTime = currentClickTime - mLastClickTime

        if (elapsedTime <= minClickInterval)
            return

        mLastClickTime = currentClickTime

        onSingleClick(view)
    }
}

/**
 *  중복 클릭 방지 Click Listener
 *   - 해당 listener가 적용된 모든 버튼들에 대해서 중복 클릭을 방지 한다.
 *
 * @property onSingleClick
 */
class OnGlobalSingleClickListener(private val minClickInterval: Long = MIN_CLICK_INTERVAL, val onSingleClick: (view: View) -> Unit) : View.OnClickListener {
    override fun onClick(view: View) {
        val currentClickTime = SystemClock.elapsedRealtime()
        val elapsedTime = currentClickTime - SingleClickValues.globalSingleLastClickTime

        if (elapsedTime <= minClickInterval)
            return

        SingleClickValues.globalSingleLastClickTime = currentClickTime

        onSingleClick(view)
    }
}

private object SingleClickValues {
    var globalSingleLastClickTime: Long = 0
    const val MIN_CLICK_INTERVAL: Long = 300
}