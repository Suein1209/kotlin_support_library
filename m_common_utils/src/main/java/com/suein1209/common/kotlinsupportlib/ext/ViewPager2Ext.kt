@file:Suppress("unused")

package com.suein1209.common.kotlinsupportlib.ext

import androidx.core.view.get
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager2.widget.ViewPager2

/**
 * Viewpager에 Visible 된 아이템 range 반환
 */
fun ViewPager2.getVisibleRange(): Pair<Int, Int> {
    val layoutManger = ((this[0] as? RecyclerView)?.layoutManager as? LinearLayoutManager)
    layoutManger ?: return Pair(0, 0)
    val firstVisible = layoutManger.findFirstVisibleItemPosition()
    val lastVisible = layoutManger.findLastVisibleItemPosition()
    return Pair(firstVisible, lastVisible)
}