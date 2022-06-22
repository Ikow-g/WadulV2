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

class OfficeActivity : AppCompatActivity(), OnMapReadyCallback, AdapterView.OnItemSelectedListener {

    private var unitlayanan = arrayOf("Dinas Perhubungan", "Dinas Kesehatan", "PDAM", "Dinas Tata Ruang")
    private var latitude : Double = -7.96558
    private var longitude : Double = 112.63877
    var unit = String()
    var spinnerul : Spinner? = null
    lateinit var bottomNav : BottomNavigationView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_office)
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
                    val intent = Intent(this, EmergencyActivity::class.java)
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
        spinnerul = findViewById(R.id.s_unitlayananoffice)
        spinnerul!!.onItemSelectedListener = this
        val aa = ArrayAdapter(this, android.R.layout.simple_spinner_item, unitlayanan)
        aa.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinnerul!!.adapter = aa
        spinnerul!!.onItemSelectedListener = this

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
                .title(unit)
        )
        googleMap.moveCamera(CameraUpdateFactory.newLatLng(marker))
    }

    override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
        if (parent != null) {
            val text: String = parent.getItemAtPosition(position).toString()
            unit = text
            when (unit) {
                "Dinas Tata Ruang" -> {
                    latitude = -7.96558
                    longitude = 112.63877
                }
                "PDAM" -> {
                    latitude = -7.92687
                    longitude = 112.65050
                }
                "Dinas Kesehatan" -> {
                    latitude = -8.13655
                    longitude = 112.57172
                }
                "Dinas Perhubungan" -> {
                    latitude = -7.92937
                    longitude = 112.65010
                }
            }
        }
    }

    override fun onNothingSelected(arg0: AdapterView<*>) {

    }
}