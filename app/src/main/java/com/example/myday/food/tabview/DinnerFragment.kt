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

class DinnerFragment: Fragment() {
    private var _binding: FoodViewpagerBinding? = null
    private val binding get() = _binding!!
    private val currentUser = FirebaseAuth.getInstance().currentUser
    private val db = FirebaseFirestore.getInstance()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _binding = FoodViewpagerBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val userInfo = db.collection("User").document(currentUser?.uid.toString()).get()
        userInfo.addOnSuccessListener {document ->
            if (document.exists()) {
                val foodArchives = document.toObject<UserDocument>()?.foodArchive
                val dinnerFoodLists = foodArchives?.filter{it.period == Time.DINNER}?.map{it.foodList}

                val adapterOne = ListOneDinner()
                adapterOne.selectedList = dinnerFoodLists as MutableList<Selected>
                binding.list1.layoutManager = LinearLayoutManager(activity)
                binding.list1.adapter = adapterOne

                val adapterTwo = ListTwoDinner()
                adapterTwo.selectedList = dinnerFoodLists
                binding.list2.layoutManager = LinearLayoutManager(activity)
                binding.list2.adapter = adapterTwo
            }

        }

    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}