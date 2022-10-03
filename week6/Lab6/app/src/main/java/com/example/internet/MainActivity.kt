package com.example.internet

import android.app.DownloadManager
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Environment
import android.view.Menu
import android.view.MenuItem
import android.widget.Button
import android.widget.TextView
import androidx.core.content.FileProvider.getUriForFile
import com.google.android.material.snackbar.Snackbar
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.scalars.ScalarsConverterFactory
import retrofit2.http.GET
import java.lang.Exception
import java.net.HttpURLConnection
import java.net.URL
import javax.net.SocketFactory
import com.android.volley.Request
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import java.io.File

class MainActivity : AppCompatActivity() {
    private val serverAddress = "cse.hansung.ac.kr"
    private val scheme = "http"
    private lateinit var output:TextView
    private lateinit var outputBy:TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        output = findViewById(R.id.textView)
        outputBy = findViewById(R.id.textViewBy)

        if (isNetworkAvailable())
            Snackbar.make(output, "Network available", Snackbar.LENGTH_SHORT).show()
        else
            Snackbar.make(output, "Network unavailable", Snackbar.LENGTH_SHORT).show()

        findViewById<Button>(R.id.java_socket).setOnClickListener { javaSocket() }
        findViewById<Button>(R.id.java_http).setOnClickListener { httpLib() }
        findViewById<Button>(R.id.retrofit).setOnClickListener { retrofitWithCoroutine() }
        findViewById<Button>(R.id.download).setOnClickListener { downloadManager() }
        findViewById<Button>(R.id.openDownload).setOnClickListener { openDownload() }
        findViewById<Button>(R.id.volley).setOnClickListener { volley() }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.restApi -> startActivity(Intent(this, RestActivity::class.java))
        }
        return super.onOptionsItemSelected(item)
    }

    private fun isNetworkAvailable(): Boolean {
        val connectivityManager = getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val nw = connectivityManager.activeNetwork ?: return false
        val actNw = connectivityManager.getNetworkCapabilities(nw) ?: return false
        println("${actNw.hasTransport(NetworkCapabilities.TRANSPORT_WIFI)}, ${actNw.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR)}, " +
        "${actNw.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET)}")
        return actNw.hasCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET)
    }

    private fun javaSocket() {
        output.text = ""
        outputBy.text = ""

        CoroutineScope(Dispatchers.IO).launch {
            val sock = SocketFactory.getDefault().createSocket(serverAddress, 80)
            val istream = sock.getInputStream()
            val ostream = sock.getOutputStream()
            ostream.write("GET /\r\n".toByteArray())
            ostream.flush()
            val r = istream.readBytes()
            withContext(Dispatchers.Main) {
                outputBy.text = "JAVA Socket"
                output.text = r.decodeToString()
            }
            sock.close()
        }
    }

    private fun httpLib() {
        output.text = ""
        outputBy.text = ""

        CoroutineScope(Dispatchers.IO).launch {
            val conn = when(scheme) {
                "https" -> URL("$scheme://$serverAddress").openConnection() as HttpURLConnection
                else -> URL("http://$serverAddress").openConnection() as HttpURLConnection
            }
            val istream = conn.inputStream
            val r = istream.readBytes()
            withContext(Dispatchers.Main) {
                outputBy.text = "HTTP URL Connection"
                output.text = r.decodeToString()
            }
            conn.disconnect()
        }
    }

    interface RestApi {
        @GET("/")
        fun getRoot(): Call<String>

        @GET("/")
        suspend fun getRoot2(): String
    }

    private fun retrofitWithCoroutine() {
        output.text = ""
        outputBy.text = ""

        val retrofit = Retrofit.Builder()
            .baseUrl("$scheme://$serverAddress")
            .addConverterFactory(ScalarsConverterFactory.create())
            .build()

        val api = retrofit.create(RestApi::class.java)
        var result : String

        CoroutineScope(Dispatchers.IO).launch {
            try {
                result = api.getRoot2()
            } catch (e: Exception) {
                result = "Failed to connect $serverAddress"
            }
            withContext(Dispatchers.Main) {
                outputBy.text = "Retrofit with Coroutine"
                output.text = result
            }
        }
    }

    private fun retrofit() {
        output.text = ""
        outputBy.text = ""

        val retrofit = Retrofit.Builder()
            .baseUrl("$scheme://$serverAddress")
            .addConverterFactory(ScalarsConverterFactory.create())
            .build()

        val api = retrofit.create(RestApi::class.java)
        api.getRoot().enqueue(object : Callback<String> {
            override fun onResponse(call: Call<String>, response: Response<String>) {
                outputBy.text = "Retrofit"
                output.text = response.body()
            }

            override fun onFailure(call: Call<String>, t: Throwable) {
                outputBy.text = "Retrofit"
                output.text = "Faild to connect $serverAddress"
            }
        })
    }

    private fun volley() {
        output.text = ""
        outputBy.text = ""

        val queue = Volley.newRequestQueue(this)
        val url = "$scheme://$serverAddress"

        val stringReqeust = StringRequest(
            Request.Method.GET, url,
            { response ->
                outputBy.text = "Volley"
                output.text = response },
            {
                output.text = "Volley"
                output.text = "Failed to connect $serverAddress" })
        queue.add(stringReqeust)
    }

    private val receiver = object : BroadcastReceiver() {
        override fun onReceive(p0: Context?, p1: Intent?) {
            val downloadId = intent?.getLongExtra(DownloadManager.EXTRA_DOWNLOAD_ID, -1)
            when (intent?.action) {
                DownloadManager.ACTION_DOWNLOAD_COMPLETE -> {
                    val filePath = File(getExternalFilesDir(Environment.DIRECTORY_DOWNLOADS), "testdownload.png")
                    println("Download Completed! $filePath ${filePath.length()}")
                }
                else -> {
                    println(intent?.action)
                }
            }
        }
    }

    private fun downloadManager() {
        val downloadURL = Uri.parse("https://www.hansung.ac.kr/sites/hansung/images/common/logo.png")
        val iFilter = IntentFilter(DownloadManager.ACTION_DOWNLOAD_COMPLETE)
        registerReceiver(receiver, iFilter)
        val dManager = getSystemService(DOWNLOAD_SERVICE) as DownloadManager
        val request = DownloadManager.Request(downloadURL).apply {
            setTitle("Download")
            setDescription("Downloading a File")
            setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_ONLY_COMPLETION)
            setDestinationInExternalFilesDir(baseContext, Environment.DIRECTORY_DOWNLOADS, "testdownload.png")
        }

        val dID = dManager.enqueue(request)
    }

    private fun openDownload() {
        val filePath = File(getExternalFilesDir(Environment.DIRECTORY_DOWNLOADS), "testdownload.png")
        val contentUri: Uri =
            getUriForFile(this, "com.example.internet_ex.file_provider", filePath)
        println(contentUri)
        val i = Intent().apply {
            action = Intent.ACTION_VIEW
            data = contentUri
            addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
        }
        startActivity(i)
    }
}