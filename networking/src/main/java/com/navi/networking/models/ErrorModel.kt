package com.navi.networking.models

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class ErrorModel(
    @field:Json(name = "code") val code: Int?,
    @field:Json(name = "message") val message: String?
)