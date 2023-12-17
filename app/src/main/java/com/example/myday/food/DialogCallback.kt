package com.example.myday.food

interface DialogCallback {
    fun onConfirm(foodInfo: Row, count: Int)
}