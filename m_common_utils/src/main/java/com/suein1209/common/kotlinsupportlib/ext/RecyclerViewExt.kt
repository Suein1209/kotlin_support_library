@file:Suppress("unused")

package com.suein1209.common.kotlinsupportlib.ext

import android.view.View
import android.widget.HorizontalScrollView
import android.widget.LinearLayout
import androidx.annotation.IntRange
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

/**
 * RecyclerView 상에서 visible ViewHolder 목록을 반환한다.
 * - 단일, 혹은 안전한 view type 의 경우 [getVisibleViewHoldersForLinear]를 사용하는 것이 더 편리 합니다.
 */
fun RecyclerView.getVisibleViewHoldersForLinear(): List<Pair<Int, RecyclerView.ViewHolder>> {
    val itemCount = this.adapter?.itemCount ?: 0
    if (itemCount == 0) return listOf()

    val start = (this.layoutManager as LinearLayoutManager).findFirstVisibleItemPosition()
    val end = (this.layoutManager as LinearLayoutManager).findLastVisibleItemPosition()

    val result = mutableListOf<Pair<Int, RecyclerView.ViewHolder>>()
    if (start < end) {
        (start..end).forEach { position ->
            val viewHolder = this.findViewHolderForAdapterPosition(position)
            viewHolder?.let {
                result.add(position to it)
            }
        }
    } else {
        val viewHolder = this.findViewHolderForAdapterPosition(start)
        viewHolder?.let {
            result.add(start to it)
        }
    }
    return result
}

/**
 * RecyclerView 상에서 찾는 타입의 visible ViewHolder 반환한다.
 */
inline fun <reified T> RecyclerView.findViewHoldersForLinear(): T? {
    val itemCount = this.adapter?.itemCount ?: 0
    if (itemCount == 0) return null

    val start = (this.layoutManager as LinearLayoutManager).findFirstVisibleItemPosition()
    val end = (this.layoutManager as LinearLayoutManager).findLastVisibleItemPosition()

    var result: T? = null
    if (start < end) {
        (start..end).forEach { position ->
            val viewHolder = this.findViewHolderForAdapterPosition(position) as? T
            if (viewHolder != null) {
                result = viewHolder
            }
        }
    } else {
        result = this.findViewHolderForAdapterPosition(start) as? T
    }
    return result
}

fun RecyclerView.getCompleteVisibleViewHoldersForLinear(): List<Pair<Int, RecyclerView.ViewHolder>> {
    return kotlin.runCatching {
        val itemCount = this.adapter?.itemCount ?: 0
        if (itemCount == 0) return listOf()

        val start = (this.layoutManager as LinearLayoutManager).findFirstCompletelyVisibleItemPosition()
        val end = (this.layoutManager as LinearLayoutManager).findLastCompletelyVisibleItemPosition()

        val result = mutableListOf<Pair<Int, RecyclerView.ViewHolder>>()
        if (start < end) {
            (start..end).forEach { position ->
                val viewHolder = this.findViewHolderForAdapterPosition(position)
                viewHolder?.let {
                    result.add(position to it)
                }
            }
        } else {
            val viewHolder = this.findViewHolderForAdapterPosition(start)
            viewHolder?.let {
                result.add(start to it)
            }
        }
        result
    }.getOrNull() ?: listOf()
}

/**
 * 가로 리스트 화면일 시 아이템을 눌렀을 위치 기준으로 가운데로 아이템을 옮긴다.
 * - 즉, 가쪽에 있는 아이템을 눌렀을때 해당 아이템을 가운데로 옮기는 역할을 한다.
 */
fun RecyclerView.moveToCenterPosition(@IntRange(from = 0, to = Int.MAX_VALUE.toLong()) selectedPosition: Int) {
    if (this.measuredWidth <= 0) return
    if (this.layoutManager is LinearLayoutManager) {
        val selectedView = (this.layoutManager as LinearLayoutManager).findViewByPosition(selectedPosition)
        if (selectedView != null) {
            //visible 범위 안에 있다면
            moveToCenterPosition(this, selectedView)
        } else {
            //visibie 범위 안에 없다면
            scrollToPosition(selectedPosition)
            this.post {
                (this.layoutManager as LinearLayoutManager).findViewByPosition(selectedPosition)?.let { moveToCenterPosition(this, it) }
            }
        }
    } else throw Exception("LinearLayoutManager 만 사용 가능합니다.")
}

private fun moveToCenterPosition(recyclerView: RecyclerView, selectedItemView: View) {
    if (selectedItemView.width > 0) {
        val center = (recyclerView.measuredWidth / 2)
        val tempLeft = if (selectedItemView.left < 0) {
            selectedItemView.left + kotlin.math.abs(selectedItemView.left)
        } else {
            selectedItemView.left
        }
        val selectedViewCenter = tempLeft + (selectedItemView.width / 2)
        recyclerView.post {
            recyclerView.smoothScrollBy(selectedViewCenter - center, 0)
        }
    }
}


/**
 * 가로 리스트 화면일 시 아이템을 눌렀을 위치 기준으로 가운데로 아이템을 옮긴다.
 * - 즉, 가쪽에 있는 아이템을 눌렀을때 해당 아이템을 가운데로 옮기는 역할을 한다.
 */
fun HorizontalScrollView.moveToCenterPosition(@IntRange(from = 0, to = Int.MAX_VALUE.toLong()) selectedPosition: Int, totalScrolled: Int) {
    if (this.measuredWidth <= 0 || this.childCount <= 0) return
    val viewContainer = kotlin.runCatching { this.getChildAt(0) as? LinearLayout }.getOrNull()
    viewContainer ?: return
    val selectedItemView = kotlin.runCatching { viewContainer.getChildAt(selectedPosition) }.getOrNull()
    selectedItemView ?: return
    val selectedViewLeft = if (selectedItemView.left - totalScrolled > 0) selectedItemView.left - totalScrolled else 0
    if (selectedItemView.width > 0) {
        val center = (this.measuredWidth / 2)
        val tempLeft = if (selectedViewLeft < 0) {
            selectedViewLeft + kotlin.math.abs(selectedViewLeft)
        } else {
            selectedViewLeft
        }
        val selectedViewCenter = tempLeft + (selectedItemView.width / 2)
        this.post {
            this.smoothScrollBy(selectedViewCenter - center, 0)
        }
    }
}

/**
 * 현재 리스트가 스크롤이 가능한 상태인지 반환
 * - 스크롤이 가능하다면? 혹은 가능하지 않다면에 대한 처리를 하기 위함이다.
 */
fun RecyclerView.isRecyclerScrollable(): Boolean {
    val layoutManager = this.layoutManager as? LinearLayoutManager
    val adapter = this.adapter
    return if (layoutManager == null || adapter == null) false else layoutManager.findLastCompletelyVisibleItemPosition() < adapter.itemCount - 1
}