package com.singpentingyakin.wadulv2

import android.content.ContentValues.TAG
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.*
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.view.GravityCompat
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.android.synthetic.main.activity_dashboard.*

class DashboardActivity : AppCompatActivity() {

    private lateinit var mAuth: FirebaseAuth
    lateinit var toggle: ActionBarDrawerToggle

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_dashboard)
        supportActionBar?.hide()
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)

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

//        database = FirebaseDatabase.getInstance("https://wadulv2-default-rtdb.asia-southeast1.firebasedatabase.app").getReference("users")
//        database.child(uid).get().addOnSuccessListener {
//            if (it.exists()){
//                nik = it.child("nik").value.toString()
//                telepon = it.child("telepon").value.toString()
//                val fullname = findViewById<TextView>(R.id.FullName)
//                fullname.text = namalengkap
//            }else{
//                Toast.makeText(this,"Error", Toast.LENGTH_SHORT).show()
//            }
//        }.addOnFailureListener{
//            Toast.makeText(this,"Failed", Toast.LENGTH_SHORT).show()
//        }

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

        //drawer
        //semua tutorial pake toolbar, aku pingin biar dia buka drawer tapi pake profile.
        supportActionBar
        val toolbar = null
        setSupportActionBar(toolbar)
        toggle = ActionBarDrawerToggle(this,drawer_layout,toolbar,R.string.open,R.string.close)
        drawer_layout.addDrawerListener(toggle)
        toggle.syncState()

    }
}