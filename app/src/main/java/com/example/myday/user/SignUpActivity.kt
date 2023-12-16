package com.example.myday.user

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.example.myday.databinding.MemberPageBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class SignUpActivity : AppCompatActivity() {
    private lateinit var binding: MemberPageBinding
    private lateinit var auth: FirebaseAuth

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
            auth = FirebaseAuth.getInstance()
            createUser(
                UserDto(
                    name = binding.userName.text.toString(),
                    email = binding.email.text.toString(),
                    password = binding.password.text.toString(),
                    gender =  when {
                        binding.female.isChecked -> Gender.FEMALE
                        else -> Gender.MALE
                    },
                    height = binding.height.value,
                    weight = binding.weight.value
                )
            )


        }
    }

    private fun createUser(userDto: UserDto) {
        userDto.email?.let {
            userDto.password?.let { it1 ->
                auth.createUserWithEmailAndPassword(it, it1)
                    .addOnCompleteListener(this) {
                        task ->
                        if (task.isSuccessful) {
                            Log.v("SignUpActivity", "Successfully created user with uid: ${task.result?.user?.uid}")
                            saveUserIntoDB(userDto)
                        } else {
                            Log.v("SignUpActivity", "Failed to create user: ${task.exception}")
                        }
                    }
            }
        }
    }
    private fun saveUserIntoDB(userDto: UserDto) {
        val userId = auth.currentUser?.uid
        if (userDto!= null) {
            userDto.email?.let {
                FirebaseFirestore.getInstance()
                    .collection("User")
                    .document(it)
                    .set(userDto)
                    .addOnSuccessListener {
                        val intent = Intent(this, LoginActivity::class.java)
                        Log.v("SignUpActivity", "Successfully saved user info")
                        startActivity(intent)
                    }
                    .addOnFailureListener {
                        Log.v("SignUpActivity", "Failed to save user info: ${it.message}")
                    }
            }
        }

    }
}

