package com.example.myday

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Spinner
import androidx.fragment.app.Fragment
import com.example.myday.databinding.FragmentExerciseBinding
import com.example.myday.databinding.FragmentHomeBinding
import com.google.android.material.internal.ViewUtils
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.selects.select

class ExerciseFragment : Fragment() {
    private var _binding: FragmentExerciseBinding? = null
    private val binding get() = _binding!!
    private lateinit var exerciseSpinners: List<Spinner>
    private lateinit var timeSpinners: List<Spinner>
    private var userWeight: Double = 0.0 // 사용자의 몸무게 변수 추가
    private val currentUser = FirebaseAuth.getInstance().currentUser
    private val db = FirebaseFirestore.getInstance()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        exerciseSpinners = listOf(
            binding.spinnerExercise1,
            binding.spinnerExercise2,
            binding.spinnerExercise3,
            binding.spinnerExercise4
            // 추가적인 exercise 스피너들
        )

        timeSpinners = listOf(
            binding.spinnerTime1,
            binding.spinnerTime2,
            binding.spinnerTime3,
            binding.spinnerTime4
            // 추가적인 time 스피너들
        )

        // Cloud Firestore 인스턴스 초기화
        val db = FirebaseFirestore.getInstance()

        // 사용자 정보 가져오기
        val user = FirebaseAuth.getInstance().currentUser
        if (user != null) {
            val userId = user.uid // 사용자 UID 가져오기
            db.collection("User")
                .document(userId) // UID를 사용하여 문서 가져오기
                .get()
                .addOnSuccessListener { document: DocumentSnapshot ->
                    if (document.exists()) {
                        val userName = document.getString("name") ?: "사용자"
                        binding.exerciseGreeting.text = "${userName}님, 오늘은 어떤 운동을 하셨나요?"
                        userWeight = document.getDouble("weight") ?: 0.0 // 사용자의 몸무게 값 가져오기 (50은 기본값)
                        calculateAndDisplayTotalExerciseTime()
                    } else {
                        // 문서가 없을 경우 처리
                        binding.exerciseGreeting.text = "사용자 정보 없음"
                    }
                }
                .addOnFailureListener { e: Exception ->
                    // 오류 처리
                    binding.exerciseGreeting.text = "사용자 정보를 가져오지 못했습니다: $e"
                }
        } else {
            // 사용자가 로그인되어 있지 않을 경우 처리
            binding.exerciseGreeting.text = "로그인되지 않았습니다."
        }

        for ((index, spinner) in exerciseSpinners.withIndex()) {
            val exerciseItems = resources.getStringArray(R.array.exercise1)
            val exerciseAdapter = ArrayAdapter(requireContext(), android.R.layout.simple_spinner_item, exerciseItems)
            exerciseAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            spinner.adapter = exerciseAdapter

            spinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
                override fun onItemSelected(
                    parent: AdapterView<*>?,
                    view: View?,
                    position: Int,
                    id: Long
                ) {
                    // 선택된 항목에 대한 처리
                    val selectedItem = exerciseItems[position]
                    if (selectedItem != "선택해주세요") {
                        val selectedTimeStr = selectedItem.replace("[^\\d.]".toRegex(), "")
                        val timeValue = if (selectedTimeStr.isNotEmpty()) {
                            selectedTimeStr.toInt()
                        } else {
                            0
                        }
                        addTimeToTotalExercise(timeValue)
                    }
                }

                override fun onNothingSelected(parent: AdapterView<*>?) {
                    // 아무것도 선택되지 않은 경우 처리
                }
            }
        }

        for ((index, spinner) in timeSpinners.withIndex()) {
            val timeItems = resources.getStringArray(R.array.time1)
            val timeAdapter = ArrayAdapter(requireContext(), android.R.layout.simple_spinner_item, timeItems)
            timeAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            spinner.adapter = timeAdapter

            spinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
                override fun onItemSelected(
                    parent: AdapterView<*>?,
                    view: View?,
                    position: Int,
                    id: Long
                ) {
                    // 선택된 항목에 대한 처리
                    val selectedItem = timeItems[position]
                    if (selectedItem != "선택해주세요") {
                        val timeValue = selectedItem.replace("[^\\d.]".toRegex(), "").toInt()
                        addTimeToTotalExercise(timeValue)
                    }
                }

                override fun onNothingSelected(parent: AdapterView<*>?) {
                    // 아무것도 선택되지 않은 경우 처리
                }
            }
        }

        binding.calcButton.setOnClickListener {
            calculateAndDisplayTotalExerciseTime()
        }
        binding.add1.setOnClickListener {
            calculateAndDisplayTotalExerciseTime()
        }
        binding.add2.setOnClickListener {
            calculateAndDisplayTotalExerciseTime()
        }
        binding.add3.setOnClickListener {
            calculateAndDisplayTotalExerciseTime()
        }
        binding.add4.setOnClickListener {
            calculateAndDisplayTotalExerciseTime()
        }
    }

    private fun calculateAndDisplayTotalExerciseTime() {
        var totalExerciseTime = 0.0

        // 스피너에서 선택된 운동과 시간을 가져와서 계산합니다.
        for (index in 0 until exerciseSpinners.size) {
            val selectedExercise = exerciseSpinners[index].selectedItem.toString()
            val selectedTimeStr = timeSpinners[index].selectedItem.toString()
            val selectedTime = if (selectedTimeStr.isEmpty() || selectedTimeStr == "선택해주세요") {
                0
            } else {
                selectedTimeStr.replace("[^\\d.]".toRegex(), "").toInt()
            }
            totalExerciseTime += calculateExerciseTime(selectedExercise, selectedTime, userWeight)

        }

        // 계산된 운동 시간을 UI에 표시
        displayExercise1(totalExerciseTime)
    }

    private fun calculateExerciseTime(selectedExercise: String, selectedTime: Int, userWeight: Double): Double {

        return when (selectedExercise) {
            "천천히 걷기" -> 3.5 * 2 * userWeight * selectedTime / 1000 * 5
            "강아지 산책시키기" -> 3.5 * 3 * userWeight * selectedTime / 1000 * 5 // 강아지 산책시키기 계산값 수정
            "조깅하기" -> 3.5 * 7 * userWeight * selectedTime / 1000 * 5
            "자전거 타기" -> 3.5 * 2 * userWeight * selectedTime / 1000 * 5
            "수영하기" -> 3.5 * 7 * userWeight * selectedTime / 1000 * 5
            "테니스/스쿼시" -> 3.5 * 7 * userWeight * selectedTime / 1000 * 5
            "계단오르기" -> 3.5 * 8 * userWeight * selectedTime / 1000 * 5
            "달리기" -> 3.5 * 12 * userWeight * selectedTime / 1000 * 5
            else -> 0.0
        }
    }
    private fun displayExercise1(calculatedTime: Double) {
        val result1 = calculatedTime.toInt()
        binding.result1.text = "오늘의 총 소모 칼로리는 $result1 kcal 입니다."
        val uid = currentUser?.uid
        val userRef = uid?.let { db.collection("User").document(it) }
        userRef?.get()?.addOnSuccessListener { document ->
            if (document.exists()) {
                val spendKcalSum = (document.get("spendKcalSum") as Long).toInt()
                userRef.update("spendKcalSum", spendKcalSum + result1)
            }
        }
    }

    private fun addTimeToTotalExercise(timeValue: Int) {
        // 이 부분에 선택된 운동 시간을 전체 운동 시간에 더하는 로직을 추가하세요.
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _binding = FragmentExerciseBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}