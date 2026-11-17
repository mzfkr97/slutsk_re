package com.romanzhurid.bus.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.romanzhurid.brandbook.R
import com.romanzhurid.brandbook.components.button.FavoriteButton
import com.romanzhurid.brandbook.components.card.AppCard
import com.romanzhurid.brandbook.components.card.NoContent
import com.romanzhurid.brandbook.components.toolbar.AppToolbar
import com.romanzhurid.brandbook.theme.AppTheme
import com.romanzhurid.bus.model.BusScheduleUi
import com.romanzhurid.bus.model.BusTimeUi
import com.romanzhurid.common.ext.ResumeEffect
import com.romanzhurid.common.uistate.collectUiState
import com.romanzhurid.domain.bus.model.BusDayType
import com.romanzhurid.navigation.composition.LocalBackHandler
import java.time.LocalDateTime
import kotlinx.coroutines.delay
import kotlin.time.Duration.Companion.milliseconds

@Composable
internal fun BusDetailScreen(viewModel: BusDetailViewModel) {
    val onBack = LocalBackHandler.current
    val uiState by viewModel.collectUiState()
    var now by remember { mutableStateOf(LocalDateTime.now()) }
    ResumeEffect { now = LocalDateTime.now() }
    LaunchedEffect(Unit) {
        while (true) {
            delay((60_000L - System.currentTimeMillis() % 60_000L).milliseconds)
            now = LocalDateTime.now()
        }
    }

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
                        verticalArrangement = Arrangement.spacedBy(AppTheme.dimensions.small)
                    ) {
                        items(
                            items = uiState.schedules,
                            key = { it.id }
                        ) { schedule ->
                            BusScheduleCard(schedule, now)
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun BusScheduleCard(schedule: BusScheduleUi, now: LocalDateTime) {
    val hasAllDays = schedule.allDays.isNotEmpty()
    val hasWorkingDays = schedule.workTimes.isNotEmpty()
    val hasWeekend = schedule.weekendTimes.isNotEmpty()
    val nextDeparture = remember(schedule, now) { findNextDeparture(schedule, now) }

    AppCard(modifier = Modifier.fillMaxWidth()) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(AppTheme.dimensions.medium),
            verticalArrangement = Arrangement.spacedBy(AppTheme.dimensions.medium)
        ) {
            Surface(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(AppTheme.dimensions.xxSmall),
                color = MaterialTheme.colorScheme.primaryContainer
            ) {
                Text(
                    modifier = Modifier.fillMaxWidth()
                        .padding(AppTheme.dimensions.xxSmall),
                    text = "${schedule.startStation} → ${schedule.endStation}",
                    style = AppTheme.typography.bodyLarge,
                    color = MaterialTheme.colorScheme.onPrimaryContainer
                )
            }

            if (hasAllDays || hasWorkingDays || hasWeekend) {
                Column(
                    verticalArrangement = Arrangement.spacedBy(AppTheme.dimensions.small)
                ) {
                    if (hasAllDays) {
                        ScheduleSection(
                            days = stringResource(R.string.bus__all_days),
                            times = schedule.allDays,
                            highlightedIndex = nextDeparture?.takeIf { it.dayType == BusDayType.ALL_DAYS }?.index
                        )
                    }

                    if (hasWorkingDays) {
                        ScheduleSection(
                            days = stringResource(R.string.bus__workdays),
                            times = schedule.workTimes,
                            highlightedIndex = nextDeparture?.takeIf { it.dayType == BusDayType.WORKDAY }?.index
                        )
                    }

                    if (hasWeekend) {
                        ScheduleSection(
                            days = stringResource(R.string.bus__weekends),
                            times = schedule.weekendTimes,
                            highlightedIndex = nextDeparture?.takeIf { it.dayType == BusDayType.WEEKEND }?.index
                        )
                    }
                }
            } else {
                Text(
                    text = stringResource(R.string.bus__no_departures),
                    style = AppTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }

            if (schedule.allStation.isNotBlank()) {
                HorizontalDivider(color = MaterialTheme.colorScheme.outlineVariant)
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

@Composable
private fun ScheduleSection(days: String, times: List<BusTimeUi>, highlightedIndex: Int?) {
    Surface(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(AppTheme.dimensions.xxSmall),
        color = MaterialTheme.colorScheme.surfaceContainer
    ) {
        Column(
            modifier = Modifier.padding(AppTheme.dimensions.xxSmall),
            verticalArrangement = Arrangement.spacedBy(AppTheme.dimensions.xxMicro)
        ) {
            Text(
                text = days,
                style = AppTheme.typography.labelLarge,
                color = MaterialTheme.colorScheme.primary
            )
            BoxWithConstraints(modifier = Modifier.fillMaxWidth()) {
                val columns = (maxWidth / 120.dp).toInt().coerceIn(1, 3)
                Column(verticalArrangement = Arrangement.spacedBy(AppTheme.dimensions.xxMicro)) {
                    times.chunked(columns).forEachIndexed { rowIndex, row ->
                        Row(horizontalArrangement = Arrangement.spacedBy(AppTheme.dimensions.xxMicro)) {
                            row.forEachIndexed { columnIndex, departure ->
                                val isHighlighted = rowIndex * columns + columnIndex == highlightedIndex
                                Surface(
                                    modifier = Modifier.weight(1f),
                                    shape = RoundedCornerShape(AppTheme.dimensions.xxSmall),
                                    color = if (isHighlighted) MaterialTheme.colorScheme.primary
                                    else MaterialTheme.colorScheme.surface
                                ) {
                                    Column(modifier = Modifier.padding(AppTheme.dimensions.xxSmall)) {
                                        Text(
                                            text = departure.time,
                                            style = AppTheme.typography.bodyMedium,
                                            color = if (isHighlighted) MaterialTheme.colorScheme.onPrimary
                                            else MaterialTheme.colorScheme.onSurface
                                        )
                                        departure.note?.takeIf { it.isNotBlank() }?.let { note ->
                                            Text(
                                                text = note,
                                                style = AppTheme.typography.bodySmall,
                                                color = if (isHighlighted) MaterialTheme.colorScheme.onPrimary
                                                else MaterialTheme.colorScheme.onSurfaceVariant
                                            )
                                        }
                                    }
                                }
                            }
                            repeat(columns - row.size) {
                                Spacer(modifier = Modifier.weight(1f))
                            }
                        }
                    }
                }
            }
        }
    }
}
