package com.singpentingyakin.wadulv2

import android.app.AlertDialog
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Spinner
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.google.android.gms.tasks.OnCompleteListener
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.QuerySnapshot
import kotlinx.android.synthetic.main.activity_office.*


class OfficeActivity : AppCompatActivity(), OnMapReadyCallback, AdapterView.OnItemSelectedListener {

    private var unitlayanan = arrayOf("Dinas Perhubungan", "Dinas Kesehatan", "PDAM", "Dinas Tata Ruang")
    private var latitude : Double = -7.96558
    private var longitude : Double = 112.63877
    var unit = String()
    var spinnerul : Spinner? = null
    lateinit var bottomNav : BottomNavigationView
    private lateinit var isdialog: AlertDialog

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_office)
        supportActionBar?.hide()
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
        startLoading()

        val rootRef = FirebaseFirestore.getInstance()
        val subjectsRef = rootRef.collection("kabkota").document("Kabupaten Malang").collection("instansi")
        val spinner = findViewById<View>(R.id.s_unitlayananoffice) as Spinner
        spinner.onItemSelectedListener = this
        val subjects: MutableList<String?> = ArrayList()
        val adapter = ArrayAdapter(applicationContext, android.R.layout.simple_spinner_item, subjects)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinner.adapter = adapter
        subjectsRef.get().addOnCompleteListener { task ->
            if (task.isSuccessful) {
                isDismiss()
                for (document in task.result) {
                    val subject = document.getString("nama_instansi")
                    subjects.add(subject)
                }
                adapter.notifyDataSetChanged()
            }
        }

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
//        spinnerul = findViewById(R.id.s_unitlayananoffice)
//        spinnerul!!.onItemSelectedListener = this
//        val aa = ArrayAdapter(this, android.R.layout.simple_spinner_item, unitlayanan)
//        aa.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
//        spinnerul!!.adapter = aa
//        spinnerul!!.onItemSelectedListener = this

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
                "Dinas Pemuda dan Olahraga" -> {
                    latitude = -7.96558
                    longitude = 112.63877
                }
                "Dinas Pekerjaan Umum dan Perumahan Rakyat" -> {
                    latitude = -7.92687
                    longitude = 112.65050
                }
            }
        }
    }

    override fun onNothingSelected(arg0: AdapterView<*>) {

    }

    fun startLoading(){
        /**set View*/
        val infalter = this.layoutInflater
        val dialogView = infalter.inflate(R.layout.loading_item,null)
        /**set Dialog*/
        val bulider = AlertDialog.Builder(this)
        bulider.setView(dialogView)
        bulider.setCancelable(false)
        isdialog = bulider.create()
        isdialog.show()
    }
    fun isDismiss(){
        isdialog.dismiss()
    }
}