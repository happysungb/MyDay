package com.example.myday

import android.annotation.SuppressLint
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.core.view.GravityCompat
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.myday.databinding.ActivityMainBinding
import com.example.myday.databinding.FoodListBinding
import com.google.gson.GsonBuilder
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var adapter: NutritionAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val drawerLayout = binding.drawer
        binding.buttonOpenNav.setOnClickListener {
            drawerLayout.openDrawer(GravityCompat.START)
        }

        val foodEditText = binding.foodEt
        binding.kcalSearchBtn.setOnClickListener {
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
                        Log.v("retrofit", "success")
                        response.body()?.I2790?.row?.let {
                                it1 -> initNutritionRecyclerView(it1)
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
    fun initNutritionRecyclerView(foodNutritionList: MutableList<Row>){
        Log.v("NutritionList", foodNutritionList[0].DESC_KOR)
        adapter = NutritionAdapter()
        adapter.datas = foodNutritionList
        binding.recyclerView.adapter = adapter
        binding.recyclerView.layoutManager = LinearLayoutManager(this)
        binding.recyclerView.visibility = View.VISIBLE
    }



}