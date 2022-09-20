package com.example.animation

import android.animation.ObjectAnimator
import android.animation.ValueAnimator
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.TextView

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        /*ValueAnimator.ofFloat(0f, 100f).apply {
            duration = 1000
            addUpdateListener {
                println(it.animatedValue)
            }
            start()
        }

        val tv = findViewById<TextView>(R.id.textView)
        ObjectAnimator.ofFloat(tv, "translationX", 0f, 500f).apply {
            duration = 2000
            start()
        }*/
    }
}