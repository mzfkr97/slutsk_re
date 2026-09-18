package com.romanzhurid.bus.ui

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.romanzhurid.brandbook.R
import com.romanzhurid.brandbook.components.button.FavoriteButton
import com.romanzhurid.brandbook.components.card.AppCard
import com.romanzhurid.brandbook.components.card.NoContent
import com.romanzhurid.brandbook.components.text.SectionHeader
import com.romanzhurid.brandbook.components.toolbar.AppToolbarWithSearch
import com.romanzhurid.brandbook.ext.highlightText
import com.romanzhurid.brandbook.theme.AppTheme
import com.romanzhurid.bus.model.BusListItem
import com.romanzhurid.bus.model.BusListItem.BusUi
import com.romanzhurid.bus.ui.BusListViewModel.Event
import com.romanzhurid.common.uistate.CollectEventEffect
import com.romanzhurid.common.uistate.collectUiState
import com.romanzhurid.navigation.composition.LocalBackHandler

@Composable
internal fun BusListScreen(
    viewModel: BusListViewModel,
    onNavigateToDetail: (busNumber: Int) -> Unit,
) {
    val onBack = LocalBackHandler.current
    val uiState by viewModel.collectUiState()

    viewModel.CollectEventEffect { event ->
        when (event) {
            is Event.NavigateToDetail -> {
                onNavigateToDetail(event.busNumber)
            }
        }
    }

    Scaffold(
        topBar = {
            AppToolbarWithSearch(
                title = stringResource(R.string.bus__screen_title),
                query = uiState.searchQuery,
                isSearchAvailable = uiState.allBuses.isNotEmpty(),
                onQueryChange = viewModel::searchItem,
                onBack = { onBack.invoke() }
            )
        }
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            val listState = rememberLazyListState()
            if (uiState.buses.isEmpty()) {
                NoContent(R.string.common__no_items)
            } else {
                LazyColumn(
                    state = listState,
                    modifier = Modifier.fillMaxSize(),
                    contentPadding = PaddingValues(vertical = AppTheme.dimensions.small),
                ) {
                    items(
                        items = uiState.buses,
                        key = { item ->
                            when (item) {
                                is BusListItem.Header -> "header_${item.title}"
                                is BusUi -> "bus_${item.id}"
                            }
                        },
                        contentType = { item ->
                            when (item) {
                                is BusListItem.Header -> "header"
                                is BusUi -> "bus"
                            }
                        },
                    ) { item ->
                        when (item) {
                            is BusListItem.Header -> {
                                SectionHeader(item.title)
                            }
                            is BusUi -> {
                                BusCard(
                                    modifier = Modifier
                                        .padding(
                                            horizontal = AppTheme.dimensions.small,
                                            vertical = AppTheme.dimensions.xxMicro
                                        )
                                        .animateItem(),
                                    bus = item,
                                    query = uiState.searchQuery,
                                    onClick = { viewModel.onBusClicked(item.busNumber) },
                                    onToggleFavorite = {
                                        viewModel.onToggleFavorite(item.id, item.isFavorite)
                                    }
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun BusCard(
    modifier: Modifier,
    bus: BusUi,
    query: String,
    onClick: () -> Unit,
    onToggleFavorite: () -> Unit
) {
    AppCard(
        modifier = modifier.clickable(onClick = onClick)
    ) {
        Row(
            modifier = Modifier
                .fillMaxSize()
                .padding(8.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = highlightText(
                        text = "№${bus.busNumber}",
                        query = query
                    ),
                    color = MaterialTheme.colorScheme.primary,
                    style = AppTheme.typography.titleMedium
                )

                val route = listOfNotNull(bus.startStation, bus.endStation).joinToString(" — ")
                if (route.isNotBlank()) {
                    Text(
                        text = highlightText(
                            text = route,
                            query = query
                        ),
                        style = AppTheme.typography.labelMedium
                    )
                }
            }

            FavoriteButton(
                isFavorite = bus.isFavorite,
                onClick = onToggleFavorite
            )
        }
    }
}
