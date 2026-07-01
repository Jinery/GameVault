package com.kychnoo.gamevault.data.manager.snackbar

import com.kychnoo.gamevault.data.model.states.SnackbarState
import com.kychnoo.gamevault.data.model.types.snackbar.SnackbarTypes
import kotlinx.coroutines.flow.Flow

interface SnackbarManager {
    val snackbarEvents: Flow<SnackbarState>
    suspend fun showSnackbar(message: String, type: SnackbarTypes = SnackbarTypes.INFO)
}