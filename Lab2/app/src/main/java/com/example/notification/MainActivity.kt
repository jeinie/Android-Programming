package com.example.notification

import android.Manifest
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.content.pm.PackageManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AlertDialog
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val button = findViewById<Button>(R.id.button)
        button.setOnClickListener {
            // 알림 시작
            showNotification()
        }

        // 권한 요청
        requestSinglePermission(Manifest.permission.POST_NOTIFICATIONS)

        // 채널 만들기
        createNotificationChannel()
    }

    private val channelID = "default"

    private fun showNotification() {
        val builder = NotificationCompat.Builder(this, channelID)
            .setSmallIcon(R.mipmap.ic_launcher)
            .setContentTitle("Notification Title")
            .setContentText("Notification body")
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
        NotificationManagerCompat.from(this)
            .notify(1, builder.build())
    }

    private fun createNotificationChannel() {
        val channel = NotificationChannel(
            channelID, "default channel",
            NotificationManager.IMPORTANCE_DEFAULT
        )
        channel.description = "description text of this channel."
        val notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        notificationManager.createNotificationChannel(channel)
    }

    private fun requestSinglePermission(permission: String) {
        if (checkSelfPermission(permission) == PackageManager.PERMISSION_GRANTED)
            return

        val requestPermLauncher = registerForActivityResult(ActivityResultContracts.RequestPermission()) {
            if (it == false) { // permission is not granted!
                AlertDialog.Builder(this).apply {
                    setTitle("Warning")
                    setMessage("Warning")
                }.show()
            }
        }

        if (shouldShowRequestPermissionRationale(permission)) {
            // you should explain the reason why this app needs the permission.
            AlertDialog.Builder(this).apply {
                setTitle("Reason")
                setMessage("Reason")
                setPositiveButton("Allow") { _, _ -> requestPermLauncher.launch(permission) }
                setNegativeButton("Deny") { _, _ -> }
            }.show()
        } else {
            // should be called in onCreate()
            requestPermLauncher.launch(permission)
        }
    }
}