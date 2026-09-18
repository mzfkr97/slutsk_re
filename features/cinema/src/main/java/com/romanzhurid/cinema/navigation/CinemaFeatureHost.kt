package com.romanzhurid.cinema.navigation

import androidx.compose.runtime.Composable
import androidx.navigation3.runtime.entryProvider
import com.romanzhurid.cinema.di.CinemaFeatureScope
import com.romanzhurid.cinema.ui.CinemaScreen
import com.romanzhurid.cinema.ui.CinemaViewModel
import com.romanzhurid.navigation.AppRoute
import com.romanzhurid.navigation.host.FeatureHost
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun CinemaFeatureHost(route: AppRoute.Cinema) {
    FeatureHost(
        route = route,
        featureScope = CinemaFeatureScope,
        initialStack = {
            listOf(CinemaFeatureRoute.Cinema)
        },
        entryProviderFactory = { scope ->
            entryProvider {
                entry<CinemaFeatureRoute.Cinema> {
                    val viewModel = koinViewModel<CinemaViewModel>(scope = scope)
                    CinemaScreen(viewModel)
                }
            }
        }
    )
}
