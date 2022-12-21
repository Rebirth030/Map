package com.example.map

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.map.databinding.ActivityStartBinding

class StartActivity : AppCompatActivity() {
    private lateinit var binding: ActivityStartBinding
    private lateinit var arrayList: ArrayList<String>


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_start)
        binding = ActivityStartBinding.inflate(layoutInflater)
        setContentView(binding.root)

        buttonInit()

        MapUtil.checkPermission(this)
    }

    private fun buttonInit() {
        binding.createMarker.setOnClickListener {
            if (binding.etLocationName.text.isEmpty() || binding.editTextNumberDecimal2.text.isEmpty() || binding.editTextNumberDecimal3.text.isEmpty() || binding.etLocationNameTwo.text.isEmpty() || binding.editTextNumberDecimal2Two.text.isEmpty() || binding.editTextNumberDecimal3Two.text.isEmpty() && MapUtil.checkIfLatLongValid(
                    binding.editTextNumberDecimal2.text.toString(),
                    binding.editTextNumberDecimal3.text.toString()
                ) && MapUtil.checkIfLatLongValid(
                    binding.editTextNumberDecimal2Two.text.toString(),
                    binding.editTextNumberDecimal3Two.text.toString()
                )
            ) {
                Toast.makeText(this, "Please fill every field with valid data", Toast.LENGTH_SHORT)
                    .show()
            } else {
                val intent = Intent(this, MapsActivity::class.java)
                initArrayList()
                intent.putExtra("data", arrayList)
                startActivity(intent)
            }
        }
    }

    private fun initArrayList() {
        arrayList = arrayListOf(
            binding.etLocationName.text.toString(),
            binding.editTextNumberDecimal2.text.toString(),
            binding.editTextNumberDecimal3.text.toString(),
            binding.etLocationNameTwo.text.toString(),
            binding.editTextNumberDecimal2Two.text.toString(),
            binding.editTextNumberDecimal3Two.text.toString()
        )
    }
}