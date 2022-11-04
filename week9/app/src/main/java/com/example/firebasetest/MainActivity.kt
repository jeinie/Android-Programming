package com.example.firebasetest

import android.content.Intent
import android.graphics.BitmapFactory
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ImageView
import com.example.firebasetest.LoginActivity
import com.example.firebasetest.R
import com.example.firebasetest.databinding.ActivityMainBinding
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.google.firebase.remoteconfig.ktx.remoteConfig
import com.google.firebase.remoteconfig.ktx.remoteConfigSettings
import com.google.firebase.storage.ktx.storage


class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        //throw java.lang.RuntimeException("##################################")

        if (Firebase.auth.currentUser == null) {
            startActivity(
                Intent(this, LoginActivity::class.java)
            )
            finish()
        }
        binding.textUID.text = Firebase.auth.currentUser?.uid ?: "No User"
        binding.buttonSignout.setOnClickListener {
            Firebase.auth.signOut()
            startActivity(Intent(this, LoginActivity::class.java))
        }

        val remoteConfig = Firebase.remoteConfig
        remoteConfig.setDefaultsAsync(R.xml.rc_defaults)

        val rootRef = Firebase.storage.reference
        var ref = rootRef.child("winter.jpeg")
        ref.getBytes(Long.MAX_VALUE).addOnCompleteListener {
            if (it.isSuccessful) {
                val bmp = BitmapFactory.decodeByteArray(it.result, 0, it.result!!.size)
                val imgView = findViewById<ImageView>(R.id.imageView)
                imgView.setImageBitmap(bmp)
            }
        }

        val settings = remoteConfigSettings {
            minimumFetchIntervalInSeconds = 1
        }
        remoteConfig.setConfigSettingsAsync(settings)

        binding.refreshBtn.setOnClickListener {
            remoteConfig.fetchAndActivate().addOnSuccessListener {
                val season = remoteConfig.getString("season")
                println("########################## $season")

                when(season) {
                    "spring" -> ref = rootRef.child("spring.jpeg")
                    "summer" -> ref = rootRef.child("summer.jpeg")
                    "fall" -> ref = rootRef.child("fall.jpeg")
                    "winter" -> ref = rootRef.child("winter.jpeg")
                }
                ref.getBytes(Long.MAX_VALUE).addOnCompleteListener {
                    if (it.isSuccessful) {
                        val bmp = BitmapFactory.decodeByteArray(it.result, 0, it.result!!.size)
                        val imgView = findViewById<ImageView>(R.id.imageView)
                        imgView.setImageBitmap(bmp)
                    }
                }
            }
        }

        /* 둘 중의 하나의 방법을 정해서 하면 됨
        ref.getBytes(Long.MAX_VALUE).addOnSuccessListener {

        }.addOnFailureListener {

        }*/
    }
}