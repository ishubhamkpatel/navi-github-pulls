package com.navi.git.models

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class SearchUiModel(val owner: String, val repo: String, val prState: String) : Parcelable {
    companion object {
        val default by lazy { SearchUiModel("", "", "") }
    }
}
