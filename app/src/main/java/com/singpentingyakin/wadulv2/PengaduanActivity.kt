package com.singpentingyakin.wadulv2

import android.app.AlertDialog
import android.app.DatePickerDialog
import android.content.Intent
import android.icu.util.Calendar
import android.location.Geocoder
import android.location.Location
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Looper
import android.util.Log
import androidx.appcompat.app.AppCompatDelegate
import android.view.View
import android.widget.*
import androidx.annotation.RequiresApi
import com.google.android.gms.location.*
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.singpentingyakin.wadulv2.databinding.ActivityPengaduanBinding
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.android.synthetic.main.activity_pengaduan.*
import java.util.*

class PengaduanActivity : AppCompatActivity(),AdapterView.OnItemSelectedListener {

    private var unitlayanan = arrayOf("Dinas Perhubungan", "Dinas Kesehatan", "POLRI", "TNI")
    private val c = Calendar.getInstance()
    private val year = c.get(Calendar.YEAR)
    private val month = c.get(Calendar.MONTH)
    private val day = c.get(Calendar.DAY_OF_MONTH)
    var spinnerul : Spinner? = null
    lateinit var fusedLocationProviderClient: FusedLocationProviderClient

    var unit : String? = null
    var tanggalfull : String? = null
    var lat : String? = null
    var long: String? = null

    private lateinit var isdialog: AlertDialog
    private lateinit var binding : ActivityPengaduanBinding
    lateinit var bottomNav : BottomNavigationView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_pengaduan)
        supportActionBar?.hide()
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)

        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this)
        getLastLocation()

        binding = ActivityPengaduanBinding.inflate(layoutInflater)
        setContentView(binding.root)

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
        spinnerul = binding.sUnitlayanan
        spinnerul!!.onItemSelectedListener = this
        val aa = ArrayAdapter(this, android.R.layout.simple_spinner_item, unitlayanan)
        aa.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinnerul!!.adapter = aa
        spinnerul!!.onItemSelectedListener = this

//        Isi tanggal
        s_tanggal.setOnClickListener {
            clickDataPicker()
        }

        val isiuid = UUID.randomUUID().toString()

        binding.button2.setOnClickListener {
            startLoading()
            //        Isi lokasi
            val textlokasi = findViewById<EditText>(R.id.s_lokasi).text.toString()
            //        Isi keperluan
            val textkep  = findViewById<EditText>(R.id.s_keperluan).text.toString()
            //        Isi deskripsi
            val deskripsi : String = findViewById<EditText>(R.id.isiasp).text.toString()
            saveFireStore(isiuid, unit!!, textkep, lat!!, long!!,
                textlokasi, tanggalfull!!, deskripsi, "1")
        }

        binding.button3.setOnClickListener {
            val intent = Intent(this, DashboardActivity::class.java)
            startActivity(intent)
            finish()
        }
    }

    override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
        if (parent != null) {
            val text: String = parent.getItemAtPosition(position).toString()
            unit = text
        }
    }

    override fun onNothingSelected(arg0: AdapterView<*>) {

    }

    @RequiresApi(Build.VERSION_CODES.N)
    fun clickDataPicker() {
        val dpd = DatePickerDialog(this, { _, year, monthOfYear, dayOfMonth ->
            val tanggalhari = dayOfMonth.toString()
            val bulanhari = (monthOfYear + 1).toString()
            val tahunhari = year.toString()
            tanggalfull = "$tanggalhari-$bulanhari-$tahunhari"
            s_tanggal.setText("$tanggalhari/$bulanhari/$tahunhari")
        }, year, month, day)
        dpd.show()
    }

    private fun saveFireStore(id: String, unitlayanan: String, keperluan: String, latitude: String, longitude: String, alamat: String, tanggal: String, deskripsi: String, status: String) {
        val db = FirebaseFirestore.getInstance()
        val isipengaduan = hashMapOf(
            "idpengaduan" to id,
            "unitlayanan" to  unitlayanan,
            "keperluan" to keperluan,
            "latitude" to latitude,
            "longitude" to longitude,
            "alamat" to alamat,
            "tanggal" to tanggal,
            "deskripsi" to deskripsi,
            "status" to status
        )
        db.collection("pengaduan").document(id).set(isipengaduan).addOnSuccessListener {
            Toast.makeText(applicationContext, "Berhasil membuat pengaduan", Toast.LENGTH_SHORT).show()
            isDismiss()
            val intent = Intent(this, DashboardActivity::class.java)
            startActivity(intent)
            finish()
        }.addOnFailureListener{
            Toast.makeText(applicationContext, "Pengaduan gagal ! Silahkan coba kembali", Toast.LENGTH_SHORT).show()
        }
    }
    private fun getLastLocation(){
        fusedLocationProviderClient.lastLocation.addOnCompleteListener {task->
            val location: Location? = task.result
            if(location == null){
                newLocationData()
            }else{
                Log.d("Debug:" ,"Your Location:"+ location.longitude)
                s_lokasi.setText("${location.longitude} " + " ${location.latitude} " + getCityName(location.latitude,location.longitude))
                long = location.longitude.toString()
                lat = location.latitude.toString()
            }
        }
    }
    private fun newLocationData(){
        val locationRequest =  LocationRequest()
        locationRequest.priority = LocationRequest.PRIORITY_HIGH_ACCURACY
        locationRequest.interval = 0
        locationRequest.fastestInterval = 0
        locationRequest.numUpdates = 1
        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this)
        fusedLocationProviderClient.requestLocationUpdates(
            locationRequest,locationCallback, Looper.myLooper()
        )
    }

    private val locationCallback = object : LocationCallback(){
        override fun onLocationResult(locationResult: LocationResult) {
            val lastLocation: Location? = locationResult.lastLocation
            if (lastLocation != null) {
                Log.d("Debug:","your last last location: "+ lastLocation.longitude.toString())
            }
        }
    }

    private fun getCityName(lat: Double,long: Double):String{
        val cityName: String
        val countryName: String
        val geoCoder = Geocoder(this, Locale.getDefault())
        val adresss = geoCoder.getFromLocation(lat,long,3)

        cityName = adresss[0].locality
        countryName = adresss[0].countryName
        Log.d("Debug:", "Your City: $cityName ; your Country $countryName")
        return cityName
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