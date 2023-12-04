package com.example.myday

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.myday.databinding.FoodListBinding

class NutritionAdapter: RecyclerView.Adapter<NutritionAdapter.MyNutritionViewHolder>() {
    var datas = mutableListOf<Row>()
    inner class MyNutritionViewHolder(private val binding: FoodListBinding): RecyclerView.ViewHolder(binding.root) {
        fun bind(data: Row) {
            binding.foodName.text = data.DESC_KOR
            binding.foodKcal.text = data.NUTR_CONT1
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyNutritionViewHolder {
        val binding = FoodListBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return MyNutritionViewHolder(binding)
    }

    override fun onBindViewHolder(holder: MyNutritionViewHolder, position: Int) {
        holder.bind(datas[position])
    }

    override fun getItemCount(): Int {
        return datas.size
    }
}