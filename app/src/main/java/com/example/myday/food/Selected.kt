package com.example.myday.food

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Selected(
    val name: String? = null,
    val kcal: Int? = null, // 열량(kcal)
    val carbohydrate: Int? = null, // 탄수화물(g)
    val protein: Int? = null, // 단백질(g)
    val fat: Int? = null, // 지방(g)
    val sugar: Int? = null, // 당류(g)
    val sodium: Int? = null, // 나트륨(mg)
    val cholesterol: Int? = null, // 콜레스테롤(mg)
    val saturatedFat: Int? = null, // 포화지방산(g)
    val transFat: Int? = null, // 트랜스지방(g)
    val count: Int? = null // 섭취량
) : Parcelable
