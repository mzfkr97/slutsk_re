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
import com.romanzhurid.brandbook.theme.AppTheme
import com.romanzhurid.common.uistate.collectUiState
import com.romanzhurid.currencies.model.CurrencyItem
import androidx.compose.foundation.lazy.items
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.withStyle
import com.romanzhurid.brandbook.components.toolbar.AppToolbarWithSearch
import com.romanzhurid.navigation.composition.LocalBackHandler

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun CurrenciesScreen(
    viewModel: CurrenciesViewModel,
) {
    val onBack = LocalBackHandler.current
    val uiState by viewModel.collectUiState()
    val isRefreshing = uiState.isLoading
    val pullRefreshState = rememberPullRefreshState(
        refreshing = isRefreshing,
        onRefresh = { viewModel.loadCurrencies() }
    )
    Scaffold(
        topBar = {
            AppToolbarWithSearch(
                title = stringResource(R.string.currencies_screen_title),
                query = uiState.searchQuery,
                onQueryChange = {
                    viewModel.searchItem(it)
                },
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
                                query = uiState.searchQuery,
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
        style = AppTheme.typography.labelMedium,
        color = AppTheme.colorScheme.primary,
        modifier = Modifier
            .fillMaxWidth()
            .background(AppTheme.colorScheme.primaryContainer)
            .padding(
                horizontal = AppTheme.dimensions.medium,
                vertical = AppTheme.dimensions.small
            )
    )
}

@Composable
fun highlightText(
    text: String,
    query: String
): AnnotatedString {
    if (query.isBlank()) return AnnotatedString(text)

    val lowerText = text.lowercase()
    val lowerQuery = query.lowercase()

    val startIndex = lowerText.indexOf(lowerQuery)

    if (startIndex == -1) return AnnotatedString(text)

    val endIndex = startIndex + query.length

    return buildAnnotatedString {
        append(text.substring(0, startIndex))

        withStyle(
            style = SpanStyle(
                color = AppTheme.colorScheme.primary,
                fontWeight = FontWeight.Bold
            )
        ) {
            append(text.substring(startIndex, endIndex))
        }

        append(text.substring(endIndex))
    }
}

@Composable
fun CurrencyCard(
    modifier: Modifier,
    currency: CurrencyItem.CurrencyUi,
    query: String,
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
                        text = highlightText(currency.name, query),
                        style = AppTheme.typography.titleMedium
                    )
                    Text(
                        text = highlightText(currency.abbreviation, query),
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
            style = AppTheme.typography.labelLarge,
            fontWeight = FontWeight.SemiBold,
            color = textColor
        )
    }
}
