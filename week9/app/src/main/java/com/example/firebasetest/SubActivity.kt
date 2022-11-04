package com.example.firebasetest

import android.graphics.BitmapFactory
import android.os.Bundle
import android.os.PersistableBundle
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import com.example.firebasetest.databinding.ActivityMainBinding
import com.google.firebase.ktx.Firebase
import com.google.firebase.remoteconfig.ktx.remoteConfig
import com.google.firebase.remoteconfig.ktx.remoteConfigSettings
import com.google.firebase.storage.ktx.storage

class SubActivity:AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?, persistentState: PersistableBundle?) {
        super.onCreate(savedInstanceState)
        val binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val rootRef = Firebase.storage.reference
        val ref = rootRef.child("icon1.jpg")
        ref.getBytes(Long.MAX_VALUE).addOnCompleteListener {
            if (it.isSuccessful) {
                val bmp = BitmapFactory.decodeByteArray(it.result, 0, it.result!!.size)
                val imgView = findViewById<ImageView>(R.id.imageView)
                imgView.setImageBitmap(bmp)
            }
        }

        val remoteConfig = Firebase.remoteConfig
        remoteConfig.setDefaultsAsync(R.xml.rc_defaults)
        val settings = remoteConfigSettings {
            minimumFetchIntervalInSeconds = 1
        }
        remoteConfig.setConfigSettingsAsync(settings)

        remoteConfig.fetchAndActivate().addOnSuccessListener {
            val season = remoteConfig.getString("season")
            println("########################## $season")
        }

        /* 둘 중의 하나의 방법을 정해서 하면 됨
        ref.getBytes(Long.MAX_VALUE).addOnSuccessListener {

        }.addOnFailureListener {

        }*/
    }

}