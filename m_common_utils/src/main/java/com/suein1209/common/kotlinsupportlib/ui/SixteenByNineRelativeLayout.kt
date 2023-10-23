package com.suein1209.common.kotlinsupportlib.ui

import android.content.Context
import android.util.AttributeSet
import android.view.ViewGroup
import android.widget.RelativeLayout

class SixteenByNineRelativeLayout : RelativeLayout {
    constructor(context: Context) : this(context, null)
    constructor(context: Context, attrs: AttributeSet?) : this(context, attrs, 0)
    constructor(context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0) : super(context, attrs, defStyleAttr)

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        if (layoutParams.height == ViewGroup.LayoutParams.WRAP_CONTENT) {
            val sixteenNineHeight = MeasureSpec.makeMeasureSpec(MeasureSpec.getSize(widthMeasureSpec) * 9 / 16, MeasureSpec.EXACTLY)
            super.onMeasure(widthMeasureSpec, sixteenNineHeight)
        } else
            super.onMeasure(widthMeasureSpec, heightMeasureSpec)
    }
}