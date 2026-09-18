package com.romanzhurid.brandbook.ext

import android.annotation.SuppressLint
import android.content.Context
import android.content.pm.PackageInfo
import android.content.pm.PackageManager
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Build

@SuppressLint("MissingPermission")
fun Context.isNetworkAvailable(): Boolean {
    val connectivityManager = getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
    val network = connectivityManager.activeNetwork
    val capabilities = connectivityManager.getNetworkCapabilities(network)
    return capabilities != null && (
        capabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) ||
            capabilities.hasTransport(
                NetworkCapabilities.TRANSPORT_CELLULAR
            )
        )
}

fun Context.isNetworkAvailable(failBlock: () -> Unit, successBlock: () -> Unit) {
    if (this.isNetworkAvailable()) {
        successBlock()
    } else {
        failBlock()
    }
}

fun PackageManager.getPackageInfoCompat(packageName: String, flags: Int = 0): PackageInfo =
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
        getPackageInfo(packageName, PackageManager.PackageInfoFlags.of(flags.toLong()))
    } else {
        @Suppress("DEPRECATION")
        getPackageInfo(packageName, flags)
    }