package com.singpentingyakin.wadulv2

import android.app.AlertDialog
import android.content.Intent
import android.location.Geocoder
import android.location.Location
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Looper
import android.util.Log
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatDelegate
import com.google.android.gms.location.*
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.firebase.firestore.FirebaseFirestore
import com.singpentingyakin.wadulv2.databinding.ActivityAspirasiBinding
import kotlinx.android.synthetic.main.activity_aspirasi.*
import java.util.*

class AspirasiActivity : AppCompatActivity(),AdapterView.OnItemSelectedListener {

    private var unitlayanan = arrayOf("Dinas Perhubungan", "Dinas Kesehatan", "POLRI", "TNI")
    var spinnerulasp : Spinner? = null
    lateinit var fusedLocationProviderClient: FusedLocationProviderClient
    var unit : String? = null
    private lateinit var binding : ActivityAspirasiBinding
    private lateinit var isdialog: AlertDialog
    lateinit var bottomNav : BottomNavigationView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_aspirasi)
        supportActionBar?.hide()
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)

        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this)
        getLastLocation()
        startLoading()

        binding = ActivityAspirasiBinding.inflate(layoutInflater)
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

                }
            }
        }

        val rootRef = FirebaseFirestore.getInstance()
        val subjectsRef = rootRef.collection("kabkota").document("Kabupaten Malang").collection("instansi")
        val spinner = findViewById<View>(R.id.s_unitlayananasp) as Spinner
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

        //        Isi unit layanan
//        spinnerulasp = binding.sUnitlayananasp
//        spinnerulasp!!.onItemSelectedListener = this
//        val aa = ArrayAdapter(this, android.R.layout.simple_spinner_item, unitlayanan)
//        aa.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
//        spinnerulasp!!.adapter = aa
//        spinnerulasp!!.onItemSelectedListener = this

        val isiuid = UUID.randomUUID().toString()

        binding.button2asp.setOnClickListener {
            startLoading()
            val textlokasi = findViewById<EditText>(R.id.s_lokasiasp).text.toString()
            val perihal = findViewById<EditText>(R.id.s_keperluanasp).text.toString()
            val deskripsi : String = findViewById<EditText>(R.id.isiasp2).text.toString()
            saveFireStore(isiuid, unit!!, perihal, textlokasi, deskripsi)
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

    private fun saveFireStore(id: String, unitlayanan: String, perihal: String, lokasi: String, deskripsi: String) {
        val db = FirebaseFirestore.getInstance()
        val isipengaduan = hashMapOf(
            "idaspirasi" to id,
            "unitlayanan" to  unitlayanan,
            "perihal" to perihal,
            "lokasi" to lokasi,
            "deskripsi" to deskripsi
        )
        db.collection("aspirasi").document(id).set(isipengaduan).addOnSuccessListener {
            Toast.makeText(applicationContext, "Berhasil membuat aspirasi", Toast.LENGTH_SHORT).show()
            isDismiss()
            val intent = Intent(this, DashboardActivity::class.java)
            startActivity(intent)
            finish()
        }.addOnFailureListener{
            Toast.makeText(applicationContext, "Pembuatan aspirasi gagal ! Silahkan coba kembali", Toast.LENGTH_SHORT).show()
        }
    }

    private fun getLastLocation(){
        fusedLocationProviderClient.lastLocation.addOnCompleteListener {task->
            val location: Location? = task.result
            if(location == null){
                newLocationData()
            }else{
                Log.d("Debug:" ,"Your Location:"+ location.longitude)
                s_lokasiasp.setText("${location.longitude} " + " ${location.latitude} " + getCityName(location.latitude,location.longitude))
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