package com.example.map

import android.location.Location
import com.google.android.gms.maps.model.LatLng

class MapUtil {
    companion object {
        fun calculateDistance(latLngOne: LatLng, latLngTwo: LatLng): Double {
            val locationOne = Location("One")
            val locationTwo = Location("Two")
            locationOne.longitude = latLngOne.longitude
            locationOne.latitude = latLngOne.latitude
            locationTwo.longitude = latLngTwo.longitude
            locationTwo.latitude = latLngTwo.latitude
            return locationOne.distanceTo(locationTwo) / 1000.0
        }

        fun checkIfLatLongValid(longitude: String, latitude: String, ): Boolean {
            return !(longitude.toDouble() < -180.0 || longitude.toDouble() > 180.0 || latitude.toDouble() < -90.0 || latitude.toDouble() > 90.0)
        }
    }
}