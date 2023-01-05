package com.example.map

import android.Manifest
import android.app.Activity
import android.content.Context
import android.content.pm.PackageManager
import android.content.res.Configuration
import android.location.Location
import android.os.Build
import android.os.VibrationEffect
import android.os.Vibrator
import androidx.appcompat.app.AlertDialog
import androidx.core.app.ActivityCompat
import com.google.android.gms.maps.model.LatLng
import java.util.*

/**
 * Klasse um Methoden von verschiedenen Klassen her aufrufen zu können
 */
class MapUtil {
    companion object {
         private const val LOCATION_PERMISSION_REQUEST_CODE = 1

        /**
         * berechnet die Distanz zwischen Markern in Kilometern
         */
        fun calculateDistanceInKM(
            startLocations: LatLng,
            endLocation: LatLng,
            startName: String,
            endName: String
        ): Float {
            val locationOne = Location(startName)
            val locationTwo = Location(endName)
            locationOne.longitude = startLocations.longitude
            locationOne.latitude = startLocations.latitude
            locationTwo.longitude = endLocation.longitude
            locationTwo.latitude = endLocation.latitude
            return locationOne.distanceTo(locationTwo) / 1000.0F
        }

        /**
         * guckt ob die Latitude und Longitude Werte gültig sind
         */
        fun checkIfLatLongValid(latitude: String, longitude: String): Boolean {
            return longitude.toDouble() > -180.0 && longitude.toDouble() < 180.0 && latitude.toDouble() > -90.0 && latitude.toDouble() < 90.0
        }

        /**
         * guckt ob die Erlaubnis zur lokalisierung gegeben wurde, wenn nicht, wird die Erlaubnis angefragt
         */
        fun checkPermission(activity: Activity) {
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

        /**
         * ZUSATZFUNKTION
         * ändert die Sprache mit setLocale, startet dabei jedoch die aktuelle Aktivität neu
         */
         fun showChangeLang(activity: Activity) {
            val mBuilder = AlertDialog.Builder(activity)
            mBuilder.setTitle("Choose Language")
            mBuilder.setSingleChoiceItems(arrayOf("Deutsch", "English"), -1) { dialog, which ->
                if (which == 0) {
                    setLocate("de", activity)
                    activity.recreate()
                } else if (which == 1) {
                    setLocate("en", activity)
                    activity.recreate()
                }

                dialog.dismiss()
            }
            val mDialog = mBuilder.create()

            mDialog.show()

        }

        /**
         * ZUSATZINHALT
         * ändert die Spracheinstellung in der durch das Auswählen der anderen Strings Datei
         */
        private fun setLocate(Lang: String, activity: Activity) {

            val locale = Locale(Lang)

            Locale.setDefault(locale)

            val config = Configuration()

            config.locale = locale
            activity.baseContext.resources.updateConfiguration(config, activity.baseContext.resources.displayMetrics)

            val editor = activity.getSharedPreferences("Settings", Context.MODE_PRIVATE).edit()
            editor.putString("My_Lang", Lang)
            editor.apply()
        }

        /**
         * ZUSATZINHALT
         * lässt das Handy vibrieren
         */
         fun vibratePhone(activity: Activity) {
            val vibrator = activity.getSystemService(Context.VIBRATOR_SERVICE) as Vibrator
            if (vibrator.hasVibrator()) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                    vibrator.vibrate(
                        VibrationEffect.createOneShot(
                            500,
                            VibrationEffect.DEFAULT_AMPLITUDE
                        )
                    )
                } else {
                    vibrator.vibrate(500)
                }
            }
        }

    }
}