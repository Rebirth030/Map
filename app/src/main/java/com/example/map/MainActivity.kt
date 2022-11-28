package com.example.map

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.map.databinding.ActivityMainBinding
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment

 class MainActivity : AppCompatActivity(),OnMapReadyCallback {
    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val mapFragment = supportFragmentManager.findFragmentById(binding.maps.id) as SupportMapFragment
        mapFragment.getMapAsync(this)
    }

     override fun onMapReady(p0: GoogleMap) {
     }
 }