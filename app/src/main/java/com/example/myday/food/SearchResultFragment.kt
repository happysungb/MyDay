package com.example.myday.food

import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.Spinner
import androidx.annotation.RequiresApi
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.myday.R
import com.example.myday.databinding.RecyclerFragmentBinding
import com.example.myday.user.FoodArchive
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore
import java.time.LocalDate
import java.time.ZoneId
import java.util.Date

class SearchResultFragment: Fragment(), DialogCallback {
    private var _binding: RecyclerFragmentBinding? = null
    private val binding get() = _binding!!
    private val currentUser = FirebaseAuth.getInstance().currentUser
    private val db = FirebaseFirestore.getInstance()
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _binding = RecyclerFragmentBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val adapter = NutritionAdapter(this)
        adapter.datas = requireArguments().getParcelableArrayList("resultList")!!
        binding.recyclerView.layoutManager  = LinearLayoutManager(activity)
        binding.recyclerView.adapter = adapter
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onConfirm(foodInfo: Row, count: Int) {
        backToPreviousFragment()
        val uid = currentUser?.uid
        val period = getPeriod()
        val userSelection = buildSelectedInstance(
            foodInfo.DESC_KOR, foodInfo.NUTR_CONT1, foodInfo.NUTR_CONT2,
            foodInfo.NUTR_CONT3, foodInfo.NUTR_CONT4, foodInfo.NUTR_CONT5, foodInfo.NUTR_CONT6,
            foodInfo.NUTR_CONT7, foodInfo.NUTR_CONT8, foodInfo.NUTR_CONT9, count)
        val foodArchive = FoodArchive(LocalDate.now().toDate(), period, userSelection)

        val userRef = uid?.let { db.collection("User").document(it) }
        userRef?.update("foodArchive", FieldValue.arrayUnion(foodArchive))
            ?.addOnSuccessListener { Log.d("SearchResultFragment", "DocumentSnapshot successfully updated!") }
            ?.addOnFailureListener { e -> Log.w("SearchResultFragment", "Error updating document", e) }

        var kcalSum = 0
        var carboSum = 0
        var proteinSum = 0
        var fatSum = 0

        userRef?.get()?.addOnSuccessListener { document ->
            if (document.exists()) {
                kcalSum = (document.get("kcalSum") as Long).toInt()
                carboSum = (document.get("carboSum") as Long).toInt()
                proteinSum = (document.get("proteinSum") as Long).toInt()
                fatSum = (document.get("fatSum") as Long).toInt()
            }
        }
        userRef?.update("kcalSum", (kcalSum + foodInfo.NUTR_CONT1.toDoubleOrZero() * count))
        userRef?.update("carboSum", (carboSum + foodInfo.NUTR_CONT2.toDoubleOrZero() * count))
        userRef?.update("proteinSum", (proteinSum + foodInfo.NUTR_CONT3.toDoubleOrZero() * count))
        userRef?.update("fatSum", (fatSum + foodInfo.NUTR_CONT4.toDoubleOrZero() * count))
    }

    private fun getPeriod(): Time {
        return when (requireActivity().findViewById<Spinner>(R.id.spinner).selectedItem.toString()) {
            "아침" -> Time.BREAKFAST
            "점심" -> Time.LUNCH
            else -> Time.DINNER
        }
    }

    private fun backToPreviousFragment() {
        requireActivity().supportFragmentManager.popBackStack()
        val searchFragment = requireActivity().findViewById<LinearLayout>(R.id.food_fragment_container)
        searchFragment.visibility = View.GONE
        val mainFragment = requireActivity().findViewById<LinearLayout>(R.id.food_mainlayout)
        mainFragment.visibility = View.VISIBLE
    }

    private fun buildSelectedInstance(name: String, kcal: String, carbohydrate: String,
                                      protein: String, fat: String, sugar: String, sodium: String,
                                      cholesterol: String, saturatedFat: String,
                                      transFat: String, count: Int): Selected {
        return Selected(name, kcal.toDoubleOrZero(), carbohydrate.toDoubleOrZero(), protein.toDoubleOrZero(),
            fat.toDoubleOrZero(), sugar.toDoubleOrZero(), sodium.toDoubleOrZero(), cholesterol.toDoubleOrZero(),
            saturatedFat.toDoubleOrZero(), transFat.toDoubleOrZero(), count)
    }

    private fun String.toDoubleOrZero(): Int {
        return if (this.isEmpty()) {
            0
        } else {
            this.toDoubleOrNull()?.toInt() ?: 0
        }
    }

    // LocalDate를 Date로 변환
    fun LocalDate.toDate(): Date {
        return Date.from(this.atStartOfDay(ZoneId.systemDefault()).toInstant())
    }

    // Date를 LocalDate로 변환
    fun Date.toLocalDate(): LocalDate {
        return this.toInstant().atZone(ZoneId.systemDefault()).toLocalDate()
    }

}