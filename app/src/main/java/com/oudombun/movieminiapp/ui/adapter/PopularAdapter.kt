package com.oudombun.movieminiapp.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.oudombun.movieminiapp.data.response.MovieResult
import com.oudombun.movieminiapp.data.response.ResultX
import com.oudombun.movieminiapp.databinding.ItemPopularBinding
import com.oudombun.movieminiapp.util.RvDiffUtil

class PopularAdapter : RecyclerView.Adapter<PopularAdapter.MyViewHolder>() {

    private var movies = emptyList<ResultX>()

    class MyViewHolder(private val binding: ItemPopularBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(result: ResultX){
            binding.result = result
            binding.executePendingBindings()
        }

        companion object {
            fun from(parent: ViewGroup): MyViewHolder {
                val layoutInflater = LayoutInflater.from(parent.context)
                val binding = ItemPopularBinding.inflate(layoutInflater, parent, false)
                return MyViewHolder(binding)
            }
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        return MyViewHolder.from(parent)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val currentRecipe = movies[position]
        holder.bind(currentRecipe)
    }

    override fun getItemCount(): Int {
        return movies.size
    }

    fun setData(newData: MovieResult){
        val recipesDiffUtil = RvDiffUtil(movies, newData.results)
        val diffUtilResult = DiffUtil.calculateDiff(recipesDiffUtil)
        movies = newData.results
        diffUtilResult.dispatchUpdatesTo(this)
    }
}