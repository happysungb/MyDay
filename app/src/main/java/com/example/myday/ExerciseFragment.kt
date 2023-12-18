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
import kotlinx.coroutines.selects.select

class ExerciseFragment: Fragment() {
    private var _binding: FragmentExerciseBinding? = null
    private val binding get() = _binding!!
    private lateinit var exerciseSpinners: List<Spinner>
    private lateinit var timeSpinners: List<Spinner>

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
            totalExerciseTime += calculateExerciseTime(selectedExercise, selectedTime)
        }

        // 계산된 운동 시간을 UI에 표시
        displayExerciseTime(totalExerciseTime)
    }

    private fun calculateExerciseTime(selectedExercise: String, selectedTime: Int): Double {
        val userWeight = 50

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

    private fun displayExerciseTime(calculatedTime: Double) {
        // 운동 시간 값을 텍스트로 변환
        val formattedTime = String.format("%.2f", calculatedTime) // 소수점 두 자리까지 표시하도록 설정

        // UI 상의 TextView에 운동 시간 값을 설정
        binding.calcResult.text = "오늘의 총 소모 칼로리는 $formattedTime kcal 입니다."
    }



    private fun addTimeToTotalExercise(timeValue: Int) {

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