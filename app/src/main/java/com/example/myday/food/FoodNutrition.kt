package com.example.myday.food

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class FoodNutrition(
    val I2790: I2790
) : Parcelable

@Parcelize
data class I2790(
    val RESULT: RESULT,
    val row: MutableList<Row>,
    val total_count: String
) : Parcelable

@Parcelize
data class Row(
    val DESC_KOR: String,
    val FOOD_CD: String,
    val GROUP_NAME: String,
    val MAKER_NAME: String,
    val NUM: String,
    val NUTR_CONT1: String,
    val NUTR_CONT2: String,
    val NUTR_CONT3: String,
    val NUTR_CONT4: String,
    val NUTR_CONT5: String,
    val NUTR_CONT6: String,
    val NUTR_CONT7: String,
    val NUTR_CONT8: String,
    val NUTR_CONT9: String,
    val RESEARCH_YEAR: String,
    val SAMPLING_MONTH_CD: String,
    val SAMPLING_MONTH_NAME: String,
    val SAMPLING_REGION_CD: String,
    val SAMPLING_REGION_NAME: String,
    val SERVING_SIZE: String,
    val SERVING_UNIT: String,
    val SUB_REF_NAME: String
) : Parcelable

@Parcelize
data class RESULT(
    val CODE: String,
    val MSG: String
) : Parcelable