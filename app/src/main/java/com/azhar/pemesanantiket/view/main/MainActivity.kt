package com.azhar.pemesanantiket.view.main

import android.content.Intent
import android.graphics.Color
import android.os.Build
import android.os.Bundle
import android.view.View
import android.view.WindowManager
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.azhar.pemesanantiket.R
import com.azhar.pemesanantiket.view.history.HistoryActivity
import com.azhar.pemesanantiket.view.input.DataKapalActivity
import com.azhar.pemesanantiket.view.input.DataKeretaActivity
import com.azhar.pemesanantiket.view.input.DataPesawatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    private lateinit var auth: FirebaseAuth
    private lateinit var database: FirebaseDatabase
    private lateinit var usersRef: DatabaseReference

    private lateinit var tvWelcomeMessage: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        auth = FirebaseAuth.getInstance()
        database = FirebaseDatabase.getInstance()
        usersRef = database.getReference("users")

        tvWelcomeMessage = findViewById(R.id.tvWelcomeMessage)

        setStatusBar()

        // Menampilkan nama pengguna jika sudah login
        val currentUser = auth.currentUser
        currentUser?.let {
            // Ambil data nama pengguna dari Firebase Realtime Database
            val userId = currentUser.uid
            val userRef = usersRef.child(userId)

            userRef.addValueEventListener(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    if (snapshot.exists()) {
                        val fullName = snapshot.child("fullName").getValue(String::class.java)
                        fullName?.let {
                            tvWelcomeMessage.text = it
                        }
                    }
                }

                override fun onCancelled(error: DatabaseError) {
                    // Handle canceled event
                }
            })
        }

        imageProfile.setOnClickListener {
            val intent = Intent(this@MainActivity, HistoryActivity::class.java)
            startActivity(intent)
        }

        cvPesawat.setOnClickListener {
            val intent = Intent(this@MainActivity, DataPesawatActivity::class.java)
            startActivity(intent)
        }

        cvKapal.setOnClickListener {
            val intent = Intent(this@MainActivity, DataKapalActivity::class.java)
            startActivity(intent)
        }

        cvKereta.setOnClickListener {
            val intent = Intent(this@MainActivity, DataKeretaActivity::class.java)
            startActivity(intent)
        }
    }

    private fun setStatusBar() {
        if (Build.VERSION.SDK_INT < 21) {
            setWindowFlag(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS, true)
        }
        if (Build.VERSION.SDK_INT >= 19) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                window.decorView.systemUiVisibility =
                    View.SYSTEM_UI_FLAG_LAYOUT_STABLE or View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
            }
        }
        if (Build.VERSION.SDK_INT >= 21) {
            setWindowFlag(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS, false)
            window.statusBarColor = Color.TRANSPARENT
        }
    }

    private fun setWindowFlag(bits: Int, on: Boolean) {
        val window = window
        val layoutParams = window.attributes
        if (on) {
            layoutParams.flags = layoutParams.flags or bits
        } else {
            layoutParams.flags = layoutParams.flags and bits.inv()
        }
        window.attributes = layoutParams
    }
}
