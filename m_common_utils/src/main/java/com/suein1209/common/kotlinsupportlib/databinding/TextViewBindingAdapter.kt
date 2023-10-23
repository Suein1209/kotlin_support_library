@file:Suppress("unused")

package com.suein1209.common.kotlinsupportlib.databinding

import android.graphics.Typeface
import android.widget.TextView
import androidx.databinding.BindingAdapter
import com.suein1209.common.kotlinsupportlib.ScreenUtils
import com.suein1209.common.kotlinsupportlib.StringUtils
import com.suein1209.common.kotlinsupportlib.ViewUtils
import com.suein1209.common.kotlinsupportlib.commaStr
import com.suein1209.common.kotlinsupportlib.doLineFeedCharWithLeftMargin
import com.suein1209.common.kotlinsupportlib.doLineFeedCharWithWidth
import com.suein1209.common.kotlinsupportlib.fromHTML
import com.suein1209.common.kotlinsupportlib.px
import com.suein1209.common.kotlinsupportlib.screenWidth
import kotlin.math.roundToInt

/**
 * TextView DataBinding Adapter
 *
 * Created by suein1209
 */
object TextViewBindingAdapter {

    /**
     * Comma String 설정
     * - "123456" -> "123,456"
     */
    @JvmStatic
    @BindingAdapter("textComma")
    fun TextView.setCommaText(numStr: String?) {
        this.text = numStr.commaStr
    }

    /**
     * Comma String 설정
     * - "123456" -> "123,456"
     */
    @JvmStatic
    @BindingAdapter("longComma", "longCommaLimit", "longCommaLimitExpression", requireAll = false)
    fun TextView.setCommaLongText(longComma: Long?, longCommaLimit: Long? = null, longCommaLimitExpression: String? = null) {
        longComma ?: return
        if (longCommaLimit != null && longComma > longCommaLimit) {
            this.text = longCommaLimitExpression ?: ""
        } else {
            this.text = longComma.commaStr
        }
    }

    /**
     * - 1,000 -> 1.0K
     * - 10,000 -> 10.0K
     * - 100,000 -> 100.0K
     * - 1,000,000 -> 1.0M
     * - 1,000,000,000 -> 1.0B
     */
    @JvmStatic
    @BindingAdapter("tenthsReadableText")
    fun TextView.setTenthsReadableText(count: Long?) {
        this.text = StringUtils.getTenthsReadableText(count)
    }

    /**
     * Char 단위 개행 적용(for DP)
     * - TextView width = Display Width - textBreakLeftScreenMargin
     */
    @JvmStatic
    @BindingAdapter("textBreakPerChar", "textBreakLeftScreenMarginDP", requireAll = true)
    fun TextView.setBreakPerCharWithScreenLeftMargin(text: String?, textBreakLeftScreenMarginDP: Int) {
        if (!text.isNullOrEmpty()) {
            doLineFeedCharWithLeftMargin(textBreakLeftScreenMarginDP.px, text)
        } else {
            this.text = text
        }
    }

    /**
     * Char 단위 개행 적용
     * - TextView width = Display Width - textBreakLeftScreenMargin
     */
    @JvmStatic
    @BindingAdapter("textBreakPerChar", "textBreakLeftScreenMargin", requireAll = true)
    fun TextView.setBreakPerCharWithScreenLeftMargin(text: String?, textBreakLeftScreenMargin: Float) {
        if (!text.isNullOrEmpty()) {
            doLineFeedCharWithLeftMargin(textBreakLeftScreenMargin.toInt(), text)
        } else {
            this.text = text
        }
    }

    /**
     * Char 단위 개행 적용
     * - TextView width = textBreakViewWidth
     */
    @JvmStatic
    @BindingAdapter("textBreakPerChar", "textBreakViewWidth", requireAll = true)
    fun TextView.setBreakPerCharWithViewWidth(text: String?, textBreakViewWidth: Int?) {
        if (!text.isNullOrEmpty() && textBreakViewWidth != null) {
            doLineFeedCharWithWidth(textBreakViewWidth.px, text)
        } else {
            this.text = text
        }
    }

    /**
     * Char 단위 개행 적용
     * - TextView width = Display Width 대비 TextView With가 차지하는 percent
     */
    @JvmStatic
    @BindingAdapter("textBreakPerChar", "textBreakViewPercent", requireAll = true)
    fun TextView.setBreakPerCharWithViewPercent(text: String?, textBreakViewPercent: Float?) {
        if (!text.isNullOrEmpty() && textBreakViewPercent != null && textBreakViewPercent > 0.0f) {
            doLineFeedCharWithWidth((textBreakViewPercent * this.context.screenWidth.toFloat() / 100F).roundToInt(), text)
        } else {
            this.text = text
        }
    }

    @BindingAdapter("textBreakPerChar", "textBreakLeftScreenMargin", "textBreakCharLimit", requireAll = true)
    @JvmStatic
    fun TextView.setBreakPerCharWithScreenLeftMargin(text: String?, textBreakLeftScreenMargin: Float, textBreakCharLimit: Int? = 1) {
        if (!text.isNullOrEmpty()) {
            if (text.length > (textBreakCharLimit ?: 1)) {
                this.text = text
            } else {
                ViewUtils.setLineFeedCharacter(this, text, ScreenUtils.getScreenWidth(context) - textBreakLeftScreenMargin.toInt())
            }
        } else {
            this.text = text
        }
    }

    /**
     * Html String을 설정한다.
     */
    @JvmStatic
    @BindingAdapter("textHtml")
    fun TextView.setTextHtml(text: String?) {
        if (text.isNullOrEmpty()) {
            this.text = ""
        } else {
            fromHTML(text)
        }
    }

    /**
     * Text Bold 설정
     */
    @JvmStatic
    @BindingAdapter("textBold")
    fun TextView.setTextBold(isBold: Boolean) {
        if (isBold) {
            setTypeface(null, Typeface.BOLD)
        } else {
            setTypeface(null, Typeface.NORMAL)
        }
    }


    /**
     * 밀리초를 00:00:00로 변환해 textview에 설정한다.
     * - 시간없이 분,초 는 필수여서 "00:02" 식으로 표현된다.
     * - 시간이 있을때만 10:00:00 라는 식으로 표현된다.
     */
    @JvmStatic
    @BindingAdapter("convertToTimeFormat")
    fun TextView.setMilliSecondToHHMMSS(millis: Long?) {
        this.text = StringUtils.getMilliSecondToHHMMSS(millis)
    }

    @JvmStatic
    @BindingAdapter("setConfirmedDoubleDigit")
    fun TextView.setConfirmedDoubleDigit(digit: Int?) {
        this.text = String.format("%02d", digit)
    }
}