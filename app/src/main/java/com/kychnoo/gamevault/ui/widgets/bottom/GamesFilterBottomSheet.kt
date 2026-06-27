package com.kychnoo.gamevault.ui.widgets.bottom

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilterChip
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.RadioButton
import androidx.compose.material3.SheetState
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.kychnoo.gamevault.R
import com.kychnoo.gamevault.data.model.gameFilters.GameFilters
import com.kychnoo.gamevault.data.model.genres.GenreData
import com.kychnoo.gamevault.data.model.ui.UiState

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun GamesFilterBottomSheet(
    initialFilters: GameFilters,
    genresState: UiState<List<GenreData>>,
    onDismissRequest: () -> Unit,
    onApplyFilters: (GameFilters) -> Unit,
    sheetState: SheetState = rememberModalBottomSheetState()
) {
    var tempOrdering by remember { mutableStateOf(initialFilters.ordering) }
    val tempSelectedGenres = remember { mutableStateListOf<Int>().apply { addAll(initialFilters.selectedGenres) } }

    ModalBottomSheet(
        onDismissRequest = onDismissRequest,
        sheetState = sheetState,
        containerColor = MaterialTheme.colorScheme.surfaceContainerLow
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp)
                .padding(bottom = 32.dp)
        ) {
            Text(
                text = stringResource(R.string.filters_title),
                style = MaterialTheme.typography.titleLarge,
                modifier = Modifier.padding(bottom = 16.dp)
            )

            Text(
                text = stringResource(R.string.sorting_title),
                style = MaterialTheme.typography.labelLarge
            )

            val sortingOptions = listOf(
                "-added" to stringResource(R.string.sort_by_popularity),
                "-released" to stringResource(R.string.sort_by_release_date),
                "-metacritic" to stringResource(R.string.sort_by_ranking)
            )
            sortingOptions.forEach { (value, label) ->
                Row(
                    modifier = Modifier.fillMaxWidth().height(44.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    RadioButton(
                        selected = (tempOrdering == value),
                        onClick = { tempOrdering = value }
                    )
                    Text(text = label, modifier = Modifier.padding(start = 8.dp))
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            Text(
                text = stringResource(R.string.genres_title),
                style = MaterialTheme.typography.labelLarge,
                modifier = Modifier.padding(bottom = 8.dp)
            )

            when (genresState) {
                is UiState.Loading -> {
                    Box(modifier = Modifier.fillMaxWidth().height(48.dp), contentAlignment = Alignment.Center) {
                        CircularProgressIndicator(modifier = Modifier.size(24.dp))
                    }
                }
                is UiState.Error -> {
                    Text(
                        text = genresState.message,
                        color = MaterialTheme.colorScheme.error,
                        style = MaterialTheme.typography.bodyMedium
                    )
                }
                is UiState.Success -> {
                    LazyRow(
                        horizontalArrangement = Arrangement.spacedBy(8.dp),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        items(
                            items = genresState.data,
                            key = { genre -> genre.id }
                        ) { genre ->
                            val isSelected = tempSelectedGenres.contains(genre.id)
                            FilterChip(
                                selected = isSelected,
                                onClick = {
                                    if (isSelected) tempSelectedGenres.remove(genre.id)
                                    else tempSelectedGenres.add(genre.id)
                                },
                                label = { Text(genre.name) }
                            )
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(32.dp))

            Button(
                onClick = {
                    onApplyFilters(
                        GameFilters(
                            ordering = tempOrdering,
                            selectedGenres = tempSelectedGenres.toList()
                        )
                    )
                },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(
                    text = stringResource(R.string.apply_filters),
                    fontSize = 16.sp,
                    color = MaterialTheme.colorScheme.onPrimary
                )
            }
        }
    }
}