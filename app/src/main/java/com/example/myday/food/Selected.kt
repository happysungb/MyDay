package com.example.myday.food

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Selected(
    val name: String,
    val kcal: Int, // 열량(kcal)
    val carbohydrate: Int, // 탄수화물(g)
    val protein: Int, // 단백질(g)
    val fat: Int, // 지방(g)
    val sugar: Int, // 당류(g)
    val sodium: Int, // 나트륨(mg)
    val cholesterol: Int, // 콜레스테롤(mg)
    val saturatedFat: Int, // 포화지방산(g)
    val transFat: Int, // 트랜스지방(g)
    val count: Int // 섭취량

) : Parcelable
