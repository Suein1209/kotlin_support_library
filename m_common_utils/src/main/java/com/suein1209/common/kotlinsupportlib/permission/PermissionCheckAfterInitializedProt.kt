package com.suein1209.common.kotlinsupportlib.permission

import android.app.Activity
import androidx.activity.ComponentActivity
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.Fragment
import com.suein1209.common.kotlinsupportlib.permission.common.PermissionResultState
import com.suein1209.common.kotlinsupportlib.permission.utils.PermissionCheckSaveStateMgr
import com.suein1209.common.kotlinsupportlib.permission.utils.PermissionStateProt

interface PermissionCheckAfterInitializedProt : PermissionStateProt {

    /**
     * [Fragment.registerForActivityResult] 경우 클래스가 초기화 되기전에 선언을 원칙으로 하고 있다.
     * 따라서 만약 초기화가 끝나고 이후 onClick 등에서 사용할때는 아래 기능으로 초기화를 우선 진행해야 한다.
     *
     * 해당 기능의 callback 으로 전달된 부분은 사용자가 권한 요청 다이얼로그에서 '예' 혹은 '아니오'를 누른경우에만 callback 으로 들어오게 됨으로
     *
     * for Fragment
     */
    fun Fragment.getPermissionActivityResultLauncher(granted: () -> Unit = {}, denied: () -> Unit, completeDenied: () -> Unit = {}): ActivityResultLauncher<Array<String>> {
        return registerForActivityResult(ActivityResultContracts.RequestMultiplePermissions()) { permissionsResult ->
            onCommonResultAfterSystemDialog(activity = requireActivity(), permissionsResult = permissionsResult, callback = { permissionResultState ->
                onCommonPermissionActivityResultLauncher(permissionResultState, granted = granted, denied = denied, completeDenied = completeDenied, isPriorityAlreadyDenied = false)
            }, isPriorityAlreadyDenied = false)
        }
    }

    /**
     * [Fragment.registerForActivityResult] 경우 클래스가 초기화 되기전에 선언을 원칙으로 하고 있다.
     * 따라서 만약 초기화가 끝나고 이후 onClick 등에서 사용할때는 아래 기능으로 초기화를 우선 진행해야 한다.
     *
     * 해당 기능의 callback 으로 전달된 부분은 사용자가 권한 요청 다이얼로그에서 '예' 혹은 '아니오'를 누른경우에만 callback 으로 들어오게 됨으로
     *
     * for Activity
     */
    fun ComponentActivity.getPermissionActivityResultLauncher(granted: () -> Unit = {}, denied: () -> Unit, completeDenied: () -> Unit = {}): ActivityResultLauncher<Array<String>> {
        return registerForActivityResult(ActivityResultContracts.RequestMultiplePermissions()) { permissionsResult ->
            onCommonResultAfterSystemDialog(activity = this, permissionsResult = permissionsResult, callback = { permissionResultState ->
                onCommonPermissionActivityResultLauncher(permissionResultState, granted = granted, denied = denied, completeDenied = completeDenied, isPriorityAlreadyDenied = false)
            }, isPriorityAlreadyDenied = false)
        }
    }

    /**
     * 공통 callback 처리
     * [PermissionResultState.COMPLETE_DENIED] 여기에서 해당 선택은 전달 될 수 없다.
     * - enum 재활용을 위해
     */
    private fun onCommonPermissionActivityResultLauncher(selectedState: PermissionResultState, isPriorityAlreadyDenied: Boolean, granted: () -> Unit = {}, denied: () -> Unit, completeDenied: () -> Unit = {}) {
        when (selectedState) {
            PermissionResultState.GRANTED -> granted()
            PermissionResultState.DENIED -> denied()
            PermissionResultState.ALREADY_DENIED -> if (isPriorityAlreadyDenied) completeDenied() else denied()
            PermissionResultState.COMPLETE_DENIED -> completeDenied()
        }
    }

    /**
     * 현재 설정된 권한 체크 및 Launch 실행
     * 1. 우선 현재 설정되어 있는지 권한을 체크후
     *   - 권한이 이미 설정되어 있다면 [granted]로 콜백을
     *
     * 2. 권한을 요청한 적이 없다면 [ActivityResultLauncher]를 실행한다.
     *
     * - [completeDenied] 람다를 파라미터로 넘기면 isPriorityAlreadyDenied 가 설정되어 shouldShowRequestPermissionRationale 상태를 체크 할 수 있습니다.
     *
     * for Fragment
     */
    fun Fragment.launchPermissionRequest(permissionList: List<String>, launcher: ActivityResultLauncher<Array<String>>, completeDenied: (() -> Unit)? = null, granted: (() -> Unit)? = null) {
        this.requireActivity().onCommonLaunchPermissionRequest(permissionList, launcher, isPriorityAlreadyDenied = completeDenied != null, completeDenied = completeDenied, granted = granted)
    }

    /**
     * 현재 설정된 권한 체크 및 Launch 실행
     * 1. 우선 현재 설정되어 있는지 권한을 체크후
     *   - 권한이 이미 설정되어 있다면 [granted]로 콜백을
     *
     * 2. 권한을 요청한 적이 없다면 [ActivityResultLauncher]를 실행한다.
     *
     * - [completeDenied] 람다를 파라미터로 넘기면 isPriorityAlreadyDenied 가 설정되어 shouldShowRequestPermissionRationale 상태를 체크 할 수 있습니다.
     *
     * for Activity
     */
    fun ComponentActivity.launchPermissionRequest(permissionList: List<String>, launcher: ActivityResultLauncher<Array<String>>, completeDenied: (() -> Unit)? = null, granted: (() -> Unit)? = null) {
        onCommonLaunchPermissionRequest(permissionList, launcher, isPriorityAlreadyDenied = completeDenied != null, completeDenied = completeDenied, granted = granted)
    }

    /**
     * [launchPermissionRequest] 공통 result 처리
     */
    private fun Activity.onCommonLaunchPermissionRequest(permissionList: List<String>, launcher: ActivityResultLauncher<Array<String>>, isPriorityAlreadyDenied: Boolean = false, completeDenied: (() -> Unit)? = null, granted: (() -> Unit)? = null) {
        when (getPermissionsState(permissionList, isPriorityAlreadyDenied)) {
            PermissionResultState.ALREADY_DENIED -> {
                if (isPriorityAlreadyDenied) {
                    completeDenied?.invoke()
                } else {
                    PermissionCheckSaveStateMgr.setAlreadyDenied(this, permissionList) // already denied 설정
                    launcher.launch(permissionList.toTypedArray())
                }
            }
            PermissionResultState.GRANTED -> {
                if (granted == null) {
                    //등록하지 않았다면 activity for result 까지 승인을 유보한다.
                    launcher.launch(permissionList.toTypedArray())
                } else {
                    //등록을 했다면 바로 호출해 승인됨을 알려준다.
                    granted()
                }
                PermissionCheckSaveStateMgr.removeState(this, permissionList)
            }
            else -> launcher.launch(permissionList.toTypedArray())
        }
    }
}