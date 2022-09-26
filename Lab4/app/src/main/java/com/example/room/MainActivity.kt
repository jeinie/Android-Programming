package com.example.room

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.lang.StringBuilder

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val db = MyDatabase.getDatabase(this)
        val dao = db.getMyDao()

        CoroutineScope(Dispatchers.IO).launch {
            dao.insertStudent(Student(1, "james"))
            dao.insertStudent(Student(2, "top"))
        }

        val allStudents = dao.getAllStudents()
        allStudents.observe(this) {
            val str = StringBuilder().apply {
                for ((id, name) in it) {
                    append(id)
                    append("-")
                    append(name)
                    append("\n")
                }
            }.toString()
            val textview = findViewById<TextView>(R.id.textView)
            textview.text = str
        }

        findViewById<Button>(R.id.insert).setOnClickListener {
            val id = findViewById<EditText>(R.id.editTextTextPersonName).text.toString()
            val name = findViewById<EditText>(R.id.editTextTextPersonName2).text.toString()
            CoroutineScope(Dispatchers.IO).launch {
                dao.insertStudent(Student(id.toInt(), name))
            }
        }

        findViewById<Button>(R.id.delete).setOnClickListener {
            val id = findViewById<EditText>(R.id.editTextTextPersonName).text.toString()
            CoroutineScope(Dispatchers.IO).launch {
                dao.deleteStudent(Student(id.toInt(),""))
            }
        }
    }
}