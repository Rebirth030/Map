package com.example.map

import android.content.pm.PackageManager
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import com.example.map.databinding.ActivityStartBinding

class StartActivity : AppCompatActivity() {
    private lateinit var binding: ActivityStartBinding
    companion object {
        private const val LOCATION_PERMISSION_REQUEST_CODE = 1

    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_start)
        binding = ActivityStartBinding.inflate(layoutInflater)
        setContentView(binding.root)

        buttonInit()

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
    }

    private fun buttonInit() {
        binding.createMarker.setOnClickListener {
            if (binding.etLocationName.text.isEmpty() || binding.editTextNumberDecimal2.text.isEmpty() || binding.editTextNumberDecimal3.text.isEmpty() || binding.etLocationNameTwo.text.isEmpty() || binding.editTextNumberDecimal2Two.text.isEmpty() || binding.editTextNumberDecimal3Two.text.isEmpty()){
                Toast.makeText(this, "Please fill every field", Toast.LENGTH_SHORT).show()
            }
        }
    }
}