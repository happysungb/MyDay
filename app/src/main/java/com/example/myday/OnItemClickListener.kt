package com.example.myday

import android.os.Bundle

interface OnItemClickListener {
    val arguments: Bundle

    fun onItemClicked(name: String, kcal: String)
}