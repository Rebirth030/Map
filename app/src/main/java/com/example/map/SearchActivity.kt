package com.example.map

import android.location.Geocoder
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.map.databinding.ActivitySearchBinding
import com.google.android.gms.maps.model.LatLng
import java.util.*

/**
 * ZUSATZINHALT
 * Aktivität um einen neuen Marker zu sätzen
 */
class SearchActivity : AppCompatActivity() {
    private lateinit var binding: ActivitySearchBinding

    /**
     * ZUSATZINHALT
     * onCreate der Searchaktivität
     * ruf textViewInit auf
     */
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySearchBinding.inflate(layoutInflater)
        setContentView(binding.root)

        textViewInit()

    }

    /**
     * ZUSATZINHALT
     * setzt den createMarker Button um damit die eingabe der text Felder entgegen zu nehmen und entweder mit get Ardess die LatLong werte zu erhalten oder direkt mit den LatLong werten die Aktivität zu beenden.
     */
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
                    MapUtil.vibratePhone(this)
                    Toast.makeText(this, getString(R.string.LocationNotFound), Toast.LENGTH_SHORT).show()
                }
            } else if (txtTwo != "" && txtThree != "") {
                intent.putExtra("Lat", txtTwo.toDouble())
                intent.putExtra("Lng", txtThree.toDouble())
                setResult(RESULT_OK, intent)
                finish()
            }
        }
    }

    /**
     * ZUSATZINHALT
     * gibt die LatLong werte einer Adresse zurück
     */
    private fun getAddresses(address: String): LatLng {
        val geocoder = Geocoder(this, Locale.getDefault())
        val addressGeo = geocoder.getFromLocationName(address, 1)
        return LatLng(
            addressGeo[0].latitude,
            addressGeo[0].longitude
        )
    }
}