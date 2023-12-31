@file:Suppress("unused")

package com.suein1209.common.kotlinsupportlib

import android.util.Base64
import java.io.UnsupportedEncodingException
import java.security.MessageDigest
import java.security.NoSuchAlgorithmException
import java.security.SecureRandom

val String.md5: String
    get() {
        return String(HashUtils.encodeHex(HashUtils.md5Bytes(Helper.getRawBytes(this))))
    }

val ByteArray.md5: String
    get() {
        return String(HashUtils.encodeHex(HashUtils.md5Bytes(this)))
    }

val ByteArray.md5Bytes: ByteArray
    get() {
        return HashUtils.getDigest(HashUtils.MD5).digest(this)
    }

val String.sha1: String
    get() {
        return String(HashUtils.encodeHex(HashUtils.sha1Bytes(Helper.getRawBytes(this))))
    }

val ByteArray.sha1: String
    get() {
        return String(HashUtils.encodeHex(HashUtils.sha1Bytes(this)))
    }

val ByteArray.sha1Bytes: String
    get() {
        return String(HashUtils.encodeHex(HashUtils.sha256Bytes(this)))
    }

val ByteArray.sha256: String
    get() {
        return String(HashUtils.encodeHex(HashUtils.sha256Bytes(this)))
    }

val String.sha256: String
    get() {
        return String(HashUtils.encodeHex(HashUtils.sha256Bytes(Helper.getRawBytes(this))))
    }

val ByteArray.sha256Bytes: ByteArray
    get() {
        return HashUtils.getDigest(HashUtils.SHA_256).digest(this)
    }

val ByteArray.base64: String
    get() {
        return HashUtils.base64(this)
    }

val String.base64Bytes: ByteArray
    get() {
        return HashUtils.base64Bytes(this)
    }

object HashUtils {
    internal const val MD5 = "MD5"
    internal const val SHA_1 = "SHA-1"
    internal const val SHA_256 = "SHA-256"
    private val DIGITS_LOWER = charArrayOf('0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'a', 'b', 'c', 'd', 'e', 'f')
    private val DIGITS_UPPER = charArrayOf('0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'A', 'B', 'C', 'D', 'E', 'F')

    fun md5(data: ByteArray): String {
        return String(encodeHex(md5Bytes(data)))
    }

    fun md5(text: String): String {
        return String(encodeHex(md5Bytes(Helper.getRawBytes(text))))
    }

    fun md5Bytes(data: ByteArray): ByteArray {
        return getDigest(MD5).digest(data)
    }

    fun sha1(data: ByteArray): String {
        return String(encodeHex(sha1Bytes(data)))
    }

    fun sha1(text: String): String {
        return String(encodeHex(sha1Bytes(Helper.getRawBytes(text))))
    }

    fun sha1Bytes(data: ByteArray): ByteArray {
        return getDigest(SHA_1).digest(data)
    }

    fun sha256(data: ByteArray): String {
        return String(encodeHex(sha256Bytes(data)))
    }

    fun sha256(text: String): String {
        return String(encodeHex(sha256Bytes(Helper.getRawBytes(text))))
    }

    fun sha256Bytes(data: ByteArray): ByteArray {
        return getDigest(SHA_256).digest(data)
    }

    fun base64Bytes(text: String) = Helper.base64Decode(text)

    fun base64(data: ByteArray) = Helper.base64Encode(data)

    fun getDigest(algorithm: String): MessageDigest {
        try {
            return MessageDigest.getInstance(algorithm)
        } catch (e: NoSuchAlgorithmException) {
            throw IllegalArgumentException(e)
        }
    }

    fun encodeHex(data: ByteArray, toLowerCase: Boolean = true): CharArray {
        return encodeHex(data, if (toLowerCase) DIGITS_LOWER else DIGITS_UPPER)
    }

    fun encodeHex(data: ByteArray, toDigits: CharArray): CharArray {
        val l = data.size
        val out = CharArray(l shl 1)
        var i = 0
        var j = 0
        while (i < l) {
            out[j++] = toDigits[(240 and data[i].toInt()).ushr(4)]
            out[j++] = toDigits[15 and data[i].toInt()]
            i++
        }
        return out
    }

    fun bytesToHex(bytes: ByteArray, toUpperCase: Boolean = true): String {
        val hexArray = if (toUpperCase) DIGITS_UPPER else DIGITS_LOWER
        val hexChars = CharArray(bytes.size * 2)
        for (j in bytes.indices) {
            val v = bytes[j].toInt() and 0xFF
            hexChars[j * 2] = hexArray[v ushr 4]
            hexChars[j * 2 + 1] = hexArray[v and 0x0F]
        }
        return String(hexChars)
    }
}

internal object Helper {

    fun getRandomString(): String = SecureRandom().nextLong().toString()

    fun getRandomBytes(size: Int): ByteArray {
        val random = SecureRandom()
        val bytes = ByteArray(size)
        random.nextBytes(bytes)
        return bytes
    }

    fun getRawBytes(text: String): ByteArray {
        return try {
            text.toByteArray(Charsets.UTF_8)
        } catch (e: UnsupportedEncodingException) {
            text.toByteArray()
        }
    }

    fun getString(data: ByteArray): String {
        return try {
            String(data, Charsets.UTF_8)
        } catch (e: UnsupportedEncodingException) {
            String(data)
        }

    }

    fun base64Decode(text: String): ByteArray {
        return Base64.decode(text, Base64.NO_WRAP)
    }

    fun base64Encode(data: ByteArray): String {
        return Base64.encodeToString(data, Base64.NO_WRAP)
    }
}