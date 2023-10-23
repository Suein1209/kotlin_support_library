package com.suein1209.common.kotlinsupportlib

import android.app.Activity
import android.graphics.Color
import android.graphics.Rect
import android.graphics.drawable.ColorDrawable
import android.util.Log
import android.view.Gravity
import android.view.View
import android.view.ViewTreeObserver
import android.view.WindowManager
import android.widget.PopupWindow
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.DefaultLifecycleObserver
import androidx.lifecycle.LifecycleOwner
import com.suein1209.common.kotlinsupportlib.common.CommonUtilsConst
import java.util.concurrent.atomic.AtomicBoolean

@Suppress("unused")
class KeyboardDetectHelper(
    private val activity: Activity,
    private val onDetectKeyboard: ((isShow: Boolean) -> Unit)
) : PopupWindow(activity) {

    @Suppress("PrivatePropertyName")
    private val RATIO = 150
    private var rootView = View(activity)
    private var rootViewHeight = 0
    private val isShowKeyboard = AtomicBoolean(false)

    fun isShowKeyboard() = isShowKeyboard.get()

    private val onGlobalLayoutListener = ViewTreeObserver.OnGlobalLayoutListener {
        try {
            val imeVisible = if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.R) {
                ViewCompat.getRootWindowInsets(rootView)?.isVisible(WindowInsetsCompat.Type.ime()) ?: false
            } else {
                val rect = Rect()
                rootView.getWindowVisibleDisplayFrame(rect)

                if (rect.bottom > rootViewHeight) {
                    rootViewHeight = rect.bottom
                }

                rootViewHeight - rect.bottom > RATIO // keyboardHeight = rootViewHeight - rect.bottom
            }

            if (isShowKeyboard.compareAndSet(!imeVisible, imeVisible)) {
                onDetectKeyboard.invoke(imeVisible)
            }
        } catch (e: Exception) {
            if (BuildConfig.DEBUG) {
                Log.e(CommonUtilsConst.LOG_TAG, e.stackTraceToString())
            }
        }
    }

    private val lifecycleObserver = object : DefaultLifecycleObserver {
        override fun onResume(owner: LifecycleOwner) {
            super.onResume(owner)
            onCreate()
        }

        override fun onDestroy(owner: LifecycleOwner) {
            if (activity is AppCompatActivity) {
                activity.lifecycle.removeObserver(this)
            }
            onDestroy()
            super.onDestroy(owner)
        }
    }

    init {
        if (activity is AppCompatActivity && !activity.isDestroyed && !activity.isFinishing) {
            activity.lifecycle.addObserver(lifecycleObserver)
        }
    }

    fun onCreate() {
        if (activity.isDestroyed || activity.isFinishing) return
        try {
            contentView = rootView
            rootView.viewTreeObserver?.run {
                removeOnGlobalLayoutListener(onGlobalLayoutListener)
                addOnGlobalLayoutListener(onGlobalLayoutListener)
            }

            width = 1
            height = WindowManager.LayoutParams.MATCH_PARENT
            setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
            inputMethodMode = INPUT_METHOD_NEEDED
            @Suppress("DEPRECATION")
            if (android.os.Build.VERSION.SDK_INT < android.os.Build.VERSION_CODES.R) {
                /*
                This constant was deprecated in API level 30.
                Call Window#setDecorFitsSystemWindows(boolean) with false and install an OnApplyWindowInsetsListener on your root content view that fits insets of type Type#ime().
                 */
                softInputMode = WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE
            }

            if (!isShowing) {
                activity.window.decorView.let {
                    it.post {
                        showAtLocation(it, Gravity.NO_GRAVITY, 0, 0)
                    }
                }
            }
        } catch (e: Exception) {
            if (BuildConfig.DEBUG) {
                Log.e(CommonUtilsConst.LOG_TAG, e.stackTraceToString())
            }
        }
    }

    fun onDestroy() {
        try {
            dismiss()
            rootView.viewTreeObserver.removeOnGlobalLayoutListener(onGlobalLayoutListener)
        } catch (e: Exception) {
            if (BuildConfig.DEBUG) {
                Log.e(CommonUtilsConst.LOG_TAG, e.stackTraceToString())
            }
        }
    }
}