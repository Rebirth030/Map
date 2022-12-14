package com.example.map

import android.location.Address
import android.location.Geocoder
import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.SearchView
import androidx.appcompat.app.AppCompatActivity
import com.example.map.databinding.ActivitySearchBinding
import com.google.android.gms.maps.model.LatLng
import java.util.*

class SearchActivity : AppCompatActivity() {
    private lateinit var binding: ActivitySearchBinding
    private lateinit var locationAdapter: ArrayAdapter<Address>
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySearchBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val lastSearchedAddresses = arrayOf("")

        val lastLocationAdapter =
            ArrayAdapter(this, android.R.layout.simple_list_item_1, lastSearchedAddresses)

        binding.searchBar.setQuery(intent.getStringExtra("startingLetters"), true)
        binding.searchBar.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(p0: String): Boolean {
                val latLng = getAddresses(p0)
                println(latLng.toString() + "nds")
                intent.putExtra("Lat", latLng.latitude)
                intent.putExtra("Lng", latLng.longitude)
                setResult(RESULT_OK, intent)
                finish()
                return false
            }

            override fun onQueryTextChange(p0: String?): Boolean {
                return false
            }

        })
    }

    private fun getAddresses(address: String): LatLng {
        val geocoder = Geocoder(this, Locale.getDefault())
        return LatLng(
            geocoder.getFromLocationName(address, 1)[0].latitude,
            geocoder.getFromLocationName(address, 1)[0].longitude
        )
    }
}