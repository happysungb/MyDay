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
    lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        auth = Firebase.auth
        val intent = Intent()
        intent.getStringExtra("email")?.let {
            intent.getStringExtra("password")?.let {
                createNewUser(it, it)
            }
        }

    }

    private fun createNewUser(email: String, password: String) {
        auth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener(this) { task: Task<AuthResult> ->
            if (task.isSuccessful) {
                auth.currentUser?.sendEmailVerification()
                    ?.addOnCompleteListener {
                        sendTask ->
                        if (sendTask.isSuccessful) {
                            Log.d("JoinActivity", "Email sent.")
                        } else {
                            Log.d("JoinActivity", "Email not sent.")
                        }
                    }
            } else {
                Log.v("createUserWithEmail:failure", task.exception.toString())
            }
        }
    }
}
