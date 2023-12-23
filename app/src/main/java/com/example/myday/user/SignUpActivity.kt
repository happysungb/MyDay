package com.example.myday.user

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.example.myday.databinding.MemberPageBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class SignUpActivity : AppCompatActivity() {
    private lateinit var binding: MemberPageBinding
    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = MemberPageBinding.inflate(layoutInflater)
        setContentView(binding.root)

        auth = FirebaseAuth.getInstance()

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
            val email = binding.email.text.toString()
            val password = binding.password.text.toString()
            val name = binding.userName.text.toString()
            val gender = when {
                binding.female.isChecked -> Gender.FEMALE
                else -> Gender.MALE
            }
            val height = binding.height.value
            val weight = binding.weight.value
            val birthDateStr = binding.birthDate.text.toString()

            // 생년월일 형식을 검증하고 파싱합니다.
            val dateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
            var birthDate: Date? = null
            try {
                birthDate = dateFormat.parse(birthDateStr)
            } catch (e: Exception) {
                Log.v("SignUpActivity", "Invalid birth date format")
                // 사용자에게 날짜 형식이 잘못되었다고 알림을 주는 처리를 추가할 수 있습니다.
                return@setOnClickListener
            }


            // 이메일과 비밀번호의 유효성 검사
            if (email.isNotBlank() && password.isNotBlank() && birthDate != null) {
                val userDto = UserDto(
                    name = name,
                    email = email,
                    password = password,
                    gender = gender,
                    height = height,
                    weight = weight,
                    birthDate = birthDate.time // Date를 Timestamp로 변환합니다.
                )
                createUser(userDto)
            } else {
                // 이메일, 비밀번호 또는 생년월일이 비어있을 경우 로그 출력
                Log.v("SignUpActivity", "Email, password, or birth date is empty")
            }
        }
    }

    private fun createUser(userDto: UserDto) {
        auth.createUserWithEmailAndPassword(userDto.email!!, userDto.password!!)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    Log.v("SignUpActivity", "Successfully created user with uid: ${task.result?.user?.uid}")
                    saveUserIntoDB(userDto)
                } else {
                    // 회원가입 실패 시 오류 로그 출력
                    Log.v("SignUpActivity", "Failed to create user: ${task.exception}")
                }
            }
    }

    private fun saveUserIntoDB(userDto: UserDto) {
        val userId = auth.currentUser?.uid ?: run {
            Log.v("SignUpActivity", "Auth currentUser is null, cannot save user info")
            return
        }

        FirebaseFirestore.getInstance()
            .collection("User")
            .document(userId)
            .set(userDto)
            .addOnSuccessListener {
                val intent = Intent(this, LoginActivity::class.java)
                Log.v("SignUpActivity", "Successfully saved user info")
                startActivity(intent)
                finish()
            }
            .addOnFailureListener {
                // DB 저장 실패 시 오류 로그 출력
                Log.v("SignUpActivity", "Failed to save user info: ${it.message}")
            }
    }
}


