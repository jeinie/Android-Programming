package com.example.animationlab

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.EditText
import androidx.lifecycle.ViewModelProvider
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

        val viewModel = ViewModelProvider(this)[MyViewModel::class.java]
        viewModel.currentText1.observe(this) {
            binding.sceneRoot.findViewById<EditText>(R.id.edit_student)
            binding.sceneRoot.findViewById<EditText>(R.id.edit_worker)
        }

        scene1 = Scene.getSceneForLayout(binding.sceneRoot, R.layout.student, this)
        scene2 = Scene.getSceneForLayout(binding.sceneRoot, R.layout.worker, this)

        binding.radioGroup.setOnCheckedChangeListener { _, checkId ->
            when(checkId) {
                R.id.radio_student -> {
                    TransitionManager.go(scene1, ChangeBounds())
                    binding.sceneRoot.findViewById<EditText>(R.id.edit_student).setText(viewModel.currentText1.value)
                }
                R.id.radio_worker -> {
                    viewModel.currentText1.value = binding.sceneRoot.findViewById<EditText>(R.id.edit_student).text.toString()
                    TransitionManager.go(scene2, ChangeBounds())
                }
            }
        }
    }
}