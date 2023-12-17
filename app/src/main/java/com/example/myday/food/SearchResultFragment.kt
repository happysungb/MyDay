package com.example.myday.food

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.myday.databinding.RecyclerFragmentBinding

class SearchResultFragment: Fragment() {
    private var _binding: RecyclerFragmentBinding? = null
    private val binding get() = _binding!!
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _binding = RecyclerFragmentBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val adapter = NutritionAdapter()
        adapter.datas = requireArguments().getParcelableArrayList("resultList")!!
        binding.recyclerFragment.layoutManager  = LinearLayoutManager(activity)
        binding.recyclerFragment.adapter = adapter
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}