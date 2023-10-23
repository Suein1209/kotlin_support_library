@file:Suppress("DEPRECATION", "unused")

package com.suein1209.common.kotlinsupportlib

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Rect
import android.os.Build
import android.text.Html
import android.text.TextUtils
import android.view.View
import android.view.ViewGroup
import android.view.ViewTreeObserver
import android.view.WindowManager
import android.view.animation.AccelerateInterpolator
import android.view.animation.Animation
import android.view.animation.Transformation
import android.view.animation.TranslateAnimation
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import android.widget.RelativeLayout
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.core.view.WindowCompat
import androidx.core.view.isVisible
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.RecyclerView
import com.suein1209.common.kotlinsupportlib.px
import com.suein1209.common.kotlinsupportlib.ext.isNotNullEmpty
import com.suein1209.common.kotlinsupportlib.ext.setAnimationListener

/**
 * View에 관련된 Utils 모음
 *
 * Created by suein1209
 */
object ViewUtils {

    /**
     * 키패드(Soft Keyboard) 감추기
     */
    @JvmStatic
    fun hideIME(et: EditText?) {
        et ?: return
        (et.context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager).hideSoftInputFromWindow(et.windowToken, 0)
    }

    /**
     * 키패드(Soft Keyboard) 보이기
     */
    @JvmStatic
    fun showIME(et: EditText?, flag: Int = 0) {
        et ?: return
        if (et.requestFocus()) {
            (et.context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager).showSoftInput(et, flag)
        }
    }

    /**
     * 키패드(Soft Keyboard) 보이기
     */
    @JvmStatic
    fun hideIME(activity: Activity) {
        activity.currentFocus?.windowToken ?: return
        (activity.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager).hideSoftInputFromWindow(activity.currentFocus?.windowToken, 0)
    }

    /**
     * 키패드(Soft Keyboard) 토글
     */
    fun toggleIME(et: EditText?) {
        et ?: return
        if (et.requestFocus()) {
            (et.context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager).toggleSoftInput(0, 0)
        }
    }

    /**
     * 글자 단위 개행
     * @param textView 적용될 텍스트 뷰
     * @param frameWidth 텍스트 뷰의 실제 넓이
     */
    @JvmStatic
    fun setLineFeedCharacter(textView: TextView, text: String?, frameWidth: Int) {

        if (text.isNullOrEmpty()) {
            return
        }

        if (frameWidth <= 0) {
            textView.text = text.trim()
            return
        }

        var resultText = ""
        val paint: Paint = textView.paint
        var orgText: String = text
        var endIndex = paint.breakText(orgText, true, frameWidth.toFloat(), null)
        // StringIndexOutOfBoundsException : 일부 특수기호에 대해 breakText에서 잘못된 index 반환 (ex.(⌯˃̶᷄ ꈊ˂̶᷄ ૢ))
        kotlin.runCatching {
            while (true) {
                if (orgText.substring(0, endIndex).contains("\n")) {
                    endIndex = getLineFeedCharacterEndIndex(orgText.indexOf("\n"))
                    resultText += orgText.substring(0, endIndex)
                    orgText = orgText.substring(endIndex)
                } else {
                    resultText += "${orgText.substring(0, endIndex).trim()}\n"
                    orgText = orgText.substring(endIndex).trim()
                }
                if (TextUtils.isEmpty(orgText)) {
                    break
                }
                endIndex = paint.breakText(orgText, true, frameWidth.toFloat(), null)
            }
        }.onFailure {
            resultText = text
        }

        textView.text = resultText.trim()
    }

    @JvmStatic
    fun getLineFeedCharacter(textView: TextView, text: String?, frameWidth: Int): String? {

        if (text.isNullOrEmpty()) {
            return text
        }

        if (frameWidth <= 0) {
            return text
        }

        var resultText = ""
        val paint: Paint = textView.paint
        var orgText: String = text
        var endIndex = paint.breakText(orgText, true, frameWidth.toFloat(), null)
        kotlin.runCatching {
            while (true) {
                if (orgText.substring(0, endIndex).contains("\n")) {
                    endIndex = getLineFeedCharacterEndIndex(orgText.indexOf("\n"))
                    resultText += orgText.substring(0, endIndex)
                    orgText = orgText.substring(endIndex)
                } else {
                    resultText += "${orgText.substring(0, endIndex).trim()}\n"
                    orgText = orgText.substring(endIndex).trim()
                }
                if (TextUtils.isEmpty(orgText)) {
                    break
                }
                endIndex = paint.breakText(orgText, true, frameWidth.toFloat(), null)
            }
        }.onFailure {
            resultText = text
        }
        return resultText.trim()
    }

    private fun getLineFeedCharacterEndIndex(baseIndex: Int): Int {
        return if (baseIndex == 0) {
            1
        } else baseIndex
    }
}

/**
 * OnGlobalLayout Listener & Remove
 */
inline fun View.waitForLayout(crossinline function: () -> Unit) {
    viewTreeObserver.addOnGlobalLayoutListener(object : ViewTreeObserver.OnGlobalLayoutListener {
        override fun onGlobalLayout() {
            runCatching {
                viewTreeObserver.removeOnGlobalLayoutListener(this)
                function()
            }
        }
    })
}

/**
 * instead of:
 *  for (i in 0..layout - 1) {
 *      layout.getChildAt(i).visibility = View.GONE
 *  }
 */
inline val ViewGroup.views
    get() = (0 until childCount).map { getChildAt(it) }

/**
 * add View to ViewGroup
 */
operator fun ViewGroup.plusAssign(child: View?) = addView(child)

/**
 * remove view from ViewGroup
 */
operator fun ViewGroup.minusAssign(child: View?) = removeView(child)

/**
 * TextView Ellipsize 여부를 확인한다.
 */
fun TextView?.isEllipsized(): Boolean {
    return if (this == null) {
        false
    } else {
        val lineCount = this.layout?.lineCount
        if (lineCount != null && lineCount > 0) {
            this.layout.getEllipsisCount(lineCount - 1) > 0
        } else {
            false
        }
    }
}

/**
 * Html String을 적용한다.
 */
fun TextView.fromHTML(text: String?) {
    kotlin.runCatching {
        if (text.isNotNullEmpty()) {
            // ex) 내일 6/8(토) 발송 예정
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                this.text = Html.fromHtml(text, Html.FROM_HTML_MODE_COMPACT)
            } else {
                @Suppress("DEPRECATION")
                this.text = Html.fromHtml(text)
            }
        }
    }.onFailure {
        this.text = ""
    }
}

/**
 * TextView html 설정
 *
 *  val tempTextView = TextView(context)
 *  tempTextView.html = "<TAG> String </TAG>"
 */
var TextView.html: String
    get() = this.text.toString()
    set(value) = this.fromHTML(value)

/**
 * 키패드(Soft Keyboard) 비노출
 */
fun EditText.hideIME() {
    ViewUtils.hideIME(this)
}

/**
 * 키패드(Soft Keyboard) 비노출
 */
fun Activity.hideIME() {
    ViewUtils.hideIME(this)
}

/**
 * 키패드(Soft Keyboard) 노출
 */
fun EditText.showIME() {
    ViewUtils.showIME(this, 0)
}

/**
 * 키패드(Soft Keyboard) 토클
 */
fun EditText.toggleIME() {
    ViewUtils.toggleIME(this)
}

/**
 * 글자 단위 개행
 * - 파라미터([pText]) 혹은 기존에 설정되어 있는 text를 글자(Char) 개행을 하고 재 설정을 한다.
 * - [layoutWidthPx] 표시될 TextView 가로 사이즈
 */
fun TextView?.doLineFeedCharWithWidth(layoutWidthPx: Int, pText: String? = null) {
    this ?: return
    ViewUtils.setLineFeedCharacter(this, pText ?: this.text.toString(), layoutWidthPx)
}

/**
 * 글자 단위 개행
 * - 파라미터([pText]) 혹은 기존에 설정되어 있는 text를 글자(Char) 개행을 하고 재 설정을 한다.
 * - Display Width - [leftMarginPx] = TextView width
 */
fun TextView?.doLineFeedCharWithLeftMargin(leftMarginPx: Int, pText: String? = null) {
    this ?: return
    val screenWidth = this.context.screenWidth
    if (screenWidth < leftMarginPx) return
    ViewUtils.setLineFeedCharacter(this, pText ?: this.text.toString(), screenWidth - leftMarginPx)
}

/**
 * RecyclerView 좌우끝 / 중간 마진 Decoration
 * @param dataSize 데이타 갯수
 * @param edge 양끝 수치 (dp)
 * @param between 아이템 사이 수치 (dp)
 */
fun RecyclerView.addMarginItemDecoration(dataSize: Int, edge: Int, between: Int) {
    addItemDecoration(object : RecyclerView.ItemDecoration() {
        override fun getItemOffsets(outRect: Rect, view: View, parent: RecyclerView, state: RecyclerView.State) {
            super.getItemOffsets(outRect, view, parent, state)
            val position = parent.getChildAdapterPosition(view)
            if (position == dataSize - 1) {
                outRect.right += edge.px
            } else {
                outRect.right += between.px
            }
            if (parent.getChildAdapterPosition(view) == 0) {
                outRect.left += edge.px
            }
        }
    })
}

/**
 * RecyclerView Divider
 */
fun RecyclerView.addDefaultDividerDecorations(dividerResourceId: Int, isEnableVertical: Boolean = true, isEnableHorizontal: Boolean = true, specificVerticalViewTypeList: List<Int> = listOf(), specificHorizontalViewTypeList: List<Int> = listOf()) {
    ContextCompat.getDrawable(this.context, dividerResourceId)?.let {
        if (isEnableHorizontal) {
            val dividerHorizontal = object : DividerItemDecoration(this.context, HORIZONTAL) {
                override fun getItemOffsets(outRect: Rect, view: View, parent: RecyclerView, state: RecyclerView.State) {
                    if (specificHorizontalViewTypeList.isNotEmpty()) {
                        val viewType = parent.adapter?.getItemViewType(parent.getChildAdapterPosition(view))
                        if (viewType != null && specificHorizontalViewTypeList.contains(viewType)) {
                            super.getItemOffsets(outRect, view, parent, state)
                        }
                    } else {
                        super.getItemOffsets(outRect, view, parent, state)
                    }
                }
            }
            dividerHorizontal.setDrawable(it)
            this.addItemDecoration(dividerHorizontal)
        }

        if (isEnableVertical) {
            val dividerVertical = object : DividerItemDecoration(this.context, VERTICAL) {
                //가로 줄은 1단, 2단, 광고 딜만 적용한다.
                override fun getItemOffsets(outRect: Rect, view: View, parent: RecyclerView, state: RecyclerView.State) {
                    if (specificVerticalViewTypeList.isNotEmpty()) {
                        val viewType = parent.adapter?.getItemViewType(parent.getChildAdapterPosition(view))
                        if (viewType != null && specificVerticalViewTypeList.contains(viewType)) {
                            super.getItemOffsets(outRect, view, parent, state)
                        }
                    } else {
                        super.getItemOffsets(outRect, view, parent, state)
                    }
                }
            }
            dividerVertical.setDrawable(it)
            this.addItemDecoration(dividerVertical)
        }
    }
}

fun View.collapseAnim(animDuration: Long? = null) {
    val tempView = this
    val initialHeight = tempView.measuredHeight
    val animation = object : Animation() {
        override fun applyTransformation(interpolatedTime: Float, t: Transformation?) {
            if (interpolatedTime == 1f) {
                tempView.visibility = View.GONE
            } else {
                tempView.layoutParams.height = initialHeight - (initialHeight * interpolatedTime).toInt()
                tempView.requestLayout()
            }
        }

        override fun willChangeBounds(): Boolean {
            return true
        }
    }.apply {
        duration = animDuration ?: (initialHeight / tempView.context.resources.displayMetrics.density).toLong()
    }
    tempView.startAnimation(animation)
}

fun View.expandAnim(animDuration: Long? = null) {
    val tempView = this
    tempView.measure(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.WRAP_CONTENT)
    val targetHeight = tempView.measuredHeight
    tempView.layoutParams.height = 0
    tempView.visibility = View.VISIBLE
    val animation = object : Animation() {
        override fun applyTransformation(interpolatedTime: Float, t: Transformation?) {
            tempView.layoutParams.height = if (interpolatedTime == 1f) {
                RelativeLayout.LayoutParams.WRAP_CONTENT
            } else {
                (targetHeight * interpolatedTime).toInt()
            }
            tempView.requestLayout()
        }

        override fun willChangeBounds(): Boolean {
            return true
        }
    }.apply {
        duration = animDuration ?: (targetHeight / tempView.context.resources.displayMetrics.density).toLong()
    }
    tempView.startAnimation(animation)
}

/**
 * 해당 뷰를 숨기는데 현재 위치에서 뷰의 포지션을 height 사이즈 만큼 위로 옮기면서 숨긴다
 */
fun View.collapseAnimOnPosition(animDuration: Long = 300, isDirectionDownToUp: Boolean? = null, isDirectionUpToDown: Boolean? = null) {
    val tempView = this
    val targetHeight = tempView.measuredHeight

    val directionHeight = if (isDirectionDownToUp == true) {
        -targetHeight.toFloat()
    } else if (isDirectionUpToDown == true) {
        targetHeight.toFloat()
    } else {
        -targetHeight.toFloat()
    }

    val outAnim = TranslateAnimation(0f, 0f, 0f, directionHeight).apply {
        duration = animDuration
        interpolator = AccelerateInterpolator()
        setAnimationListener(animationEnd = {
            tempView.isVisible = false
        })
    }
    tempView.startAnimation(outAnim)
}

/**
 * 해당 뷰를 노출하는데 -height 위치에서 뷰의 포지션을 height 사이즈 만큼 위로 옮기면서 숨긴다
 */
fun View.expandAnimOnPosition(animDuration: Long = 300, isDirectionDownToUp: Boolean? = null) {
    val tempView = this
    tempView.measure(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.WRAP_CONTENT)
    val targetHeight = tempView.measuredHeight
    tempView.isVisible = true

    val directionHeight = if (isDirectionDownToUp == true) {
        targetHeight.toFloat()
    } else {
        -targetHeight.toFloat()
    }

    val inAnim = TranslateAnimation(0f, 0f, directionHeight, 0f).apply {
        duration = animDuration
        interpolator = AccelerateInterpolator()
    }
    tempView.startAnimation(inAnim)
}

/**
 * Status bar를 투명하게 만든다.
 */
@SuppressLint("ObsoleteSdkInt")
@Suppress("DEPRECATION")
fun Activity.makeStatusBarTransparent() {
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
        window.apply {
            clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)
            addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                decorView.systemUiVisibility =
                    View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN or View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR
            } else {
                decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
            }
            statusBarColor = Color.TRANSPARENT
        }
    }
}

/**
 * Status bar height
 */
@SuppressLint("InternalInsetResource", "DiscouragedApi")
fun Context.statusBarHeight(): Int {
    val resourceId = resources.getIdentifier("status_bar_height", "dimen", "android")
    return if (resourceId > 0) resources.getDimensionPixelSize(resourceId)
    else 0
}

/**
 * 하단 소프트키가 있는 navigation height
 */
@SuppressLint("InternalInsetResource", "DiscouragedApi")
fun Context.navigationHeight(): Int {
    val resourceId = resources.getIdentifier("navigation_bar_height", "dimen", "android")
    return if (resourceId > 0) resources.getDimensionPixelSize(resourceId)
    else 0
}

fun Activity.setStatusBarTransparent() {
    window.apply {
        setFlags(
            WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS,
            WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS
        )
    }
    if (Build.VERSION.SDK_INT >= 30) {
        WindowCompat.setDecorFitsSystemWindows(window, false)
    }
}

fun Activity.setStatusBarOrigin() {
    window.apply {
        clearFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS)
    }
    if (Build.VERSION.SDK_INT >= 30) {
        WindowCompat.setDecorFitsSystemWindows(window, true)
    }
}