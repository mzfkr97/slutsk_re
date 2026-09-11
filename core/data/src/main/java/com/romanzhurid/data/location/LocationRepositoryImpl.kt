package com.romanzhurid.data.location

import android.annotation.SuppressLint
import android.content.Context
import com.google.android.gms.location.LocationServices
import com.google.android.gms.location.Priority
import com.google.android.gms.tasks.CancellationTokenSource
import com.romanzhurid.domain.exception.LocationUnavailableException
import com.romanzhurid.domain.exception.NoLocationPermissionException
import com.romanzhurid.domain.location.LocationRepository
import com.romanzhurid.domain.location.model.GeoLocation
import kotlinx.coroutines.suspendCancellableCoroutine
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException

class LocationRepositoryImpl(context: Context) : LocationRepository {

    private val fusedLocationClient = LocationServices.getFusedLocationProviderClient(context)

    @SuppressLint("MissingPermission")
    override suspend fun getLocation(): GeoLocation =
        suspendCancellableCoroutine { cont ->
            fusedLocationClient.getCurrentLocation(
                Priority.PRIORITY_HIGH_ACCURACY,
                CancellationTokenSource().token
            )
                .addOnSuccessListener { location ->
                    if (location != null) {
                        cont.resume(
                            GeoLocation(
                                latitude = location.latitude,
                                longitude = location.longitude
                            )
                        )
                    } else {
                        cont.resumeWithException(LocationUnavailableException())
                    }
                }
                .addOnFailureListener { exception ->
                    cont.resumeWithException(exception)
                }
        }
}
