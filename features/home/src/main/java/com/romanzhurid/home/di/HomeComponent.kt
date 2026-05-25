package com.romanzhurid.home.di

import com.romanzhurid.home.navigation.HomeFeatureHostViewModelFactory
import com.romanzhurid.home.home.HomeViewModelFactory
import dagger.Component

@HomeScope
@Component(
    dependencies = [HomeComponentDependencies::class]
)
interface HomeComponent {

    fun getHomeFeatureHostViewModelFactory(): HomeFeatureHostViewModelFactory
    fun getHomeViewModelFactory(): HomeViewModelFactory

    companion object {
        private var component: HomeComponent? = null

        fun get(): HomeComponent {
            return component ?: throw NotImplementedError("This component must be initialized")
        }
    }
}
