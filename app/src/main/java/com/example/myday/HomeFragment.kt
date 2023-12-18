package com.example.myday

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.myday.databinding.FragmentHomeBinding
import com.example.myday.user.Gender
import com.example.myday.user.UserDto
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import java.util.Calendar

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

        val user = FirebaseAuth.getInstance().currentUser

        // 현재 사용자가 있을 경우 Cloud Firestore에서 사용자 정보 가져오기
        user?.uid?.let { userId ->
            db.collection("User")
                .document(userId)
                .get()
                .addOnSuccessListener { document ->
                    if (document.exists()) {
                        val userName = document.getString("name") ?: "사용자"
                        val userDto = document.toObject(UserDto::class.java) // UserDto로 변환

                        // UserDto에서 성별 정보 가져오기
                        val userGender = userDto?.gender

                        // UserDto에서 생년월일, 체중, 신장 정보 가져오기
                        val userBirthDate = userDto?.birthDate
                        val userWeight = userDto?.weight?.toInt() ?: 0
                        val userHeight = userDto?.height?.toInt() ?: 0

                        // 로그로 변수 값 확인
                        Log.d("HomeFragment", "User Gender: $userGender")
                        Log.d("HomeFragment", "User Weight: $userWeight")
                        Log.d("HomeFragment", "User Height: $userHeight")

                        // 생년월일 정보로 나이 계산
                        val age = calculateAge(userBirthDate)

                        // 권장 칼로리 계산
                        val recommendedCalories =
                            calculateRecommendedCalories(userGender, age, userWeight, userHeight)
                        val kcalSum = document.get("kcalSum").toString().toInt()
                        val carboSum = document.get("carboSum").toString().toInt()
                        val proteinSum = document.get("proteinSum").toString().toInt()
                        val fatSum = document.get("fatSum").toString().toInt()
                        val spendKcalSum = document.get("spendKcalSum").toString().toInt()
                        val currKcal = kcalSum - spendKcalSum
                        val score = when ((currKcal / recommendedCalories) * 100) {
                            in 90..100 -> 100
                            in 80..89 -> 90
                            in 70..79 -> 80
                            in 60..69 -> 70
                            in 50..59 -> 60
                            in 40..49 -> 50
                            in 30..39 -> 40
                            in 20..29 -> 30
                            in 10..19 -> 20
                            in 1..9 -> 10
                            else -> 10
                        }
                        val uid = currentUser?.uid
                        uid?.let { db.collection("User").document(it) }?.update("score", score)

                        // 텍스트뷰에 권장 칼로리 표시
                        binding.homeKcal.text = "${currKcal}/${recommendedCalories}kcal"
                        binding.homeRecommendedIntake.text = "목표 칼로리: $recommendedCalories kcal"
                        binding.homeAteKcal.text = "섭취 칼로리: ${kcalSum}kcal"
                        binding.homeExerciseKcal.text = "운동 칼로리: ${spendKcalSum}kcal"
                        binding.greetingTextView.text = "${userName}님, 안녕하세요."
                        binding.homeAteCarbo.text = "${carboSum}g"
                        binding.homeAteProtein.text = "${proteinSum}g"
                        binding.homeAteFat.text = "${fatSum}g"
                        binding.homeScore.text = "오늘의 달성도 점수: ${score}점"

                    } else {
                        // 문서가 없을 경우 처리
                        binding.greetingTextView.text = "사용자 정보 없음"
                        binding.homeRecommendedIntake.text = "목표 칼로리: 정보 없음" // 혹은 다른 기본값 설정
                    }
                }
                .addOnFailureListener { e ->
                    // 오류 처리
                    binding.greetingTextView.text = "사용자 정보를 가져오지 못했습니다: $e"
                    binding.homeRecommendedIntake.text = "목표 칼로리: 오류" // 오류 발생 시에도 처리
                }
        } ?: run {
            // 사용자가 로그인되어 있지 않을 경우 처리
            binding.greetingTextView.text = "로그인되지 않았습니다."
            binding.homeRecommendedIntake.text = "목표 칼로리: 로그인 필요" // 혹은 다른 기본값 설정
        }
    }

    // 생년월일을 나이로 변환하는 함수
    private fun calculateAge(birthDate: Long?): Int {
        birthDate?.let {
            val currentDate = Calendar.getInstance().timeInMillis
            val ageInMillis = currentDate - birthDate
            val ageCalendar = Calendar.getInstance()
            ageCalendar.timeInMillis = ageInMillis
            val age = ageCalendar.get(Calendar.YEAR) - 1970 // 만 나이로 계산
            Log.d("HomeFragment", "Calculated Age: $age") // 계산된 나이를 로그로 출력
            return age
        }
        return 0 // 생년월일이 없을 경우 0 반환
    }

    // 남성의 권장 칼로리를 계산하는 함수
    private fun calculateRecommendedCaloriesForMale(
        age: Int,
        weight: Int,
        height: Int
    ): Int {
        val result = (10 * weight) + (6.25 * height.toDouble()) + (5 * age) + 5
        Log.d("CaloriesCalculation", "Male Calories: $result")
        return result.toInt()
    }

    // 여성의 권장 칼로리를 계산하는 함수
    private fun calculateRecommendedCaloriesForFemale(
        age: Int,
        weight: Int,
        height: Int
    ): Int {
        val result = (10 * weight) + (6.25 * height.toDouble()) + (5 * age) - 161
        Log.d("CaloriesCalculation", "Female Calories: $result")
        return result.toInt()
    }

    // 사용자의 성별, 생년월일, 체중, 신장 정보를 사용하여 권장 칼로리를 계산하는 함수
    private fun calculateRecommendedCalories(
        gender: Gender?,
        age: Int,
        weight: Int,
        height: Int
    ): Int {
        return when (gender) {
            Gender.MALE -> calculateRecommendedCaloriesForMale(age, weight, height)
            Gender.FEMALE -> calculateRecommendedCaloriesForFemale(age, weight, height)
            else -> {
                // 성별 정보가 없거나 unknown인 경우 기본값 또는 예외 처리
                // 여기서 기본값 또는 예외 처리를 구현하세요.
                0 // 임시로 0을 반환하도록 수정
            }
        }
    }
}