package com.oudombun.movieminiapp.data.network

import com.oudombun.movieminiapp.data.response.Genre
import com.oudombun.movieminiapp.data.response.MovieDetail
import com.oudombun.movieminiapp.data.response.MovieResult
import retrofit2.Response
import javax.inject.Inject


class RemoteDataSource @Inject constructor(
    private val moveApi: MovieApi
) {
    suspend fun getGenre(apiKey:String): Response<Genre> {
        return moveApi.getGenre(apiKey)
    }

    suspend fun getMovies(apiKey:String,genreId:String?=""): Response<MovieResult> {
        return moveApi.getPopularMovies(apiKey,genreId)
    }

    suspend fun getTopRateMovies(apiKey:String,genreId:String?=""): Response<MovieResult> {
        return moveApi.getTopRateMovies(apiKey,genreId)
    }

    suspend fun getUpComingMovies(apiKey:String,genreId:String?=""): Response<MovieResult> {
        return moveApi.getUpcomingMovies(apiKey,genreId)
    }

    suspend fun getMovieDetail(apiKey:String,id:String): Response<MovieDetail> {
        return moveApi.getMovieDetail(apiKey,id)
    }

}