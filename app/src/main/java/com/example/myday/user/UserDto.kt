package com.example.myday.user

import com.example.myday.food.Selected
import com.example.myday.food.Time
import java.time.LocalDate

data class UserDto(
    var name :String? = null,
    var email :String? = null,
    var password :String? = null,
    var gender :Gender? = null,
    var height :Int? = null,
    var weight :Int? = null,
    var birthDate: Long? = null,
    var timestamp :Long? = null,
    var foodArchive :MutableList<FoodArchive> = mutableListOf()
)

data class FoodArchive (
    var date: LocalDate, // 날짜
    val period: Time, // 아침 점심 저녁
    val foodList: Selected // 음식 리스트
)
