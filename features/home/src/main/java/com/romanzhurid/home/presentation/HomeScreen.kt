package com.romanzhurid.home.presentation

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import com.romanzhurid.brandbook.components.toolbar.AppToolbar
import com.romanzhurid.brandbook.R
import com.romanzhurid.navigation.AppRoute
import com.romanzhurid.navigation.composition.LocalAppNavigator

@Composable
fun HomeScreen() {
    val navigator = LocalAppNavigator.current
    Scaffold(
        topBar = {
            AppToolbar(
                title = stringResource(R.string.home__title),
                showBackBtn = false
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            Button(onClick = { navigator.navigate(AppRoute.Currencies()) }) {
                Text(text = "Go to Currencies")
            }
        }
    }
}
