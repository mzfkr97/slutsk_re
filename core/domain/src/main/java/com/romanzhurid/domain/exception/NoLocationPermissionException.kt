package com.romanzhurid.domain.exception

class NoLocationPermissionException : RuntimeException(
    "Location permission not granted"
)

class LocationUnavailableException : Exception()