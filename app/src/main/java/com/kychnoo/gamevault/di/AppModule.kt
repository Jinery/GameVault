package com.kychnoo.gamevault.di

import androidx.room.Room
import com.kychnoo.gamevault.data.local.AppDatabase
import com.kychnoo.gamevault.data.manager.favorites.FavoritesManager
import com.kychnoo.gamevault.data.manager.favorites.FavoritesManagerImp
import com.kychnoo.gamevault.data.manager.snackbar.SnackbarManager
import com.kychnoo.gamevault.data.manager.snackbar.SnackbarManagerImpl
import com.kychnoo.gamevault.data.remote.repository.FavoriteGamesRepository
import com.kychnoo.gamevault.data.remote.repository.RawgDetailGamesRepository
import com.kychnoo.gamevault.data.remote.repository.RawgDevelopmentTeamsRepository
import com.kychnoo.gamevault.data.remote.repository.RawgGamesRepository
import com.kychnoo.gamevault.data.remote.repository.RawgGenresRepository
import com.kychnoo.gamevault.data.remote.repository.RawgScreenshotsRepository
import com.kychnoo.gamevault.data.remote.repository.SearchRepository
import com.kychnoo.gamevault.network.RetrofitClient
import com.kychnoo.gamevault.provider.AndroidResourceProvider
import com.kychnoo.gamevault.ui.viewModel.FavoritesViewModel
import com.kychnoo.gamevault.ui.viewModel.GameDetailViewModel
import com.kychnoo.gamevault.ui.viewModel.MainViewModel
import com.kychnoo.gamevault.ui.viewModel.SearchViewModel
import org.koin.android.ext.koin.androidContext
import org.koin.core.module.dsl.viewModel
import org.koin.dsl.module

val appModule = module {
    single { RetrofitClient.api }

    single { AndroidResourceProvider(androidContext()) }

    single {
        Room.databaseBuilder(
            androidContext(),
            AppDatabase::class.java,
            "game_vault_database"
        )
        .addMigrations(AppDatabase.MIGRATION_1_2)
        .build()
    }

    single { get<AppDatabase>().searchHistoryDao() }
    single { get<AppDatabase>().favoritesDao() }

    single { RawgDetailGamesRepository(get(), get()) }
    single { RawgGamesRepository(get(), get()) }
    single { RawgScreenshotsRepository(get(), get()) }
    single { RawgDevelopmentTeamsRepository(get(), get()) }
    single { SearchRepository(get()) }
    single { RawgGenresRepository(get(), get()) }
    single { FavoriteGamesRepository(get(), get()) }

    single<FavoritesManager> { FavoritesManagerImp(get()) }
    single<SnackbarManager> { SnackbarManagerImpl() }

    viewModel { MainViewModel(get(), get(), get()) }
    viewModel { GameDetailViewModel(get(), get(), get(), get(), get(), get()) }
    viewModel { SearchViewModel(get(), get(), get(), get(), get()) }
    viewModel { FavoritesViewModel(get(), get(), get(), get()) }
}