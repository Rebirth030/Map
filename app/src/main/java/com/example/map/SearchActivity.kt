package com.example.map

import android.location.Geocoder
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.map.databinding.ActivitySearchBinding
import com.google.android.gms.maps.model.LatLng
import java.util.*


class SearchActivity : AppCompatActivity() {
    private lateinit var binding: ActivitySearchBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySearchBinding.inflate(layoutInflater)
        setContentView(binding.root)

        textViewInit()

    }

    private fun textViewInit() {
        binding.createMarker.setOnClickListener {
            val txtOne = binding.etLocationName.text.toString()
            val txtTwo = binding.editTextNumberDecimal2.text.toString()
            val txtThree = binding.editTextNumberDecimal3.text.toString()
            if (txtOne != "") {
                try {
                    val latLng = getAddresses(txtOne)
                    intent.putExtra("Lat", latLng.latitude)
                    intent.putExtra("Lng", latLng.longitude)
                    setResult(RESULT_OK, intent)
                    finish()
                } catch (e: java.lang.IndexOutOfBoundsException) {
                    Toast.makeText(this, "Error: Location not found", Toast.LENGTH_SHORT).show()
                }
            }
            if (txtTwo != "" && txtThree != "") {
                intent.putExtra("Lat", txtTwo)
                intent.putExtra("Lng", txtThree)
                setResult(RESULT_OK, intent)
                finish()
            }
        }
    }


    private fun getAddresses(address: String): LatLng {
        val geocoder = Geocoder(this, Locale.getDefault())
        val addressGeo = geocoder.getFromLocationName(address, 1)
        return LatLng(
            addressGeo[0].latitude,
            addressGeo[0].longitude
        )
    }
}