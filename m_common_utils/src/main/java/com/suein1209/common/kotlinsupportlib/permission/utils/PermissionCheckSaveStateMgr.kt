package com.suein1209.common.kotlinsupportlib.permission.utils

import android.content.Context
import android.content.SharedPreferences

@Suppress("MayBeConstant", "MemberVisibilityCanBePrivate")
object PermissionCheckSaveStateMgr {

    private val PrefKeyAlreadyDenied = "PermissionCheckSaveStateAlreadyDenied"
    private val PrefKeyCompleteDenied = "PermissionCheckSaveStateCompleteDenied"
    private var sharedAlreadyDeniedPref: SharedPreferences? = null
    private var sharedCompleteDeniedPref: SharedPreferences? = null

    private fun getSharedAlreadyDeniedPref(context: Context): SharedPreferences {
        if (sharedAlreadyDeniedPref == null) {
            sharedAlreadyDeniedPref = context.getSharedPreferences(PrefKeyAlreadyDenied, Context.MODE_PRIVATE);
        }
        return sharedAlreadyDeniedPref!!
    }

    private fun getSharedCompleteDeniedPref(context: Context): SharedPreferences {
        if (sharedCompleteDeniedPref == null) {
            sharedCompleteDeniedPref = context.getSharedPreferences(PrefKeyCompleteDenied, Context.MODE_PRIVATE);
        }
        return sharedCompleteDeniedPref!!
    }

    fun setAlreadyDenied(context: Context, permissionNameList: List<String>) {
        if (permissionNameList.isEmpty()) return
        val edit = getSharedAlreadyDeniedPref(context).edit()
        permissionNameList.forEach { permissionName ->
            edit.putBoolean(permissionName, true)
        }
        edit.apply()
    }

    fun setAlreadyDenied(context: Context, permissionName: String) {
        getSharedAlreadyDeniedPref(context).edit().putBoolean(permissionName, true).apply()
    }

    fun isAlreadyDenied(context: Context, permissionName: String): Boolean {
        return getSharedAlreadyDeniedPref(context).getBoolean(permissionName, false)
    }

    fun removeAlreadyDenied(context: Context, permissionName: String) {
        getSharedAlreadyDeniedPref(context).edit().remove(permissionName).apply()
    }

    fun setCompleteDeniedWhenAlreadyDenied(context: Context, permissionNameList: List<String>) {
        for (permissionName in permissionNameList) {
            if (isAlreadyDenied(context, permissionName)) {
                removeAlreadyDenied(context, permissionName)
                setCompleteDenied(context, permissionName)
            } else {
                setAlreadyDenied(context, permissionName)
            }
        }
    }

    fun setCompleteDeniedWhenAlreadyDenied(context: Context, permissionName: String) {
        if (isAlreadyDenied(context, permissionName)) {
            removeAlreadyDenied(context, permissionName)
            setCompleteDenied(context, permissionName)
        } else {
            setAlreadyDenied(context, permissionName)
        }
    }

    fun isExistsCompleteDenied(context: Context, permissionNames: List<String>): Boolean {
        return permissionNames.any { permissionName -> isCompleteDenied(context, permissionName) }
    }

    fun isCompleteDenied(context: Context, permissionName: String): Boolean {
        return getSharedCompleteDeniedPref(context).getBoolean(permissionName, false)
    }

    fun setCompleteDenied(context: Context, permissionName: String) {
        getSharedCompleteDeniedPref(context).edit().putBoolean(permissionName, true).apply()
    }

    fun removeCompleteDenied(context: Context, permissionName: String) {
        return getSharedCompleteDeniedPref(context).edit().remove(permissionName).apply()
    }

    fun removeState(context: Context, permissionName: String) {
        removeAlreadyDenied(context, permissionName)
        removeCompleteDenied(context, permissionName)
    }

    fun removeState(context: Context, permissionNameList: List<String>) {
        if (permissionNameList.isEmpty()) return

        val alreadyDeniedEdit = getSharedAlreadyDeniedPref(context).edit()
        val completeDeniedEdit = getSharedCompleteDeniedPref(context).edit()

        for (permissionName in permissionNameList) {
            alreadyDeniedEdit.remove(permissionName)
            completeDeniedEdit.remove(permissionName)
        }

        alreadyDeniedEdit.apply()
        completeDeniedEdit.apply()
    }
}