package com.example.animationlab

import android.os.Bundle
import android.transition.*
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import com.example.animationlab.databinding.ActivityTransitionBinding

class TransitionActivity:AppCompatActivity() {
    private lateinit var binding: ActivityTransitionBinding
    private lateinit var scene1:Scene
    private lateinit var scene2:Scene

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityTransitionBinding.inflate(layoutInflater)
        setContentView(binding.root)
        scene1 = Scene.getSceneForLayout(binding.sceneRoot, R.layout.scene_1, this)
        scene2 = Scene.getSceneForLayout(binding.sceneRoot, R.layout.scene_2, this)
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.transition_menu, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.scene_1 -> TransitionManager.go(scene1, ChangeBounds())
            R.id.scene_2 -> TransitionManager.go(scene2, Fade().addListener(Scene1to2TransitionListener()))
        }
        return super.onOptionsItemSelected(item)
    }

    inner class Scene1to2TransitionListener : Transition.TransitionListener {
        override fun onTransitionStart(transition: Transition) {
        }

        override fun onTransitionEnd(transition: Transition) {
            println("onTransitionEnd ###################")
        }

        override fun onTransitionCancel(transition: Transition) {
        }

        override fun onTransitionResume(transition: Transition) {
        }

        override fun onTransitionPause(transition: Transition) {
        }
    }
}