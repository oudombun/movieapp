@file:OptIn(ExperimentalMaterial3Api::class)

package com.oudombun.movieminiapp.ui.compose

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Download
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilterChip
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.AsyncImage
import com.oudombun.movieminiapp.data.response.Genre
import com.oudombun.movieminiapp.data.response.GenreX
import com.oudombun.movieminiapp.data.response.ResultX
import com.oudombun.movieminiapp.ui.viewmodels.MainViewModel
import com.oudombun.movieminiapp.util.NetworkResult
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.Locale

private val AppBackground = Color(0xFF151618)
private val AppSurface = Color(0xFF222632)
private val AppAccent = Color(0xFFFFC114)
private val AppTextMuted = Color(0xFFB8BCC2)

private enum class MainTab(val label: String) {
    Home("Home"),
    Search("Search"),
    Download("Download"),
    Profile("Profile")
}

private const val ROUTE_HOME = "home"
private const val ROUTE_DETAIL = "detail"

@Composable
fun AppNav() {
    val navController = rememberNavController()
    var selectedMovie by remember { mutableStateOf<ResultX?>(null) }
    val viewModel: MainViewModel = hiltViewModel()

    NavHost(
        navController = navController,
        startDestination = ROUTE_HOME
    ) {
        composable(ROUTE_HOME) {
            MovieApp(
                viewModel = viewModel,
                onMovieSelected = { movie ->
                    selectedMovie = movie
                    navController.navigate(ROUTE_DETAIL)
                }
            )
        }
        composable(ROUTE_DETAIL) {
            selectedMovie?.let { movie ->
                MovieDetailScreen(
                    movie = movie,
                    onBack = {
                        navController.popBackStack()
                        selectedMovie = null
                    }
                )
            }
        }
    }
}

@Composable
fun MovieApp(viewModel: MainViewModel, onMovieSelected: (ResultX) -> Unit) {
    var selectedTab by rememberSaveable { mutableStateOf(MainTab.Home) }

    MovieTheme {
        Scaffold(
            containerColor = AppBackground,
            bottomBar = {
                NavigationBar(
                    containerColor = AppSurface,
                    tonalElevation = 0.dp
                ) {
                    MainTab.values().forEach { tab ->
                        val selected = selectedTab == tab
                        NavigationBarItem(
                            selected = selected,
                            onClick = { selectedTab = tab },
                            icon = { TabIcon(tab, selected = selected) },
                            label = {
                                Text(
                                    tab.label,
                                    color = if (selected) AppAccent else AppTextMuted
                                )
                            }
                        )
                    }
                }
            }
        ) { innerPadding ->
            when (selectedTab) {
                MainTab.Home -> HomeRoute(
                    viewModel = viewModel,
                    onMovieSelected = onMovieSelected,
                    modifier = Modifier.padding(innerPadding)
                )

                MainTab.Search,
                MainTab.Download,
                MainTab.Profile -> PlaceholderTab(
                    title = selectedTab.label,
                    modifier = Modifier.padding(innerPadding)
                )
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MovieDetailScreen(movie: ResultX, onBack: () -> Unit) {
    MovieTheme {
        Scaffold(
            containerColor = AppBackground
        ) { innerPadding ->
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .background(AppBackground)
                    .padding(innerPadding),
                contentPadding = PaddingValues(20.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                item {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(bottom = 8.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        IconButton(onClick = onBack) {
                            Icon(
                                imageVector = Icons.Filled.ArrowBack,
                                contentDescription = "Back",
                                tint = Color.White
                            )
                        }
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(
                            text = movie.title,
                            style = MaterialTheme.typography.titleMedium,
                            color = Color.White,
                            fontWeight = FontWeight.SemiBold,
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis
                        )
                    }
                }
                item {
                    AsyncImage(
                        model = imageUrl(movie.poster_path),
                        contentDescription = movie.title,
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(280.dp)
                            .clip(RoundedCornerShape(24.dp)),
                        contentScale = ContentScale.Crop
                    )
                }
                item {
                    Text(
                        text = movie.title,
                        style = MaterialTheme.typography.headlineSmall,
                        color = Color.White,
                        fontWeight = FontWeight.Bold
                    )
                }
                item {
                    Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                        MovieMetaChip(text = "Release ${formatMovieDate(movie.release_date)}")
                        MovieMetaChip(text = "Rating %.1f".format(movie.vote_average))
                    }
                }
                item {
                    Text(
                        text = movie.overview.ifBlank { "No overview available." },
                        style = MaterialTheme.typography.bodyLarge,
                        color = AppTextMuted
                    )
                }
            }
        }
    }
}

@Composable
private fun MovieTheme(content: @Composable () -> Unit) {
    MaterialTheme(content = content)
}

@Composable
private fun TabIcon(tab: MainTab, selected: Boolean) {
    val icon = when (tab) {
        MainTab.Home -> Icons.Default.Home
        MainTab.Search -> Icons.Default.Search
        MainTab.Download -> Icons.Default.Download
        MainTab.Profile -> Icons.Default.Person
    }
    Icon(
        imageVector = icon,
        contentDescription = tab.label,
        tint = if (selected) AppAccent else AppTextMuted
    )
}

@Composable
private fun HomeRoute(
    viewModel: MainViewModel,
    onMovieSelected: (ResultX) -> Unit,
    modifier: Modifier = Modifier
) {
    val genreState by viewModel.genreResponse.observeAsState(NetworkResult.Loading())
    val popularState by viewModel.popularResponse.observeAsState(NetworkResult.Loading())
    val topRatedState by viewModel.recommendResponse.observeAsState(NetworkResult.Loading())
    val upcomingState by viewModel.upcomingResponse.observeAsState(NetworkResult.Loading())
    var selectedGenreId by rememberSaveable { mutableStateOf("") }

    LaunchedEffect(Unit) {
        viewModel.getGenre()
    }

    LaunchedEffect(selectedGenreId) {
        viewModel.getPopularMovies(selectedGenreId)
        viewModel.getTopRateMovies(selectedGenreId)
        viewModel.getUpComingMovies(selectedGenreId)
    }

    HomeScreen(
        genreState = genreState,
        popularState = popularState,
        topRatedState = topRatedState,
        upcomingState = upcomingState,
        selectedGenreId = selectedGenreId,
        onAllSelected = { selectedGenreId = "" },
        onGenreSelected = { selectedGenreId = it.id.toString() },
        onMovieSelected = onMovieSelected,
        modifier = modifier
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun HomeScreen(
    genreState: NetworkResult<Genre>,
    popularState: NetworkResult<com.oudombun.movieminiapp.data.response.MovieResult>,
    topRatedState: NetworkResult<com.oudombun.movieminiapp.data.response.MovieResult>,
    upcomingState: NetworkResult<com.oudombun.movieminiapp.data.response.MovieResult>,
    selectedGenreId: String,
    onAllSelected: () -> Unit,
    onGenreSelected: (GenreX) -> Unit,
    onMovieSelected: (ResultX) -> Unit,
    modifier: Modifier = Modifier
) {
    var query by rememberSaveable { mutableStateOf("") }

    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .background(AppBackground),
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(18.dp)
    ) {
        item {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                OutlinedTextField(
                    value = query,
                    onValueChange = { query = it },
                    modifier = Modifier.weight(1f),
                    singleLine = true,
                    placeholder = {
                        Text(
                            "Search for movies, series and more",
                            color = AppTextMuted
                        )
                    },
                    leadingIcon = {
                        Icon(
                            imageVector = Icons.Default.Search,
                            contentDescription = null,
                            tint = AppTextMuted
                        )
                    },
                    shape = RoundedCornerShape(24.dp),
                    colors = TextFieldDefaults.outlinedTextFieldColors(
                        focusedBorderColor = AppSurface,
                        unfocusedBorderColor = AppSurface,
                        cursorColor = AppAccent,
                        textColor = Color.White,
                        placeholderColor = AppTextMuted
                    )
                )
                IconButton(onClick = { /* Profile - stub */ }) {
                    Icon(
                        imageVector = Icons.Default.Person,
                        contentDescription = "Profile",
                        tint = Color.White
                    )
                }
            }
        }
        item {
            GenreSection(
                state = genreState,
                selectedGenreId = selectedGenreId,
                onAllSelected = onAllSelected,
                onGenreSelected = onGenreSelected
            )
        }
        item {
            MovieSection(
                title = "Featured Series",
                state = popularState,
                onMovieSelected = onMovieSelected
            )
        }
        item {
            MovieSection(
                title = "Recommended For You",
                state = topRatedState,
                onMovieSelected = onMovieSelected
            )
        }
        item {
            MovieSection(
                title = "Just Added",
                state = upcomingState,
                onMovieSelected = onMovieSelected
            )
        }
    }
}

@Composable
private fun GenreSection(
    state: NetworkResult<Genre>,
    selectedGenreId: String,
    onAllSelected: () -> Unit,
    onGenreSelected: (GenreX) -> Unit
) {
    Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
        when (state) {
            is NetworkResult.Loading -> SkeletonGenreRow()
            is NetworkResult.Error -> ErrorText(state.message)
            is NetworkResult.Success -> {
                val genres = state.data?.genres.orEmpty()
                LazyRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    item {
                        FilterChip(
                            selected = selectedGenreId == "",
                            onClick = onAllSelected,
                            label = { Text("All") }
                        )
                    }
                    items(genres) { genre ->
                        FilterChip(
                            selected = selectedGenreId == genre.id.toString(),
                            onClick = { onGenreSelected(genre) },
                            label = { Text(genre.name) }
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun MovieSection(
    title: String,
    state: NetworkResult<com.oudombun.movieminiapp.data.response.MovieResult>,
    onMovieSelected: (ResultX) -> Unit
) {
    Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
        Text(
            text = title,
            style = MaterialTheme.typography.titleMedium,
            color = AppAccent,
            fontWeight = FontWeight.SemiBold
        )
        when (state) {
            is NetworkResult.Loading -> SkeletonMovieRow()
            is NetworkResult.Error -> ErrorText(state.message)
            is NetworkResult.Success -> {
                val movies = state.data?.results.orEmpty()
                if (movies.isEmpty()) {
                    ErrorText("No movies found.")
                } else {
                    LazyRow(horizontalArrangement = Arrangement.spacedBy(14.dp)) {
                        items(movies, key = { it.id }) { movie ->
                            MovieCard(movie = movie, onClick = { onMovieSelected(movie) })
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun MovieCard(movie: ResultX, onClick: () -> Unit) {
    Surface(
        modifier = Modifier
            .width(170.dp)
            .clickable(onClick = onClick),
        color = AppSurface,
        shape = RoundedCornerShape(20.dp)
    ) {
        Column(modifier = Modifier.padding(12.dp)) {
            AsyncImage(
                model = imageUrl(movie.poster_path),
                contentDescription = movie.title,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(220.dp)
                    .clip(RoundedCornerShape(16.dp)),
                contentScale = ContentScale.Crop
            )
            Spacer(modifier = Modifier.height(10.dp))
            Text(
                text = movie.title,
                color = Color.White,
                style = MaterialTheme.typography.titleSmall,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = formatMovieDate(movie.release_date),
                color = AppTextMuted,
                style = MaterialTheme.typography.bodySmall
            )
            Spacer(modifier = Modifier.height(6.dp))
            Row(verticalAlignment = Alignment.CenterVertically) {
                Box(
                    modifier = Modifier
                        .size(8.dp)
                        .clip(RoundedCornerShape(99.dp))
                        .background(AppAccent)
                )
                Spacer(modifier = Modifier.width(6.dp))
                Text(
                    text = "%.1f".format(movie.vote_average),
                    color = AppAccent,
                    style = MaterialTheme.typography.labelLarge
                )
            }
        }
    }
}

@Composable
private fun MovieMetaChip(text: String) {
    Surface(
        color = AppSurface,
        shape = RoundedCornerShape(999.dp)
    ) {
        Text(
            text = text,
            modifier = Modifier.padding(horizontal = 14.dp, vertical = 8.dp),
            color = Color.White,
            style = MaterialTheme.typography.labelLarge
        )
    }
}

@Composable
private fun PlaceholderTab(title: String, modifier: Modifier = Modifier) {
    Box(
        modifier = modifier
            .fillMaxSize()
            .background(AppBackground),
        contentAlignment = Alignment.Center
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Text(
                text = title,
                style = MaterialTheme.typography.headlineMedium,
                color = Color.White,
                fontWeight = FontWeight.Bold
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = "This tab is ready for the next Compose screen.",
                color = AppTextMuted
            )
        }
    }
}

private val SkeletonColor = Color(0xFF2A2E3A)

@Composable
private fun SkeletonBox(
    modifier: Modifier,
    shape: RoundedCornerShape = RoundedCornerShape(8.dp)
) {
    Box(
        modifier = modifier
            .clip(shape)
            .background(SkeletonColor)
    )
}

@Composable
private fun SkeletonMovieCard() {
    Surface(
        modifier = Modifier.width(170.dp),
        color = AppSurface,
        shape = RoundedCornerShape(20.dp)
    ) {
        Column(modifier = Modifier.padding(12.dp)) {
            SkeletonBox(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(220.dp),
                shape = RoundedCornerShape(12.dp)
            )
            Spacer(modifier = Modifier.height(10.dp))
            SkeletonBox(modifier = Modifier.fillMaxWidth(0.85f).height(16.dp))
            Spacer(modifier = Modifier.height(4.dp))
            SkeletonBox(modifier = Modifier.fillMaxWidth(0.5f).height(12.dp))
            Spacer(modifier = Modifier.height(6.dp))
            SkeletonBox(modifier = Modifier.width(40.dp).height(12.dp))
        }
    }
}

@Composable
private fun SkeletonMovieRow() {
    LazyRow(horizontalArrangement = Arrangement.spacedBy(14.dp)) {
        items(4) {
            SkeletonMovieCard()
        }
    }
}

@Composable
private fun SkeletonGenreRow() {
    Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
        repeat(5) {
            SkeletonBox(
                modifier = Modifier
                    .height(32.dp)
                    .width((72 + it * 20).dp),
                shape = RoundedCornerShape(999.dp)
            )
        }
    }
}

@Composable
private fun ErrorText(message: String?) {
    Text(
        text = message ?: "Something went wrong.",
        color = AppTextMuted,
        style = MaterialTheme.typography.bodyMedium
    )
}

private fun imageUrl(path: String): String = "https://image.tmdb.org/t/p/w300/$path"

private fun formatMovieDate(date: String): String {
    return try {
        val parsed = SimpleDateFormat("yyyy-MM-dd", Locale.US).parse(date)
        if (parsed == null) {
            date
        } else {
            SimpleDateFormat("MMM dd, yyyy", Locale.getDefault()).format(parsed)
        }
    } catch (_: ParseException) {
        date
    }
}
