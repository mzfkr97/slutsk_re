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
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.IconButton
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.outlined.StarBorder
import androidx.compose.material.pullrefresh.PullRefreshIndicator
import androidx.compose.material.pullrefresh.pullRefresh
import androidx.compose.material.pullrefresh.rememberPullRefreshState
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.romanzhurid.brandbook.R
import com.romanzhurid.brandbook.components.toolbar.AppToolbar
import com.romanzhurid.brandbook.theme.AppTheme
import com.romanzhurid.common.uistate.collectUiState
import com.romanzhurid.domain.currencies.model.Currency

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun CurrenciesScreen(
    viewModel: CurrenciesViewModel,
    onBack: () -> Unit
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
                subtitle = stringResource(R.string.currencies_last_update_prefix) + uiState.lastUpdateTimeMs,
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
                contentPadding = PaddingValues(vertical = 8.dp),
            ) {
                if (uiState.favorites.isNotEmpty()) {
                    item {
                        SectionHeader("Favorites")
                    }
                    items(
                        items = uiState.favorites,
                        key = { it.id },
                        contentType = { "currency" }
                    ) { currency ->

                        CurrencyCard(Modifier
                            .padding(horizontal = 8.dp, vertical = 4.dp).animateItem(),
                            currency = currency,
                            onToggleFavorite = { viewModel.onToggleFavorite(currency.id) }
                        )
                    }
                }

                if (uiState.favorites.isNotEmpty() && uiState.others.isNotEmpty()) {
                    item {
                        Spacer(Modifier.height(12.dp))
                    }
                }
                item {
                    SectionHeader("All currencies")
                }
                items(
                    items = uiState.others,
                    key = { it.id },
                    contentType = { "currency" }
                ) { currency ->
                    CurrencyCard(Modifier
                        .padding(horizontal = 8.dp, vertical = 4.dp).animateItem(),
                        currency = currency,
                        onToggleFavorite = { viewModel.onToggleFavorite(currency.id) }
                    )
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
            .background(MaterialTheme.colorScheme.background)
            .padding(horizontal = 16.dp, vertical = 8.dp)
    )
}

@Composable
private fun RateBadge(rate: Double, isFavorite: Boolean) {
    val formattedRate = remember(rate) { "%.4f".format(rate) }

    val bg = if (isFavorite)
        MaterialTheme.colorScheme.primary.copy(alpha = 0.12f)
    else
        MaterialTheme.colorScheme.surfaceVariant

    val textColor = if (isFavorite)
        MaterialTheme.colorScheme.primary
    else
        MaterialTheme.colorScheme.onSurfaceVariant

    Box(
        modifier = Modifier
            .background(bg, RoundedCornerShape(10.dp))
            .padding(horizontal = 12.dp, vertical = 6.dp)
    ) {
        Text(
            text = formattedRate,
            style = MaterialTheme.typography.labelLarge,
            fontWeight = FontWeight.SemiBold,
            color = textColor
        )
    }
}

@Composable
fun CurrencyCard(
    modifier: Modifier = Modifier,
    currency: Currency,
    onToggleFavorite: () -> Unit = {}
) {
    val borderColor = if (currency.isFavorite)
        AppTheme.colorScheme.primary
    else
        AppTheme.colorScheme.outlineVariant

    Card(
        modifier = modifier.fillMaxWidth(),
        shape = RoundedCornerShape(AppTheme.dimensions.medium),
        colors = CardDefaults.cardColors(
            containerColor = AppTheme.colorScheme.surface
        ),
        elevation = CardDefaults.cardElevation(0.dp),
        border = BorderStroke(1.dp, borderColor)
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
                    Text(
                        text = currency.scale.toString(),
                        style = AppTheme.typography.labelMedium,
                        fontWeight = FontWeight.Medium
                    )
                    RateBadge(currency.officialRate, currency.isFavorite)
                    Spacer(Modifier.width(8.dp))
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
private fun FavoriteButton(
    isFavorite: Boolean,
    onClick: () -> Unit
) {
    val tint = if (isFavorite)
        AppTheme.colorScheme.tertiary
    else
        AppTheme.colorScheme.onSurfaceVariant


    IconButton(onClick = onClick) {
        Icon(
            imageVector = if (isFavorite)
                Icons.Filled.Star
            else
                Icons.Outlined.StarBorder,
            contentDescription = null,
            tint = tint,
            modifier = Modifier
        )
    }
}
