package com.example.wadulv2

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.*
import androidx.appcompat.app.AppCompatDelegate
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase

class DashboardActivity : AppCompatActivity() {

    private lateinit var mAuth: FirebaseAuth
    private lateinit var database : DatabaseReference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_dashboard)
        supportActionBar?.hide()
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)

        mAuth = FirebaseAuth.getInstance()
        val currentUser = mAuth.currentUser

        val uid = currentUser?.uid.toString()
        var namalengkap = String()
        var nik = String()
        var telepon = String()
        database = FirebaseDatabase.getInstance("https://wadulv2-default-rtdb.asia-southeast1.firebasedatabase.app").getReference("users")
        database.child(uid).get().addOnSuccessListener {
            if (it.exists()){
                namalengkap = it.child("namalengkap").value.toString()
                nik = it.child("nik").value.toString()
                telepon = it.child("telepon").value.toString()
                val fullname = findViewById<TextView>(R.id.FullName)
                fullname.text = namalengkap
            }else{
                Toast.makeText(this,"Error", Toast.LENGTH_SHORT).show()
            }
        }.addOnFailureListener{
            Toast.makeText(this,"Failed", Toast.LENGTH_SHORT).show()
        }

        val email = findViewById<TextView>(R.id.Email)
        email.text = currentUser?.email

        val logout = findViewById<Button>(R.id.Logoutbtn)
        logout.setOnClickListener {
            mAuth.signOut()
            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
            finish()
        }

        val pengaduan = findViewById<LinearLayout>(R.id.pengaduan_linear)
        pengaduan.setOnClickListener {
            val intentasp = Intent(this@DashboardActivity, AspirasiActivity::class.java)
            intentasp.putExtra("namalengkap", namalengkap)
            intentasp.putExtra("nik", nik)
            intentasp.putExtra("telepon", telepon)
            startActivity(intentasp)
            finish()
        }
    }
}