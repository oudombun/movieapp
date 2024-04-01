package com.oudombun.movieminiapp.data.response

data class MovieResult(
    val page: Int,
    val results: List<ResultX>,
    val total_pages: Int,
    val total_results: Int
)