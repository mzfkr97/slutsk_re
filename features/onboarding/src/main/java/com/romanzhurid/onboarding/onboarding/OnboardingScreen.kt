package com.romanzhurid.onboarding.onboarding

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.romanzhurid.brandbook.R
import com.romanzhurid.brandbook.components.button.AppBaseButton
import com.romanzhurid.brandbook.theme.AppTheme
import com.romanzhurid.common.uistate.CollectEventEffect
import com.romanzhurid.common.uistate.collectUiState
import com.romanzhurid.navigation.AppRoute
import com.romanzhurid.onboarding.model.IntroPage
import com.romanzhurid.onboarding.onboarding.OnboardingViewModel.Event
import com.romanzhurid.onboarding.onboarding.OnboardingViewModel.UiState
import kotlinx.coroutines.launch

@Composable
internal fun OnboardingScreen(
    viewModel: OnboardingViewModel,
    clearAndPushAppRoute: (AppRoute) -> Unit,
) {

    val uiState by viewModel.collectUiState()

    viewModel.CollectEventEffect { event ->
        when (event) {
            is Event.OnClearAndPush -> {
                clearAndPushAppRoute(event.destination)
            }
        }
    }

    OnboardingScreenContent(
        uiState = uiState,
        onFinishIntro = viewModel::onFinishIntro,
    )
}

@Composable
private fun OnboardingScreenContent(
    uiState: UiState,
    onFinishIntro: () -> Unit,
) {
    val pagerState = rememberPagerState { uiState.pages.size }
    val scope = rememberCoroutineScope()

    Scaffold(
        topBar = {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(AppTheme.dimensions.medium),
                contentAlignment = Alignment.CenterEnd
            ) {
                if (pagerState.currentPage < uiState.pages.size - 1) {
                    TextButton(onClick = onFinishIntro) {
                        Text(
                            text = stringResource(R.string.onboarding__skip),
                            style = MaterialTheme.typography.labelLarge,
                            color = MaterialTheme.colorScheme.primary
                        )
                    }
                }
            }
        },
        bottomBar = {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(AppTheme.dimensions.medium),
                verticalArrangement = Arrangement.spacedBy(AppTheme.dimensions.small),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                PagerIndicator(
                    count = uiState.pages.size,
                    currentPage = pagerState.currentPage
                )

                Spacer(modifier = Modifier.height(AppTheme.dimensions.medium))

                AppBaseButton(
                    modifier = Modifier.fillMaxWidth(),
                    text = if (pagerState.currentPage == uiState.pages.size - 1) {
                        stringResource(R.string.onboarding__get_started)
                    } else {
                        stringResource(R.string.onboarding__next)
                    },
                    onClick = {
                        if (pagerState.currentPage < uiState.pages.size - 1) {
                            scope.launch {
                                pagerState.animateScrollToPage(pagerState.currentPage + 1)
                            }
                        } else {
                            onFinishIntro()
                        }
                    }
                )
            }
        }
    ) { padding ->
        HorizontalPager(
            state = pagerState,
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
        ) { pageIndex ->
            val page = uiState.pages[pageIndex]
            IntroPageContent(page)
        }
    }
}

@Composable
private fun IntroPageContent(page: IntroPage) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(AppTheme.dimensions.large),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Box(
            modifier = Modifier
                .size(200.dp)
                .clip(CircleShape)
                .background(MaterialTheme.colorScheme.primaryContainer),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = "Logo",
                style = MaterialTheme.typography.headlineMedium,
                color = MaterialTheme.colorScheme.onPrimaryContainer
            )
        }

        Spacer(modifier = Modifier.height(AppTheme.dimensions.large))

        Text(
            text = stringResource(page.title),
            style = MaterialTheme.typography.headlineMedium.copy(
                fontWeight = FontWeight.Bold,
                fontSize = AppTheme.localFontsSize.xxSmall
            ),
            textAlign = TextAlign.Center
        )

        Spacer(modifier = Modifier.height(AppTheme.dimensions.small))

        Text(
            text = stringResource(page.description),
            style = MaterialTheme.typography.bodyLarge,
            textAlign = TextAlign.Center,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
    }
}

@Composable
private fun PagerIndicator(
    count: Int,
    currentPage: Int
) {
    Row(
        horizontalArrangement = Arrangement.spacedBy(AppTheme.dimensions.small),
        verticalAlignment = Alignment.CenterVertically
    ) {
        repeat(count) { index ->
            val isSelected = index == currentPage
            Box(
                modifier = Modifier
                    .size(if (isSelected) AppTheme.dimensions.xSmall else AppTheme.dimensions.small)
                    .clip(CircleShape)
                    .background(
                        if (isSelected) MaterialTheme.colorScheme.primary
                        else MaterialTheme.colorScheme.outlineVariant
                    )
            )
        }
    }
}
