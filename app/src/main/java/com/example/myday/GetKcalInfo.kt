package com.example.myday

import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path

interface GetKcalInfo {
    @GET("DESC_KOR={input}")
    fun getNutritionData(@Path("input") name: String): Call<FoodNutrition>
}