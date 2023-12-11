package com.example.myday.user


import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.google.android.gms.tasks.Task
import com.google.firebase.Firebase
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.auth

class MakeNewUserActivity : AppCompatActivity(){
    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        auth = Firebase.auth

        val email = intent.getStringExtra("email")
        val password = intent.getStringExtra("password")

        if (email != null && password != null) {
            createNewUser(email, password)
        }

        val name = intent.getStringExtra("name")
        val gender = intent.getStringExtra("gender")
        val height = intent.getStringExtra("height")
        val weight = intent.getStringExtra("weight")

        Log.d("MakeNewUserActivity", "Name: $name, Gender: $gender, Height: $height, Weight: $weight")
    }

    private fun createNewUser(email: String, password: String) {
        auth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    // 사용자 계정 생성에 성공한 경우
                    auth.currentUser?.sendEmailVerification()
                        ?.addOnCompleteListener { verificationTask ->
                            if (verificationTask.isSuccessful) {
                                // 이메일 인증 메일 전송 성공
                                Log.d("JoinActivity", "Email sent.")
                            } else {
                                // 이메일 인증 메일 전송 실패
                                Log.d("JoinActivity", "Email not sent.")
                            }
                        }
                } else {
                    // 사용자 계정 생성 실패
                    Log.v("createUserWithEmail:failure", task.exception.toString())
                }
            }
    }
}
