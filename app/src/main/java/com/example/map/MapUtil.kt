package com.example.map

import android.Manifest
import android.app.Activity
import android.content.pm.PackageManager
import android.location.Location
import androidx.core.app.ActivityCompat
import com.google.android.gms.maps.model.LatLng

class MapUtil {
    companion object {
        private const val LOCATION_PERMISSION_REQUEST_CODE = 1

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
            return (longitude.toDouble() > -180.0 && longitude.toDouble() < 180.0 && latitude.toDouble() > -90.0 && latitude.toDouble() < 90.0)
        }

         fun checkPermission(activity:Activity) {
            if (ActivityCompat.checkSelfPermission(
                    activity,
                    Manifest.permission.ACCESS_FINE_LOCATION
                ) != PackageManager.PERMISSION_GRANTED
            ) {
                ActivityCompat.requestPermissions(
                    activity,
                    arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),
                    LOCATION_PERMISSION_REQUEST_CODE
                )
                return
            }
        }
    }
}