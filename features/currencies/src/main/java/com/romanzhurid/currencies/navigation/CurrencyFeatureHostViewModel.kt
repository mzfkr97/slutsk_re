package com.romanzhurid.currencies.navigation

import androidx.lifecycle.ViewModel
import com.romanzhurid.navigation.navigator.NavigationDelegate
import com.romanzhurid.navigation.navigator.NavigationDelegateImpl

class CurrencyFeatureHostViewModel :
    ViewModel(),
    NavigationDelegate<CurrencyFeatureRoute> by NavigationDelegateImpl(
        initialStack = listOf(CurrencyFeatureRoute.Currencies)
    )
