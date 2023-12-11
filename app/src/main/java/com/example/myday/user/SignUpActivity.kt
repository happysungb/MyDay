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

        binding.joinBtn.setOnClickListener {
            val userInfo = getUserInfo()
            val intent = Intent(this, MakeNewUserActivity::class.java)
            intent.putExtra("name", userInfo["name"].toString())
            intent.putExtra("email", userInfo["email"].toString())
            intent.putExtra("password", userInfo["password"].toString())
            intent.putExtra("gender", userInfo["gender"].toString())
            intent.putExtra("height", userInfo["height"].toString())
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
        val np = binding.height
        np.minValue = 100
        np.maxValue = 250
        info["height"] = np.value.toString()
        return info
    }
}

