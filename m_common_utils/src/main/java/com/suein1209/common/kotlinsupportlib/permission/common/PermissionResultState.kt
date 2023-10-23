package com.suein1209.common.kotlinsupportlib.permission.common

enum class PermissionResultState {
    /**
     * 퍼미션 다이얼로그 화면에서 승인함
     */
    GRANTED,

    /**
     * 퍼미션 다이얼로그 화면에서 거절함
     */
    DENIED,

    /**
     * 완벽하게 거절된경우(다이얼로그도 안뜨고)
     */
    COMPLETE_DENIED,

    /**
     * 이미 전에 거절을 했던가, Setting -> Application -> Permission 에서 사용자가 직접 거절을 했던가
     */
    ALREADY_DENIED
}