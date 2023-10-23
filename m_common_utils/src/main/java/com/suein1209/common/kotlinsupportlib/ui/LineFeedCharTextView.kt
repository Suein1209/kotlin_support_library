package com.suein1209.common.kotlinsupportlib.ui

import android.content.Context
import android.util.AttributeSet
import androidx.appcompat.widget.AppCompatTextView
import com.suein1209.common.kotlinsupportlib.ViewUtils

class LineFeedCharTextView : AppCompatTextView {
    constructor(context: Context) : super(context)
    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs)
    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr)

    override fun onLayout(changed: Boolean, left: Int, top: Int, right: Int, bottom: Int) {
        text = ViewUtils.getLineFeedCharacter(this, this.text.toString(), right - left - this.paddingLeft - this.paddingRight)
        super.onLayout(changed, left, top, right, bottom)
    }
}