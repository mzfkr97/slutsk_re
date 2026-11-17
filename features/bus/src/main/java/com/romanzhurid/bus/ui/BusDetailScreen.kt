package com.romanzhurid.bus.ui

import android.widget.Space
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.romanzhurid.brandbook.R
import com.romanzhurid.brandbook.components.button.FavoriteButton
import com.romanzhurid.brandbook.components.card.AppCard
import com.romanzhurid.brandbook.components.card.NoContent
import com.romanzhurid.brandbook.components.toolbar.AppToolbar
import com.romanzhurid.brandbook.theme.AppTheme
import com.romanzhurid.bus.model.BusScheduleUi
import com.romanzhurid.common.ext.AppSmallSpacer
import com.romanzhurid.common.uistate.collectUiState
import com.romanzhurid.navigation.composition.LocalBackHandler

@Composable
internal fun BusDetailScreen(viewModel: BusDetailViewModel) {
    val onBack = LocalBackHandler.current
    val uiState by viewModel.collectUiState()

    Scaffold(
        topBar = {
            AppToolbar(
                title = R.string.bus__detail_title,
                subtitle = "№${uiState.busNumber}",
                onBack = { onBack.invoke() },
                actionContent = uiState.favorite?.let { favorite ->
                    {
                        FavoriteButton(
                            isFavorite = favorite.isFavorite,
                            onClick = viewModel::onToggleFavorite
                        )
                    }
                }
            )
        }
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            when {
                uiState.isLoading -> {
                    CircularProgressIndicator(
                        modifier = Modifier.align(Alignment.Center),
                        color = MaterialTheme.colorScheme.primary
                    )
                }
                uiState.schedules.isEmpty() -> {
                    NoContent(R.string.common__no_items)
                }
                else -> {
                    LazyColumn(
                        modifier = Modifier.fillMaxSize(),
                        contentPadding = PaddingValues(
                            horizontal = AppTheme.dimensions.small,
                            vertical = AppTheme.dimensions.small
                        ),
                        verticalArrangement = Arrangement.spacedBy(AppTheme.dimensions.xMicro)
                    ) {
                        items(
                            items = uiState.schedules,
                            key = { it.id }
                        ) { schedule ->
                            BusScheduleCard(schedule)
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun BusScheduleCard(schedule: BusScheduleUi) {
    AppCard {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp)
        ) {
            Text(
                text = "${schedule.startStation} → ${schedule.endStation}",
                style = AppTheme.typography.titleMedium,
                color = MaterialTheme.colorScheme.primary
            )

            AppSmallSpacer()

            Column(
                modifier = Modifier
                    .fillMaxWidth(),
                verticalArrangement = Arrangement.spacedBy(AppTheme.dimensions.xMicro)
            ) {
                Row(
                    horizontalArrangement = Arrangement.spacedBy(AppTheme.dimensions.xMicro)
                ) {
                    Text(
                        text = schedule.workingDays,
                        style = AppTheme.typography.titleMedium
                    )
                }
                AppSmallSpacer()
                Text(
                    text = schedule.workTime,
                    style = AppTheme.typography.bodyMedium
                )

                AppSmallSpacer()
                if (schedule.weekend.isNotBlank() && schedule.weekendTime.isNotBlank()) {
                    Row(
                        horizontalArrangement = Arrangement.spacedBy(AppTheme.dimensions.xMicro)
                    ) {
                        Text(
                            text = schedule.weekend,
                            style = AppTheme.typography.titleMedium,
                            color = AppTheme.colorScheme.error
                        )
                    }
                    AppSmallSpacer()
                    Text(
                        text = schedule.weekendTime,
                        style = AppTheme.typography.bodyMedium,
                        color = AppTheme.colorScheme.error
                    )
                }

                if (schedule.allStation.isNotBlank()) {
                    AppSmallSpacer()
                    Text(
                        modifier = Modifier.fillMaxWidth(),
                        text = schedule.allStation,
                        textAlign = TextAlign.Center,
                        style = AppTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }
        }
    }
}
