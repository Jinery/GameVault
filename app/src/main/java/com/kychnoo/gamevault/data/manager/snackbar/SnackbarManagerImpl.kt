package com.kychnoo.gamevault.data.manager.snackbar

import com.kychnoo.gamevault.data.model.states.SnackbarState
import com.kychnoo.gamevault.data.model.types.snackbar.SnackbarTypes
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.receiveAsFlow

class SnackbarManagerImpl : SnackbarManager {
    private val _snackbarEvents = Channel<SnackbarState>(Channel.BUFFERED)
    override val snackbarEvents: Flow<SnackbarState> = _snackbarEvents.receiveAsFlow()

    override suspend fun showSnackbar(message: String, type: SnackbarTypes) {
        _snackbarEvents.send(SnackbarState(message, type))
    }
}