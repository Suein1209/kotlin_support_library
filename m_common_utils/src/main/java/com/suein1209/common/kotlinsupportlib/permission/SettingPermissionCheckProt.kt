package com.suein1209.common.kotlinsupportlib.permission

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.provider.Settings
import androidx.activity.ComponentActivity
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.app.NotificationManagerCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.DefaultLifecycleObserver
import androidx.lifecycle.LifecycleOwner
import com.suein1209.common.kotlinsupportlib.permission.common.PermissionResultState
import com.suein1209.common.kotlinsupportlib.permission.utils.PermissionStateProt

/**
 * Setting Activity Permission Check 를 편하게 사용하도록
 */
interface SettingPermissionCheckProt : PermissionStateProt {

    private companion object {
        var singleLauncherMap: SingleLauncherPrevInfo? = null

        private class SingleLauncherPrevInfo(var checkPermission: List<String>, var prevState: PermissionResultState)

        private const val NotificationPermissionName = "android.permission.POST_NOTIFICATIONS"
    }

    /**
     * setting permission check launcher 실행
     * for Fragment
     */
    fun Fragment.launchSettingPermissionActivity(permissions: List<String>, launcher: ActivityResultLauncher<Intent>) {
        if (permissions.isEmpty()) {
            throw IllegalAccessException("확인할 수 있는 권한이 없습니다.")
        }
        launchSettingActivity(activity = this.requireActivity(), launcher = launcher, permissions = permissions)
    }

    /**
     * setting permission check launcher 실행
     * for Activity
     */
    fun ComponentActivity.launchSettingPermissionActivity(permissions: List<String>, launcher: ActivityResultLauncher<Intent>) {
        if (permissions.isEmpty()) {
            throw IllegalAccessException("확인할 수 있는 권한이 없습니다.")
        }
        launchSettingActivity(activity = this, launcher = launcher, permissions = permissions)
    }

    fun ComponentActivity.startNotificationSettingPermissionActivity(activity: Activity, onSettingActivityResultState: SettingActivityResultState) {
        val currentPrevState = activity.getNotificationPermissionState()
        singleLauncherMap = SingleLauncherPrevInfo(prevState = currentPrevState, checkPermission = listOf(NotificationPermissionName))

        val intent = Intent().apply {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                action = Settings.ACTION_APP_NOTIFICATION_SETTINGS
                putExtra(Settings.EXTRA_APP_PACKAGE, activity.packageName)
            } else {
                action = "android.settings.APP_NOTIFICATION_SETTINGS"
                putExtra("app_package", activity.packageName)
                putExtra("app_uid", activity.applicationInfo?.uid)
            }
            flags = Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
        }

        lifecycle.addObserver(object : DefaultLifecycleObserver {
            var isNeedPass = false
            override fun onCreate(owner: LifecycleOwner) {
                super.onCreate(owner)
                isNeedPass = true
            }

            override fun onResume(owner: LifecycleOwner) {
                super.onResume(owner)
                if (!isNeedPass) {
                    lifecycle.removeObserver(this)
                    onSettingActivityResult(this@startNotificationSettingPermissionActivity, onSettingActivityResultState)
                    singleLauncherMap = null
                } else {
                    isNeedPass = false
                }
            }
        })

        startActivity(intent)
    }

    /**
     * setting permission check Activity for result callback 처리
     * for Activity
     */
    fun ComponentActivity.getSettingPermissionActivityResultLauncher(onSettingActivityResult: SettingActivityResultState): ActivityResultLauncher<Intent> {
        val launcher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
            onSettingActivityResult(this, onSettingActivityResult)
        }
        lifecycle.addObserver(object : DefaultLifecycleObserver {
            override fun onDestroy(owner: LifecycleOwner) {
                singleLauncherMap = null
                super.onDestroy(owner)
            }
        })

        return launcher
    }

    /**
     * setting permission check Activity for result callback 처리
     * for Activity
     */
    fun Fragment.getSettingPermissionActivityResultLauncher(onSettingActivityResultState: SettingActivityResultState): ActivityResultLauncher<Intent> {
        val launcher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
            onSettingActivityResult(requireActivity(), onSettingActivityResultState)
        }

        lifecycle.addObserver(object : DefaultLifecycleObserver {
            override fun onDestroy(owner: LifecycleOwner) {
                singleLauncherMap = null
                super.onDestroy(owner)
            }
        })
        return launcher
    }

    private fun Context.getNotificationPermissionState(): PermissionResultState {
        return if (NotificationManagerCompat.from(this).areNotificationsEnabled()) {
            PermissionResultState.GRANTED
        } else {
            PermissionResultState.DENIED
        }
    }

    private fun launchSettingActivity(activity: Activity, launcher: ActivityResultLauncher<Intent>, permissions: List<String>) {
        val currentState = activity.getPermissionsState(permissions, isPriorityAlreadyDenied = true)
        singleLauncherMap = SingleLauncherPrevInfo(prevState = currentState, checkPermission = permissions)
        val intent = Intent().apply {
            action = Settings.ACTION_APPLICATION_DETAILS_SETTINGS
            data = Uri.fromParts("package", activity.packageName, null)
            addCategory(Intent.CATEGORY_DEFAULT)
        }
        launcher.launch(intent)
    }

    private fun onSettingActivityResult(activity: Activity, onSettingActivityResultState: SettingActivityResultState) {
        if (singleLauncherMap != null) {
            val currentPermissionSate = activity.getPermissionStateAfterSetting(singleLauncherMap!!.checkPermission)
            onSettingActivityResultState(singleLauncherMap!!.prevState != currentPermissionSate, currentPermissionSate, singleLauncherMap!!.checkPermission)
        } else {
//            WLog.printStackTrace(IllegalAccessException("설정한 권한 정보를 찾을 수 없습니다..."))
        }
    }

    private fun Activity.getPermissionStateAfterSetting(permissions: List<String>): PermissionResultState {
        return if (isNotificationPermission(permissions)) {
            getNotificationPermissionState()
        } else {
            getPermissionsState(permissions, isPriorityAlreadyDenied = true)
        }
    }

    private fun isNotificationPermission(permissions: List<String>) = permissions.size == 1 && permissions.contains(NotificationPermissionName)

    fun Activity.startNotificationSettingActivity() {
        Intent().apply {
            action = Settings.ACTION_APPLICATION_DETAILS_SETTINGS
            data = Uri.fromParts("package", this@startNotificationSettingActivity.packageName, null)
            addCategory(Intent.CATEGORY_DEFAULT)
        }.let { startActivity(it) }
    }

}

typealias SettingActivityResultState = (isChanged: Boolean, state: PermissionResultState, permissions: List<String>) -> Unit