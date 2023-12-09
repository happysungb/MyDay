package com.example.myday

import android.os.Bundle
import android.util.Log
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.GravityCompat
import androidx.fragment.app.add
import androidx.fragment.app.commit
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.myday.databinding.ActivityMainBinding
import com.example.myday.food.DialogCallback
import com.example.myday.food.FoodNutrition
import com.example.myday.food.GetKcalInfo
import com.example.myday.food.Row
import com.example.myday.food.SearchResultFragment
import com.example.myday.food.Selected
import com.example.myday.food.SelectedAdapter
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory


class MainActivity : AppCompatActivity(), DialogCallback {

    private lateinit var mainBinding: ActivityMainBinding
    private lateinit var foodNutritionList: MutableList<Row>
    private val selected: MutableList<Selected> = mutableListOf()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mainBinding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(mainBinding.root)

        // 메뉴바 버튼 클릭시
        val drawerLayout = mainBinding.drawer
        mainBinding.buttonOpenNav.setOnClickListener {
            drawerLayout.openDrawer(GravityCompat.START)
        }

        // 칼로리 검색 버튼 클릭시
        val foodEditText = mainBinding.foodEt
        mainBinding.kcalSearchBtn.setOnClickListener {
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
                            Toast.makeText(this@MainActivity, "검색 결과가 없습니다.", Toast.LENGTH_SHORT).show()
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

    }

    // 섭취량 선택후 확인 버튼 클릭시
    override fun onConfirm(name: String, kcal: Int, count: Int) {
        selected.add(Selected(name, kcal, count))
        val adapter = SelectedAdapter()
        adapter.selectedList = selected
        mainBinding.recyclerSelected.layoutManager = LinearLayoutManager(this)
        mainBinding.recyclerSelected.adapter = adapter
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


}