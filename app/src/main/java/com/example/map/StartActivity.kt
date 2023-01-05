package com.example.map

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.map.databinding.ActivityStartBinding


@Suppress("DEPRECATION")
class StartActivity : AppCompatActivity() {
    private lateinit var binding: ActivityStartBinding

    /**
     * onCreate ruf buttonInit auf und checkPermission von MapUtil
     */
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_start)
        binding = ActivityStartBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setSupportActionBar(binding.toolBar2)

        buttonInit()

        MapUtil.checkPermission(this)
    }

    /**
     * ZUSATZINHALT
     * erstellt ItemMenu
     */
    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.main_menu, menu)
        return true
    }

    /**
     * ZUSATZINHALT
     * macht Funktionalität des ItemMenus
     */
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        val itemView = item.itemId
        if (itemView == R.id.ChangeLang) MapUtil.showChangeLang(this)
        if (itemView == R.id.marker) Toast.makeText(this, getString(R.string.notAvailable), Toast.LENGTH_SHORT).show()
        if (itemView == R.id.markerOpt) Toast.makeText(this, getString(R.string.notAvailable), Toast.LENGTH_SHORT).show()
        return false
    }

    /**
     * setzt den Button, um auf drücken die Werte an dei MapsAktivität weiter zu geben.
     */
    private fun buttonInit() {
        binding.createMarker.setOnClickListener {
            if (binding.etLocationName.text.isNotEmpty() && binding.editTextNumberDecimal2.text.isNotEmpty() && binding.editTextNumberDecimal3.text.isNotEmpty() && binding.etLocationNameTwo.text.isNotEmpty() && binding.editTextNumberDecimal2Two.text.isNotEmpty() && binding.editTextNumberDecimal3Two.text.isNotEmpty() && MapUtil.checkIfLatLongValid(
                    binding.editTextNumberDecimal2.text.toString(),
                    binding.editTextNumberDecimal3.text.toString()
                ) && MapUtil.checkIfLatLongValid(
                    binding.editTextNumberDecimal2Two.text.toString(),
                    binding.editTextNumberDecimal3Two.text.toString()
                )
            ) {
                val intent = Intent(this, MapsActivity::class.java)
               intent.putExtra("data", initArrayList())
                startActivity(intent)

            } else {
                MapUtil.vibratePhone(this)
                Toast.makeText(this, getString(R.string.validDataError), Toast.LENGTH_SHORT)
                    .show()
            }
        }
    }

    /**
     * initialisiert die arraylist aus den Angaben in den Zeilen
     */
    private fun initArrayList(): ArrayList<String> {
        return arrayListOf(
            binding.etLocationName.text.toString(),
            binding.editTextNumberDecimal2.text.toString(),
            binding.editTextNumberDecimal3.text.toString(),
            binding.etLocationNameTwo.text.toString(),
            binding.editTextNumberDecimal2Two.text.toString(),
            binding.editTextNumberDecimal3Two.text.toString()
        )
    }
}