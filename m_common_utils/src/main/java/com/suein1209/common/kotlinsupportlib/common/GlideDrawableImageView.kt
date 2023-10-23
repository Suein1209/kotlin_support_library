package com.suein1209.common.kotlinsupportlib.common

import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.Drawable
import android.widget.ImageView
import com.bumptech.glide.request.target.ImageViewTarget
import com.bumptech.glide.request.transition.Transition

/**
 * Glide 이미지 뷰
 * 옵션 widthFillScale 값이 true 일 경우 레이아웃 크기 스케일링
 */
class GlideDrawableImageView(private val imageView: ImageView) : ImageViewTarget<Drawable>(imageView) {

    override fun setResource(resource: Drawable?) = imageView.setImageDrawable(resource)

    override fun onResourceReady(resource: Drawable, transition: Transition<in Drawable>?) {
        super.onResourceReady(resource, transition)
        val with = imageView.width
        imageView.layoutParams?.let { layoutParam ->
            layoutParam.width = with
            kotlin.runCatching {
                if (resource is BitmapDrawable) {
                    resource.bitmap?.let {
                        val ratio = layoutParam.width.toFloat() / it.width.toFloat()
                        layoutParam.height = (it.height * ratio).toInt()
                    }
                }
            }
        }
    }
}