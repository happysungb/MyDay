package com.example.myday

import retrofit2.Call
import retrofit2.http.GET

interface GetKcalInfo {
    @GET("/myday/food/search")
    fun getNutritionData(): Call<FoodNutrition>
}