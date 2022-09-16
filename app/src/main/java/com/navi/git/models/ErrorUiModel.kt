package com.navi.git.models

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class ErrorUiModel(
    val title: String,
    val message: String,
    val fallbackBtn: String
) : Parcelable {
    companion object {
        val default by lazy { ErrorUiModel(title = "", message = "", fallbackBtn = "") }
    }
}
