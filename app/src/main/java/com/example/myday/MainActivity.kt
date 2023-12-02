package com.example.myday

import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import com.example.myday.databinding.ActivityMainBinding
import com.google.android.material.navigation.NavigationView


class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val drawerLayout = binding.drawer
        binding.buttonOpenNav.setOnClickListener {
            drawerLayout.openDrawer(GravityCompat.START)
        }

        val foodEditText = binding.foodEt
        val kcalSearchBtn = binding.kcalSearchBtn
        binding.kcalSearchBtn.setOnClickListener {
            val inputText = foodEditText.text.toString()

            val url = "http://openapi.foodsafetykorea.go.kr/api/27dfc35a066042d49e98/I2790/json/1/5/DESC_KOR=${inputText}"
            val intent = Intent(Intent.ACTION_VIEW)
            intent.data = Uri.parse(url)
            startActivity(intent)
        }





    }

}