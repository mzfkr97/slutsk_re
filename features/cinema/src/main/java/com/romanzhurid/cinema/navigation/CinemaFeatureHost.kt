package com.romanzhurid.cinema.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation3.runtime.entryProvider
import com.romanzhurid.cinema.di.CinemaComponentDependenciesProvider
import com.romanzhurid.cinema.di.CinemaComponentHolder
import com.romanzhurid.cinema.ui.CinemaScreen
import com.romanzhurid.cinema.ui.CinemaViewModel
import com.romanzhurid.navigation.AppRoute
import com.romanzhurid.navigation.host.FeatureHost

@Composable
fun CinemaFeatureHost(route: AppRoute.Cinema) {
    val context = LocalContext.current.applicationContext

    val component = remember(route.instanceId) {
        CinemaComponentHolder.get(
            instanceId = route.instanceId,
            dependencies = (context as CinemaComponentDependenciesProvider)
                .cinemaComponentDependencies
        )
    }

    val entryProvider = remember(component) {
        entryProvider {
            entry<CinemaFeatureRoute.Cinema> {
                val viewModel = viewModel<CinemaViewModel>(
                    factory = component.getCinemaViewModelFactory()
                )
                CinemaScreen(viewModel)
            }
            entry<CinemaFeatureRoute.CinemaDetails> {
//                val viewModel = viewModel<CinemaViewModel>(
//                    factory = component.getCinemaViewModelFactory()
//                )
//                CinemaScreen(viewModel)
            }
            entry<CinemaFeatureRoute.CinemaGallery> {
//                val viewModel = viewModel<CinemaViewModel>(
//                    factory = component.getCinemaGalleryViewModelFactory()
//                )
//                CinemaGalleryScreen(viewModel)
            }
        }
    }

    FeatureHost(
        route = route.instanceId,
        clearComponent = {
            CinemaComponentHolder.clear(route.instanceId)
        },
        initialStack = {
            listOf(CinemaFeatureRoute.Cinema)
        },
        entryProviderFactory = {
            entryProvider
        }
    )
}
