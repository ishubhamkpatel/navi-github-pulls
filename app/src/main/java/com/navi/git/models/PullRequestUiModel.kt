package com.navi.git.models

import com.navi.networking.models.data.PullRequestDataModel
import com.navi.utils.DateFormat
import com.navi.utils.parseDateAndTime

data class PullRequestUiModel(
    val id: Long?,
    val title: String?,
    val createdDate: String?,
    val closedDate: String?,
    val userName: String?,
    val userImage: String?
)

fun PullRequestDataModel.toUiModel() = PullRequestUiModel(
    id = id,
    title = title,
    createdDate = createdDate?.parseDateAndTime(
        inputFormat = DateFormat.yyyy_MM_dd_T_HH_mm_ss_Z,
        outputFormat = DateFormat.MMM_dd_yyyy_HH_mm
    ),
    closedDate = closedDate?.parseDateAndTime(
        inputFormat = DateFormat.yyyy_MM_dd_T_HH_mm_ss_Z,
        outputFormat = DateFormat.MMM_dd_yyyy_HH_mm
    ),
    userName = user?.name,
    userImage = user?.image
)
