package com.example.wadulv2

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.ProgressBar
import android.widget.Toast
import androidx.appcompat.app.AppCompatDelegate
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase

class RegisterProcessActivity : AppCompatActivity() {

    private var auth: FirebaseAuth? = null
    private lateinit var database : DatabaseReference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register_process)
        supportActionBar?.hide()
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)

        auth = FirebaseAuth.getInstance()
        val intent = intent
        val nama = intent.getStringExtra("nama").toString()
        val nik = intent.getStringExtra("nik").toString()
        val telepon = intent.getStringExtra("telepon").toString()
        val email = intent.getStringExtra("email").toString()
        val password = intent.getStringExtra("password").toString()

        val progressBar = findViewById<ProgressBar>(R.id.progressBar2) as ProgressBar
        progressBar!!.visibility = View.VISIBLE

        //authenticate user
        auth!!.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener(this@RegisterProcessActivity) { task ->
                // If sign in fails, display a message to the user. If sign in succeeds
                // the auth state listener will be notified and logic to handle the
                // signed in user can be handled in the listener.
                if (!task.isSuccessful) {
                    // there was an error
                    if (password.length < 6) {
                        val intent = Intent(this@RegisterProcessActivity, RegisterActivity::class.java)
                        startActivity(intent)
                        finish()
                    } else {
                        val intent = Intent(this@RegisterProcessActivity, RegisterActivity::class.java)
                        startActivity(intent)
                        finish()
                    }
                } else {
                    val currentUser = auth!!.currentUser
                    val uid = currentUser?.uid.toString()
//                    Database
                    database = FirebaseDatabase.getInstance("https://wadulv2-default-rtdb.asia-southeast1.firebasedatabase.app").getReference("users")
                    val user = user(uid, nama, nik, telepon, email)
                    database.child(uid).setValue(user)
                    progressBar!!.visibility = View.GONE
                    Toast.makeText(applicationContext, "Akun berhasil dibuat silahkan login", Toast.LENGTH_SHORT).show()
                    val intent = Intent(this@RegisterProcessActivity, LoginActivity::class.java)
                    startActivity(intent)
                    finish()
                }
            }
    }
}