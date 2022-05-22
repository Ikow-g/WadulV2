package com.example.wadulv2

import android.content.Intent
import android.os.Bundle
import android.text.TextUtils
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import com.example.wadulv2.databinding.ActivityRegisterBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase

class RegisterActivity : AppCompatActivity() {
    private var auth: FirebaseAuth? = null
    private lateinit var database : DatabaseReference
    private lateinit var binding : ActivityRegisterBinding
//    Isian
    private var inputNama: EditText? = null
    private var inputNIK: EditText? = null
    private var inputTelepon: EditText? = null
    private var inputEmail: EditText? = null
    private var inputPassword: EditText? = null
//    Tombol
    private var btnSignUp: Button? = null
    private var btnBack: Button? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_register)
        supportActionBar?.hide()
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)

        auth = FirebaseAuth.getInstance()
        inputNama = findViewById<View>(R.id.reg_nama) as EditText
        inputNIK = findViewById<View>(R.id.reg_nik) as EditText
        inputTelepon = findViewById<View>(R.id.reg_telepon) as EditText
        inputEmail = findViewById<View>(R.id.reg_email) as EditText
        inputPassword = findViewById<View>(R.id.reg_password) as EditText
        btnBack = findViewById<View>(R.id.backtologin) as Button
        btnSignUp = findViewById<View>(R.id.daftar) as Button

        btnBack!!.setOnClickListener { finish() }

        //            Simpan data kependudukan
        binding = ActivityRegisterBinding.inflate(layoutInflater)
        setContentView(binding.root)

        //balik
        binding.backtologin.setOnClickListener {
            startActivity(
                Intent(
                    this@RegisterActivity,
                    LoginActivity::class.java
                )
            )
        }

        binding.daftar.setOnClickListener(View.OnClickListener {
//            Kamus
            val email = binding.regEmail.text.toString().trim { it <= ' ' }
            val password = binding.regPassword.text.toString().trim { it <= ' ' }
            val namalengkap = binding.regNama.text.toString()
            val nik = binding.regNik.text.toString()
            val telepon = binding.regTelepon.text.toString()
            val email2 = binding.regEmail.text.toString()
//            Database
            database = FirebaseDatabase.getInstance("https://wadulv2-default-rtdb.asia-southeast1.firebasedatabase.app").getReference("users")
            val user = user(namalengkap, nik, telepon, email2)
            database.child(namalengkap).setValue(user)
//            Autentikasi
            if (TextUtils.isEmpty(email)) {
                Toast.makeText(applicationContext, "Enter email address!", Toast.LENGTH_SHORT)
                    .show()
                return@OnClickListener
            }
            if (TextUtils.isEmpty(password)) {
                Toast.makeText(applicationContext, "Enter password!", Toast.LENGTH_SHORT).show()
                return@OnClickListener
            }
            if (password.length < 6) {
                Toast.makeText(
                    applicationContext,
                    "Password too short, enter minimum 6 characters!",
                    Toast.LENGTH_SHORT
                ).show()
                return@OnClickListener
            }

            //create user
            auth!!.createUserWithEmailAndPassword(email, password).addOnCompleteListener(this@RegisterActivity) { task ->
                Toast.makeText(this@RegisterActivity,"createUserWithEmail:onComplete:" + task.isSuccessful, Toast.LENGTH_SHORT).show()
                    // If sign in fails, display a message to the user. If sign in succeeds
                    // the auth state listener will be notified and logic to handle the
                    // signed in user can be handled in the listener.
                if (!task.isSuccessful) {
                    Toast.makeText(this@RegisterActivity, "Authentication failed." + task.exception,Toast.LENGTH_SHORT).show()
                } else {
                    startActivity(Intent(this@RegisterActivity, DashboardActivity::class.java))
                    finish()
                }
            }
//
        })
    }
}