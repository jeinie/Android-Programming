package com.example.lab4_prac_01

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import com.example.lab4_prac_01.databinding.ActivityMainBinding
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.lang.StringBuilder

class MainActivity : AppCompatActivity() {
    lateinit var binding: ActivityMainBinding
    lateinit var myDao: MyDao

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        myDao = MyDatabase.getDatabase(this).getMyDao()

        CoroutineScope(Dispatchers.IO).launch {
            with(myDao) {
                insertStudent(Student(1, "james"))
                insertStudent(Student(2, "top"))
                insertClass(ClassInfo(1, "c-lang", "Mon 9:00", "E301", 1))
                insertClass(ClassInfo(2, "android prog", "Tue 9:00", "E302", 1))
                insertEnrollment(Enrollment(1, 1))
                insertEnrollment(Enrollment(1, 2))
            }
        }

        val allStudents = myDao.getAllStudents()
        allStudents.observe(this) {
            val str = StringBuilder().apply {
                for ((id, name) in it) {
                    append(id)
                    append("-")
                    append(name)
                    append("\n")
                }
            }.toString()
            binding.textStudentList.text = str
        }

        binding.queryStudent.setOnClickListener {
            val id = binding.editStudentId.text.toString().toInt()
            CoroutineScope(Dispatchers.IO).launch {
                val results = myDao.getStudentsWithEnrollment(id)
                if (results.isNotEmpty()) {
                    val str = StringBuilder().apply {
                        append(results[0].student.id)
                        append("-")
                        append(results[0].student.name)
                        append(":")
                        for (c in results[0].enrollments) {
                            append(c.cid)
                            val cls_result = myDao.getClassInfo(c.cid)
                            if (cls_result.isNotEmpty()) {
                                append("(${cls_result[0].name}")
                                append(",")
                            }
                        }
                    }
                    withContext(Dispatchers.Main) {
                        binding.textQueryStudent.text = str
                    }
                } else {
                    withContext(Dispatchers.Main) {
                        binding.textQueryStudent.text = ""
                    }
                }
            }
        }

        binding.addStudent.setOnClickListener {
            val id = binding.editStudentId.text.toString().toInt()
            val name = binding.editStudentName.text.toString()
            if (id > 0 && name.isNotEmpty()) {
                CoroutineScope(Dispatchers.IO).launch {
                    myDao.insertStudent(Student(id, name))
                }
            }
        }

        binding.deleteStudent.setOnClickListener {
            val id = findViewById<EditText>(R.id.edit_student_id).text.toString()
            CoroutineScope(Dispatchers.IO).launch {
                myDao.deleteStudent(Student(id.toInt(),""))
            }
        }
    }
}