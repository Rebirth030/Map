package com.example.map

import android.content.Intent
import android.location.Address
import android.location.Geocoder
import android.location.Location
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.example.map.databinding.ActivityMapsBinding
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.Marker
import com.google.android.gms.maps.model.MarkerOptions
import java.io.IOException
import java.util.*

/**
 * @author Julian Martens
 * Erstellt die Hauptaktivität mit der Karte
 */

@Suppress("DEPRECATION")
class MapsActivity : AppCompatActivity(), OnMapReadyCallback, GoogleMap.OnMarkerClickListener {
    private lateinit var binding: ActivityMapsBinding
    private lateinit var map: GoogleMap
    private lateinit var fusedLocationClient: FusedLocationProviderClient
    private lateinit var lastLocation: Location
    private lateinit var arrayList: ArrayList<String>
    private lateinit var startMarker: LatLng
    private lateinit var startMarkerName: String
    private lateinit var endMarker: LatLng
    private lateinit var endMarkerName: String
    private var markerList: ArrayList<MarkerOptions> = ArrayList()
    private var markerTitleList: ArrayList<String> = ArrayList()

    /**
     * Oncreate setzt binding, Google Maps Fragment und die Aktuelle Location
     */
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMapsBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setSupportActionBar(binding.toolBar)

        val mapFragment = supportFragmentManager
            .findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)

        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)
    }

    /**
     * ZUSATZINHALT
     * setzt onCreate für das Options-menu
     */
    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.main_menu, menu)
        return true
    }

    /**
     * initialisiert die Karte,
     * ruft setUpMap auf,
     * holt sich die Position der Marker, von der Startaktivität, platziert die Marker und ruft create distance zwischen den Markern auf
     */
    override fun onMapReady(googleMap: GoogleMap) {
        map = googleMap

        map.uiSettings.isZoomControlsEnabled = true
        map.setOnMarkerClickListener(this)


        setUpMap()

        arrayList = intent.getStringArrayListExtra("data") as ArrayList<String>
        getMarkerFromArrayList()
        placeMarkerOnMap(startMarker, startMarkerName)
        placeMarkerOnMap(endMarker, endMarkerName)
        createDistanceAlert(
            MapUtil.calculateDistanceInKM(
                startMarker,
                endMarker,
                startMarkerName,
                endMarkerName
            ).toString(), startMarkerName, endMarkerName
        )
    }

    /**
     * ZUSATZINHALT
     * Funktion der Menuitems des OptionMenus
     */
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        val itemView = item.itemId
        if (itemView == R.id.ChangeLang) MapUtil.showChangeLang(this)
        if (itemView == R.id.marker) {
            val intent = Intent(this, SearchActivity::class.java)
            startActivityForResult(intent, 201)
        }
        if (itemView == R.id.markerOpt) showMarkerDistOpt()
            return false
    }

    /**
     * setzt onMarkerClick auf false
     */
    override fun onMarkerClick(p0: Marker) = false

    /**
     * ZUSATZINHALT
     * onActivityResult für das Ergebnis der Marker setzen Funktion
     */
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == 201 && resultCode == RESULT_OK && data != null) {
            placeMarkerOnMap(
                LatLng(
                    data.getDoubleExtra("Lat", 0.0),
                    data.getDoubleExtra("Lng", 0.0)
                )
            )
        }
    }

    /**
     * ruft checkPermission auf,
     * setzt Maptyp und aktualisiert die aktuelle Position
     */
    private fun setUpMap() {
        MapUtil.checkPermission(this)
        try {
            map.isMyLocationEnabled = true
        } catch (o: Exception) {
            MapUtil.vibratePhone(this)
            Toast.makeText(
                this,
                getString(R.string.locationError),
                Toast.LENGTH_LONG
            ).show()
        }

        map.mapType = GoogleMap.MAP_TYPE_HYBRID
        fusedLocationClient.lastLocation.addOnSuccessListener(this) { location ->

            if (location != null) {
                lastLocation = location
                val currentLatLng = LatLng(location.latitude, location.longitude)
                placeMarkerOnMap(currentLatLng)
                map.animateCamera(CameraUpdateFactory.newLatLngZoom(currentLatLng, 18f))
            }
        }
    }

    /**
     * ZUSATZINHALT
     * Funktion, die die Marker auf Karte setzt, wobei nur der LatLong Wert übergeben werden muss und der Name der Location mit der getAdress Funktion herausgefunden wird
     */
    private fun placeMarkerOnMap(location: LatLng) {
        val markerOptions = MarkerOptions().position(location)

        markerOptions.title(getAddress(location))

        markerList.add(markerOptions)
        markerTitleList.add(markerOptions.title.toString())
        map.addMarker(markerOptions)
        map.animateCamera(CameraUpdateFactory.newLatLng(location))
    }

    /**
     * Setzt Marker mit LatLong Wert und dem Namen des Markers
     */
    private fun placeMarkerOnMap(location: LatLng, name: String) {
        val markerOptions = MarkerOptions().position(location)

        markerOptions.title(name)

        markerList.add(markerOptions)
        markerTitleList.add(name)
        map.addMarker(markerOptions)
    }

    /**
     * get Address ermittelt mit hilfe des LatLong Werts die zugehörige Adresse mit GeoCoder
     */
    private fun getAddress(latLng: LatLng): String {
        val geocoder = Geocoder(this, Locale.getDefault())
        val addresses: List<Address>?
        var addressText = ""

        try {
            addresses = geocoder.getFromLocation(latLng.latitude, latLng.longitude, 1)
            if (null != addresses && addresses.isNotEmpty()) {
                addressText = addresses[0].getAddressLine(0)

            }
        } catch (e: IOException) {
            Log.e("MapsActivity", e.localizedMessage as String)
        }

        return addressText
    }

    /**
     * speichert Marker von Startaktivität
     */
    private fun getMarkerFromArrayList() {
        startMarker = LatLng(arrayList[1].toDouble(), arrayList[2].toDouble())
        endMarker = LatLng(arrayList[4].toDouble(), arrayList[5].toDouble())
        startMarkerName = arrayList[0]
        endMarkerName = arrayList[3]
    }

    /**
     * erstellt einen Alert-Dialog mithilfe der Distanz und dem Start und Ende als String
     */
    private fun createDistanceAlert(distance: String, start: String, end: String) {
        val builder = AlertDialog.Builder(this)
        builder.setTitle(getString(R.string.distanceHeaderFirst) + " " + start + " " + getString(R.string.and) + " " + end)
        builder.setMessage(getString(R.string.dist) + ": " + distance + "km")

        builder.setNeutralButton("OK") { dialog, _ ->
            dialog.dismiss()
        }
        builder.show()
    }

    /**
     * ZUSATZINHALT
     * Erstellt den Alert-Dialog, um zwei Marker auswählen zu können,
     * auf erfolgreiches auswählen, wird createDistanceAlert mit den zwei ausgewählten markern aufgerufen.
     */
    private fun showMarkerDistOpt(): Array<Int> {
        val checkedItems = BooleanArray(markerTitleList.size)
        val mBuilder = AlertDialog.Builder(this)
        val arrayList: ArrayList<Int> = ArrayList()

        mBuilder.setTitle(R.string.chooseMarker)
        mBuilder.setMultiChoiceItems(
            markerTitleList.toTypedArray(),
            checkedItems
        ) { _, which, isChecked ->
            checkedItems[which] = isChecked
        }

        mBuilder.setPositiveButton("Done") { dialog, which ->
            for (i in checkedItems.indices) {
                if (checkedItems[i]) {
                    arrayList.add(i)
                }
            }
            dialog.dismiss()
            if (arrayList.size == 2) {
                createDistanceAlert(
                    MapUtil.calculateDistanceInKM(
                        markerList[arrayList[0]].position,
                        markerList[arrayList[1]].position,
                        markerList[arrayList[0]].title.toString(),
                        markerList[arrayList[1]].title.toString()
                    ).toString(),
                    markerList[arrayList[0]].title.toString(),
                    markerList[arrayList[1]].title.toString()
                )
            } else {
                MapUtil.vibratePhone(this)
                Toast.makeText(this, R.string.ErrorTwoMarker, Toast.LENGTH_SHORT).show()
            }
        }

        mBuilder.setNegativeButton("CANCEL") { dialog, _ -> dialog.dismiss()}

        mBuilder.create()
        val mDialog = mBuilder.create()

        mDialog.show()
        return arrayList.toTypedArray()
    }


}