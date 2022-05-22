package com.example.wadulv2

import android.content.Intent
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.appcompat.app.AppCompatDelegate
import com.google.firebase.auth.FirebaseAuth
import androidx.navigation.findNavController
import com.bumptech.glide.Glide

class DashboardActivity : AppCompatActivity() {

    private lateinit var mAuth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_dashboard)
        supportActionBar?.hide()
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)

        mAuth = FirebaseAuth.getInstance()
        val currentUser = mAuth.currentUser

        val fullname = findViewById<TextView>(R.id.FullName) as TextView
        fullname.text = currentUser?.displayName
        val email = findViewById<TextView>(R.id.Email) as TextView
        email.text = currentUser?.email

        val logout = findViewById<Button>(R.id.Logoutbtn) as Button
        logout.setOnClickListener {
            mAuth.signOut()
            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
            finish()
        }

        val pengaduan = findViewById<LinearLayout>(R.id.pengaduan_linear) as LinearLayout
        pengaduan.setOnClickListener {
            startActivity(
                Intent(
                    this@DashboardActivity,
                    PengaduanActivity::class.java
                )
            )
        }

        val aspirasi = findViewById<LinearLayout>(R.id.aspirasi_linear)
        aspirasi.setOnClickListener{
            startActivity(
                Intent(
                    this@DashboardActivity,
                    AspirasiActivity::class.java
                )
            )
        }

        val tiket = findViewById<LinearLayout>(R.id.tiket_linear)
        tiket.setOnClickListener {
            startActivity(
                Intent(
                    this@DashboardActivity,
                    TiketActivity::class.java
                )
            )
        }
    }
}