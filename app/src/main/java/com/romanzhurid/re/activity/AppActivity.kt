package com.romanzhurid.re.activity

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.navigation3.runtime.NavKey
import androidx.navigation3.runtime.entryProvider
import com.romanzhurid.brandbook.components.errorbottomsheet.ErrorBottomSheet
import com.romanzhurid.brandbook.components.progress.ProgressItem
import com.romanzhurid.brandbook.theme.AppTheme
import com.romanzhurid.bus.navigation.BusFeatureHost
import com.romanzhurid.cinema.navigation.CinemaFeatureHost
import com.romanzhurid.common.ProgressState
import com.romanzhurid.common.uistate.CollectEventEffect
import com.romanzhurid.common.uistate.collectUiState
import com.romanzhurid.currencies.navigation.CurrencyFeatureHost
import com.romanzhurid.home.navigation.HomeFeatureHost
import com.romanzhurid.navigation.AppNavDisplay
import com.romanzhurid.navigation.AppRoute
import com.romanzhurid.navigation.composition.LocalNavigator
import com.romanzhurid.navigation.navigator.NavigatorImpl
import com.romanzhurid.navigation.navigator.rememberNavigator
import com.romanzhurid.onboarding.navigation.OnboardingFeatureHost
import com.romanzhurid.re.activity.AppActivityViewModel.Event
import com.romanzhurid.re.activity.AppActivityViewModel.UiState
import com.romanzhurid.re.ext.setSlideDownExitAnimation
import com.romanzhurid.settings.navigation.SettingsFeatureHost
import org.koin.androidx.viewmodel.ext.android.viewModel

class AppActivity : ComponentActivity() {

    private val viewModel: AppActivityViewModel by viewModel()

    override fun onCreate(savedInstanceState: Bundle?) {

        val splashScreen = installSplashScreen()
        super.onCreate(savedInstanceState)

        enableEdgeToEdge()

        setContent {
            val navigator = rememberNavigator(viewModel.resolveBackStack())

            val uiState by viewModel.collectUiState()

            splashScreen.setSlideDownExitAnimation()

            viewModel.CollectEventEffect { event ->
                when (event) {
                    is Event.Finish -> finish()
                }
            }

            CompositionLocalProvider(
                LocalNavigator provides navigator
            ) {
                AppTheme(isSystemDarkTheme = uiState.isDarkTheme) {
                    MainScreen(
                        uiState = uiState,
                        resetErrorState = viewModel::resetErrorState,
                        activityBack = {
                            if (navigator.back()) {
                                true
                            } else {
                                viewModel.finish()
                                true
                            }
                        },
                        navigator = navigator
                    )
                }
            }
        }
    }
}

@Composable
fun MainScreen(
    uiState: UiState,
    resetErrorState: () -> Unit,
    activityBack: () -> Boolean,
    navigator: NavigatorImpl<NavKey>,
) {
    val appEntryProvider = remember {
        entryProvider<NavKey> {
            entry<AppRoute.Onboarding>(content = ::OnboardingFeatureHost)
            entry<AppRoute.Home>(content = ::HomeFeatureHost)
            entry<AppRoute.Currencies>(content = ::CurrencyFeatureHost)
            entry<AppRoute.Settings>(content = ::SettingsFeatureHost)
            entry<AppRoute.Cinema>(content =::CinemaFeatureHost)
            entry<AppRoute.Bus>(content = ::BusFeatureHost)
        }
    }

    Scaffold(
        contentWindowInsets = WindowInsets(0, 0, 0, 0)
    ) { padding ->
        AppNavDisplay(
            modifier = Modifier.padding(padding),
            backStack = navigator.backStack,
            entryProvider = appEntryProvider,
            onBack = activityBack
        )
    }

    uiState.errorState?.let {
        ErrorBottomSheet(
            errorState = it,
            onClick = resetErrorState,
            onDismiss = resetErrorState
        )
    }

    (uiState.progressState as? ProgressState.Show)?.let { loading ->
        ProgressItem(
            resId = loading.resId,
            onCancel = loading.onCancel
        )
    }
}