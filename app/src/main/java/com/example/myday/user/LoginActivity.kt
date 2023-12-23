package com.example.myday.user

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.CheckBox
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.myday.MainActivity
import com.example.myday.R
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.auth
import com.google.firebase.firestore.FirebaseFirestore

class LoginActivity : AppCompatActivity() {
    private lateinit var auth: FirebaseAuth
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.login_page)
        auth = Firebase.auth

        val loginButton: Button = findViewById(R.id.login_btn)
        loginButton.setOnClickListener {
            val email = findViewById<EditText>(R.id.login_email).text.toString()
            val password = findViewById<EditText>(R.id.login_password).text.toString()
            loginUser(email, password)
        }

        val joinButton: Button = findViewById(R.id.join_btn)
        joinButton.setOnClickListener {
            val intent = Intent(this, SignUpActivity::class.java)
            startActivity(intent)
        }
    }

    override fun onStart() {
        super.onStart()
        val currentUser = auth.currentUser
        if (currentUser != null) {
            FirebaseFirestore.getInstance().collection("User").document(currentUser.uid)
                .get().addOnSuccessListener { document ->
                    if (document.getBoolean("autoLogin") == true) {
                        startActivity(Intent(this, MainActivity::class.java))
                    }
                }
        }
    }
    private fun loginUser(email: String, password: String) {
        auth.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    // 로그인 성공, 사용자 정보 가져오기
                    val userRef =
                        auth.currentUser?.let {
                            FirebaseFirestore.getInstance().collection("User").document(it.uid)
                        }
                    val checked: Boolean = findViewById<CheckBox>(R.id.autoLoginCheck).isChecked
                    Log.v("autolc1", checked.toString())
                    userRef?.update("autoLogin", checked)
                    userRef?.get()
                        ?.addOnSuccessListener { document ->
                            val userName = document.getString("name")
                            val intent = Intent(this, MainActivity::class.java)
                            intent.putExtra("userName", userName)
                            intent.putExtra("userEmail",email)
                            startActivity(intent)
                            finish()
                        }
                        ?.addOnFailureListener {
                            Toast.makeText(this, "오류: ${it.message}", Toast.LENGTH_SHORT).show()
                        }
                } else {
                    Toast.makeText(this, "로그인 실패", Toast.LENGTH_SHORT).show()
                }
            }
    }
}