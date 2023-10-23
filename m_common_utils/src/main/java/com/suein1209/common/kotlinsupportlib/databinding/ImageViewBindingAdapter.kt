@file:Suppress("unused")

package com.suein1209.common.kotlinsupportlib.databinding

import android.annotation.SuppressLint
import android.graphics.drawable.Drawable
import android.widget.ImageView
import androidx.databinding.BindingAdapter
import com.bumptech.glide.Glide
import com.bumptech.glide.load.MultiTransformation
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.bumptech.glide.request.RequestOptions
import com.suein1209.common.kotlinsupportlib.common.GlideDrawableImageView

object ImageViewBindingAdapter {

    const val OVERRIDE_NONE = 0

    /**
     * 이미지 노출(from Url)
     */
    @SuppressLint("CheckResult")
    @JvmStatic
    @BindingAdapter("srcUrl", "placeholder", "overrideSize", "overrideWidth", "overrideHeight", requireAll = false)
    fun ImageView.loadUrlAsync(srcUrl: String?, placeholder: Drawable? = null, overrideSize: Int = OVERRIDE_NONE, overrideWidth: Int = OVERRIDE_NONE, overrideHeight: Int = OVERRIDE_NONE) {
        loadAsyncImg(imageView = this, srcUrl = srcUrl, placeholder = placeholder, overrideSize = overrideSize, overrideWidth = overrideWidth, overrideHeight = overrideHeight, isCircleImage = false)
    }

    @SuppressLint("CheckResult")
    @JvmStatic
    @BindingAdapter("srcCircleUrl", "placeholder", "overrideSize", "overrideWidth", "overrideHeight", requireAll = false)
    fun ImageView.loadUrlCircleAsync(srcUrl: String?, placeholder: Drawable? = null, overrideSize: Int = OVERRIDE_NONE, overrideWidth: Int = OVERRIDE_NONE, overrideHeight: Int = OVERRIDE_NONE) {
        loadAsyncImg(imageView = this, srcUrl = srcUrl, placeholder = placeholder, overrideSize = overrideSize, overrideWidth = overrideWidth, overrideHeight = overrideHeight, isCircleImage = true)
    }

    @SuppressLint("CheckResult")
    fun loadAsyncImg(imageView: ImageView, srcUrl: String?, placeholder: Drawable? = null, overrideSize: Int, overrideWidth: Int, overrideHeight: Int, isCircleImage: Boolean) {
        Glide.with(imageView.context).load(srcUrl).apply {
            diskCacheStrategy(DiskCacheStrategy.RESOURCE)
            if (isCircleImage) transform(MultiTransformation(CenterCrop(), RoundedCorners(Int.MAX_VALUE)))
            transition(DrawableTransitionOptions.withCrossFade())
            if (placeholder != null) {
                placeholder(placeholder)
                error(placeholder)
                fallback(placeholder)
            }
            if (overrideSize != OVERRIDE_NONE) {
                override(overrideSize)
            } else if (overrideWidth != OVERRIDE_NONE && overrideHeight != OVERRIDE_NONE) {
                override(overrideWidth, overrideHeight)
            }
        }.into(imageView)
    }

    @SuppressLint("CheckResult")
    @JvmStatic
    @BindingAdapter("srcRatioUrl", "placeholder", "overrideSize", "overrideWidth", "overrideHeight", requireAll = false)
    fun ImageView.loadUrlRatioAsync(srcUrl: String?, placeholder: Drawable? = null, overrideSize: Int = OVERRIDE_NONE, overrideWidth: Int = OVERRIDE_NONE, overrideHeight: Int = OVERRIDE_NONE) {
        // Glide에 옵션을 설정합니다.
        val requestOptions = RequestOptions()
            .diskCacheStrategy(DiskCacheStrategy.ALL) // 디스크 캐시 사용

        // 이미지의 세로 크기에 맞게 ImageView의 높이를 조정합니다.
        Glide.with(this.context)
            .load(srcUrl)
            .apply(requestOptions)
            .apply {
                if (placeholder != null) {
                    placeholder(placeholder)
                    error(placeholder)
                    fallback(placeholder)
                }
                if (overrideSize != OVERRIDE_NONE) {
                    override(overrideSize)
                } else if (overrideWidth != OVERRIDE_NONE && overrideHeight != OVERRIDE_NONE) {
                    override(overrideWidth, overrideHeight)
                }
            }.into(GlideDrawableImageView(this@loadUrlRatioAsync))
    }
}