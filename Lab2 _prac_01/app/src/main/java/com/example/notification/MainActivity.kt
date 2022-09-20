package com.example.notification

import android.Manifest
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.content.pm.PackageManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AlertDialog
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat

class MainActivity : AppCompatActivity() {
    private var channelID_d = "default"
    private var channelID_a = "ad"
    private var count = 0

    private var myNotificationID = 1
        get() = field++

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val button = findViewById<Button>(R.id.button)
        val notify_btn = findViewById<Button>(R.id.notify)

        button.setOnClickListener {
            // 알림 시작
            showNotification()
        }

        notify_btn.setOnClickListener {
            //channelID = "ad"
            showNotification1()
        }

        // 채널 만들기
        createNotificationChannel()

        // 권한 요청
        requestSinglePermission(Manifest.permission.POST_NOTIFICATIONS)
    }


    private fun showNotification() {
        val builder = NotificationCompat.Builder(this, channelID_d)
            .setSmallIcon(R.mipmap.ic_launcher)
            .setContentTitle("Notification Lab")
            .setContentText("Notification #${++count}")
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
        NotificationManagerCompat.from(this)
            .notify(1, builder.build())
    }

    private fun showNotification1() {
        val text = findViewById<EditText>(R.id.editText).text
        val builder = NotificationCompat.Builder(this, channelID_a)
            .setSmallIcon(R.mipmap.ic_launcher)
            .setContentTitle("Notification Lab2")
            .setContentText(text)
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
        NotificationManagerCompat.from(this)
            .notify(2, builder.build())
    }

    private fun createNotificationChannel() {
        val channel_default = NotificationChannel(
            channelID_d, "default channel",
            NotificationManager.IMPORTANCE_DEFAULT
        )
        channel_default.description = "description text of this channel."

        val channel_ad = NotificationChannel(
            channelID_a, "ad",
            NotificationManager.IMPORTANCE_DEFAULT
        )
        channel_ad.description = "description text of this channel."

        val notificationManager =
            getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        notificationManager.createNotificationChannel(channel_default)
        notificationManager.createNotificationChannel(channel_ad)

        //Log.d("channel 2개 만들어짐?", channelID)
    }

    private fun requestSinglePermission(permission: String) {
        if (checkSelfPermission(permission) == PackageManager.PERMISSION_GRANTED)
            return

        val requestPermLauncher =
            registerForActivityResult(ActivityResultContracts.RequestPermission()) {
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