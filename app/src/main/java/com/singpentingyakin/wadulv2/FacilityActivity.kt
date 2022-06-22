package com.singpentingyakin.wadulv2

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Spinner
import androidx.appcompat.app.AppCompatDelegate
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.singpentingyakin.wadulv2.databinding.ActivityOfficeBinding
import kotlinx.android.synthetic.main.activity_office.*

class FacilityActivity : AppCompatActivity(), OnMapReadyCallback, AdapterView.OnItemSelectedListener {

    private var lokasi = arrayOf("Terminal Arjosari", "Stasiun Malang Kota Lama", "Halte Bus Soekarno-Hatta")
    private var latitude : Double = -7.96558
    private var longitude : Double = 112.63877
    var fasum = String()
    var spinnerlok : Spinner? = null
    lateinit var bottomNav : BottomNavigationView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_facility)
        supportActionBar?.hide()
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)

        bottomNav = findViewById(R.id.bottom_navigation)
        bottomNav.setOnItemReselectedListener {
            when(it.itemId){
                R.id.home -> {
                    val intent = Intent(this, DashboardActivity::class.java)
                    startActivity(intent)
                    finish()
                }
                R.id.darurat -> {
                    val intent = Intent(this, PengaduanActivity::class.java)
                    startActivity(intent)
                    finish()
                }
                R.id.profile -> {
                    val intent = Intent(this, AspirasiActivity::class.java)
                    startActivity(intent)
                    finish()
                }
            }
        }

        //        Isi unit layanan
        spinnerlok = findViewById(R.id.s_lokasifasum)
        spinnerlok!!.onItemSelectedListener = this
        val aa = ArrayAdapter(this, android.R.layout.simple_spinner_item, lokasi)
        aa.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinnerlok!!.adapter = aa
        spinnerlok!!.onItemSelectedListener = this

        val mapFragment = supportFragmentManager.findFragmentById(R.id.map) as? SupportMapFragment
        mapFragment?.getMapAsync(this)

        button3.setOnClickListener {
            val intent = Intent(this, DashboardActivity::class.java)
            startActivity(intent)
            finish()
        }

    }

    override fun onMapReady(googleMap: GoogleMap) {
        val marker = LatLng(latitude, longitude)
        googleMap.addMarker(
            MarkerOptions()
                .position(marker)
                .title(fasum)
        )
        googleMap.moveCamera(CameraUpdateFactory.newLatLng(marker))
    }

    override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
        if (parent != null) {
            val text: String = parent.getItemAtPosition(position).toString()
            fasum = text
            when (fasum) {
                "Terminal Arjosari" -> {
                    latitude = -7.96558
                    longitude = 112.63877
                }
                "Stasium Malang Kota Lama" -> {
                    latitude = -7.92687
                    longitude = 112.65050
                }
                "Halte Bus Soekarno-Hatta" -> {
                    latitude = -8.13655
                    longitude = 112.57172
                }
            }
        }
    }

    override fun onNothingSelected(arg0: AdapterView<*>) {

    }
}