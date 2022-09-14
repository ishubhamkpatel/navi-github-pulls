package com.navi.git

import android.content.Context
import android.net.ConnectivityManager
import android.net.Network
import android.net.NetworkCapabilities
import android.net.NetworkRequest
import com.navi.logger.Logger
import com.navi.utils.ConnectivityStore
import dagger.hilt.android.qualifiers.ApplicationContext
import java.util.concurrent.atomic.AtomicBoolean
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ConnectivityMonitor @Inject constructor(
    @ApplicationContext context: Context,
    private val connectivityStore: ConnectivityStore
) {
    private val validNetworkConnections by lazy<MutableSet<Network>> { hashSetOf() }
    private val isNetworkCallbackRegistered = AtomicBoolean(false)

    private val connectivityManager by lazy {
        context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
    }
    private lateinit var networkCallback: ConnectivityManager.NetworkCallback

    fun init() {
        if (isNetworkCallbackRegistered.compareAndSet(false, true)) {
            networkCallback = createNetworkCallback()
            val networkRequest = NetworkRequest.Builder()
                .addCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET)
                .build()
            try {
                connectivityManager.registerNetworkCallback(networkRequest, networkCallback)
            } catch (e: Exception) {
                isNetworkCallbackRegistered.set(false)
                Logger.e(e)
            }
        }
    }

    private fun createNetworkCallback() = object : ConnectivityManager.NetworkCallback() {
        override fun onAvailable(network: Network) {
            super.onAvailable(network)
            val networkCapabilities = connectivityManager.getNetworkCapabilities(network)
            val hasNetworkCapability = networkCapabilities?.hasCapability(
                NetworkCapabilities.NET_CAPABILITY_INTERNET
            ) ?: false
            if (hasNetworkCapability) {
                validNetworkConnections.add(network)
                updateStatus()
            }
        }

        override fun onLost(network: Network) {
            super.onLost(network)
            validNetworkConnections.remove(network)
            updateStatus()
        }
    }

    private fun updateStatus() {
        with(validNetworkConnections.size > 0) {
            connectivityStore.setNetworkAvailability(status = this)
        }
    }

    fun dispose() {
        if (isNetworkCallbackRegistered.compareAndSet(true, false)) {
            try {
                connectivityManager.unregisterNetworkCallback(networkCallback)
            } catch (e: Exception) {
                isNetworkCallbackRegistered.set(true)
                Logger.e(e)
            }
        }
    }
}