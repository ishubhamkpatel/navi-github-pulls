package com.navi.utils

fun Int?.orDefault(default: Int = 0): Int = this ?: default

inline fun <T, R> safeLet(vararg p: T?, block: (Array<out T>) -> R?): R? where T : Any, R : Any {
    return if (p.filterNotNull().size == p.size) {
        @Suppress("UNCHECKED_CAST")
        block(p as Array<out T>)
    } else null
}