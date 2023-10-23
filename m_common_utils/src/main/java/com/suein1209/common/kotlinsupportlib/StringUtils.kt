@file:Suppress("unused")

package com.suein1209.common.kotlinsupportlib

import android.os.Build
import android.text.Html
import android.text.Spanned
import com.suein1209.common.kotlinsupportlib.ext.isNotNullEmpty
import java.text.DecimalFormat
import java.text.NumberFormat
import java.util.concurrent.TimeUnit
import kotlin.math.floor
import kotlin.math.log10
import kotlin.math.pow


/**
 * Comma String(Int)
 * 123456 -> "123,456"
 */
val Int?.commaStr: String
    get() = if (this == null) {
        ""
    } else {
        StringUtils.getCommaString(this)
    }

/**
 * Comma String(Long)
 * 123456 -> "123,456"
 */
val Long?.commaStr: String
    get() = if (this == null) {
        ""
    } else {
        StringUtils.getCommaString(this)
    }

/**
 * Comma String
 * "123456" -> "123,456"
 */
val String?.commaStr: String
    get() = if (this == null) {
        ""
    } else {
        StringUtils.getCommaString(this)
    }

/**
 * "Contains letters".containsLatinLetter // true
 * "12345".containsLatinLetter // false
 */
val String.containsLatinLetter: Boolean
    get() = matches(Regex(".*[A-Za-z].*"))

/**
 * "Contains digits 123".containsDigit // true
 */
val String.containsDigit: Boolean
    get() = matches(Regex(".*[0-9].*"))

val String.containsHexColor: Boolean
    get() = matches(Regex("#[A-Za-f0-9]{6,8}"))

/**
 * "12kstd123".isAlphanumeric // true
 */
val String.isAlphanumeric: Boolean
    get() = matches(Regex("[A-Za-z0-9]*"))

val String.hasLettersAndDigits: Boolean
    get() = containsLatinLetter && containsDigit

val String.isIntegerNumber: Boolean
    get() = toIntOrNull() != null

val String.toDecimalNumber: Boolean
    get() = toDoubleOrNull() != null

val String?.isHtml: Boolean
    get() = if (this != null) StringUtils.isHTML(this) else false

fun String?.toHtmlSpanned(): Spanned? = StringUtils.fromHTML(this)

/**
 * String Utils
 *
 * Created by suein1209
 */
object StringUtils {
    /**
     * Comma String 반환(Int)
     * - 123456 -> "123,456"
     */
    @JvmStatic
    fun getCommaString(number: Int): String {
        return kotlin.runCatching {
            NumberFormat.getInstance().format(number)
        }.getOrNull() ?: ""
    }

    /**
     * Comma String 반환(Int)
     * - 123456 -> "123,456"
     */
    @JvmStatic
    fun getCommaString(number: Long): String {
        return kotlin.runCatching {
            NumberFormat.getInstance().format(number)
        }.getOrNull() ?: ""
    }

    /**
     * Comma String 반환
     * - "123456" -> "123,456"
     */
    @JvmStatic
    fun getCommaString(number: String): String {
        return kotlin.runCatching {
            getCommaString(number.toLong())
        }.getOrNull() ?: ""
    }

    /**
     * 밀리초를 00:00:00로 변환해 textview에 설정한다.
     * - 시간없이 분,초 는 필수여서 "00:02" 식으로 표현된다.
     * - 시간이 있을때만 10:00:00 라는 식으로 표현된다.
     */
    private const val CONVERT_TIME_FORMAT_DEFAULT = "00:00"
    fun getMilliSecondToHHMMSS(millis: Long?): String {
        return if (millis != null && millis > 0) {
            val hour = TimeUnit.MILLISECONDS.toHours(millis)
            val minute = TimeUnit.MILLISECONDS.toMinutes(millis) - TimeUnit.HOURS.toMinutes(TimeUnit.MILLISECONDS.toHours(millis))
            val second = TimeUnit.MILLISECONDS.toSeconds(millis) - TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(millis))

            if ((hour + minute + second) > 0) {
                var textBuilder = String.format("%02d", second)
                textBuilder = String.format("%02d", minute) + ":" + textBuilder
                if (hour > 0) {
                    textBuilder = String.format("%02d", hour) + ":" + textBuilder
                }
                textBuilder
            } else {
                CONVERT_TIME_FORMAT_DEFAULT
            }
        } else {
            CONVERT_TIME_FORMAT_DEFAULT
        }
    }

    /**
     * byte 를 kb, mb, tb 등의 수치로 변경한다.
     */
    fun getByteToHumanReadableText(bytes: Long) = when {
        bytes == Long.MIN_VALUE || bytes < 0 -> "0Byte"
        bytes < 1024L -> "$bytes Byte"
        bytes <= 0xfffccccccccccccL shr 40 -> "%.1f KB".format(bytes.toDouble() / (0x1 shl 10))
        bytes <= 0xfffccccccccccccL shr 30 -> "%.1f MB".format(bytes.toDouble() / (0x1 shl 20))
        bytes <= 0xfffccccccccccccL shr 20 -> "%.1f GB".format(bytes.toDouble() / (0x1 shl 30))
        bytes <= 0xfffccccccccccccL shr 10 -> "%.1f TB".format(bytes.toDouble() / (0x1 shl 40))
        bytes <= 0xfffccccccccccccL -> "%.1f PB".format((bytes shr 10).toDouble() / (0x1 shl 40))
        else -> "%.1f EB".format((bytes shr 20).toDouble() / (0x1 shl 40))
    }

    /**
     * - 1,000 -> 1.0K
     * - 10,000 -> 10.0K
     * - 100,000 -> 100.0K
     * - 1,000,000 -> 1.0M
     * - 1,000,000,000 -> 1.0B
     */
    fun getTenthsReadableText(count: Long?): String {
        if (count == null || count <= 0L) return "0"
        val suffix = charArrayOf(' ', 'K', 'M', 'B', 'T', 'P', 'E')
        val countToDouble = count.toDouble()
        val value = floor(log10(countToDouble)).toInt()
        val base = value / 3
        return if (value >= 3 && base < suffix.size) {
            val value2 = count / 10.0.pow((base * 3).toDouble())
            val digit = floor(value2 * 10.0.pow(1.toDouble())) / 10.0.pow(1.toDouble())
            DecimalFormat("#0.0").format(
                digit
            ) + suffix[base]
        } else {
            DecimalFormat("#,##0").format(countToDouble)
        }
    }

    fun isHTML(text: String): Boolean {
        val htmlPattern = Regex("<([a-z][a-z0-9]*)\\b[^>]*>(.*?)</\\1>", RegexOption.IGNORE_CASE)
        return htmlPattern.containsMatchIn(text)
    }

    fun fromHTML(text: String?): Spanned? {
        return kotlin.runCatching {
            if (text.isNotNullEmpty()) {
                // ex) 내일 6/8(토) 발송 예정
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                    Html.fromHtml(text, Html.FROM_HTML_MODE_COMPACT)
                } else {
                    @Suppress("DEPRECATION")
                    Html.fromHtml(text)
                }
            } else null
        }.getOrNull()
    }
}