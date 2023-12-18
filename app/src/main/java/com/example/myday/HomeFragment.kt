package com.example.myday

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.myday.databinding.FragmentHomeBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class HomeFragment : Fragment() {
    private lateinit var binding: FragmentHomeBinding
    private lateinit var auth: FirebaseAuth
    private var currentUser: FirebaseUser? = null
    private val db: FirebaseFirestore = Firebase.firestore // Cloud Firestore 인스턴스

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        auth = FirebaseAuth.getInstance()
        currentUser = auth.currentUser

        // Firebase Authentication에서 현재 사용자 가져오기
        val user = FirebaseAuth.getInstance().currentUser

        // 현재 사용자가 있을 경우 Cloud Firestore에서 사용자 정보 가져오기
        user?.uid?.let { userId ->
            db.collection("User")
                .document(userId)
                .get()
                .addOnSuccessListener { document ->
                    if (document.exists()) {
                        val userName = document.getString("name") ?: "사용자"
                        binding.greetingTextView.text = "${userName}님, 안녕하세요."
                    } else {
                        // 문서가 없을 경우 처리
                        binding.greetingTextView.text = "사용자 정보 없음"
                    }
                }
                .addOnFailureListener { e ->
                    // 오류 처리
                    binding.greetingTextView.text = "사용자 정보를 가져오지 못했습니다: $e"
                }
        } ?: run {
            // 사용자가 로그인되어 있지 않을 경우 처리
            binding.greetingTextView.text = "로그인되지 않았습니다."
        }
    }

    // onDestroyView() 함수는 필요 없으므로 주석 처리 또는 삭제
}
