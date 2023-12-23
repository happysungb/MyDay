package com.example.myday.food.tabview

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.myday.databinding.FoodViewpagerBinding
import com.example.myday.food.Selected
import com.example.myday.food.Time
import com.example.myday.user.UserDocument
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.toObject

class NutritionViewPagerFragment(): Fragment(){
    private var _binding: FoodViewpagerBinding? = null
    private val binding get() = _binding!!
    private val currentUser = FirebaseAuth.getInstance().currentUser
    private val db = FirebaseFirestore.getInstance()
    private var position = 0

    constructor(position: Int) : this() {
        this.position = position
    }
    
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _binding = FoodViewpagerBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val userInfo = db.collection("User").document(currentUser?.uid.toString()).get()
        userInfo.addOnSuccessListener {document ->
            val foodArchives = document.toObject<UserDocument>()?.foodArchive
            val foodLists: MutableList<Selected>
            val adapterOne: ListOneAdapter
            val adapterTwo: ListTwoAdapter
            if (document.exists()) {
                when(position) {
                    0 -> {
                        foodLists = foodArchives?.filter { it.period == Time.BREAKFAST}?.map{it.foodList} as MutableList<Selected>
                        adapterOne = ListOneAdapter(Time.BREAKFAST)
                        adapterOne.breakfast = foodLists
                        binding.list1.layoutManager = LinearLayoutManager(activity)
                        binding.list1.adapter = adapterOne
                        adapterTwo = ListTwoAdapter(Time.BREAKFAST)
                        adapterTwo.breakfast = foodLists
                        binding.list2.layoutManager = LinearLayoutManager(activity)
                        binding.list2.adapter = adapterTwo
                        }
                    1 -> {
                        foodLists = foodArchives?.filter { it.period == Time.LUNCH}?.map{it.foodList} as MutableList<Selected>
                        adapterOne = ListOneAdapter(Time.LUNCH)
                        adapterOne.lunch = foodLists
                        binding.list1.layoutManager = LinearLayoutManager(activity)
                        binding.list1.adapter = adapterOne
                        adapterTwo = ListTwoAdapter(Time.LUNCH)
                        adapterTwo.lunch = foodLists
                        binding.list2.layoutManager = LinearLayoutManager(activity)
                        binding.list2.adapter = adapterTwo
                    }
                    else -> {
                        foodLists = foodArchives?.filter { it.period == Time.DINNER}?.map{it.foodList} as MutableList<Selected>
                        adapterOne = ListOneAdapter(Time.DINNER)
                        adapterOne.dinner = foodLists
                        binding.list1.layoutManager = LinearLayoutManager(activity)
                        binding.list1.adapter = adapterOne
                        adapterTwo = ListTwoAdapter(Time.DINNER)
                        adapterTwo.dinner = foodLists
                        binding.list2.layoutManager = LinearLayoutManager(activity)
                        binding.list2.adapter = adapterTwo
                    }
                }
            }

        }

    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
