package com.singpentingyakin.wadulv2

import android.app.AlertDialog
import android.content.ContentValues
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.EditText
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.content.ContextCompat
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.android.synthetic.main.activity_ticket_check.*

class TicketCheckActivity : AppCompatActivity() {

    private lateinit var isdialog: AlertDialog
    lateinit var bottomNav : BottomNavigationView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_ticket_check)
        supportActionBar?.hide()
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)

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

        val db = FirebaseFirestore.getInstance()
        var isistatus: String

        button2ticket.setOnClickListener {
            startLoading()
            val nomortiket = findViewById<EditText>(R.id.s_nomortiket).text.toString()
            val docRef = db.collection("pengaduan").document(nomortiket)
            docRef.get().addOnSuccessListener { document ->
                if (document != null) {
                    isistatus = document.getString("status").toString()
                    if(isistatus == "1"){
                        status.setText("Dalam Proses")
                        status.setTextColor(ContextCompat.getColor(this, R.color.red))
                        isDismiss()
                    }else if(isistatus == "0"){
                        status.setText("Selesai Diproses")
                        status.setTextColor(ContextCompat.getColor(this, R.color.black))
                        isDismiss()
                    }else{
                        status.setText("Tiket tidak ditemukan")
                        status.setTextColor(ContextCompat.getColor(this, R.color.black))
                        isDismiss()
                    }
//                    Di pengaduan tidak ketemu, coba cari di aspirasi
                } else {
                    val docRef = db.collection("aspirasi").document(nomortiket)
                    docRef.get().addOnSuccessListener { document ->
                        if (document != null) {
                            isistatus = document.getString("status").toString()
                            if(isistatus == "1"){
                                status.setText("Dalam Proses")
                                status.setTextColor(ContextCompat.getColor(this, R.color.red))
                                isDismiss()
                            }else if(isistatus == "0"){
                                status.setText("Selesai Diproses")
                                status.setTextColor(ContextCompat.getColor(this, R.color.black))
                                isDismiss()
                            }else{
                                status.setText("Tiket tidak ditemukan")
                                status.setTextColor(ContextCompat.getColor(this, R.color.black))
                                isDismiss()
                            }
                        } else {
                            Log.d(ContentValues.TAG, "No such document")
                            status.setText("Tiket tidak ditemukan")
                            status.setTextColor(ContextCompat.getColor(this, R.color.black))
                            isDismiss()
                        }
                    }.addOnFailureListener { exception ->
                        Log.d(ContentValues.TAG, "get failed aspirasi with ", exception)
                    }
                }
            }.addOnFailureListener { exception ->
                Log.d(ContentValues.TAG, "get failed pengaduan with ", exception)
            }
        }
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