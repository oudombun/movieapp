package com.oudombun.movieminiapp.ui.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.oudombun.movieminiapp.data.response.GenreX
//import com.oudombun.movieminiapp.ui.adapter.PopularAdapter
import com.oudombun.movieminiapp.databinding.FragmentHomeBinding
import com.oudombun.movieminiapp.ui.adapter.GenreAdapter
import com.oudombun.movieminiapp.ui.viewmodels.MainViewModel
import com.oudombun.movieminiapp.ui.adapter.PopularAdapter
import com.oudombun.movieminiapp.util.NetworkResult
import dagger.hilt.android.AndroidEntryPoint



@AndroidEntryPoint
class HomeFragment : Fragment(), GenreAdapter.GenreClickListener {
    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!
    private lateinit var mainViewModel: MainViewModel
    private val mAdapter by lazy { PopularAdapter() }
    private val rAdapter by lazy { PopularAdapter() }
    private val uAdapter by lazy { PopularAdapter() }
    lateinit var gAdapter: GenreAdapter
    var genreId =""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mainViewModel = ViewModelProvider(requireActivity()).get(MainViewModel::class.java)
        gAdapter = GenreAdapter(requireContext())
        gAdapter.setGenreClickListener(this)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        binding.mainViewModel = mainViewModel
        setupRecyclerView()
        requestGenre()
        requestPopularMovie()
        requestTopRatedMovie()
        requestUpComingMovie()

        return binding.root
    }

    private fun requestGenre() {
        mainViewModel.getGenre()
        mainViewModel.genreResponse.observe(viewLifecycleOwner) { response ->
            when (response) {
                is NetworkResult.Success -> {
                    binding.progressBar1.visibility=View.GONE
                    response.data?.let {
                        gAdapter.setData(it)
                    }
                }

                is NetworkResult.Error -> {
                    binding.progressBar1.visibility=View.GONE
                    Toast.makeText(
                        requireContext(),
                        response.message.toString(),
                        Toast.LENGTH_SHORT
                    ).show()
                }

                is NetworkResult.Loading -> {
                    binding.progressBar1.visibility=View.VISIBLE
                }
            }
        }
    }

    private fun requestPopularMovie(genreId:String?="") {
        mainViewModel.getPopularMovies(genreId)
        mainViewModel.popularResponse.observe(viewLifecycleOwner) { response ->
            when (response) {
                is NetworkResult.Success -> {
                    binding.txtPopular.visibility=View.VISIBLE
                    binding.progressBar2.visibility=View.GONE
                    response.data?.let {
                        mAdapter.setData(it)
                    }
                }

                is NetworkResult.Error -> {
                    binding.progressBar2.visibility=View.GONE
                    binding.txtPopular.visibility=View.GONE
                    Toast.makeText(
                        requireContext(),
                        response.message.toString(),
                        Toast.LENGTH_SHORT
                    ).show()
                }

                is NetworkResult.Loading -> {
                    binding.progressBar2.visibility=View.VISIBLE
                }
            }
        }
    }

    private fun requestTopRatedMovie(genreId:String?="") {
        mainViewModel.getTopRateMovies(genreId)
        mainViewModel.recommendResponse.observe(viewLifecycleOwner) { response ->
            when (response) {
                is NetworkResult.Success -> {
                    binding.txtRecommend.visibility=View.VISIBLE
                    binding.progressBar3.visibility=View.GONE
                    response.data?.let {
                        rAdapter.setData(it)
                    }
                }

                is NetworkResult.Error -> {
                    binding.progressBar3.visibility=View.GONE
                    binding.txtRecommend.visibility=View.GONE
                    Toast.makeText(
                        requireContext(),
                        response.message.toString(),
                        Toast.LENGTH_SHORT
                    ).show()
                }

                is NetworkResult.Loading -> {
                    binding.progressBar3.visibility=View.VISIBLE
                }
            }
        }
    }

    private fun requestUpComingMovie(genreId:String?="") {
        mainViewModel.getUpComingMovies(genreId)
        mainViewModel.upcomingResponse.observe(viewLifecycleOwner) { response ->
            when (response) {
                is NetworkResult.Success -> {
                    binding.txtUpcoming.visibility=View.VISIBLE
                    binding.progressBar4.visibility=View.GONE
                    response.data?.let {
                        uAdapter.setData(it)
                    }
                }

                is NetworkResult.Error -> {
                    binding.progressBar4.visibility=View.GONE
                    binding.txtUpcoming.visibility=View.GONE
                    Toast.makeText(
                        requireContext(),
                        response.message.toString(),
                        Toast.LENGTH_SHORT
                    ).show()
                }

                is NetworkResult.Loading -> {
                    binding.progressBar4.visibility=View.VISIBLE
                }
            }
        }
    }

    private fun setupRecyclerView() {
        binding.rvPopular.adapter = mAdapter
        binding.rvPopular.layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)

        binding.rvRecommend.adapter = rAdapter
        binding.rvRecommend.layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)

        binding.rvUpComing.adapter = uAdapter
        binding.rvUpComing.layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)

        binding.recyclerView1.adapter = gAdapter
        binding.recyclerView1.layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)

    }

    override fun onGenreClicked(genre: GenreX) {
        genreId = genre.id.toString()
        requestPopularMovie(genreId)
        requestTopRatedMovie(genreId)
        requestUpComingMovie(genreId)
    }

}