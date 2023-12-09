package com.example.myday.food

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.myday.databinding.SelectedListBinding

class SelectedAdapter: RecyclerView.Adapter<SelectedAdapter.MySelectedViewHolder>() {
    var selectedList = mutableListOf<Selected>()
    inner class MySelectedViewHolder(private val binding: SelectedListBinding): RecyclerView.ViewHolder(binding.root) {
        fun bind(data: Selected) {
            binding.selectedFoodName.text = data.name
            "${data.kcal}kcal".also { binding.selectedFoodKcal.text = it }
            "${data.count}인분".also { binding.selectedFoodCount.text = it }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MySelectedViewHolder {
        val binding = SelectedListBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return MySelectedViewHolder(binding)
    }

    override fun onBindViewHolder(holder: MySelectedViewHolder, position: Int) {
        holder.bind(selectedList[position])
    }

    override fun getItemCount(): Int {
        return selectedList.size
    }
}