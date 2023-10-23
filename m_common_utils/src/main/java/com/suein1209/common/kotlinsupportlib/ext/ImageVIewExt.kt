package com.suein1209.common.kotlinsupportlib.ext

import android.graphics.drawable.Drawable
import android.widget.ImageView
import com.suein1209.common.kotlinsupportlib.databinding.ImageViewBindingAdapter

fun ImageView.loadUrlAsync(srcUrl: String?, placeholder: Drawable? = null, overrideSize: Int = ImageViewBindingAdapter.OVERRIDE_NONE, overrideWidth: Int = ImageViewBindingAdapter.OVERRIDE_NONE, overrideHeight: Int = ImageViewBindingAdapter.OVERRIDE_NONE, isCircleImage: Boolean = false) {
    ImageViewBindingAdapter.loadAsyncImg(
        imageView = this,
        srcUrl = srcUrl,
        placeholder = placeholder,
        overrideSize = overrideSize,
        overrideWidth = overrideWidth,
        overrideHeight = overrideHeight,
        isCircleImage = isCircleImage
    )
}