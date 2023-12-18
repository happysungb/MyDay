package com.example.myday.food.tabview

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.myday.databinding.SelectedListTwoBinding
import com.example.myday.food.Selected

class ListTwoAdapter: RecyclerView.Adapter<ListTwoAdapter.MySelectedViewHolder>() {
    var selectedList = mutableListOf<Selected>()
    inner class MySelectedViewHolder(private val binding: SelectedListTwoBinding): RecyclerView.ViewHolder(binding.root) {
        fun bind(data: Selected) {
            binding.list2Name.text = data.name
            "${data.carbohydrate}g".also { binding.list2Carbo.text = it }
            "${data.protein}g".also { binding.list2Protein.text = it }
            "${data.fat}g".also { binding.list2Fat.text = it }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MySelectedViewHolder {
        val binding = SelectedListTwoBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return MySelectedViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return selectedList.size
    }

    override fun onBindViewHolder(holder: MySelectedViewHolder, position: Int) {
        holder.bind(selectedList[position])
    }
}