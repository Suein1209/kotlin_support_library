@file:Suppress("RegExpRedundantEscape", "RegExpDuplicateCharacterInClass")

package com.suein1209.common.kotlinsupportlib

import java.util.regex.Pattern

object NetUtils {

    /**
     * 메일 주소 규칙이 맞는지 체크
     */
    @JvmStatic
    fun isEmailValid(email: String?): Boolean {
        email ?: return false
        val expression = "[a-zA-Z0-9\\+\\.\\_\\%\\-\\+]{1,256}\\@[a-zA-Z0-9][a-zA-Z0-9\\-]{0,64}(\\.[a-zA-Z0-9][a-zA-Z0-9\\-]{0,25})+"
        val pattern = Pattern.compile(expression, Pattern.CASE_INSENSITIVE)
        val matcher = pattern.matcher(email)
        return matcher.matches()
    }
}

/**
 * 메일 주소 규칙이 맞는지 체크
 */
fun String.isEmailValid() = NetUtils.isEmailValid(this)