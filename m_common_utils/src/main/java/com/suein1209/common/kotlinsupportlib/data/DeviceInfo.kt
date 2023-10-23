package com.suein1209.common.kotlinsupportlib.data


class DeviceInfo {
    /**  디바이스 정보*/
    var device: Device = Device()
    /**  네트워크 정보*/
    var network: Network = Network()
    /**  배터리 정보*/
    var battery: Battery = Battery()

    /** 디바이스 정보*/
    class Device {
        /** 모델 명*/
        var model: String? = null
        /** Android API 버전*/
        var ver: Int? = null
        /** OS - Android */
        var os: String = "Android"
        /**  전체 메모리*/
        var totalMem: Long? = null
        /**  가용 메모리*/
        var availMem: Long? = null
        /**  resolution*/
        var resolution: String? = null
    }

    /** 네트워크 정보*/
    class Network {
        /**  연결 네트워크*/
        var connect: String? = null
        /**  신호세기 */
        var signal: Int? = null
        /**  망 타입 */
        var type: Int? = null
        /**  통신사업자 */
        var operator: String? = null
    }

    /** 배터리 정보*/
    class Battery {
        /** 절전모드 */
        var savingMode: Boolean = false
    }
}