@file:Suppress("unused", "MemberVisibilityCanBePrivate")

package com.suein1209.common.kotlinsupportlib

import android.annotation.SuppressLint
import android.app.ActivityManager
import android.content.Context
import android.content.pm.PackageManager
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Build
import android.os.PowerManager
import android.telephony.TelephonyManager
import android.util.Log
import com.suein1209.common.kotlinsupportlib.common.CommonUtilsConst
import com.suein1209.common.kotlinsupportlib.data.DeviceInfo
import java.security.MessageDigest


/**
 * 디바이스 관련 utils
 */
object DeviceUtils {

    fun Context.isConnected() = isNetworkConnected(this)

    /**
     * 네트워크 상태 조회 권한을 가지고 있는지 여부
     *
     * @param context
     * @return
     */
    @JvmStatic
    fun hasPermissionAccessNetworkState(context: Context): Boolean {
        val res = context.checkCallingOrSelfPermission("android.permission.ACCESS_NETWORK_STATE")
        return res == PackageManager.PERMISSION_GRANTED
    }

    /**
     * 네트워크에 연결 여부를 리턴한다.
     */
    @SuppressLint("MissingPermission")
    @JvmStatic
    fun isNetworkConnected(context: Context): Boolean {
        try {
            (context.getSystemService(Context.CONNECTIVITY_SERVICE) as? ConnectivityManager)?.let {
                val activeNetwork = it.activeNetwork ?: return false
                val networkCapabilities = it.getNetworkCapabilities(activeNetwork) ?: return false
                return networkCapabilities.hasCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET)
            }
        } catch (e: Exception) {
            if (BuildConfig.DEBUG) {
                Log.e(CommonUtilsConst.LOG_TAG, e.stackTraceToString())
            }
        }
        return false
    }

    /**
     * 네트워크 망 타입을 리턴한다.
     */
    @SuppressLint("MissingPermission")
    @Suppress("DEPRECATION")
    fun getNetworkType(context: Context): Int {
        var networkType = TelephonyManager.NETWORK_TYPE_UNKNOWN
        (context.getSystemService(Context.TELEPHONY_SERVICE) as? TelephonyManager)?.let {
            networkType = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                it.dataNetworkType
            } else {
                it.networkType
            }
        }
        return networkType
    }

    /**
     * 네트워크 Operator 를 리턴한다.
     */
    fun getNetworkOperatorName(context: Context): String? {
        return (context.getSystemService(Context.TELEPHONY_SERVICE) as? TelephonyManager)?.networkOperatorName
    }

    /**
     * 활성 네트워크가 WIFI 인지 확인
     */
    @SuppressLint("MissingPermission")
    fun isActiveNetworkWiFi(context: Context): Boolean {
        try {
            val connectivityManager = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
            connectivityManager.activeNetwork?.run {
                connectivityManager.getNetworkCapabilities(this)?.let {
                    return it.hasTransport(NetworkCapabilities.TRANSPORT_WIFI)
                }
            }
        } catch (e: Exception) {
            if (BuildConfig.DEBUG) {
                Log.e(CommonUtilsConst.LOG_TAG, e.stackTraceToString())
            }
        }
        return false
    }

    /**
     * 활성 네트워크가 Cellular 인지 확인
     */
    @SuppressLint("MissingPermission")
    fun isActiveNetworkCellular(context: Context): Boolean {
        try {
            val connectivityManager = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
            connectivityManager.activeNetwork?.run {
                connectivityManager.getNetworkCapabilities(this)?.let {
                    return it.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR)
                }
            }
        } catch (e: Exception) {
            if (BuildConfig.DEBUG) {
                Log.e(CommonUtilsConst.LOG_TAG, e.stackTraceToString())
            }
        }
        return false
    }

    /**
     * 활성화된 네트워크를 리턴한다.
     */
    @SuppressLint("MissingPermission")
    fun activeNetwork(context: Context): String? {
        val connectivityManager = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        try {
            connectivityManager.getNetworkCapabilities(connectivityManager.activeNetwork)?.run {
                return when {
                    hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) -> "CELLULAR"
                    hasTransport(NetworkCapabilities.TRANSPORT_WIFI) -> "WIFI"
                    hasTransport(NetworkCapabilities.TRANSPORT_BLUETOOTH) -> "BLUETOOTH"
                    hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET) -> "ETHERNET"
                    hasTransport(NetworkCapabilities.TRANSPORT_VPN) -> "VPN"
                    hasTransport(NetworkCapabilities.TRANSPORT_WIFI_AWARE) -> "WIFI_AWARE"
                    hasTransport(NetworkCapabilities.TRANSPORT_LOWPAN) -> "LOWPAN"
                    else -> "UNKNOWN"
                }
            }
            return "UNKNOWN"
        } catch (e: Exception) {
            if (BuildConfig.DEBUG) {
                Log.e(CommonUtilsConst.LOG_TAG, e.stackTraceToString())
            }
        }
        return null
    }

    /**
     * Cellular 신호세기
     * 0~4 사이의 크기를 리턴한다.
     */
    fun getConnectedCellularSignalLevel(context: Context): Int? {
        (context.getSystemService(Context.TELEPHONY_SERVICE) as? TelephonyManager)?.let {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
                return it.signalStrength?.level
            }
        }
        return null
    }

    /**
     * 디바이스 절전모드 사용 여부
     */
    fun isBatterySavingMode(context: Context): Boolean? {
        (context.getSystemService(Context.POWER_SERVICE) as? PowerManager)?.let {
            return it.isPowerSaveMode
        }
        return null
    }

    /**
     * 디바이스의 총 메모리 용량
     */
    fun getTotalMemory(context: Context): Long? {
        try {
            (context.getSystemService(Context.ACTIVITY_SERVICE) as? ActivityManager)?.let {
                val mi = ActivityManager.MemoryInfo()
                it.getMemoryInfo(mi)
                return mi.totalMem
            }
        } catch (e: Exception) {
            if (BuildConfig.DEBUG) {
                Log.e(CommonUtilsConst.LOG_TAG, e.stackTraceToString())
            }
        }
        return null
    }

    /**
     * 디바이스의 가용 메모리 용량
     */
    fun getAvailableMemory(context: Context): Long? {
        try {
            (context.getSystemService(Context.ACTIVITY_SERVICE) as? ActivityManager)?.let {
                val mi = ActivityManager.MemoryInfo()
                it.getMemoryInfo(mi)
                return mi.availMem
            }
        } catch (e: Exception) {
            if (BuildConfig.DEBUG) {
                Log.e(CommonUtilsConst.LOG_TAG, e.stackTraceToString())
            }
        }
        return null
    }

    /**
     * 어플리케이션 라벨
     */
    fun getApplicationLabel(context: Context, pkgName: String?): String? {
        var label: String? = null
        pkgName?.run {
            try {
                val pm = context.packageManager
                @Suppress("DEPRECATION")
                pm.getApplicationInfo(this, PackageManager.GET_META_DATA).let {
                    label = pm.getApplicationLabel(it).toString()
                }
            } catch (e: Exception) {
                if (BuildConfig.DEBUG) {
                    Log.e(CommonUtilsConst.LOG_TAG, e.stackTraceToString())
                }
            }
        }
        return label
    }

    /**
     * 서명 인증서 디지털 지문
     */
    @SuppressLint("PackageManagerGetSignatures")
    @Suppress("DEPRECATION")
    fun getCertificateFingerprint(context: Context, algorithm: String): String {
        val fingerprint = StringBuffer()

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
            val signingInfo = context.packageManager.getPackageInfo(context.packageName, PackageManager.GET_SIGNING_CERTIFICATES).signingInfo
            if (signingInfo.hasMultipleSigners()) {
                signingInfo.apkContentsSigners
            } else {
                signingInfo.signingCertificateHistory
            }
        } else {
            context.packageManager.getPackageInfo(
                context.packageName,
                PackageManager.GET_SIGNATURES
            )?.signatures
        }?.forEach { signature ->
            val md = MessageDigest.getInstance(algorithm)
            md.update(signature.toByteArray())
            fingerprint.append(HashUtils.bytesToHex(md.digest()))
        }

        return fingerprint.toString()
    }

    fun getDeviceInfo(context: Context?): DeviceInfo {
        return DeviceInfo().apply {
            if (context != null) {
                // Device 정보
                device.apply {
                    model = Build.MODEL // 모델명
                    ver = Build.VERSION.SDK_INT // Android version
                    os = "Android" // OS
                    getTotalMemory(context)?.let {
                        totalMem = it // total memory
                    }
                    getAvailableMemory(context)?.let {
                        availMem = it  // available memory
                    }
                    resolution = "${ScreenUtils.getScreenWidth(context)}x${ScreenUtils.getScreenHeight(context)}" // resolution
                }
                // 네트워크 정보
                network.apply {
                    connect = activeNetwork(context) // 연결된 네트워크
                    if (isActiveNetworkCellular(context)) {
                        type = getNetworkType(context) // 망 타입
                        getNetworkOperatorName(context)?.let {
                            operator = it // 통신사
                        }
                        getConnectedCellularSignalLevel(context)?.let {
                            signal = it // 신호세기
                        }
                    }
                }
                // 배터리 정보
                isBatterySavingMode(context)?.let {
                    battery.savingMode = it // 절전모드
                }
            }
        }
    }
}