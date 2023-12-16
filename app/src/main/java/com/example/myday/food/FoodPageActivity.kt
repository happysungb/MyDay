package com.example.myday.food

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import android.view.inputmethod.InputMethodManager
import android.widget.ArrayAdapter
import android.widget.Spinner
import android.widget.Toast
import androidx.appcompat.widget.Toolbar
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.GravityCompat
import androidx.fragment.app.add
import androidx.fragment.app.commit
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.myday.ExerciseActivity
import com.example.myday.MyPageActivity
import com.example.myday.R
import com.example.myday.databinding.FoodPageBinding
import com.google.android.material.navigation.NavigationView
import com.google.firebase.auth.FirebaseAuth
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory


class FoodPageActivity : AppCompatActivity(), DialogCallback, NavigationView.OnNavigationItemSelectedListener {

    private lateinit var binding: FoodPageBinding
    private lateinit var foodNutritionList: MutableList<Row>
    private lateinit var auth: FirebaseAuth

    private val selected: MutableList<Selected> = mutableListOf()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = FoodPageBinding.inflate(layoutInflater)
        setContentView(binding.root)
        auth = FirebaseAuth.getInstance()

        // Toolbar 설정
        val toolbar: Toolbar = findViewById(R.id.toolbar) // appbar.xml에 있는 Toolbar ID
        setSupportActionBar(toolbar)
        supportActionBar?.title = "식단 기록"

        // 메뉴바 버튼 클릭시
        toolbar.setNavigationOnClickListener {
            binding.drawer.openDrawer(GravityCompat.START)
        }

        // NavigationView 리스너 설정
        binding.navView.setNavigationItemSelectedListener(this)

        // EditView의 바깥 부분 클릭시 키보드 숨기기
        binding.mainLayout.setOnTouchListener { v, event ->
            hideKeyboard(this)
            false
        }
        // Spinner 설정(아침, 점심, 저녁, 야식, 간식 중 선택)
        val spinner: Spinner = binding.spinner
        ArrayAdapter.createFromResource(
            this,
            R.array.`when`,
            android.R.layout.simple_spinner_item
        ).also { adapter ->
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            spinner.adapter = adapter
        }
        spinner.setSelection(0) // 초기값은 아침으로 설정

        // 칼로리 검색 버튼 클릭시
        val foodEditText = binding.foodEt
        binding.kcalSearchBtn.setOnClickListener {
            val imm = getSystemService(INPUT_METHOD_SERVICE) as InputMethodManager
            imm.hideSoftInputFromWindow(foodEditText.windowToken, 0)
            val inputText = foodEditText.text.toString()
            val retrofit = Retrofit.Builder()
                .baseUrl("https://openapi.foodsafetykorea.go.kr/api/27dfc35a066042d49e98/I2790/json/1/10/")
                .addConverterFactory(GsonConverterFactory.create())
                .build()

            val service = retrofit.create(GetKcalInfo::class.java)
            val call = service.getNutritionData(inputText)

            call.enqueue(object : Callback<FoodNutrition> {
                override fun onResponse(
                    call: Call<FoodNutrition>,
                    response: Response<FoodNutrition>
                ) {
                    val body = response.body()
                    if (body != null && response.isSuccessful) {
                        if (body.I2790.RESULT.CODE == "INFO-200") {
                            Toast.makeText(this@FoodPageActivity, "검색 결과가 없습니다.", Toast.LENGTH_SHORT).show()
                        } else {
                            response.body()?.I2790?.row?.let {
                                    it1 ->
                                foodNutritionList = it1
                                initNutritionRecyclerView(savedInstanceState)
                            }
                        }
                    } else {
                        Log.v("retrofit", response.errorBody()?.string()!!)
                        Log.v("retrofit", response.code().toString())
                    }

                }
                override fun onFailure(call: Call<FoodNutrition>, t: Throwable) {
                    Log.v("retrofit", "call failed", t)
                }
            })

        }

        // 하단에 있는 다음 버튼 클릭시
        binding.nextBtn.setOnClickListener {
            if (selected.isEmpty()) {
                Toast.makeText(this, "선택된 음식이 아직 없어요!", Toast.LENGTH_SHORT).show()
            } else {
                val time: Time = when (spinner.selectedItem.toString()) {
                    "아침" -> Time.BREAKFAST
                    "점심" -> Time.LUNCH
                    "저녁" -> Time.DINNER
                    "야식" -> Time.SUPPER
                    else -> Time.SNACK
                }
                val intent = Intent(this, MyPageActivity::class.java)
                intent.putExtra("selectedList", ArrayList(selected))
                intent.putExtra("time", time)
                startActivity(intent)
            }
        }

    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.navigation_mypage -> {
                // 마이페이지 액티비티로 이동
                val intent = Intent(this, MyPageActivity::class.java)
                startActivity(intent)
            }
            R.id.navigation_health -> {
                val intent = Intent(this, ExerciseActivity::class.java)
                startActivity(intent)
            }
        }
        // 아이템 클릭 후 드로어 닫기
    binding.drawer.closeDrawer(GravityCompat.START)
        return true
    }
    // 섭취량 선택후 확인 버튼 클릭시
    override fun onConfirm(name: String, kcal: Int, count: Int) {
        selected.add(Selected(name, kcal, count))
        val adapter = SelectedAdapter()
        adapter.selectedList = selected
        binding.recyclerSelected.layoutManager = LinearLayoutManager(this)
        binding.recyclerSelected.adapter = adapter
        supportFragmentManager.popBackStack()
    }

    fun initNutritionRecyclerView(savedInstanceState: Bundle?) {
        if (savedInstanceState == null) {
            val bundle = Bundle()
            bundle.putParcelableArrayList("resultList", ArrayList(foodNutritionList))
            supportFragmentManager.commit {
                setReorderingAllowed(true)
                addToBackStack(null)
                add<SearchResultFragment>(R.id.fragment_container, args = bundle)
            }
        }
    }

    // 키보드 숨기기
    private fun hideKeyboard(activity: Activity) {
        val inputMethodManager = activity.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        val currentFocusedView = activity.currentFocus
        currentFocusedView?.let {
            inputMethodManager.hideSoftInputFromWindow(currentFocusedView.windowToken, InputMethodManager.HIDE_NOT_ALWAYS)
        }
    }

}