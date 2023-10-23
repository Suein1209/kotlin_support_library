package com.suein1209.common.kotlinsupportlib.permission

import android.content.Context
import androidx.activity.ComponentActivity
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.Fragment
import com.suein1209.common.kotlinsupportlib.permission.common.PermissionResultState
import com.suein1209.common.kotlinsupportlib.permission.utils.PermissionCheckSaveStateMgr

/**
 * 권한(Permission) 체크의 기본 기능들
 */
interface PermissionCheckProt : PermissionCheckAfterInitializedProt {

    /**
     * 권한 체크 후 만약 권한을 요청한적 없다면 요청하는 System Dialog 를 띄운다.
     *
     * - 파라미터 vararg Type
     * - 결과값을 람다 콜백으로 전달한다
     * -- [granted] : 권한을 승인
     * -- [denied] : 권한을 거절
     * -- [completeDenied] : 이미 이전에 권한을 거절 혹은 시스템 세팅에서 권한 해제
     *
     * --- for Activity
     */
    fun ComponentActivity.checkAndRequestPermission(vararg permissions: String, isPriorityAlreadyDenied: Boolean = false, granted: () -> Unit = {}, denied: () -> Unit = {}, completeDenied: () -> Unit = {}) {
        checkAndRequestPermission(permissions.toList(), isPriorityAlreadyDenied = isPriorityAlreadyDenied) { permissionResultState -> onCommonLambdaResult(permissionResultState, isPriorityAlreadyDenied = isPriorityAlreadyDenied, granted, denied, completeDenied) }
    }

    /**
     * 권한 체크 후 만약 권한을 요청한적 없다면 요청하는 System Dialog 를 띄운다.
     *
     * - 파라미터 vararg Type
     * - 결과값을 람다 콜백으로 전달한다
     * -- [granted] : 권한을 승인
     * -- [denied] : 권한을 거절
     * -- [completeDenied] : 이미 이전에 권한을 거절 혹은 시스템 세팅에서 권한 해제
     *
     * --- for Fragment
     */
    fun Fragment.checkAndRequestPermission(vararg permissions: String, isPriorityAlreadyDenied: Boolean = false, granted: () -> Unit = {}, denied: () -> Unit = {}, completeDenied: () -> Unit = {}) {
        checkAndRequestPermission(permissions.toList(), isPriorityAlreadyDenied = isPriorityAlreadyDenied) { permissionResultState -> onCommonLambdaResult(permissionResultState, isPriorityAlreadyDenied = isPriorityAlreadyDenied, granted, denied, completeDenied) }
    }

    /**
     * 권한 체크 후 만약 권한을 요청한적 없다면 요청하는 System Dialog 를 띄운다.
     *
     * - 파라미터 List Type
     * - 결과값을 람다 콜백으로 전달한다
     * -- [granted] : 권한을 승인
     * -- [denied] : 권한을 거절
     * -- [completeDenied] : 이미 이전에 권한을 거절 혹은 시스템 세팅에서 권한 해제
     *
     * --- for Fragment
     */
    fun Fragment.checkAndRequestPermission(permissionList: List<String>, isPriorityAlreadyDenied: Boolean = false, granted: () -> Unit = {}, denied: () -> Unit = {}, completeDenied: () -> Unit = {}) {
        checkAndRequestPermission(permissionList, isPriorityAlreadyDenied = isPriorityAlreadyDenied) { permissionResultState -> onCommonLambdaResult(permissionResultState, isPriorityAlreadyDenied = isPriorityAlreadyDenied, granted, denied, completeDenied) }
    }

    /**
     * 권한 체크 후 만약 권한을 요청한적 없다면 요청하는 System Dialog 를 띄운다.
     *
     * - 파라미터 List Type
     * - 결과값을 람다 콜백으로 전달한다
     * -- [granted] : 권한을 승인
     * -- [denied] : 권한을 거절
     * -- [completeDenied] : 이미 이전에 권한을 거절 혹은 시스템 세팅에서 권한 해제
     *
     * --- for Activity
     */
    fun ComponentActivity.checkAndRequestPermission(permissionList: List<String>, isPriorityAlreadyDenied: Boolean = false, granted: () -> Unit = {}, denied: () -> Unit = {}, completeDenied: () -> Unit = {}) {
        checkAndRequestPermission(permissionList, isPriorityAlreadyDenied = isPriorityAlreadyDenied) { permissionResultState -> onCommonLambdaResult(permissionResultState, isPriorityAlreadyDenied = isPriorityAlreadyDenied, granted, denied, completeDenied) }
    }

    /**
     * 공통 Lambda type result 처리
     */
    fun onCommonLambdaResult(permissionResultState: PermissionResultState, isPriorityAlreadyDenied: Boolean = false, granted: () -> Unit, denied: () -> Unit, completeDenied: () -> Unit) {
        when (permissionResultState) {
            PermissionResultState.GRANTED -> granted()
            PermissionResultState.DENIED -> denied()
            PermissionResultState.ALREADY_DENIED -> if (isPriorityAlreadyDenied) completeDenied() else denied()
            PermissionResultState.COMPLETE_DENIED -> completeDenied()
        }
    }

    /**
     * 권한 체크 후 만약 권한을 요청한적 없다면 요청하는 System Dialog 를 띄운다.
     * - 결과값을 [PermissionResultState] 상태로 전달한다.
     * -- [[PermissionResultState.GRANTED] : 권한을 승인
     * -- [PermissionResultState.DENIED] : 권한을 거절
     * -- [PermissionResultState.COMPLETE_DENIED] : 이미 이전에 권한을 거절 혹은 시스템 세팅에서 권한 해제
     *
     * --- for Activity
     */
    fun ComponentActivity.checkAndRequestPermission(vararg permissions: String, isPriorityAlreadyDenied: Boolean = false, callback: PermissionResultCallback) {
        checkAndRequestPermission(permissionList = permissions.toList(), isPriorityAlreadyDenied = isPriorityAlreadyDenied, callback = callback)
    }

    /**
     * 권한 체크 후 만약 권한을 요청한적 없다면 요청하는 System Dialog 를 띄운다.
     * - 결과값을 [PermissionResultState] 상태로 전달한다.
     * -- [[PermissionResultState.GRANTED] : 권한을 승인
     * -- [PermissionResultState.DENIED] : 권한을 거절
     * -- [PermissionResultState.COMPLETE_DENIED] : 이미 이전에 권한을 거절 혹은 시스템 세팅에서 권한 해제
     *
     * --- for Fragment
     */
    fun Fragment.checkAndRequestPermission(vararg permissions: String, isPriorityAlreadyDenied: Boolean = false, callback: PermissionResultCallback) {
        checkAndRequestPermission(permissionList = permissions.toList(), isPriorityAlreadyDenied = isPriorityAlreadyDenied, callback = callback)
    }

    /**
     * 권한 체크 후 만약 권한을 요청한적 없다면 요청하는 System Dialog 를 띄운다.
     * - Permission Name 을 List Param 으로 전달받는다.[permissionList]
     * - 결과값을 [PermissionResultState] 상태로 전달한다.
     * -- [[PermissionResultState.GRANTED] : 권한을 승인
     * -- [PermissionResultState.DENIED] : 권한을 거절
     * -- [PermissionResultState.COMPLETE_DENIED] : 이미 이전에 권한을 거절 혹은 시스템 세팅에서 권한 해제
     * -- [isPriorityAlreadyDenied] : shouldShowRequestPermissionRationale == true 일시 퍼미션 체크를 요청하지 않는다.
     * --- for Activity
     */
    fun ComponentActivity.checkAndRequestPermission(permissionList: List<String>, isPriorityAlreadyDenied: Boolean = false, callback: PermissionResultCallback) {
        onPermissionResult(this, permissionList, nowState = getPermissionsState(permissionList, isPriorityAlreadyDenied = isPriorityAlreadyDenied), isPriorityAlreadyDenied = isPriorityAlreadyDenied, callback = callback)
    }

    /**
     * 권한 체크 후 만약 권한을 요청한적 없다면 요청하는 System Dialog 를 띄운다.
     * - Permission Name 을 List Param 으로 전달받는다.[permissionList]
     * - 결과값을 [PermissionResultState] 상태로 전달한다.
     * -- [[PermissionResultState.GRANTED] : 권한을 승인
     * -- [PermissionResultState.DENIED] : 권한을 거절
     * -- [PermissionResultState.COMPLETE_DENIED] : 이미 이전에 권한을 거절 혹은 시스템 세팅에서 권한 해제
     * -- [isPriorityAlreadyDenied] : shouldShowRequestPermissionRationale == true 일시 퍼미션 체크를 요청하지 않는다.
     * --- for Fragment
     */
    fun Fragment.checkAndRequestPermission(permissionList: List<String>, isPriorityAlreadyDenied: Boolean = false, callback: PermissionResultCallback) {
        onPermissionResult(this, permissionList, nowState = getPermissionsState(permissionList, isPriorityAlreadyDenied), isPriorityAlreadyDenied = isPriorityAlreadyDenied, callback = callback)
    }

    private fun getContext(from: Any): Context? {
        return when (from) {
            is Fragment -> from.requireActivity()
            is ComponentActivity -> from
            else -> null
        }
    }

    /**
     * 공통 현재 결과에 대한 처리
     * - [isPriorityAlreadyDenied] : shouldShowRequestPermissionRationale == true 일시 퍼미션 체크를 요청하지 않는다.
     */
    private fun onPermissionResult(from: Any, permissionList: List<String>, nowState: PermissionResultState, isPriorityAlreadyDenied: Boolean, callback: PermissionResultCallback) {
        when (nowState) {
            PermissionResultState.GRANTED -> {
                getContext(from)?.let { context -> PermissionCheckSaveStateMgr.removeState(context, permissionList) }
                callback(PermissionResultState.GRANTED)
            }
            PermissionResultState.DENIED -> startForResult(from, permissionList, isPriorityAlreadyDenied, callback) //거절되어 있는건 현재 퍼미션이 승인되지 않은것과 같은 상태이다. 그래서 퍼미션을 요청한다.
            PermissionResultState.ALREADY_DENIED -> {
                if (isPriorityAlreadyDenied) {
                    callback(PermissionResultState.ALREADY_DENIED)
                } else {
                    //shouldShowRequestPermissionRationale == true 여도 request permission 을 요청한다.
                    getContext(from)?.let { context -> PermissionCheckSaveStateMgr.setAlreadyDenied(context, permissionList) } // already denied 설정
                    startForResult(from, permissionList, false, callback)
                }
            }

            PermissionResultState.COMPLETE_DENIED -> callback(PermissionResultState.COMPLETE_DENIED)
        }
    }

    /**
     * activity for result 를 등록한다.
     */
    private fun startForResult(from: Any, permissionList: List<String>, isPriorityAlreadyDenied: Boolean, callback: PermissionResultCallback) {
        if (from is Fragment) {
            from.onFragmentForResult(permissionList, isPriorityAlreadyDenied, callback)
        } else if (from is ComponentActivity) {
            from.onActivityForResult(permissionList, isPriorityAlreadyDenied, callback)
        }
    }

    /**
     * Fragment상 Activity for result 처리
     */
    private fun Fragment.onFragmentForResult(permissionList: List<String>, isPriorityAlreadyDenied: Boolean, callback: PermissionResultCallback) {
        registerForActivityResult(ActivityResultContracts.RequestMultiplePermissions()) { permissionsResult ->
            onCommonResultAfterSystemDialog(activity = requireActivity(), permissionsResult = permissionsResult, isPriorityAlreadyDenied = isPriorityAlreadyDenied, callback = callback)
        }.launch(permissionList.toList().toTypedArray())
    }

    /**
     * Fragment상 Activity for result 처리
     */
    private fun ComponentActivity.onActivityForResult(permissionList: List<String>, isPriorityAlreadyDenied: Boolean, callback: PermissionResultCallback) {
        registerForActivityResult(ActivityResultContracts.RequestMultiplePermissions()) { permissionsResult ->
            onCommonResultAfterSystemDialog(activity = this, permissionsResult = permissionsResult, isPriorityAlreadyDenied = isPriorityAlreadyDenied, callback = callback)
        }.launch(permissionList.toList().toTypedArray())
    }
}

/**
 * 사용자에 의한 권한 선택후 enum 타입
 */
typealias PermissionResultCallback = (permissionResultState: PermissionResultState) -> Unit