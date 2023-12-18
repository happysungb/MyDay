package com.example.myday.food.tabview

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.myday.databinding.SelectedListOneBinding
import com.example.myday.food.Selected

class ListOneAdapter: RecyclerView.Adapter<ListOneAdapter.MySelectedViewHolder>() {
    var selectedList = mutableListOf<Selected>()
    inner class MySelectedViewHolder(private val binding: SelectedListOneBinding): RecyclerView.ViewHolder(binding.root) {
        fun bind(data: Selected) {
            binding.selectedFoodName.text = data.name
            "${data.kcal}kcal".also { binding.selectedFoodKcal.text = it }
            "${data.count}인분".also { binding.selectedFoodCount.text = it }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MySelectedViewHolder {
        val binding = SelectedListOneBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return MySelectedViewHolder(binding)
    }

    override fun onBindViewHolder(holder: MySelectedViewHolder, position: Int) {
        holder.bind(selectedList[position])
    }

    override fun getItemCount(): Int {
        return selectedList.size
    }
}