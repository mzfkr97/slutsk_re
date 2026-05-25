package com.romanzhurid.brandbook.ext

import android.util.Patterns

const val EMPTY_STRING = ""
private const val PASSWORD_MIN_LENGTH = 9
private const val PASSWORD_MAX_LENGTH = 20
private const val SPECIAL_CHARS = "_!@#$%^&*()"

/**
 * Returns `true` if this char sequence contains the specified [other] sequence of characters as a substring,
 * ignoring character case
 */
fun String.containsIgnoreCase(other: CharSequence) =
    contains(
        other = other,
        ignoreCase = true
    )

/**
 * Returns `true` if this string is equal to [other], ignoring character case.
 */
fun String.equalsIgnoreCase(other: String?): Boolean = equals(
    other = other,
    ignoreCase = true
)

fun String.isValidEmail(): Boolean {
    return if (isNullOrEmpty()) {
        false
    } else {
        Patterns.EMAIL_ADDRESS.matcher(this).matches()
    }
}

fun String.isValidPassword(): Boolean {
    if (length !in PASSWORD_MIN_LENGTH..PASSWORD_MAX_LENGTH) return false
    if (none { it.isUpperCase() }) return false
    if (none { it.isLowerCase() }) return false
    if (none { it.isDigit() }) return false
    if (none { it in SPECIAL_CHARS }) return false

    return true
}
