package com.romanzhurid.re.activity

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.blur
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.lifecycle.viewmodel.navigation3.rememberViewModelStoreNavEntryDecorator
import androidx.navigation3.runtime.NavEntry
import androidx.navigation3.runtime.entryProvider
import androidx.navigation3.runtime.rememberSaveableStateHolderNavEntryDecorator
import com.romanzhurid.brandbook.components.errorbottomsheet.ErrorBottomSheet
import com.romanzhurid.brandbook.components.progress.ProgressItem
import com.romanzhurid.brandbook.theme.AppTheme
import com.romanzhurid.common.ProgressState
import com.romanzhurid.common.uistate.CollectEventEffect
import com.romanzhurid.common.uistate.collectUiState
import com.romanzhurid.home.navigation.HomeFeatureHost
import com.romanzhurid.navigation.AppNavDisplay
import com.romanzhurid.navigation.AppRoute
import com.romanzhurid.navigation.Route
import com.romanzhurid.navigation.composition.LocalAppNavigator
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

        splashScreen.setKeepOnScreenCondition {
            viewModel.stateValue.backStack == null
        }

        splashScreen.setSlideDownExitAnimation()

        enableEdgeToEdge()

        setContent {
            val uiState by viewModel.collectUiState()
            viewModel.CollectEventEffect { event ->
                when (event) {
                    Event.Finish -> {
                        onExit()
                    }
                }
            }
            CompositionLocalProvider(
                LocalAppNavigator provides viewModel.getNavigator()
            ) {
                AppTheme {
                    MainScreen(
                        uiState = uiState,
                        resetErrorState = viewModel::resetErrorState,
                        activityBack = viewModel::activityBack,
                    )
                }
            }
        }
    }

    private fun onExit() {
        finish()
    }
}

@Composable
fun MainScreen(
    uiState: UiState,
    resetErrorState: () -> Unit,
    activityBack: () -> Unit,
) {
    val appEntryProvider = remember(Unit) {
        lateinit var entryProvider: (Route) -> NavEntry<Route>

        entryProvider = entryProvider {
            entry<AppRoute.Home> { route ->
                HomeFeatureHost(route)
            }
        }
        entryProvider
    }

    Box(modifier = Modifier.fillMaxSize()) {
        Scaffold(
            contentWindowInsets = WindowInsets(0, 0, 0, 0),
            modifier = Modifier
                .fillMaxSize()
                .then(
                    if (uiState.progressState is ProgressState.Show) {
                        Modifier.blur(AppTheme.dimensions.small)
                    } else {
                        Modifier
                    }
                )
        ) { innerPadding ->
            uiState.backStack?.let { backStack ->
                AppNavDisplay(
                    modifier = Modifier.padding(innerPadding),
                    backStack = backStack,
                    entryDecorators = listOf(
                        rememberSaveableStateHolderNavEntryDecorator(),
                        rememberViewModelStoreNavEntryDecorator()
                    ),
                    onBack = activityBack,
                    entryProvider = appEntryProvider
                )
            }
        }
    }

    if (uiState.errorState != null) {
        ErrorBottomSheet(
            errorState = uiState.errorState,
            onClick = resetErrorState,
            onDismiss = resetErrorState
        )
    }

    (uiState.progressState as? ProgressState.Show)?.let { loading ->
        ProgressItem(
            resId = loading.resId,
            onCancel = (loading.onCancel)
        )
    }
}