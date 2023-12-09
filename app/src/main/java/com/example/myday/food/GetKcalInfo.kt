package com.example.myday.food

import com.example.myday.food.FoodNutrition
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path

interface GetKcalInfo {
    @GET("DESC_KOR={input}")
    fun getNutritionData(@Path("input") name: String): Call<FoodNutrition>
}