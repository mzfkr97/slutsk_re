package com.romanzhurid.currencies.presentation

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.pullrefresh.PullRefreshIndicator
import androidx.compose.material.pullrefresh.pullRefresh
import androidx.compose.material.pullrefresh.rememberPullRefreshState
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import com.romanzhurid.brandbook.R
import com.romanzhurid.brandbook.components.button.FavoriteButton
import com.romanzhurid.brandbook.components.toolbar.AppToolbar
import com.romanzhurid.brandbook.theme.AppTheme
import com.romanzhurid.common.uistate.collectUiState
import com.romanzhurid.currencies.model.CurrencyItem
import androidx.compose.foundation.lazy.items

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun CurrenciesScreen(
    viewModel: CurrenciesViewModel,
    onBack: () -> Boolean
) {
    val uiState by viewModel.collectUiState()
    val isRefreshing = uiState.isLoading
    val pullRefreshState = rememberPullRefreshState(
        refreshing = isRefreshing,
        onRefresh = { viewModel.loadCurrencies() }
    )
    Scaffold(
        topBar = {
            AppToolbar(
                title = stringResource(R.string.currencies_screen_title),
                onBack = onBack
            )
        }
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .pullRefresh(pullRefreshState)
        ) {
            val listState = rememberLazyListState()
            LazyColumn(
                state = listState,
                modifier = Modifier.fillMaxSize(),
                contentPadding = PaddingValues(vertical = AppTheme.dimensions.small),
            ) {
                items(
                    items = uiState.currencies,
                    key = { item ->
                        when (item) {
                            is CurrencyItem.Header -> "header_${item.title}"
                            is CurrencyItem.CurrencyUi -> "currency_${item.id}"
                        }
                    },
                    contentType = { item ->
                        when (item) {
                            is CurrencyItem.Header -> "header"
                            is CurrencyItem.CurrencyUi -> "currency"
                        }
                    },
                ) { item ->
                    when (item) {
                        is CurrencyItem.Header -> {
                            SectionHeader(item.title)
                        }

                        is CurrencyItem.CurrencyUi -> {
                            CurrencyCard(
                                modifier = Modifier
                                    .padding(
                                        horizontal = AppTheme.dimensions.small,
                                        vertical = AppTheme.dimensions.xxMicro
                                    )
                                    .animateItem(),
                                currency = item,
                                onToggleFavorite = {
                                    viewModel.onToggleFavorite(item.id)
                                }
                            )
                        }
                    }
                }
            }
            PullRefreshIndicator(
                refreshing = isRefreshing,
                state = pullRefreshState,
                modifier = Modifier.align(Alignment.TopCenter),
                backgroundColor = MaterialTheme.colorScheme.surface,
                contentColor = MaterialTheme.colorScheme.primary
            )
        }
    }
}

@Composable
fun SectionHeader(title: String) {
    Text(
        text = title,
        style = MaterialTheme.typography.labelMedium,
        color = MaterialTheme.colorScheme.primary,
        modifier = Modifier
            .fillMaxWidth()
            .background(MaterialTheme.colorScheme.primaryContainer)
            .padding(
                horizontal = AppTheme.dimensions.medium,
                vertical = AppTheme.dimensions.small
            )
    )
}

@Composable
fun CurrencyCard(
    modifier: Modifier,
    currency: CurrencyItem.CurrencyUi,
    onToggleFavorite: () -> Unit
) {
    val borderColor = if (currency.isFavorite) {
        AppTheme.colorScheme.primary
    } else {
        AppTheme.colorScheme.outlineVariant
    }

    Card(
        modifier = modifier.fillMaxWidth(),
        shape = RoundedCornerShape(AppTheme.dimensions.medium),
        colors = CardDefaults.cardColors(
            containerColor = AppTheme.colorScheme.surface
        ),
        elevation = CardDefaults.cardElevation(AppTheme.dimensions.xMicro),
        border = BorderStroke(
            width = AppTheme.dimensions.micro,
            color = borderColor
        )
    ) {
        Column(
            modifier = Modifier.padding(AppTheme.dimensions.medium),
            verticalArrangement = Arrangement.spacedBy(AppTheme.dimensions.small)
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = currency.name,
                        style = AppTheme.typography.titleMedium
                    )
                    Text(
                        text = currency.abbreviation,
                        style = AppTheme.typography.labelMedium,
                        color = AppTheme.colorScheme.onSurfaceVariant
                    )
                }
                Row(verticalAlignment = Alignment.CenterVertically) {
                    RateBadge(currency.officialRate, currency.isFavorite)
                    Spacer(Modifier.width(AppTheme.dimensions.small))
                    FavoriteButton(
                        isFavorite = currency.isFavorite,
                        onClick = onToggleFavorite
                    )
                }
            }
        }
    }
}

@Composable
private fun RateBadge(formattedRate: String, isFavorite: Boolean) {
    val color = if (isFavorite) {
        MaterialTheme.colorScheme.primary.copy(alpha = 0.12f)
    } else {
        MaterialTheme.colorScheme.surfaceVariant
    }

    val textColor = if (isFavorite) {
        MaterialTheme.colorScheme.primary
    } else {
        MaterialTheme.colorScheme.onSurfaceVariant
    }

    Box(
        modifier = Modifier
            .background(
                color = color,
                shape = RoundedCornerShape(AppTheme.dimensions.small)
            )
            .padding(
                horizontal = AppTheme.dimensions.xxSmall,
                vertical = AppTheme.dimensions.xxMicro
            )
    ) {
        Text(
            text = formattedRate,
            style = MaterialTheme.typography.labelLarge,
            fontWeight = FontWeight.SemiBold,
            color = textColor
        )
    }
}
