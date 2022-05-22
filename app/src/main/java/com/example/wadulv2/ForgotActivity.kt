package com.example.wadulv2

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.TextUtils
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatDelegate
import com.example.wadulv2.databinding.ActivityForgotBinding
import com.example.wadulv2.databinding.ActivityLoginBinding
import com.google.firebase.auth.FirebaseAuth

class ForgotActivity : AppCompatActivity() {

    private var auth: FirebaseAuth? = null
    private lateinit var binding : ActivityForgotBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_forgot)
        supportActionBar?.hide()
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)

        binding = ActivityForgotBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.backToLogin.setOnClickListener {
            startActivity(
                Intent(
                    this@ForgotActivity,
                    LoginActivity::class.java
                )
            )
        }

        auth = FirebaseAuth.getInstance()

        binding.submitButton.setOnClickListener(View.OnClickListener {
            val email = binding.textEmail.text.toString().trim { it <= ' ' }
            if (TextUtils.isEmpty(email)) {
                Toast.makeText(application, "Enter your registered email id", Toast.LENGTH_SHORT)
                    .show()
                return@OnClickListener
            }
            auth!!.sendPasswordResetEmail(email)
                .addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        Toast.makeText(
                            this@ForgotActivity,
                            "We have sent you instructions to reset your password!",
                            Toast.LENGTH_SHORT
                        ).show()
                    } else {
                        Toast.makeText(
                            this@ForgotActivity,
                            "Failed to send reset email!",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }
        })
    }
}