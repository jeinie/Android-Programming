package com.example.myapplication

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.myapplication.databinding.ActivityMainBinding

// 라디오 버튼으로 무슨 도형을 선택했는지 intent 로 보내기 -- 안되는 것 같음
// 라디오 버튼 따로 view 화면 따로
class MainActivity : AppCompatActivity() {

    lateinit var binding : ActivityMainBinding
    /*companion object {
        var currentFigure = ""  // 현재 도형이 무엇인지 저장
    }*/
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.radioGroup.setOnCheckedChangeListener { group, checkedId ->
            when (checkedId) {
                R.id.rectButton -> binding.view.setFigure("rect")
                R.id.circleButton -> binding.view.setFigure("circle")
            }
        }
    }
}