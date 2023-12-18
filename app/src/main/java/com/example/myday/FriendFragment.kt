package com.example.myday

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.myday.databinding.FragmentFriendBinding
import com.example.myday.user.UserDto
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.*
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore

class FriendFragment : Fragment() {

    private lateinit var binding: FragmentFriendBinding
    private lateinit var databaseReference: DatabaseReference
    private lateinit var auth: FirebaseAuth
    private var currentUser: FirebaseUser? = null
    private var foundUserKey: String? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentFriendBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        auth = FirebaseAuth.getInstance()
        currentUser = auth.currentUser
        databaseReference = FirebaseDatabase.getInstance().reference

        // 검색 버튼 클릭 리스너 설정
        binding.friendSearchButton.setOnClickListener {
            val searchEmail = binding.friendEmail.text.toString().trim()
            if (searchEmail.isNotEmpty()) {
                searchUserByEmail(searchEmail)
            } else {
                // 이메일이 비어있을 때 처리
                // 예를 들어, 사용자에게 이메일을 입력하라는 메시지 표시
            }
        }
    }

    private fun searchUserByEmail(email: String) {
        Log.d("FriendFragment", "Searching for user with email: $email")
        val trimmedEmail = email.trim()
        val firestore = FirebaseFirestore.getInstance()
        firestore.collection("User")
            .whereEqualTo("email", trimmedEmail)
            .get()
            .addOnSuccessListener { documents ->
                Log.d("FriendFragment", "Query completed. Found ${documents.size()} documents")
                if (!documents.isEmpty) {
                    val userDocument = documents.documents.first()
                    val foundUser = userDocument.toObject(UserDto::class.java)
                    foundUserKey = userDocument.id

                    foundUser?.let { user ->
                        val userName = user.name ?: "이름 없음"
                        binding.friendResult.text = "찾은 사용자: $userName"
                        binding.friendRequestButton.visibility = View.VISIBLE
                        binding.friendRequestButton.setOnClickListener { sendFriendRequest(trimmedEmail) }
                    }
                } else {
                    binding.friendResult.text = "사용자를 찾을 수 없습니다"
                    binding.friendRequestButton.visibility = View.GONE
                }
            }
            .addOnFailureListener { exception ->
                Log.e("FriendFragment", "Error fetching user by email: $trimmedEmail", exception)
                binding.friendResult.text = "오류 발생: 사용자 검색에 실패했습니다."
                binding.friendRequestButton.visibility = View.GONE
            }
    }

    private fun sendFriendRequest(email: String) {
        // 현재 사용자의 이메일 가져오기
        val currentUserEmail = currentUser?.email

        // 현재 사용자와 친구로 추가할 사용자의 이메일을 사용하여 친구 관계를 Firestore에 추가
        if (currentUserEmail != null) {
            val firestore = FirebaseFirestore.getInstance()
            val friendshipsCollection = firestore.collection("friendships")

            // 친구 관계를 나타내는 객체
            val friendship = hashMapOf(
                "user1" to currentUserEmail,
                "user2" to email,
                "created_at" to FieldValue.serverTimestamp() // 현재 서버 시간으로 저장
            )

            // 'friendships' 컬렉션에 친구 관계 추가
            friendshipsCollection.add(friendship)
                .addOnSuccessListener {
                    // 성공 처리: 예를 들어, UI 업데이트 또는 사용자에게 알림
                    // 친구 목록 업데이트
                    updateFriendListTextView()
                }
                .addOnFailureListener { e ->
                    // 실패 처리: 예를 들어, 사용자에게 오류 메시지 표시
                }
        }
    }

    private fun updateFriendListTextView() {
        val firestore = FirebaseFirestore.getInstance()
        firestore.collection("friendships")
            .whereEqualTo("user1", currentUser?.email)
            .get()
            .addOnSuccessListener { documents ->
                val friendList = documents.mapNotNull { it.getString("user2") }
                // TextView에 친구 목록 표시
                binding.friendListTextView.text = friendList.joinToString("\n")
            }
            .addOnFailureListener { e ->
                Log.e("FriendFragment", "Error fetching friends: $e")
            }
    }
}