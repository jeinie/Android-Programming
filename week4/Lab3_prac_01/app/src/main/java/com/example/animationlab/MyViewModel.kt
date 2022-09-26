package com.example.animationlab

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class MyViewModel: ViewModel() {
    val currentText1 = MutableLiveData<String>()
    val currentText2 = MutableLiveData<String>()
}