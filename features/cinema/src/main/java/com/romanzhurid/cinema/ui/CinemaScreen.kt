package com.romanzhurid.cinema.ui

import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.spring
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Surface
import androidx.compose.material.TextButton
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.fromHtml
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import coil3.compose.AsyncImage
import com.romanzhurid.brandbook.R
import com.romanzhurid.brandbook.components.card.AppCard
import com.romanzhurid.brandbook.components.card.NoContent
import com.romanzhurid.brandbook.components.text.SectionHeader
import com.romanzhurid.brandbook.components.toolbar.AppToolbar
import com.romanzhurid.brandbook.theme.AppTheme
import com.romanzhurid.cinema.model.CinemaUiItem
import com.romanzhurid.common.uistate.CollectEventEffect
import com.romanzhurid.common.uistate.collectUiState
import com.romanzhurid.common.viewer.ImageViewer
import com.romanzhurid.navigation.composition.LocalBackHandler

@Composable
fun CinemaScreen(viewModel: CinemaViewModel) {
    val onBack = LocalBackHandler.current
    val uiState by viewModel.collectUiState()

    viewModel.CollectEventEffect { event ->
        when (event) {
            is CinemaViewModel.Event.ToGallery -> {

            }
        }
    }

    Scaffold(
        topBar = {
            AppToolbar(
                title = R.string.menu__cinema,
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
            if (uiState.films.isEmpty() && uiState.isInitialLoading) {
                NoContent(R.string.common__no_items)
            } else {
                LazyColumn(
                    state = listState,
                    modifier = Modifier.fillMaxSize(),
                    contentPadding = PaddingValues(vertical = AppTheme.dimensions.small),
                ) {
                    uiState.films.forEach { item ->
                        when (item) {
                            is CinemaUiItem.CinemaDateUi -> {
                                stickyHeader(
                                    key = "header_${item.date}"
                                ) {
                                    SectionHeader(item.date)
                                }
                            }

                            is CinemaUiItem.CinemaUi -> {
                                item(
                                    key = "cinema_${item.hashCode()}",
                                    contentType = "cinema",
                                ) {
                                    CinemaCard(
                                        modifier = Modifier
                                            .padding(
                                                horizontal = AppTheme.dimensions.small,
                                                vertical = AppTheme.dimensions.xxMicro
                                            )
                                            .animateItem(),
                                        item = item,
                                        onImageClicked = { imageUrl ->
                                            viewModel.onImageClicked(imageUrl)
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
}

@Composable
fun CinemaCard(
    modifier: Modifier = Modifier,
    item: CinemaUiItem.CinemaUi,
    onImageClicked: (String) -> Unit
) {
    var expanded by rememberSaveable {
        mutableStateOf(false)
    }
    var isOverflowing by remember { mutableStateOf(false) }

    var showViewer by rememberSaveable {
        mutableStateOf(false)
    }

    AppCard(
        modifier = modifier,
        borderColor = AppTheme.colorScheme.primary,
    ) {
        Row(
            modifier = Modifier.padding(AppTheme.dimensions.medium)
        ) {
            AsyncImage(
                model = item.imageUrl,
                contentDescription = item.name,
                modifier = Modifier
                    .width(120.dp)
                    .height(180.dp)
                    .clip(RoundedCornerShape(16.dp))
                    .clickable(onClick = {
                        //onImageClicked(item.imageUrl)
                        showViewer = true
                    }),
                contentScale = ContentScale.Crop,
            )

            Spacer(modifier = Modifier.width(AppTheme.dimensions.medium))

            Column(
                modifier = Modifier.weight(1f),
                verticalArrangement = Arrangement.spacedBy(AppTheme.dimensions.xSmall)
            ) {

                Text(
                    text = item.name,
                    style = AppTheme.typography.titleLarge,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis
                )

                Surface(
                    shape = RoundedCornerShape(12.dp),
                    color = AppTheme.colorScheme.primaryContainer,
                ) {
                    Text(
                        text = item.time,
                        modifier = Modifier.padding(
                            horizontal = 12.dp,
                            vertical = 6.dp,
                        ),
                        style = AppTheme.typography.titleSmall,
                        color = AppTheme.colorScheme.onPrimaryContainer,
                    )
                }

                Text(
                    text = item.prices,
                    style = AppTheme.typography.titleMedium,
                    color = AppTheme.colorScheme.primary,
                )

                HorizontalDivider()

                if (item.descriptionHtml.isNotBlank()) {
                    Column(
                        modifier = Modifier.animateContentSize(
                            animationSpec = spring(
                                dampingRatio = Spring.DampingRatioMediumBouncy,
                                stiffness = Spring.StiffnessLow
                            )
                        )
                    ) {
                        Text(
                            text = AnnotatedString.fromHtml(item.descriptionHtml),
                            style = AppTheme.typography.bodyMedium,
                            color = AppTheme.colorScheme.onSurfaceVariant,
                            maxLines = if (expanded) Int.MAX_VALUE else 6,
                            overflow = TextOverflow.Ellipsis,
                            onTextLayout = { result ->
                                isOverflowing = result.lineCount > 6 ||
                                        result.hasVisualOverflow
                            }
                        )

                        if (isOverflowing) {
                            TextButton(
                                onClick = { expanded = !expanded }
                            ) {
                                Text(
                                    text = if (expanded) {
                                        "Свернуть"
                                    } else {
                                        "Читать далее"
                                    }
                                )
                            }
                        }
                    }
                }
            }
        }
    }

    if (showViewer) {
        ImageViewer(
            imageUrl = item.imageUrl,
            onDismiss = {
                showViewer = false
            }
        )
    }
}


