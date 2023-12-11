package com.example.myday

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import android.widget.CalendarView
import androidx.appcompat.widget.Toolbar
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import com.google.android.material.navigation.NavigationView
import java.time.LocalDate

class MyPageActivity : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_my_page)

        val toolbar: Toolbar = findViewById(R.id.toolbar)
        setSupportActionBar(toolbar)
        supportActionBar?.title = "My Kcal Calendar"
        // CalendarView 설정
        val calendarView: CalendarView = findViewById(R.id.calendarView)

        // 오늘 날짜로 캘린더 뷰 설정
        calendarView.date = System.currentTimeMillis()

        // 날짜 변경 리스너 설정
        calendarView.setOnDateChangeListener { view, year, month, dayOfMonth ->
            // 날짜가 선택되었을 때 실행할 코드
            val date = LocalDate.of(year, month + 1, dayOfMonth) // LocalDate를 사용하여 날짜 객체 생성
            displayRecordsForDate(date)
        }

        // 처음 액티비티가 시작될 때 오늘 날짜에 대한 기록을 표시
        val today = LocalDate.now()
        displayRecordsForDate(today)

        // DrawerLayout 참조
        val drawerLayout: DrawerLayout = findViewById(R.id.drawer_layout) // DrawerLayout의 ID 확인 필요

        // 네비게이션 버튼
        toolbar.setNavigationOnClickListener {
            drawerLayout.openDrawer(GravityCompat.START)
        }

        // NavigationView 리스너 설정
        val navigationView: NavigationView = findViewById(R.id.navView) // NavigationView의 ID 확인 필요
        navigationView.setNavigationItemSelectedListener(this)
    }

    private fun displayRecordsForDate(date: LocalDate) {
        // 선택된 날짜에 대한 기록을 표시하는 로직 구현
        // 데이터베이스에서 해당 날짜의 식단과 운동 기록 즉 칼로리 계산 후 칼로리 표시
    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.navigation_mypage -> {
                // 마이페이지 액티비티로 이동
                val intent = Intent(this, MyPageActivity::class.java)
                startActivity(intent)
            }
            R.id.navigation_kcal -> {
                val intent = Intent(this, MainActivity::class.java)
                intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP
                startActivity(intent)
            }
            R.id.navigation_health -> {
                val intent = Intent(this, ExerciseActivity::class.java)
                startActivity(intent)
            }
        }
        // 아이템 클릭 후 드로어 닫기
        val drawerLayout: DrawerLayout = findViewById(R.id.drawer_layout)
        drawerLayout.closeDrawer(GravityCompat.START)
        return true
    }

}