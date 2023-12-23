package com.example.myday

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupWithNavController
import com.example.myday.databinding.ActivityMainBinding
import com.google.firebase.auth.FirebaseAuth

import com.example.myday.user.LoginActivity
import com.google.firebase.firestore.FirebaseFirestore

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
    }

    override fun onStart() {
        super.onStart()
        val currentUser = auth.currentUser
        if (currentUser == null) {
            startActivity(Intent(this, LoginActivity::class.java))
        } else {
             FirebaseFirestore.getInstance().collection("User").document(currentUser.uid)
                .get().addOnSuccessListener { document ->
                    Log.v("autolc", document.getBoolean("autoLogin")!!.toString())
                     when (document.getBoolean("autoLogin")!!) {
                         true -> HomeFragment()
                         else -> startActivity(Intent(this, LoginActivity::class.java))
                     }
                }
        }
    }
}
