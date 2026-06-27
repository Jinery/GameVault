package com.kychnoo.gamevault.ui.viewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kychnoo.gamevault.R
import com.kychnoo.gamevault.data.local.entity.SearchHistoryEntity
import com.kychnoo.gamevault.data.model.GameData
import com.kychnoo.gamevault.data.model.RepResult
import com.kychnoo.gamevault.data.model.gameFilters.GameFilters
import com.kychnoo.gamevault.data.model.genres.GenreData
import com.kychnoo.gamevault.data.model.request.GameSearchParameters
import com.kychnoo.gamevault.data.model.ui.UiState
import com.kychnoo.gamevault.data.model.ui.states.SearchUiState
import com.kychnoo.gamevault.data.remote.repository.RawgGamesRepository
import com.kychnoo.gamevault.data.remote.repository.RawgGenresRepository
import com.kychnoo.gamevault.data.remote.repository.SearchRepository
import com.kychnoo.gamevault.provider.AndroidResourceProvider
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class SearchViewModel(
    private val searchRepository: SearchRepository,
    private val gamesRepository: RawgGamesRepository,
    private val genresRepository: RawgGenresRepository,
    private val resourceProvider: AndroidResourceProvider
) : ViewModel() {
    private val _searchQuery = MutableStateFlow("")
    private val _gamesState = MutableStateFlow<UiState<List<GameData>>>(UiState.Success(emptyList()))
    private val _genresState = MutableStateFlow<UiState<List<GenreData>>>(UiState.Loading)
    private val _filtersState = MutableStateFlow(GameFilters())

    val uiState: StateFlow<SearchUiState> = combine(
        _searchQuery,
        searchRepository.observeLast10(),
        _gamesState,
        _genresState,
        _filtersState
    ) { query, history, games, genres, filters ->
        SearchUiState(
            searchQuery = query,
            searchHistory = history,
            games = games,
            availableGenres = genres,
            filters = filters
        )
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = SearchUiState()
    )

    init {
        loadGenres()
    }

    private fun loadGenres() {
        viewModelScope.launch {
            _genresState.update { UiState.Loading }

            val newState = when (val result = genresRepository.getAllGenres()) {
                is RepResult.Success -> UiState.Success(result.data)
                is RepResult.Error -> UiState.Error(result.exception.message ?: resourceProvider.getString(R.string.unknown_error))
            }

            _genresState.update { newState }
        }
    }

    fun updateSearchQuery(newSearchQuery: String) {
        _searchQuery.update { newSearchQuery }
    }

    fun deleteHistoryEntry(entry: SearchHistoryEntity) {
        viewModelScope.launch {
            searchRepository.deleteEntry(entry)
        }
    }

    fun performSearch(query: String) {
        if (query.isBlank()) return

        viewModelScope.launch {
            searchRepository.addSearchQuery(query)
            _gamesState.update { UiState.Loading }

            val currentFilters = _filtersState.value

            val searchParams = GameSearchParameters(
                search = query,
                ordering = currentFilters.ordering,
                genres = if (currentFilters.selectedGenres.isNotEmpty()) {
                    currentFilters.selectedGenres.joinToString(",")
                } else null
            )

            val newState = when (val result = gamesRepository.searchGames(searchParams)) {
                is RepResult.Success<List<GameData>> -> UiState.Success(result.data)
                is RepResult.Error -> UiState.Error(result.exception.message ?: resourceProvider.getString(R.string.unknown_error))
            }

            _gamesState.update { newState }
        }
    }

    fun applyFilters(newFilters: GameFilters) {
        _filtersState.update { newFilters }
        performSearch(_searchQuery.value)
    }
}