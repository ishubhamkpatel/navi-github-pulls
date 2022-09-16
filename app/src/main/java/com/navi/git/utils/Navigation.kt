package com.navi.git.utils

import androidx.navigation.NavController
import androidx.navigation.NavDirections
import androidx.navigation.NavGraph
import com.navi.utils.orDefault

fun NavController.safeNavigate(navDirections: NavDirections) {
    val destinationId = currentDestination?.getAction(navDirections.actionId)
        ?.destinationId.orDefault()
    currentDestination?.let { node ->
        val currentNode = when (node) {
            is NavGraph -> node
            else -> node.parent
        }
        if (destinationId != 0) {
            currentNode?.findNode(destinationId)?.let { navigate(navDirections) }
        }
    }
}