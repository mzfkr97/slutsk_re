package com.romanzhurid.cinema.navigation

import androidx.compose.runtime.Composable
import androidx.navigation3.runtime.entryProvider
import com.romanzhurid.cinema.ui.CinemaScreen
import com.romanzhurid.navigation.AppRoute
import com.romanzhurid.navigation.host.FeatureHost

@Composable
fun CinemaFeatureHost(route: AppRoute.Cinema) {
    val entryProvider = entryProvider {
        entry<CinemaFeatureRoute.Cinema> {
            CinemaScreen()
        }
        entry<CinemaFeatureRoute.CinemaDetails> {
        }
        entry<CinemaFeatureRoute.CinemaGallery> {
        }
    }

    FeatureHost(
        route = route.instanceId,
        clearComponent = {},
        initialStack = {
            listOf(CinemaFeatureRoute.Cinema)
        },
        entryProviderFactory = {
            entryProvider
        }
    )
}
