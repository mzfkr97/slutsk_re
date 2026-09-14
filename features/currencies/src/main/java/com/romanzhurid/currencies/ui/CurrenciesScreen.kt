package com.romanzhurid.currencies.ui

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
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.pullrefresh.PullRefreshIndicator
import androidx.compose.material.pullrefresh.pullRefresh
import androidx.compose.material.pullrefresh.rememberPullRefreshState
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.romanzhurid.brandbook.R
import com.romanzhurid.brandbook.components.button.FavoriteButton
import com.romanzhurid.brandbook.components.card.AppCard
import com.romanzhurid.brandbook.components.card.NoContent
import com.romanzhurid.brandbook.components.text.SectionHeader
import com.romanzhurid.brandbook.components.toolbar.AppToolbarWithSearch
import com.romanzhurid.brandbook.ext.DefaultSpacer
import com.romanzhurid.brandbook.ext.highlightText
import com.romanzhurid.brandbook.theme.AppTheme
import com.romanzhurid.common.uistate.collectUiState
import com.romanzhurid.currencies.model.CurrencyItem
import com.romanzhurid.currencies.model.CurrencyItem.CurrencyUi
import com.romanzhurid.navigation.composition.LocalBackHandler

@OptIn(ExperimentalMaterialApi::class)
@Composable
internal fun CurrenciesScreen(viewModel: CurrenciesViewModel) {
    val onBack = LocalBackHandler.current
    val uiState by viewModel.collectUiState()
    val isRefreshing = uiState.isLoading

    val pullRefreshState = rememberPullRefreshState(
        refreshing = uiState.isLoading,
        onRefresh = viewModel::loadCurrencies
    )
    Scaffold(
        topBar = {
            AppToolbarWithSearch(
                title = stringResource(R.string.currencies__screen_title),
                query = uiState.searchQuery,
                isSearchAvailable = uiState.currencies.isNotEmpty(),
                onQueryChange = viewModel::searchItem,
                onBack = { onBack.invoke() }
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
            if (uiState.currencies.isEmpty()) {
                NoContent(R.string.common__no_items)
            } else {
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
                                is CurrencyUi -> "currency_${item.id}"
                            }
                        },
                        contentType = { item ->
                            when (item) {
                                is CurrencyItem.Header -> "header"
                                is CurrencyUi -> "currency"
                            }
                        },
                    ) { item ->
                        when (item) {
                            is CurrencyItem.Header -> {
                                SectionHeader(item.title)
                            }
                            is CurrencyUi -> {
                                CurrencyCard(
                                    modifier = Modifier
                                        .padding(
                                            horizontal = AppTheme.dimensions.small,
                                            vertical = AppTheme.dimensions.xxMicro
                                        )
                                        .animateItem(),
                                    currency = item,
                                    query = uiState.searchQuery,
                                    onToggleFavorite = {
                                        viewModel.onToggleFavorite(item.id)
                                    }
                                )
                            }
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
private fun CurrencyCard(
    modifier: Modifier,
    currency: CurrencyUi,
    query: String,
    onToggleFavorite: () -> Unit
) {

    AppCard(modifier) {
        Column {
            Row(
                Modifier
                    .fillMaxSize()
                    .background(MaterialTheme.colorScheme.primary)
                    .padding(8.dp)
            ) {
                Text(
                    text = highlightText(
                        text = currency.name,
                        query = query
                    ),
                    color = MaterialTheme.colorScheme.onPrimary,
                    style = AppTheme.typography.titleMedium
                )
            }

            Row(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(start = 8.dp, end = 8.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = highlightText(
                        text = currency.abbreviation,
                        query = query
                    ),
                    style = AppTheme.typography.labelMedium,
                )

                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(
                        text = currency.officialRate,
                        style = AppTheme.typography.labelLarge,
                        fontWeight = FontWeight.SemiBold,
                    )

                    Spacer(modifier = Modifier.width(4.dp))

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
private fun RateBadge(
    formattedRate: String,
    isFavorite: Boolean
) {
    val backgroundColor = if (isFavorite) {
        AppTheme.colorScheme.primary.copy(alpha = 0.12f)
    } else {
        AppTheme.colorScheme.surfaceVariant
    }

    val textColor = if (isFavorite) {
        AppTheme.colorScheme.primary
    } else {
        AppTheme.colorScheme.onSurfaceVariant
    }

    Box(
        modifier = Modifier
            .background(
                color = backgroundColor,
                shape = RoundedCornerShape(AppTheme.dimensions.small)
            )
            .padding(
                horizontal = AppTheme.dimensions.xxSmall,
            )
    ) {
        Text(
            text = formattedRate,
            style = AppTheme.typography.labelLarge,
            fontWeight = FontWeight.SemiBold,
            color = textColor
        )
    }
}
