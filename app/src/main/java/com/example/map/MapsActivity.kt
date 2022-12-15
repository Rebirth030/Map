package com.example.map

import android.content.Intent
import android.content.pm.PackageManager
import android.location.Address
import android.location.Geocoder
import android.location.Location
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
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


@Suppress("DEPRECATION")
class MapsActivity : AppCompatActivity(), OnMapReadyCallback, GoogleMap.OnMarkerClickListener {
    private lateinit var binding: ActivityMapsBinding
    private lateinit var map: GoogleMap
    private lateinit var fusedLocationClient: FusedLocationProviderClient
    private lateinit var lastLocation: Location
    private lateinit var arrayList: ArrayList<MarkerOptions>

    companion object {
        private const val LOCATION_PERMISSION_REQUEST_CODE = 1

    }

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

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.main_menu, menu)
        return true
    }


    override fun onMapReady(googleMap: GoogleMap) {
        map = googleMap

        map.uiSettings.isZoomControlsEnabled = true
        map.setOnMarkerClickListener(this)


        setUpMap()
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        val itemView = item.itemId
        if (itemView == R.id.Random) {}
        else if (itemView == R.id.marker) {
            val intent = Intent(this, SearchActivity::class.java)
            intent.putExtra("markerArray", arrayList)
            startActivityForResult(intent,201)
        }
        return false
    }

    override fun onMarkerClick(p0: Marker) = false

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == 201 && resultCode == RESULT_OK && data != null) {
            placeMarkerOnMap(LatLng(data.getDoubleExtra("Lat",0.0), data.getDoubleExtra("Lng",0.0)))
        }
        //binding.searchBar.setQuery("",true)
    }


    private fun setUpMap() {
        if (ActivityCompat.checkSelfPermission(
                this,
                android.Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            ActivityCompat.requestPermissions(
                this,
                arrayOf(android.Manifest.permission.ACCESS_FINE_LOCATION),
                LOCATION_PERMISSION_REQUEST_CODE
            )
            return
        }
        map.isMyLocationEnabled = true
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

    private fun placeMarkerOnMap(location: LatLng) {
        val markerOptions = MarkerOptions().position(location)

        markerOptions.title(getAddress(location))

        arrayList.add(markerOptions)
        map.addMarker(markerOptions)
        map.animateCamera(CameraUpdateFactory.newLatLng(location))
    }

    private fun getAddress(latLng: LatLng): String {
        // 1
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


}