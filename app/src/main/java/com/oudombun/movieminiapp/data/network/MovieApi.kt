package com.oudombun.movieminiapp.data.network

import com.oudombun.movieminiapp.data.response.Genre
import com.oudombun.movieminiapp.data.response.MovieDetail
import com.oudombun.movieminiapp.data.response.MovieResult
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface MovieApi {


    @GET("/3/genre/movie/list")
    suspend fun getGenre(
        @Query("api_key") apiKey: String
    ): Response<Genre>

    @GET("/3/discover/movie")
    suspend fun getPopularMovies(
        @Query("api_key") apiKey: String,
        @Query("with_genres") genre: String?,
    ): Response<MovieResult>

    @GET("/3/movie/top_rated")
    suspend fun getTopRateMovies(
        @Query("api_key") apiKey: String,
        @Query("with_genres") genre: String?
    ): Response<MovieResult>

    @GET("/3/movie/upcoming")
    suspend fun getUpcomingMovies(
        @Query("api_key") apiKey: String,
        @Query("with_genres") genre: String?
    ): Response<MovieResult>


    @GET("/3/movie/{id}")
    suspend fun getMovieDetail(
        @Path("id") id:String,
        @Query("api_key") apiKey: String
    ): Response<MovieDetail>

}