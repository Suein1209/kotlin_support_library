package com.suein1209.common.kotlinsupportlib.permission.utils

import android.app.Activity
import android.content.pm.PackageManager
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.suein1209.common.kotlinsupportlib.permission.common.PermissionResultState

interface PermissionStateProt : PermissionResultProt {

    /**
     * 현재 설정되어 있는 권한 상태를 반환한다.
     *  - [PermissionResultState.DENIED] : 권한 요청을 한적이 없는 상태
     *  - [PermissionResultState.GRANTED] : 권한을 얻은 상태
     *  - [PermissionResultState.COMPLETE_DENIED] : 이전에 권한을 불허한 상태 혹은 시스템 세팅에서 권한 해제
     *  -- for Fragment
     */
    fun Fragment.getPermissionsState(permissions: List<String>, isPriorityAlreadyDenied: Boolean): PermissionResultState {
        return requireActivity().getPermissionsState(permissions, isPriorityAlreadyDenied)
    }

    /**
     * 현재 설정되어 있는 권한 상태를 반환한다.
     *  - [PermissionResultState.DENIED] : 권한 요청을 한적이 없는 상태
     *  - [PermissionResultState.GRANTED] : 권한을 얻은 상태
     *  - [PermissionResultState.COMPLETE_DENIED] : 이전에 권한을 거절 상태 혹은 시스템 세팅에서 권한 해제
     *  -- for Activity
     */
    fun Activity.getPermissionsState(permissions: List<String>, isPriorityAlreadyDenied: Boolean): PermissionResultState {
        return if (permissions.map { permission -> ContextCompat.checkSelfPermission(this, permission) == PackageManager.PERMISSION_GRANTED }.contains(false)) {
            //퍼미션이 없다.
            if (isPriorityAlreadyDenied) {
                getPermissionsDeniedStateWithAlreadyDenied(permissions) //AlreadyDenied 가 한 개라도 있다면, 그러니깐 하나라도 isShowRequestPermissionRationale == true 라면 ALREADY_DENIED를 반환한다.
            } else {
                getPermissionsDeniedStateWithOutAlreadyDenied(permissions) //AlreadyDenied 가 아닌게 한 개라도 있다면, 그러니깐 하나라도 isShowRequestPermissionRationale == false 라면 DENIED를 반환한다.
            }
        } else PermissionResultState.GRANTED //퍼미션이 승인되어 있다.
    }

    /**
     * isShowRequestPermissionRationale를 무시한채 Denied 값을 가져온다.
     * - 차후 isShowRequestPermissionRationale == true 더라도 request를 요청하는 목적으로 사용된다.
     */
    private fun Activity.getPermissionsDeniedStateWithAlreadyDenied(permissions: List<String>): PermissionResultState {
        return if (permissions.toTypedArray().map {
                ActivityCompat.shouldShowRequestPermissionRationale(this, it)
            }.contains(true)) {
            PermissionResultState.ALREADY_DENIED
        } else {
            PermissionResultState.DENIED
        }
    }

    /**
     * isShowRequestPermissionRationale 체크를 우선시해 Denied 값을 가져온다.
     */
    private fun Activity.getPermissionsDeniedStateWithOutAlreadyDenied(permissions: List<String>): PermissionResultState {
        val isExistsDenied = permissions.map { permissionName -> PermissionCheckSaveStateMgr.isCompleteDenied(this, permissionName) }.contains(false) // 완벽하게 거절되지 않은 경우가 있다
        return if (isExistsDenied) {
            //완벽하게 거절되지 않은 경우가 있다면 PermissionResultState.DENIED
            if (permissions.toTypedArray().map { ActivityCompat.shouldShowRequestPermissionRationale(this, it) }.contains(false)) {
                PermissionResultState.DENIED
            } else {
                PermissionResultState.ALREADY_DENIED
            }
        } else {
            //모두 완벽하게 거절된경우
            PermissionResultState.COMPLETE_DENIED
        }
    }
}