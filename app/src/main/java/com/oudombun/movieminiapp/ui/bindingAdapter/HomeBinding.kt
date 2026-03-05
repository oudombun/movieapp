package com.oudombun.movieminiapp.ui.bindingAdapter

import android.widget.ImageView
import androidx.databinding.BindingAdapter
import coil.load
import com.google.android.material.card.MaterialCardView
import com.oudombun.movieminiapp.R
import com.oudombun.movieminiapp.data.response.ResultX

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
            layout.setOnClickListener { /* Navigation handled by Compose flow */ }
        }
    }
}