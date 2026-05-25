package com.romanzhurid.home.navigation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.romanzhurid.navigation.featurehost.NavigationChannelProvider
import javax.inject.Inject

class HomeFeatureHostViewModelFactory @Inject constructor(
    private val navigationChannelProvider: NavigationChannelProvider
) : ViewModelProvider.Factory {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return HomeFeatureHostViewModel(
            navigationChannelProvider = navigationChannelProvider
        ) as T
    }
}
