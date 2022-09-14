package com.navi.utils

import dagger.Reusable
import javax.inject.Inject

@Reusable
class ConnectivityStore @Inject constructor() {
    @Volatile
    private var isNetworkAvailable: Boolean = false

    fun setNetworkAvailability(status: Boolean) {
        isNetworkAvailable = status
    }

    fun isNetworkAvailable(): Boolean = isNetworkAvailable
}