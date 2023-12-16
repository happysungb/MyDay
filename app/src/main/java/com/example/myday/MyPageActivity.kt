package com.example.myday

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import android.widget.TextView
import androidx.appcompat.widget.Toolbar
import androidx.core.view.GravityCompat
import com.example.myday.food.FoodPageActivity
import com.example.myday.databinding.ActivityMyPageBinding
import com.google.android.material.navigation.NavigationView
import java.time.LocalDate

class MyPageActivity : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener {
    private lateinit var binding: ActivityMyPageBinding
    private lateinit var userName: String
    private lateinit var userEmail: String
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMyPageBinding.inflate(layoutInflater)
        setContentView(binding.root)

        userName = intent.getStringExtra("userName") ?: "사용자"
        userEmail = intent.getStringExtra("userEmail") ?: "이메일 없음"

        val navigationViewHeader = binding.navView.getHeaderView(0)
        navigationViewHeader.findViewById<TextView>(R.id.navName).text = userName
        navigationViewHeader.findViewById<TextView>(R.id.navEmail).text = userEmail

        //toolbar 설정
        val toolbar: Toolbar = findViewById(R.id.toolbar)
        setSupportActionBar(toolbar)
        supportActionBar?.title = "${userName}'s Kcal Calendar"

        // CalendarView 설정
        binding.calendarView.apply {
            date = System.currentTimeMillis()
            setOnDateChangeListener { view, year, month, dayOfMonth ->
                val date = LocalDate.of(year, month + 1, dayOfMonth)
                displayRecordsForDate(date)
            }
        }

        // 네비게이션 버튼
        toolbar.setNavigationOnClickListener {
            binding.drawerLayout.openDrawer(GravityCompat.START)
        }

        // NavigationView 리스너 설정
        binding.navView.setNavigationItemSelectedListener(this)

        // 처음 액티비티가 시작될 때 오늘 날짜에 대한 기록을 표시
        displayRecordsForDate(LocalDate.now())
    }

    private fun displayRecordsForDate(date: LocalDate) {
        // 선택된 날짜에 대한 기록을 표시하는 로직 구현
        // 데이터베이스에서 해당 날짜의 식단과 운동 기록 즉 칼로리 계산 후 칼로리 표시
    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.navigation_mypage -> {
                //
            }
            R.id.navigation_kcal -> {
                val intent = Intent(this, FoodPageActivity::class.java)
                intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP
                startActivity(intent)
            }
            R.id.navigation_health -> {
                val intent = Intent(this, ExerciseActivity::class.java)
                intent.putExtra("userName", userName)
                intent.putExtra("userEmail",userEmail)
                startActivity(intent)
            }
        }
        // 아이템 클릭 후 드로어 닫기
        binding.drawerLayout.closeDrawer(GravityCompat.START)
        return true
    }

}