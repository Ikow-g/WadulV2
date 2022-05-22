package com.example.wadulv2

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.appcompat.app.AppCompatDelegate
import android.view.View
import android.widget.*
import com.example.wadulv2.databinding.ActivityAspirasiBinding
import com.example.wadulv2.databinding.ActivityLoginBinding

class AspirasiActivity : AppCompatActivity(),AdapterView.OnItemSelectedListener {

    var unitlayanan = arrayOf("Dinas Perhubungan", "Dinas Kesehatan", "POLRI", "TNI")
    var keperluan = arrayOf("Parkir Liar", "Penanganan COVID", "Tindak Kriminal", "Kamtibmas")
    var kabkota = arrayOf("Kabupaten Malang", "Kota Malang")
    var kecamatan = arrayOf("Singosari", "Lowokwaru")
    var keldesa = arrayOf("Ardimulyo", "Mojolangu")
    var spinner : Spinner? = null

    private lateinit var binding : ActivityAspirasiBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_aspirasi)
        supportActionBar?.hide()
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)

        binding = ActivityAspirasiBinding.inflate(layoutInflater)
        setContentView(binding.root)

        spinner = binding.sUnitlayanan
        spinner!!.onItemSelectedListener = this

        // Create an ArrayAdapter using a simple spinner layout and languages array
        val aa = ArrayAdapter(this, android.R.layout.simple_spinner_item, unitlayanan)
        // Set layout to use when the list of choices appear
        aa.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        // Set Adapter to Spinner
        spinner!!.adapter = aa
    }

    override fun onItemSelected(arg0: AdapterView<*>, arg1: View, position: Int, id: Long) {

    }

    override fun onNothingSelected(arg0: AdapterView<*>) {

    }
}