@file:Suppress("unused")

package com.suein1209.common.kotlinsupportlib.databinding

import android.graphics.Paint
import android.view.View
import android.view.ViewGroup
import android.view.animation.Animation
import android.widget.TextView
import androidx.appcompat.widget.SwitchCompat
import androidx.core.view.*
import androidx.databinding.BindingAdapter
import com.suein1209.common.kotlinsupportlib.OnGlobalSingleClickListener
import com.suein1209.common.kotlinsupportlib.screenWidth

/**
 * 대부분 View DataBinding adapter
 *
 * Created by suein1209
 */
object CommonBindingAdapter {

    /**
     * 연타 및 동시 클릭이 방지된 Click listener 적용
     */
    @JvmStatic
    @BindingAdapter("onGlobalDebounceClick")
    fun View.setGlobalSingleClickListener(listener: View.OnClickListener?) {
        if (listener != null) {
            this.setOnClickListener(OnGlobalSingleClickListener {
                it.run(listener::onClick)
            })
        }
    }

    /**
     * View Visible or Gone
     */
    @JvmStatic
    @BindingAdapter("android:visibleIf", "animVisible", "animGone", requireAll = false)
    fun View.setVisibleIf(isVisible: Boolean, animVisible: Animation? = null, animGone: Animation? = null) {
        this.isVisible = isVisible

        when {
            isVisible && animVisible != null -> animation = animVisible
            !isVisible && animGone != null -> animation = animGone
        }
    }

    /**
     * View Visible or InVisible
     */
    @JvmStatic
    @BindingAdapter("android:inVisibleIf")
    fun View.setInVisibleIf(isInVisible: Boolean) {
        this.isInvisible = isInVisible
    }

    /**
     * View Visible or Gone
     */
    @JvmStatic
    @BindingAdapter("android:goneIf")
    fun View.setGoneIf(isGone: Boolean) {
        this.isGone = isGone
    }

    @JvmStatic
    @BindingAdapter("set_back_color")
    fun View.setBackColor(color: Int) {
        setBackgroundColor(color)
    }

    @JvmStatic
    @BindingAdapter("layoutMarginEnd")
    fun setLayoutMarginBottom(view: View, dimen: Float) {
        val layoutParams = view.layoutParams as ViewGroup.MarginLayoutParams
        layoutParams.marginEnd = dimen.toInt()
        view.layoutParams = layoutParams
    }

    @JvmStatic
    @BindingAdapter("set_underline")
    fun TextView.setUnderLine(isShowUnderLine: Boolean) {
        paintFlags = if (isShowUnderLine) {
            paintFlags or Paint.UNDERLINE_TEXT_FLAG
        } else {
            paintFlags and Paint.UNDERLINE_TEXT_FLAG.inv()
        }
    }

    @JvmStatic
    @BindingAdapter("set_selected")
    fun View.setSelectedState(selected: Boolean) {
        isSelected = selected
    }

    @JvmStatic
    @BindingAdapter("set_switch_selected")
    fun SwitchCompat.setSwitchSelectedState(selected: Boolean) {
        isSelected = selected
    }

    @JvmStatic
    @BindingAdapter("set_back_image")
    fun View.setBackRes(resource: Int) {
        setBackgroundResource(resource)
    }

    @JvmStatic
    @BindingAdapter("setWidthPx")
    fun View.setWidthPx(widthPx: Int) {
        if (widthPx > 0) {
            val lp = layoutParams
            if (lp != null) {
                lp.width = widthPx
                layoutParams = lp
            }
        }
    }

    @JvmStatic
    @BindingAdapter("setHeightPx")
    fun View.setHeightPx(heightPx: Int) {
        if (heightPx > 0) {
            val lp = layoutParams
            if (lp != null) {
                lp.height = heightPx
                layoutParams = lp
            }
        }
    }

    @JvmStatic
    @BindingAdapter("viewAspectFitRatioOfWidth")
    fun View.viewAspectFitRatioOfWidth(initRatioOfWidth: Float? = null) {
        if (initRatioOfWidth != null) {

            val viewWith = (context.screenWidth - (this.marginLeft + this.marginRight) - (this.paddingLeft + this.paddingRight))

            val heightFromRation = (viewWith.toFloat() * initRatioOfWidth / 100F).toInt()
            val tempLayoutParams = this.layoutParams
            tempLayoutParams.width = viewWith
            tempLayoutParams.height = heightFromRation
            this.layoutParams = tempLayoutParams
        }
    }
}