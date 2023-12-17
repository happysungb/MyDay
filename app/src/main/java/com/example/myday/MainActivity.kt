package com.example.myday

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.commit
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupWithNavController
import com.example.myday.databinding.ActivityMainBinding
import com.example.myday.food.FoodFragment
import com.google.firebase.auth.FirebaseAuth

import com.example.myday.user.LoginActivity

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        auth = FirebaseAuth.getInstance()
        setContentView(binding.root)
        val navHostFragment = supportFragmentManager.findFragmentById(R.id.nav_host_fragment) as NavHostFragment
        val navController = navHostFragment.navController
        binding.bottomNavi.setupWithNavController(navController)
        binding.bottomNavi.selectedItemId = R.id.home_bottom

        binding.bottomNavi.setOnItemSelectedListener {
            when (it.itemId) {
                R.id.home_bottom -> {
                    loadFragment(HomeFragment())
                    true
                }
                R.id.food_bottom -> {
                    loadFragment(FoodFragment())
                    true
                }
                R.id.exercise_bottom -> {
                    loadFragment(ExerciseFragment())
                    true
                }
                R.id.friend_bottom -> {
                    loadFragment(FriendFragment())
                    true
                }
                else -> {
                    Log.v("MainActivity", "fragment connection failed")
                    false
                }
            }
        }

    }

    override fun onStart() {
        super.onStart()
        val currentUser = auth.currentUser
        if (currentUser === null) {
            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
        }
    }

    private fun loadFragment(fragment: Fragment){
        supportFragmentManager.commit {
            setReorderingAllowed(true)
            addToBackStack(null)
            this.replace(R.id.home_fragment_container, fragment)
        }
    }

}
