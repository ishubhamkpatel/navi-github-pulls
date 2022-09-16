package com.navi.git.utils

import androidx.annotation.IdRes
import androidx.navigation.NavController
import androidx.navigation.NavGraph
import com.navi.utils.orDefault

fun NavController.safeNavigate(@IdRes resId: Int) {
    val destinationId = currentDestination?.getAction(resId)?.destinationId.orDefault()
    currentDestination?.let { node ->
        val currentNode = when (node) {
            is NavGraph -> node
            else -> node.parent
        }
        if (destinationId != 0) {
            currentNode?.findNode(destinationId)?.let { navigate(resId) }
        }
    }
}