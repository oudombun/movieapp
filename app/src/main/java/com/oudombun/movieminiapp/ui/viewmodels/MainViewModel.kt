package com.oudombun.movieminiapp.ui.viewmodels

import android.app.Application
import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.oudombun.movieminiapp.BuildConfig
import com.oudombun.movieminiapp.data.Repository
import com.oudombun.movieminiapp.data.response.Genre
import com.oudombun.movieminiapp.data.response.MovieDetail
import com.oudombun.movieminiapp.data.response.MovieResult
import com.oudombun.movieminiapp.util.NetworkResult
import kotlinx.coroutines.launch
import retrofit2.Response
import java.lang.Exception

class MainViewModel @ViewModelInject constructor(
    private val repository: Repository,
    application: Application
) : AndroidViewModel(application) {
    val apiKey = BuildConfig.API_KEY

    /** RETROFIT */
    var popularResponse: MutableLiveData<NetworkResult<MovieResult>> = MutableLiveData()
    var genreResponse: MutableLiveData<NetworkResult<Genre>> = MutableLiveData()
    var recommendResponse: MutableLiveData<NetworkResult<MovieResult>> = MutableLiveData()
    var upcomingResponse: MutableLiveData<NetworkResult<MovieResult>> = MutableLiveData()
    var detailResponse: MutableLiveData<NetworkResult<MovieDetail>> = MutableLiveData()


    fun getPopularMovies(genreId:String?="") = viewModelScope.launch {
        getPopularSafeCall(genreId)
    }

    private suspend fun getPopularSafeCall(genreId:String?="") {
        popularResponse.value = NetworkResult.Loading()
        try {
            val response = repository.remote.getMovies(apiKey,genreId)
            if(response.isSuccessful){
                val movieResult = response.body()!!
                popularResponse.value = NetworkResult.Success(movieResult)
            }else{
                popularResponse.value = handleMoviesResponse(response)
            }
        } catch (e: Exception) {
            popularResponse.value = NetworkResult.Error(e.message)
        }
    }

    fun getTopRateMovies(genreId:String?="") = viewModelScope.launch {
        getTopRateSafeCall(genreId)
    }

    private suspend fun getTopRateSafeCall(genreId:String?="") {
        recommendResponse.value = NetworkResult.Loading()
        try {
            val response = repository.remote.getTopRateMovies(apiKey,genreId)
            if(response.isSuccessful){
                val movieResult = response.body()!!
                recommendResponse.value = NetworkResult.Success(movieResult)
            }else{
                recommendResponse.value = handleMoviesResponse(response)
            }
        } catch (e: Exception) {
            recommendResponse.value = NetworkResult.Error(e.message)
        }
    }

    fun getUpComingMovies(genreId:String?="") = viewModelScope.launch {
        getUpComingSafeCall(genreId)
    }

    private suspend fun getUpComingSafeCall(genreId:String?="") {
        upcomingResponse.value = NetworkResult.Loading()
        try {
            val response = repository.remote.getUpComingMovies(apiKey,genreId)
            if(response.isSuccessful){
                val movieResult = response.body()!!
                upcomingResponse.value = NetworkResult.Success(movieResult)
            }else{
                upcomingResponse.value = handleMoviesResponse(response)
            }
        } catch (e: Exception) {
            upcomingResponse.value = NetworkResult.Error(e.message)
        }
    }

    fun getDetailMovie(id:String) = viewModelScope.launch {
        getDetailMovieSafeCall(id)
    }

    private suspend fun getDetailMovieSafeCall(id:String) {
        detailResponse.value = NetworkResult.Loading()
        try {
            val response = repository.remote.getMovieDetail(apiKey,id)
            if(response.isSuccessful){
                val movieResult = response.body()!!
                detailResponse.value = NetworkResult.Success(movieResult)
            }else{
                detailResponse.value =  NetworkResult.Error(response.message())
            }
        } catch (e: Exception) {
            detailResponse.value = NetworkResult.Error(e.message)
        }
    }

    fun getGenre() = viewModelScope.launch {
        getGenreSafeCall()
    }

    private suspend fun getGenreSafeCall() {
        genreResponse.value = NetworkResult.Loading()
        try {
            val response = repository.remote.getGenre(apiKey)
            if(response.isSuccessful){
                val result = response.body()!!
                genreResponse.value = NetworkResult.Success(result)
            }else{
                genreResponse.value = handleGenreResponse(response)
            }
        } catch (e: Exception) {
            genreResponse.value = NetworkResult.Error(e.message)
        }
    }



    private fun handleMoviesResponse(response: Response<MovieResult>): NetworkResult<MovieResult> {
        return when {
            response.message().toString().contains("timeout") -> {
                NetworkResult.Error("Timeout")
            }

            response.body()!!.results.isEmpty() -> {
                NetworkResult.Error("Movies not found.")
            }

            else -> {
                NetworkResult.Error(response.message())
            }
        }
    }

    private fun handleGenreResponse(response: Response<Genre>): NetworkResult<Genre> {
        return when {
            response.message().toString().contains("timeout") -> {
                NetworkResult.Error("Timeout")
            }

            response.body()!!.genres.isEmpty() -> {
                NetworkResult.Error("Movies not found.")
            }

            else -> {
                NetworkResult.Error(response.message())
            }
        }
    }

}