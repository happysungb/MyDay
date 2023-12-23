package com.example.myday

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper

class IntroActivity : Activity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.intro_layout)
        Handler(Looper.getMainLooper()).postDelayed({ // MainActivity로 인텐트 변경
            val intent = Intent(this@IntroActivity, MainActivity::class.java)
            startActivity(intent)
            finish()
        }, 1500)
    }

    override fun onPause() {
        super.onPause()
        finish()
    }
}
