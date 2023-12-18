package com.example.myday.food.tabview

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.myday.databinding.SelectedListOneBinding
import com.example.myday.food.Selected

class ListOneDinner: RecyclerView.Adapter<ListOneDinner.DinnerHolder>() {
    var selectedList = mutableListOf<Selected>()
    inner class DinnerHolder(private val binding: SelectedListOneBinding): RecyclerView.ViewHolder(binding.root) {
        fun bind(data: Selected, position: Int) {
            "${position+1}. ${data.name}".also {binding.selectedFoodName.text = it}
            "${data.kcal}kcal".also { binding.selectedFoodKcal.text = it }
            "${data.count}인분".also { binding.selectedFoodCount.text = it }
            "=> ${data.kcal} X ${data.count} = ${data.count?.times(data.kcal!!)}kcal".also { binding.selectedFoodSum.text = it }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DinnerHolder {
        val binding = SelectedListOneBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return DinnerHolder(binding)
    }

    override fun onBindViewHolder(holder: DinnerHolder, position: Int) {
        holder.bind(selectedList[position], position)
    }

    override fun getItemCount(): Int {
        return selectedList.size
    }
}