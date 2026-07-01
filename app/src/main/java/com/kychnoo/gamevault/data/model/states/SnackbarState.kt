package com.kychnoo.gamevault.data.model.states

import com.kychnoo.gamevault.data.model.types.snackbar.SnackbarTypes

data class SnackbarState(
    val message: String,
    val type: SnackbarTypes = SnackbarTypes.INFO
)