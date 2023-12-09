package com.example.myday

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.core.view.GravityCompat
import androidx.fragment.app.add
import androidx.fragment.app.commit
import com.example.myday.databinding.ActivityMainBinding
import com.example.myday.food.FoodNutrition
import com.example.myday.food.GetKcalInfo
import com.example.myday.food.NutritionAdapter
import com.example.myday.food.Row
import com.example.myday.food.SearchResultFragment

import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class MainActivity : AppCompatActivity() {

    private lateinit var mainBinding: ActivityMainBinding
    private lateinit var adapter: NutritionAdapter
    private lateinit var foodNutritionList: MutableList<Row>

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
            val inputText = foodEditText.text.toString()
            val retrofit = Retrofit.Builder()
                .baseUrl("https://openapi.foodsafetykorea.go.kr/api/27dfc35a066042d49e98/I2790/json/1/5/")
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
                        response.body()?.I2790?.row?.let {
                                it1 ->
                            foodNutritionList = it1
                            initNutritionRecyclerView(savedInstanceState)
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
    fun initNutritionRecyclerView(savedInstanceState: Bundle?) {
        if (savedInstanceState == null) {
            val bundle = Bundle()
            bundle.putParcelableArrayList("resultList", ArrayList(foodNutritionList))
            supportFragmentManager.commit {
                setReorderingAllowed(true)
                add<SearchResultFragment>(R.id.fragment_container, args = bundle)
            }
        }

    }


}