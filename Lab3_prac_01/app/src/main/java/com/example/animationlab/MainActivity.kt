package com.example.animationlab

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.transition.ChangeBounds
import androidx.transition.Scene
import androidx.transition.TransitionManager
import com.example.animationlab.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private lateinit var scene1:Scene
    private lateinit var scene2:Scene

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        scene1 = Scene.getSceneForLayout(binding.sceneRoot, R.layout.student, this)
        scene2 = Scene.getSceneForLayout(binding.sceneRoot, R.layout.worker, this)

        binding.radioGroup.setOnCheckedChangeListener { _, checkId ->
            when(checkId) {
                R.id.radio_student -> TransitionManager.go(scene1, ChangeBounds())
                R.id.radio_worker -> TransitionManager.go(scene2, ChangeBounds())
            }
        }
    }
}