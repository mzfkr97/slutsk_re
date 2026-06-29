package com.romanzhurid.home.navigation

import androidx.lifecycle.ViewModel
import com.romanzhurid.navigation.navigator.NavigationDelegate
import com.romanzhurid.navigation.navigator.NavigationDelegateImpl

class HomeFeatureHostViewModel :
    ViewModel(),
    NavigationDelegate<HomeFeatureRoute> by NavigationDelegateImpl(
        initialStack = listOf(HomeFeatureRoute.Home)
    ) {


}
