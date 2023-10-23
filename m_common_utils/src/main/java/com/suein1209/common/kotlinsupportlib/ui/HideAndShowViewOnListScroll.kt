@file:Suppress("MemberVisibilityCanBePrivate")

package com.suein1209.common.kotlinsupportlib.ui

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.suein1209.common.kotlinsupportlib.collapseAnimOnPosition
import com.suein1209.common.kotlinsupportlib.expandAnimOnPosition
import kotlin.math.abs

/**
 * 주로 스티키 뷰를 위해 만들어졌다.
 * 스크롤의 방향에 따라 해당 뷰를 숨기거가 혹은 노출 시켜야 하는 경우가 있는데 이때 사용된다.
 *
 * Created by suein1209
 */
@Suppress("unused")
class HideAndShowViewOnListScroll(private val hideAndShowView: View, private val expandCallback: () -> Unit = {}, private val collapsedCallback: () -> Unit = {}) : RecyclerView.OnScrollListener() {
    var totalScroll: Int = 0
        private set
    private var isExpandCategoryLayout = true

    override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
        super.onScrolled(recyclerView, dx, dy)
        totalScroll += dy
        if (totalScroll <= 0) totalScroll = 0
        if (hideAndShowView.height < totalScroll) {
            if (abs(dy) > 10) {
                if (dy < 0) expandCategoryIfNotExpand() else collapseCategoryIfNotCollapse()
            }
        } else {
            expandCategoryIfNotExpand()
        }
    }

    fun expandCategoryIfNotExpand() {
        if (!isExpandCategoryLayout) {
            isExpandCategoryLayout = true
            expandCallback.invoke()
            hideAndShowView.expandAnimOnPosition()
        }
    }

    fun collapseCategoryIfNotCollapse() {
        if (isExpandCategoryLayout) {
            isExpandCategoryLayout = false
            collapsedCallback.invoke()
            hideAndShowView.collapseAnimOnPosition()
        }
    }

    fun forceResetTotalScroll() {
        totalScroll = 0
    }
}
