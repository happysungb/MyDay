package com.example.myday.user

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.myday.databinding.MemberPageBinding

class SignUpActivity : AppCompatActivity() {
    private lateinit var binding: MemberPageBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = MemberPageBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.weight.apply {
            minValue = 30  // 최소 몸무게 30kg 설정
            maxValue = 200 // 최대 몸무게 200kg 설정
            wrapSelectorWheel = false
        }

        binding.height.apply {
            minValue = 100
            maxValue = 250
            wrapSelectorWheel = false
        }

        binding.joinBtn.setOnClickListener {
            val userInfo = getUserInfo()
            val intent = Intent(this, MakeNewUserActivity::class.java).apply {
                putExtra("name", userInfo["name"].toString())
                putExtra("email", userInfo["email"].toString())
                putExtra("password", userInfo["password"].toString())
                putExtra("gender", userInfo["gender"].toString())
                putExtra("height", userInfo["height"].toString())
                putExtra("weight", userInfo["weight"].toString())
            }
            startActivity(intent)
        }
    }

    private fun getUserInfo(): MutableMap<String, String> {
        val info = mutableMapOf<String, String>()
        info["name"] = binding.userName.text.toString()
        info["email"] = binding.email.text.toString()
        info["password"] = binding.password.text.toString()
        info["gender"] = when {
            binding.female.isChecked -> "Female"
            else -> "Male"
        }
        info["height"] = binding.height.value.toString()
        info["weight"] = binding.weight.value.toString()

        return info
    }
}

