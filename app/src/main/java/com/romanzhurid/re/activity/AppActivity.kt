package com.romanzhurid.re.activity

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
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
import com.romanzhurid.cinema.navigation.CinemaFeatureHost
import com.romanzhurid.common.ProgressState
import com.romanzhurid.common.uistate.CollectEventEffect
import com.romanzhurid.common.uistate.collectUiState
import com.romanzhurid.onboarding.navigation.OnboardingFeatureHost
import com.romanzhurid.home.navigation.HomeFeatureHost
import com.romanzhurid.currencies.navigation.CurrencyFeatureHost
import com.romanzhurid.settings.navigation.SettingsFeatureHost
import com.romanzhurid.navigation.AppNavDisplay
import com.romanzhurid.navigation.AppRoute
import com.romanzhurid.navigation.composition.LocalNavigator
import com.romanzhurid.navigation.navigator.NavigatorImpl
import com.romanzhurid.navigation.navigator.rememberNavigator
import com.romanzhurid.re.activity.MainActivityViewModel.*
import com.romanzhurid.re.application.App
import com.romanzhurid.re.ext.setSlideDownExitAnimation
import javax.inject.Inject

class AppActivity : ComponentActivity() {

    @Inject
    lateinit var factory: MainActivityViewModelFactory
    private val viewModel: MainActivityViewModel by viewModels { factory }

    override fun onCreate(savedInstanceState: Bundle?) {
        (applicationContext as App).appComponent.inject(this)

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
            entry<AppRoute.Onboarding> { OnboardingFeatureHost(it) }
            entry<AppRoute.Home> { HomeFeatureHost(it) }
            entry<AppRoute.Currencies> { CurrencyFeatureHost(it) }
            entry<AppRoute.Settings> { SettingsFeatureHost(it) }
            entry<AppRoute.Cinema> { CinemaFeatureHost(it) }
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