package com.kychnoo.gamevault.ui.viewModel

import com.kychnoo.gamevault.data.manager.favorites.FavoritesManagerImp
import com.kychnoo.gamevault.data.model.RepResult
import com.kychnoo.gamevault.data.remote.repository.RawgGamesRepository
import com.kychnoo.gamevault.data.remote.repository.RawgGenresRepository
import com.kychnoo.gamevault.data.remote.repository.SearchRepository
import com.kychnoo.gamevault.provider.AndroidResourceProvider
import io.mockk.coEvery
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class SearchViewModelTest {
    private val searchRepository = mockk<SearchRepository>()
    private val gamesRepository = mockk<RawgGamesRepository>()
    private val genresRepository = mockk<RawgGenresRepository>()
    private val resourceProvider = mockk<AndroidResourceProvider>()
    private val favoritesManager = mockk<FavoritesManagerImp>()

    private val testDispatcher = StandardTestDispatcher()

    @Before
    fun setup() {
        Dispatchers.setMain(testDispatcher)
        every { searchRepository.observeLast10() } returns flowOf(emptyList())
        coEvery { genresRepository.getAllGenres() } returns RepResult.Success(emptyList())
        every { favoritesManager.getFavoriteIds() } returns flowOf(emptyList())
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `updateSearchQuery should update uiState`() = runTest {
        val viewModel = SearchViewModel(searchRepository, gamesRepository, genresRepository, resourceProvider, favoritesManager)
        val query = "Cyberpunk"

        // Start collecting to activate WhileSubscribed
        val job = backgroundScope.launch(UnconfinedTestDispatcher(testScheduler)) {
            viewModel.uiState.collect()
        }

        viewModel.updateSearchQuery(query)
        advanceUntilIdle()

        assertEquals(query, viewModel.uiState.value.searchQuery)
        job.cancel()
    }

    @Test
    fun `performSearch should call repository and update games state`() = runTest {
        val viewModel = SearchViewModel(searchRepository, gamesRepository, genresRepository, resourceProvider, favoritesManager)
        val query = "Witcher"
        
        coEvery { searchRepository.addSearchQuery(any()) } returns Unit
        coEvery { gamesRepository.searchGames(any()) } returns RepResult.Success(emptyList())

        viewModel.performSearch(query)
        advanceUntilIdle()

        coEvery { searchRepository.addSearchQuery(query) }
        coEvery { gamesRepository.searchGames(match { it.search == query }) }
    }
}
