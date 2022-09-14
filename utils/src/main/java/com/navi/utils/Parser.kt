package com.navi.utils

import com.squareup.moshi.JsonDataException
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import java.io.IOException

val moshi by lazy<Moshi> { Moshi.Builder().addLast(KotlinJsonAdapterFactory()).build() }

inline fun <reified T : Any> T.stringify(): String = try {
    moshi.adapter(T::class.java).toJson(this)
} catch (e: IOException) {
    ""
}

inline fun <reified T : Any> String.parse(): T? = try {
    moshi.adapter(T::class.java).fromJson(this)
} catch (e: JsonDataException) {
    null
} catch (e: IOException) {
    null
}