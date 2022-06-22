package com.singpentingyakin.wadulv2

import android.Manifest
import android.content.ContentValues.TAG
import android.content.Intent
import android.content.pm.PackageManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.*
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.app.ActivityCompat
import androidx.core.view.GravityCompat
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.android.synthetic.main.activity_dashboard.*

class DashboardActivity : AppCompatActivity() {
    private val permissions = arrayOf(Manifest.permission.CAMERA,
        Manifest.permission.RECORD_AUDIO, Manifest.permission.INTERNET,
        Manifest.permission.MODIFY_AUDIO_SETTINGS, Manifest.permission.ACCESS_COARSE_LOCATION,
        Manifest.permission.ACCESS_FINE_LOCATION)
    private val requestcode = 1
    private lateinit var mAuth: FirebaseAuth
    lateinit var toggle: ActionBarDrawerToggle
    lateinit var bottomNav : BottomNavigationView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_dashboard)
        supportActionBar?.hide()
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)

        if (!isPermissionGranted()) {
            askPermissions()
        }

        bottomNav = findViewById(R.id.bottom_navigation)
        bottomNav.setOnItemReselectedListener {
            when(it.itemId){
                R.id.home -> {

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

//        Ambil data
        mAuth = FirebaseAuth.getInstance()
        val currentUser = mAuth.currentUser
        val uid = currentUser?.uid.toString()
        val namalengkap = currentUser?.displayName.toString()
        val email = currentUser?.email.toString()
        var nik = String()
        var telepon = String()
        val db = FirebaseFirestore.getInstance()
        val docRef = db.collection("users").document(uid)
        docRef.get().addOnSuccessListener { document ->
                if (document != null) {
                    nik = document.getString("nik").toString()
                    telepon = document.getString("telepon").toString()
                } else {
                    Log.d(TAG, "No such document")
                }
            }
            .addOnFailureListener { exception ->
                Log.d(TAG, "get failed with ", exception)
            }
//        Tampilkan data di dashboard
        val fullname = findViewById<TextView>(R.id.FullName)
        fullname.text = namalengkap
        val emailview = findViewById<TextView>(R.id.Email)
        emailview.text = email

        val logout = findViewById<Button>(R.id.Logoutbtn)
        logout.setOnClickListener {
            mAuth.signOut()
            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
            finish()
        }

        val pengaduan = findViewById<LinearLayout>(R.id.pengaduan_linear)
        pengaduan.setOnClickListener {
            val intentasp = Intent(this@DashboardActivity, PengaduanActivity::class.java)
            intentasp.putExtra("namalengkap", namalengkap)
            intentasp.putExtra("nik", nik)
            intentasp.putExtra("telepon", telepon)
            intentasp.putExtra("uid", uid)
            startActivity(intentasp)
            finish()
        }

        val aspirasi = findViewById<LinearLayout>(R.id.aspirasi_linear)
        aspirasi.setOnClickListener {
            val intentasp = Intent(this@DashboardActivity, AspirasiActivity::class.java)
            intentasp.putExtra("namalengkap", namalengkap)
            intentasp.putExtra("nik", nik)
            intentasp.putExtra("telepon", telepon)
            intentasp.putExtra("uid", uid)
            startActivity(intentasp)
            finish()
        }

        val cektiket = findViewById<LinearLayout>(R.id.tiket_linear)
        cektiket.setOnClickListener {
            val intentasp = Intent(this@DashboardActivity, TicketCheckActivity::class.java)
            intentasp.putExtra("namalengkap", namalengkap)
            intentasp.putExtra("nik", nik)
            intentasp.putExtra("telepon", telepon)
            intentasp.putExtra("uid", uid)
            startActivity(intentasp)
            finish()
        }

        val kantor = findViewById<LinearLayout>(R.id.kantor_linear)
        kantor.setOnClickListener {
            val intentasp = Intent(this@DashboardActivity, OfficeActivity::class.java)
            intentasp.putExtra("namalengkap", namalengkap)
            intentasp.putExtra("nik", nik)
            intentasp.putExtra("telepon", telepon)
            intentasp.putExtra("uid", uid)
            startActivity(intentasp)
            finish()
        }

        val fasum = findViewById<LinearLayout>(R.id.fasum_linear)
        fasum.setOnClickListener {
            val intentasp = Intent(this@DashboardActivity, FacilityActivity::class.java)
            intentasp.putExtra("namalengkap", namalengkap)
            intentasp.putExtra("nik", nik)
            intentasp.putExtra("telepon", telepon)
            intentasp.putExtra("uid", uid)
            startActivity(intentasp)
            finish()
        }

        val darurat = findViewById<LinearLayout>(R.id.darurat_linear)
        darurat.setOnClickListener {
            val intentasp = Intent(this@DashboardActivity, EmergencyActivity::class.java)
            intentasp.putExtra("namalengkap", namalengkap)
            intentasp.putExtra("nik", nik)
            intentasp.putExtra("telepon", telepon)
            intentasp.putExtra("uid", uid)
            startActivity(intentasp)
            finish()
        }

        //drawer
        //semua tutorial pake toolbar, aku pingin biar dia buka drawer tapi pake profile.
        supportActionBar
        val toolbar = null
        setSupportActionBar(toolbar)
        toggle = ActionBarDrawerToggle(this,drawer_layout,toolbar,R.string.open,R.string.close)
        drawer_layout.addDrawerListener(toggle)
        toggle.syncState()

    }
    private fun askPermissions() {
        ActivityCompat.requestPermissions(this, permissions, requestcode)
    }

    private fun isPermissionGranted(): Boolean {
        permissions.forEach {
            if (ActivityCompat.checkSelfPermission(this, it) != PackageManager.PERMISSION_GRANTED)
                return false
        }
        return true
    }
}