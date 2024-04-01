package com.oudombun.movieminiapp.ui.bindingAdapter

import android.util.Log
import android.widget.ImageView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.databinding.BindingAdapter
import androidx.navigation.findNavController
import coil.load
import com.google.android.material.card.MaterialCardView
import com.oudombun.movieminiapp.R
import com.oudombun.movieminiapp.data.response.MovieResult
import com.oudombun.movieminiapp.data.response.ResultX
import com.oudombun.movieminiapp.ui.fragment.HomeFragmentDirections
import java.lang.Exception


class HomeBinding {
    companion object {
        @BindingAdapter("loadImageFromUrl")
        @JvmStatic
        fun loadImageFromUrl(imageView: ImageView, imageUrl: String) {
            imageView.load("https://image.tmdb.org/t/p/w300/$imageUrl") {
                crossfade(600)
                error(R.drawable.ic_error_placeholder)
            }
        }

        @BindingAdapter("onItemClickListener")
        @JvmStatic
        fun onItemClickListener(layout: MaterialCardView, result: ResultX) {
            layout.setOnClickListener {
                try {
                    val action =
                        HomeFragmentDirections.actionHomeFragmentToDetailActivity(result)
                    layout.findNavController().navigate(action)
                } catch (e: Exception) {
                    Log.d("exception", e.toString())
                }
            }
        }
    }

}