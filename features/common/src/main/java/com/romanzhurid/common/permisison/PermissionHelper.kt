package com.romanzhurid.common.permisison

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import androidx.core.content.ContextCompat

interface PermissionHelper {
    fun hasPermission(permission: String): Boolean
    fun hasAnyPermission(vararg permissions: String): Boolean
    fun hasAllPermissions(vararg permissions: String): Boolean
    fun isLocationPermissionGranted(): Boolean
}

class PermissionHelperImpl(private val context: Context) : PermissionHelper {

    override fun hasPermission(permission: String): Boolean {
        return ContextCompat.checkSelfPermission(
            context,
            permission
        ) == PackageManager.PERMISSION_GRANTED
    }

    override fun hasAnyPermission(vararg permissions: String): Boolean {
        return permissions.any(::hasPermission)
    }

    override fun hasAllPermissions(vararg permissions: String): Boolean {
        return permissions.all(::hasPermission)
    }

    override fun isLocationPermissionGranted(): Boolean {
        return hasAnyPermission(
            Manifest.permission.ACCESS_FINE_LOCATION,
            Manifest.permission.ACCESS_COARSE_LOCATION
        )
    }
}
