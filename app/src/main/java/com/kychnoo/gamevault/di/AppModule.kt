package com.kychnoo.gamevault.di

import com.kychnoo.gamevault.data.remote.repository.RawgGamesRepository
import com.kychnoo.gamevault.network.RetrofitClient
import com.kychnoo.gamevault.provider.AndroidResourceProvider
import com.kychnoo.gamevault.ui.viewModel.MainViewModel
import org.koin.android.ext.koin.androidContext
import org.koin.core.module.dsl.viewModel
import org.koin.dsl.module

val appModule = module {
    single { RetrofitClient.api }

    single { AndroidResourceProvider(androidContext()) }

    single { RawgGamesRepository(get(), get()) }

    viewModel { MainViewModel(get(), get()) }
}