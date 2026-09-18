package com.romanzhurid.domain.location

import com.romanzhurid.domain.location.model.GeoLocation

interface LocationRepository {
    suspend fun getLocation(): GeoLocation
}
