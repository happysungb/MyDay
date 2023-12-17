package com.example.myday.food

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.myday.R
import com.example.myday.databinding.FragmentFoodBinding
import com.google.android.material.tabs.TabLayoutMediator

class FoodFragment: Fragment() {
    private var _binding: FragmentFoodBinding? = null
    private val binding get() = _binding!!
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentFoodBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        setupViewPager()
        setupTabLayoutWithViewPager()
    }

    private fun setupViewPager() {
        val adapter = NutritionViewPagerAdapter(this)
        binding.nutritionVp.adapter = adapter
    }

    private fun setupTabLayoutWithViewPager() {
        val tabTitles = resources.getStringArray(R.array.`when`)
        val tabLayout = binding.nutritionTl
        val viewPager = binding.nutritionVp

        TabLayoutMediator(tabLayout, viewPager) { tab, position ->
            tab.text = tabTitles[position]
        }.attach()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}