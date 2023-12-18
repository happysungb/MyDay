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

class FriendFragment : Fragment() {

    private lateinit var binding: FragmentFriendBinding
    private lateinit var databaseReference: DatabaseReference
    private lateinit var auth: FirebaseAuth
    private var currentUser: FirebaseUser? = null

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

            // 이메일이 비어있지 않은지 확인
            if (searchEmail.isNotEmpty()) {
                searchUserByEmail(searchEmail)
            } else {
                // 이메일이 비어있을 때 처리
                // 예를 들어, 사용자에게 이메일을 입력하라는 메시지 표시
            }
        }
    }

    private fun searchUserByEmail(email: String) {
        // Firebase Realtime Database에서 사용자를 검색하는 코드
        databaseReference.child("users")
            .orderByChild("email")
            .equalTo(email)
            .addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onDataChange(dataSnapshot: DataSnapshot) {
                    Log.d("FriendFragment", "DataSnapshot: $dataSnapshot")
                    if (dataSnapshot.exists()) {
                        val userSnapshot = dataSnapshot.children.first() // 첫 번째 사용자 정보 가져오기
                        val foundUser = userSnapshot.getValue(UserDto::class.java)
                        if (foundUser != null) {
                            val userName = foundUser.name ?: "이름 없음"
                            binding.resultTextView.text = "찾은 사용자: $userName"
                        } else {
                            binding.resultTextView.text = "사용자 정보가 없습니다"
                        }
                    } else{
                        binding.resultTextView.text = "사용자를 찾을 수 없습니다"
                    }
                }

                override fun onCancelled(databaseError: DatabaseError) {
                    // 검색 쿼리가 실패한 경우 처리
                }
            })
    }
}