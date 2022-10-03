package com.example.internetlab

import android.os.Bundle
import android.util.Log
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.internetlab.databinding.ActivityMainBinding
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.converter.scalars.ScalarsConverterFactory
import retrofit2.http.GET
import retrofit2.http.Path
import java.lang.Exception
import java.lang.StringBuilder

data class Owner(val login:String)
data class Repo(val name:String, val owner:Owner, val url:String)
data class Contributor(val login: String, val contributions: Int)

interface RestApi {
    @GET("users/{user}/repos")
    suspend fun listRepos(@Path("user") user:String):List<Repo>

    @GET("/repos/{owner}/{repo}/contributors")
    suspend fun contributors(
        @Path("owner") owner:String,
        @Path("repo") repo:String
    ): List<Contributor>
}

class MyViewModel:ViewModel() {
    private val baseURL = "https://api.github.com/"
    private lateinit var api: RestApi

    val response = MutableLiveData<String>()

    init {
        val user = ""
        retrofitInit()
        refreshData(user)
    }

    fun refreshData(user: String) {
        viewModelScope.launch {
            try {
                val repos = api.listRepos(user)
                response.value = StringBuilder().apply {
                    repos.forEach {
                        append(it.name)
                        append(" - ")
                        append(it.owner.login)
                        append("\n")
                    }
                }.toString()
            } catch (e:Exception) {
                response.value = "Failed to connect to the server"
            }
        }
    }

    private fun retrofitInit() {
        val retrofit = Retrofit.Builder()
            .baseUrl(baseURL)
            .addConverterFactory(MoshiConverterFactory.create())
            .build()

        api = retrofit.create(RestApi::class.java)
    }
}

class MainActivity : AppCompatActivity() {
    private lateinit var myViewModel: MyViewModel
    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        myViewModel = ViewModelProvider(this).get(MyViewModel::class.java)
        myViewModel.response.observe(this) {
            binding.textResponse.text = it
            val user = binding.editTextTextPersonName.text.toString()
            binding.button.setOnClickListener {
                val retrofit = Retrofit.Builder()
                    .baseUrl("https://api.github.com/")
                    .addConverterFactory(ScalarsConverterFactory.create())
                    .build()
                val api = retrofit.create(RestApi::class.java)
                var result : List<Repo>
                CoroutineScope(Dispatchers.IO).launch {
                    try {
                        result = api.listRepos(user)
                    } catch (e:Exception) {
                        Log.d("connect", "Failed to connect")
                    }
                    withContext(Dispatchers.Main) {
                        myViewModel.refreshData(user)
                    }
                }
            }
        }
    }
}