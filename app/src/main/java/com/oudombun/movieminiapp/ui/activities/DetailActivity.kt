package com.oudombun.movieminiapp.ui.activities

import android.os.Bundle
import android.view.View
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.TextView
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import coil.load
import com.oudombun.movieminiapp.R
import com.oudombun.movieminiapp.data.response.ResultX
import com.oudombun.movieminiapp.ui.viewmodels.MainViewModel
import com.oudombun.movieminiapp.util.NetworkResult
import dagger.hilt.android.AndroidEntryPoint
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*

@AndroidEntryPoint
class DetailActivity : AppCompatActivity() {
    private val movie: ResultX? by lazy {
        @Suppress("DEPRECATION")
        intent?.getParcelableExtra("result")
    }
    private val mainViewModel: MainViewModel by viewModels()
    private lateinit var toolbar: Toolbar
    private lateinit var movieTitle: TextView
    private lateinit var movieDate: TextView
    private lateinit var moviePoster: ImageView
    private lateinit var progressBar: ProgressBar
    private lateinit var detailOverView: TextView


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (movie == null) {
            finish()
            return
        }
        setContentView(R.layout.activity_detail)
        initView()
        setupDetailData()
    }

    private fun initView() {
        toolbar = findViewById(R.id.toolbar)
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setDisplayShowTitleEnabled(false)
        toolbar.setNavigationOnClickListener {
            onBackPressed()  // Or finish()
        }
        movieTitle = findViewById(R.id.movieTitle)
        movieDate = findViewById(R.id.releaseDate)
        moviePoster = findViewById(R.id.moviePoster)
        detailOverView = findViewById(R.id.movieOverview)
    }

    private fun setupDetailData() {
        val m = movie ?: return
        movieTitle.text = m.title
        movieDate.text = getStringDate(m.release_date)
        moviePoster.load("https://image.tmdb.org/t/p/w300/${m.poster_path}") {
            crossfade(600)
            error(R.drawable.ic_error_placeholder)
        }
        detailOverView.text = m.overview
    }

    private fun requestDetail() {
        movie?.let { mainViewModel.getDetailMovie(it.id.toString()) }
        mainViewModel.detailResponse.observe(this){response->
            when (response) {
                is NetworkResult.Success -> {
                    val movie = response.data!!
                    movieTitle.text = movie.title
                    movieDate.text = getStringDate(movie.release_date)
                    moviePoster.load("https://image.tmdb.org/t/p/w300/${movie.poster_path}") {
                        crossfade(600)
                        error(R.drawable.ic_error_placeholder)
                    }
                    detailOverView.text = movie.overview
                }

                is NetworkResult.Error -> {

                }

                is NetworkResult.Loading -> {

                }
            }

        }
    }

    fun getStringDate(date: String): String {
        //String date_ = date;
        val dateArray = date.split("-".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()
        val sdf = SimpleDateFormat("yyyy-MM-dd")
        try {
            val mDate = sdf.parse(date)
            val timeInMilliseconds = mDate.time
            val dateString = formatDate(timeInMilliseconds).split(" ".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()
            return dateString[0].substring(0,3) + " " + dateString[1] + ", " + dateArray[0]
        } catch (e: ParseException) {
            e.printStackTrace()
        }

        return ""
    }
    fun formatDate(timeInMillis: Long): String {
        val dateFormat = SimpleDateFormat("MMMM dd", Locale.getDefault())
        return dateFormat.format(timeInMillis)
    }
}