package com.example.myday.food.tabview

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.myday.databinding.SelectedListTwoBinding
import com.example.myday.food.Selected

class ListTwoDinner: RecyclerView.Adapter<ListTwoDinner.ListTwoDinnerHolder>() {
    var selectedList = mutableListOf<Selected>()
    inner class ListTwoDinnerHolder(private val binding: SelectedListTwoBinding): RecyclerView.ViewHolder(binding.root) {
        fun bind(data: Selected, position: Int) {
            "${position+1}. ${data.name}".also {binding.list2Name.text = it}
            "${data.carbohydrate}g".also { binding.list2Carbo.text = it }
            "${data.protein}g".also { binding.list2Protein.text = it }
            "${data.fat}g".also { binding.list2Fat.text = it }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ListTwoDinnerHolder {
        val binding = SelectedListTwoBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ListTwoDinnerHolder(binding)
    }

    override fun getItemCount(): Int {
        return selectedList.size
    }

    override fun onBindViewHolder(holder: ListTwoDinnerHolder, position: Int) {
        holder.bind(selectedList[position], position)
    }
}