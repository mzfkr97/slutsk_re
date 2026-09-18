package com.romanzhurid.common.ext

import android.Manifest
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.provider.Settings
import androidx.activity.compose.ManagedActivityResultLauncher
import androidx.core.content.ContextCompat

fun ManagedActivityResultLauncher<Array<String>, Map<String, Boolean>>.launchLocationPermission() {
    launch(
        arrayOf(
            Manifest.permission.ACCESS_FINE_LOCATION,
            Manifest.permission.ACCESS_COARSE_LOCATION
        )
    )
}

fun Context.openAppSettings() = startActivity(appSettingsIntent())

fun Context.appSettingsIntent(): Intent {
    return Intent(
        Settings.ACTION_APPLICATION_DETAILS_SETTINGS,
        Uri.fromParts("package", packageName, null)
    )
}

fun Context.hasLocationPermission(): Boolean {
    return ContextCompat.checkSelfPermission(
        this,
        Manifest.permission.ACCESS_FINE_LOCATION
    ) == PackageManager.PERMISSION_GRANTED ||
            ContextCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED
}
