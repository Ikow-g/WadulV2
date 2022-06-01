package com.example.wadulv2

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.appcompat.app.AppCompatDelegate
import android.view.View
import android.widget.*
import com.example.wadulv2.databinding.ActivityPengaduanBinding
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import java.util.*

class PengaduanActivity : AppCompatActivity(),AdapterView.OnItemSelectedListener {

    var unitlayanan = arrayOf("Dinas Perhubungan", "Dinas Kesehatan", "POLRI", "TNI")
    var keperluan = arrayOf("Parkir Liar", "Penanganan COVID", "Tindak Kriminal", "Kamtibmas")
    var kabkota = arrayOf("Kabupaten Malang", "Kota Malang")
    var kecamatan = arrayOf("Singosari", "Lowokwaru")
    var keldesa = arrayOf("Ardimulyo", "Mojolangu")

    var spinnerul : Spinner? = null
    var spinnerkep : Spinner? = null
    var spinnerkabkota : Spinner? = null
    var spinnerkec : Spinner? = null
    var spinnerdes : Spinner? = null

    private lateinit var database : DatabaseReference

    private lateinit var binding : ActivityPengaduanBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_pengaduan)
        supportActionBar?.hide()
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)

        binding = ActivityPengaduanBinding.inflate(layoutInflater)
        setContentView(binding.root)
//        Daftar isi spinner unit layanan
        spinnerul = binding.sUnitlayanan
        spinnerul!!.onItemSelectedListener = this
        val aa = ArrayAdapter(this, android.R.layout.simple_spinner_item, unitlayanan)
        aa.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinnerul!!.adapter = aa
        spinnerul!!.onItemSelectedListener = this

        spinnerkep = binding.sKeperluan
        val bb = ArrayAdapter(this, android.R.layout.simple_spinner_item, keperluan)
        bb.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinnerkep!!.adapter = bb
        spinnerkep!!.onItemSelectedListener = this

        spinnerkabkota = binding.sKabupaten
        val cc = ArrayAdapter(this, android.R.layout.simple_spinner_item, kabkota)
        cc.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinnerkabkota!!.adapter = cc
        spinnerkabkota!!.onItemSelectedListener = this

        spinnerkec = binding.sKecamatan
        val dd = ArrayAdapter(this, android.R.layout.simple_spinner_item, kecamatan)
        dd.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinnerkec!!.adapter = dd
        spinnerkec!!.onItemSelectedListener = this

        spinnerdes = binding.sKelurahan
        val ee = ArrayAdapter(this, android.R.layout.simple_spinner_item, keldesa)
        ee.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinnerdes!!.adapter = ee
        spinnerdes!!.onItemSelectedListener = this
    }

    override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
        val text: String = parent?.getItemAtPosition(position).toString()
//        Kamus
        var isiinstansi = ""
        var isikeperluan = ""
        var isikota = ""
        var isikec = ""
        var isides = ""

        if(text == "Dinas Perhubungan" || text == "Dinas Kesehatan" || text == "POLRI" || text == "TNI"){
            isiinstansi = text
        }else if(text == "Parkir Liar" || text == "Penanganan COVID" || text == "Tindak Kriminal" || text == "Kamtibmas"){
            isikeperluan = text
        }else if(text == "Kabupaten Malang" || text == "Kota Malang"){
            isikota = text
        }else if(text == "Singosari" || text == "Lowokwaru"){
            isikec = text
        }else if(text == "Ardimulyo" || text == "Mojolangu"){
            isides = text
        }

//        val isinama:String = intent.getStringExtra("namalengkap").toString()
//        val isinik:String = intent.getStringExtra("nik").toString()
//        val isitelp:String = intent.getStringExtra("telepon").toString()
        val isiuid = UUID.randomUUID().toString()

//        binding = ActivityAspirasiBinding.inflate(layoutInflater)
//        setContentView(binding.root)
//        val asp = binding.isiasp.text.toString()
//
        binding.button2.setOnClickListener {
            database = FirebaseDatabase.getInstance("https://wadulv2-default-rtdb.asia-southeast1.firebasedatabase.app").getReference("pengaduan")
            val pengaduan = pengaduan(isiinstansi, isikeperluan, isikota, isikec, isides)
            database.child(isiuid).setValue(pengaduan)
            Toast.makeText(applicationContext, "Berhasil membuat pengaduan", Toast.LENGTH_SHORT).show()
        }
    }

    override fun onNothingSelected(arg0: AdapterView<*>) {

    }
}