package com.oudombun.movieminiapp.ui.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.oudombun.movieminiapp.R
import com.oudombun.movieminiapp.data.response.Genre
import com.oudombun.movieminiapp.data.response.GenreX
import com.oudombun.movieminiapp.databinding.ItemGenreBinding
import com.oudombun.movieminiapp.util.RvDiffUtil
import kotlinx.android.synthetic.main.item_genre.view.*

class GenreAdapter(private val context: Context) : RecyclerView.Adapter<GenreAdapter.MyViewHolder>() {

    private var genres = emptyList<GenreX>()
    private var listener: GenreClickListener? = null
    private var selectedGenre: GenreX? = null

    class MyViewHolder(private val binding: ItemGenreBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(result: GenreX){
            binding.result = result
            binding.executePendingBindings()
        }

        companion object {
            fun from(parent: ViewGroup): MyViewHolder {
                val layoutInflater = LayoutInflater.from(parent.context)
                val binding = ItemGenreBinding.inflate(layoutInflater, parent, false)
                return MyViewHolder(binding)
            }
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        return MyViewHolder.from(parent)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val current = genres[position]
        holder.itemView.txtGenre.isSelected = current == selectedGenre

        if (current == selectedGenre) {
            holder.itemView.txtGenre.setTextColor(ContextCompat.getColor(context, R.color.yellow))
        } else {
            holder.itemView.txtGenre.setTextColor(ContextCompat.getColor(context, R.color.lightMediumGray))
        }

        holder.itemView.setOnClickListener {
            selectedGenre = current
            listener?.onGenreClicked(current)
            notifyDataSetChanged()
        }

        holder.bind(current)
    }

    override fun getItemCount(): Int {
        return genres.size
    }

    fun setData(newData: Genre){
        val recipesDiffUtil = RvDiffUtil(genres, newData.genres)
        val diffUtilResult = DiffUtil.calculateDiff(recipesDiffUtil)
        genres = newData.genres
        diffUtilResult.dispatchUpdatesTo(this)
    }

    fun setGenreClickListener(listener: GenreClickListener) {
        this.listener = listener
    }

    interface GenreClickListener {
        fun onGenreClicked(genre: GenreX)
    }
}