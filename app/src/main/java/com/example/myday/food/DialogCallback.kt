package com.example.myday.food

interface DialogCallback {
    fun onConfirm(name: String, kcal: Int, count: Int)
}