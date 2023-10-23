package com.suein1209.common.kotlinsupportlib.permission

import android.Manifest
import android.os.Build
import androidx.activity.ComponentActivity
import androidx.fragment.app.Fragment
import com.suein1209.common.kotlinsupportlib.permission.PermissionCheckProt
import com.suein1209.common.kotlinsupportlib.permission.PermissionResultCallback
import com.suein1209.common.kotlinsupportlib.permission.common.PermissionResultState

/**
 * 미디어 스토리지 관련 권한 처리 기능들
 */
interface PermissionMediaStorageProt : PermissionCheckProt {

    /**
     * 저장소 읽기 권한 공통 처리
     */
    private fun onCommonCheckAndResultStorageReadPermission(from: Any, includeAudio: Boolean = false, callback: (permissionResultState: PermissionResultState) -> Unit) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            val permissions = mutableListOf(Manifest.permission.READ_MEDIA_IMAGES, Manifest.permission.READ_MEDIA_VIDEO)
            if (includeAudio) {
                permissions.add(Manifest.permission.READ_MEDIA_AUDIO)
            }
            if (from is Fragment) {
                from.checkAndRequestPermission(permissions, callback = callback)
            } else if (from is ComponentActivity) {
                from.checkAndRequestPermission(permissions, callback = callback)
            }
        } else {
            val under33StoragePermission = Manifest.permission.READ_EXTERNAL_STORAGE
            if (from is Fragment) {
                from.checkAndRequestPermission(under33StoragePermission, callback = callback)
            } else if (from is ComponentActivity) {
                from.checkAndRequestPermission(under33StoragePermission, callback = callback)
            }
        }
    }

    /**
     * 미디어 저장소 읽기 권한을 체크하고 만약 체크한적이 없다면 요청 System Dialog 를 띄운다.
     * - [includeAudio] : 기본적으로 제외되어 있다.(위메프에서는 사진과 동영상만 취급하기때문)
     *
     * - 결과값을 [PermissionResultState] 상태로 전달한다.
     * -- [[PermissionResultState.GRANTED] : 권한을 승인
     * -- [PermissionResultState.DENIED] : 권한을 거절
     * -- [PermissionResultState.COMPLETE_DENIED] : 이미 이전에 권한을 거절
     *
     * --- for Fragment
     */
    fun Fragment.checkAndRequestStorageReadPermission(includeAudio: Boolean = false, callback: (permissionResultState: PermissionResultState) -> Unit) {
        onCommonCheckAndResultStorageReadPermission(from = this, includeAudio = includeAudio, callback = callback)
    }

    /**
     * 미디어 저장소 읽기 권한을 체크하고 만약 체크한적이 없다면 요청 System Dialog 를 띄운다.
     * - [includeAudio] : 기본적으로 제외되어 있다.(위메프에서는 사진과 동영상만 취급하기때문)
     *
     * - 결과값을 [PermissionResultState] 상태로 전달한다.
     * -- [[PermissionResultState.GRANTED] : 권한을 승인
     * -- [PermissionResultState.DENIED] : 권한을 거절
     * -- [PermissionResultState.COMPLETE_DENIED] : 이미 이전에 권한을 거절
     *
     * --- for Activity
     */
    fun ComponentActivity.checkAndRequestStorageReadPermission(includeAudio: Boolean = false, callback: (permissionResultState: PermissionResultState) -> Unit) {
        onCommonCheckAndResultStorageReadPermission(from = this, includeAudio = includeAudio, callback = callback)
    }

    /**
     * 미디어 저장소 읽기 권한을 체크하고 만약 체크한적이 없다면 요청 System Dialog 를 띄운다.
     * - [includeAudio] : 기본적으로 제외되어 있다.(위메프에서는 사진과 동영상만 취급하기때문)
     *
     * - 결과값을 람다 콜백으로 전달한다
     * -- [granted] : 권한을 승인
     * -- [denied] : 권한을 거절
     * -- [completeDenied] : 이미 이전에 권한을 거절 혹은 시스템 세팅에서 권한 해제
     *
     * --- for Fragment
     */
    fun Fragment.checkAndRequestStorageReadPermission(includeAudio: Boolean = false, granted: () -> Unit = {}, denied: () -> Unit = {}, completeDenied: () -> Unit = {}) {
        onCommonCheckAndResultStorageReadPermission(from = this, includeAudio = includeAudio) { permissionResultState ->
            onCommonLambdaResult(permissionResultState = permissionResultState, granted = granted, denied = denied, completeDenied = completeDenied)
        }
    }

    /**
     * 미디어 저장소 읽기 권한을 체크하고 만약 체크한적이 없다면 요청 System Dialog 를 띄운다.
     * - [includeAudio] : 기본적으로 제외되어 있다.(위메프에서는 사진과 동영상만 취급하기때문)
     *
     * - 결과값을 람다 콜백으로 전달한다
     * -- [granted] : 권한을 승인
     * -- [denied] : 권한을 거절
     * -- [completeDenied] : 이미 이전에 권한을 거절 혹은 시스템 세팅에서 권한 해제
     *
     * --- for Activity
     */
    fun ComponentActivity.checkAndRequestStorageReadPermission(includeAudio: Boolean = false, granted: () -> Unit = {}, denied: () -> Unit = {}, completeDenied: () -> Unit = {}) {
        onCommonCheckAndResultStorageReadPermission(from = this, includeAudio = includeAudio) { permissionResultState ->
            onCommonLambdaResult(permissionResultState = permissionResultState, granted = granted, denied = denied, completeDenied = completeDenied)
        }
    }


    /**
     * 저장소 쓰기 권한 공통 처리
     */
    private fun onCommonCheckAndResultStorageWritePermission(from: Any, callback: (permissionResultState: PermissionResultState) -> Unit) {
        val storageWritePermission = Manifest.permission.WRITE_EXTERNAL_STORAGE
        if (from is Fragment) {
            from.checkAndRequestPermission(storageWritePermission, callback = callback)
        } else if (from is ComponentActivity) {
            from.checkAndRequestPermission(storageWritePermission, callback = callback)
        }
    }

    /**
     * 미디어 저장소 쓰기 권한을 체크하고 만약 체크한적이 없다면 요청 System Dialog 를 띄운다.
     *
     * - 결과값을 [PermissionResultState] 상태로 전달한다.
     * -- [[PermissionResultState.GRANTED] : 권한을 승인
     * -- [PermissionResultState.DENIED] : 권한을 거절
     * -- [PermissionResultState.COMPLETE_DENIED] : 이미 이전에 권한을 거절
     *
     * --- for Fragment
     */
    fun Fragment.checkAndRequestStorageWritePermission(callback: PermissionResultCallback) {
        onCommonCheckAndResultStorageWritePermission(from = this, callback = callback)
    }

    /**
     * 미디어 저장소 쓰기 권한을 체크하고 만약 체크한적이 없다면 요청 System Dialog 를 띄운다.
     *
     * - 결과값을 [PermissionResultState] 상태로 전달한다.
     * -- [[PermissionResultState.GRANTED] : 권한을 승인
     * -- [PermissionResultState.DENIED] : 권한을 거절
     * -- [PermissionResultState.COMPLETE_DENIED] : 이미 이전에 권한을 거절
     *
     * --- for Activity
     */
    fun ComponentActivity.checkAndRequestStorageWritePermission(callback: PermissionResultCallback) {
        onCommonCheckAndResultStorageWritePermission(from = this, callback = callback)
    }

    /**
     * 미디어 저장소 쓰기 권한을 체크하고 만약 체크한적이 없다면 요청 System Dialog 를 띄운다.
     *
     * - 결과값을 람다 콜백으로 전달한다
     * -- [granted] : 권한을 승인
     * -- [denied] : 권한을 거절
     * -- [completeDenied] : 이미 이전에 권한을 거절 혹은 시스템 세팅에서 권한 해제
     *
     * --- for Fragment
     */
    fun Fragment.checkAndRequestStorageWritePermission(granted: () -> Unit = {}, denied: () -> Unit = {}, completeDenied: () -> Unit = {}) {
        onCommonCheckAndResultStorageWritePermission(from = this) { permissionResultState ->
            onCommonLambdaResult(permissionResultState = permissionResultState, granted = granted, denied = denied, completeDenied = completeDenied)
        }
    }

    /**
     * 미디어 저장소 쓰기 권한을 체크하고 만약 체크한적이 없다면 요청 System Dialog 를 띄운다.
     *
     * - 결과값을 람다 콜백으로 전달한다
     * -- [granted] : 권한을 승인
     * -- [denied] : 권한을 거절
     * -- [completeDenied] : 이미 이전에 권한을 거절 혹은 시스템 세팅에서 권한 해제
     *
     * --- for Activity
     */
    fun ComponentActivity.checkAndRequestStorageWritePermission(granted: () -> Unit = {}, denied: () -> Unit = {}, completeDenied: () -> Unit = {}) {
        onCommonCheckAndResultStorageWritePermission(from = this) { permissionResultState ->
            onCommonLambdaResult(permissionResultState = permissionResultState, granted = granted, denied = denied, completeDenied = completeDenied)
        }
    }
}