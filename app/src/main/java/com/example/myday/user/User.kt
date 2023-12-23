package com.example.myday.user

import com.example.myday.food.Selected
import com.example.myday.food.Time
import java.util.Date

data class UserDto(
    var autoLogin :Boolean? = false,
    var name :String? = null,
    var email :String? = null,
    var password :String? = null,
    var gender :Gender? = null,
    var height :Int? = null,
    var weight :Int? = null,
    var birthDate: Long? = null,
    var timestamp :Long? = null,
    var foodArchive :MutableList<FoodArchive> = mutableListOf(),
    val spendKcalSum :Int = 0,
    val kcalSum :Int = 0,
    val carboSum :Int = 0,
    val proteinSum :Int = 0,
    val fatSum :Int = 0,
    val score :Int = 0
)

data class FoodArchive (
    private var date: Date? = null, // 날짜
    val period: Time? = null, // 아침 점심 저녁
    val foodList: Selected? = null, // 음식 리스트
)

data class UserDocument(
    val foodArchive: List<FoodArchive>? = null
)