package com.example.myday

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Spinner
import android.widget.TextView
import androidx.appcompat.widget.Toolbar
import androidx.core.view.GravityCompat
import com.example.myday.databinding.FragmentExerciseBinding
import com.google.android.material.navigation.NavigationView

class ExerciseActivity : AppCompatActivity() {
    private lateinit var binding: FragmentExerciseBinding
    private lateinit var userName: String
    private lateinit var userEmail: String
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = FragmentExerciseBinding.inflate(layoutInflater)
        setContentView(binding.root)

        userName = intent.getStringExtra("userName") ?: "사용자"
        userEmail = intent.getStringExtra("userEmail") ?: "이메일 없음"

        val greetingTextView = findViewById<TextView>(R.id.exerciseGreeting)
        greetingTextView.text = "${userName}님, 운동하기 좋은 날이에요!"

        val toolbar: Toolbar = findViewById(R.id.toolbar)
        setSupportActionBar(toolbar)
        supportActionBar?.title = "운동 기록"


        //Spinner_exercise1 설정
        val spinner_exercise1: Spinner = findViewById(R.id.spinner_exercise1)
        val adapter_exercise1 = ArrayAdapter.createFromResource(
            this,
            R.array.exercise1,
            android.R.layout.simple_spinner_dropdown_item
        )
        spinner_exercise1.adapter = adapter_exercise1

        spinner_exercise1.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {
                when (position) {
                    0 -> {
                    }

                    1 -> {
                    }
                }
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {
            }
        }

        //Spinner_time1 설정
        val spinner_time1: Spinner = findViewById(R.id.spinner_time1)
        val adapter_time1 = ArrayAdapter.createFromResource(
            this,
            R.array.time1,
            android.R.layout.simple_spinner_dropdown_item
        )
        spinner_time1.adapter = adapter_time1

        spinner_time1.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {
                when (position) {
                    0 -> {
                    }

                    1 -> {
                    }
                }
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {
            }
        }

        //Spinner_exercise2 설정
        val spinner_exercise2: Spinner = findViewById(R.id.spinner_exercise2)
        val adapter_exercise2 = ArrayAdapter.createFromResource(
            this,
            R.array.exercise1,
            android.R.layout.simple_spinner_dropdown_item
        )
        spinner_exercise2.adapter = adapter_exercise2

        spinner_exercise2.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {
                when (position) {
                    0 -> {
                    }

                    1 -> {
                    }
                }
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {
            }
        }
        //Spinner_time2 설정
        val spinner_time2: Spinner = findViewById(R.id.spinner_time2)
        val adapter_time2 = ArrayAdapter.createFromResource(
            this,
            R.array.time1,
            android.R.layout.simple_spinner_dropdown_item
        )
        spinner_time2.adapter = adapter_time2

        spinner_time2.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {
                when (position) {
                    0 -> {
                    }

                    1 -> {
                    }
                }
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {
            }
        }
        //Spinner_exercise3 설정
        val spinner_exercise3: Spinner = findViewById(R.id.spinner_exercise3)
        val adapter_exercise3 = ArrayAdapter.createFromResource(
            this,
            R.array.exercise1,
            android.R.layout.simple_spinner_dropdown_item
        )
        spinner_exercise3.adapter = adapter_exercise3

        spinner_exercise3.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {
                when (position) {
                    0 -> {
                    }

                    1 -> {
                    }
                }
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {
            }
        }
        //Spinner_time3 설정
        val spinner_time3: Spinner = findViewById(R.id.spinner_time3)
        val adapter_time3 = ArrayAdapter.createFromResource(
            this,
            R.array.time1,
            android.R.layout.simple_spinner_dropdown_item
        )
        spinner_time3.adapter = adapter_time3

        spinner_time3.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {
                when (position) {
                    0 -> {
                    }

                    1 -> {
                    }
                }
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {
            }
        }
        //Spinner_exercise4 설정
        val spinner_exercise4: Spinner = findViewById(R.id.spinner_exercise4)
        val adapter_exercise4 = ArrayAdapter.createFromResource(
            this,
            R.array.exercise1,
            android.R.layout.simple_spinner_dropdown_item
        )
        spinner_exercise4.adapter = adapter_exercise4

        spinner_exercise4.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {
                when (position) {
                    0 -> {
                    }

                    1 -> {
                    }
                }
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {
            }
        }
        //Spinner_time4 설정
        val spinner_time4: Spinner = findViewById(R.id.spinner_time4)
        val adapter_time4 = ArrayAdapter.createFromResource(
            this,
            R.array.time1,
            android.R.layout.simple_spinner_dropdown_item
        )
        spinner_time4.adapter = adapter_time4

        spinner_time4.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {
                when (position) {
                    0 -> {
                    }

                    1 -> {
                    }
                }
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {
                TODO("Not yet implemented")
            }
        }
    }
}