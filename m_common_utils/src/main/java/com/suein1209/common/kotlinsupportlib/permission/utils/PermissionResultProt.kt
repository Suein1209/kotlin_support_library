package com.suein1209.common.kotlinsupportlib.permission.utils

import android.app.Activity
import com.suein1209.common.kotlinsupportlib.permission.PermissionResultCallback
import com.suein1209.common.kotlinsupportlib.permission.common.PermissionResultState

interface PermissionResultProt {
    /**
     * Fragment & Activity 시스템 다이얼로그 후 선택된 결과에 대한 처리
     */
    fun onCommonResultAfterSystemDialog(activity: Activity, permissionsResult: Map<String, Boolean>, isPriorityAlreadyDenied: Boolean, callback: PermissionResultCallback) {
        val permissionNameList = permissionsResult.keys.toList()
        val resultState = if (permissionsResult.values.contains(false)) {
            //거절 된게 있다면
            if (isPriorityAlreadyDenied) {
                PermissionResultState.DENIED
            } else {
                if (PermissionCheckSaveStateMgr.isExistsCompleteDenied(activity, permissionNameList)) {
                    PermissionResultState.COMPLETE_DENIED
                } else {
                    PermissionCheckSaveStateMgr.setCompleteDeniedWhenAlreadyDenied(activity, permissionNameList)
                    PermissionResultState.DENIED
                }
            }
        } else {
            //승인이 된것은 삭제 한다.
            PermissionCheckSaveStateMgr.removeState(activity, permissionNameList)
            PermissionResultState.GRANTED
        }
        callback(resultState)
    }
}