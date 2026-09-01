package com.romanzhurid.common.ext

import android.content.Context
import android.content.pm.PackageManager
import android.Manifest
import androidx.core.content.ContextCompat

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

/*
val context = LocalContext.current

val hasLocationPermission = remember {
    context.hasLocationPermission()
}

if (context.hasLocationPermission()) {
    // работаем с локацией
} else {
    permissionLauncher.launch(
        arrayOf(
            Manifest.permission.ACCESS_FINE_LOCATION,
            Manifest.permission.ACCESS_COARSE_LOCATION
        )
    )
}
ActivityCompat.shouldShowRequestPermissionRationale(
    activity,
    Manifest.permission.ACCESS_FINE_LOCATION
)
if (hasLocationPermission()) {
} else if (shouldShowRequestPermissionRationale(...)) {
    permissionLauncher.launch(...)
} else {
    "Don't ask again"
    permissionLauncher.launch(...)
}
 */