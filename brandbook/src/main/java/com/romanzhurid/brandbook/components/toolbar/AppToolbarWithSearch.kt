package com.romanzhurid.brandbook.components.toolbar

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.outlined.ArrowBack
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.unit.dp
import com.romanzhurid.brandbook.ext.EMPTY_STRING
import com.romanzhurid.brandbook.theme.AppTheme

@Composable
fun AppToolbarWithSearch(
    title: String,
    query: String,
    showBackBtn: Boolean = true,
    onQueryChange: (String) -> Unit,
    onBack: (() -> Unit)? = null,
) {
    var isSearchActive by remember { mutableStateOf(false) }

    val focusRequester = remember { FocusRequester() }
    val keyboardController = LocalSoftwareKeyboardController.current

    LaunchedEffect(isSearchActive) {
        if (isSearchActive) {
            focusRequester.requestFocus()
        } else {
            keyboardController?.hide()
        }
    }

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .statusBarsPadding()
            .height(AppTheme.dimensions.xxLarge)
            .padding(horizontal = AppTheme.dimensions.small)
    ) {

        // ← Back (ВСЕГДА back, без логики поиска)
        if (showBackBtn && onBack != null) {
            IconButton(
                onClick = onBack,
                modifier = Modifier.align(Alignment.CenterStart)
            ) {
                Icon(
                    imageVector = Icons.AutoMirrored.Outlined.ArrowBack,
                    contentDescription = "Back"
                )
            }
        }

        // CENTER
        if (isSearchActive) {
            TextField(
                value = query,
                onValueChange = onQueryChange,
                modifier = Modifier
                    .fillMaxWidth()
                    .align(Alignment.Center)
                    .padding(start = if (showBackBtn) 48.dp else 0.dp)
                    .focusRequester(focusRequester),
                placeholder = { Text("Search") },
                singleLine = true,
                leadingIcon = {
                    Icon(
                        imageVector = Icons.Default.Search,
                        contentDescription = null
                    )
                },
                trailingIcon = {
                    IconButton(
                        onClick = {
//                            if (query.isNotEmpty()) {
//                                onQueryChange(EMPTY_STRING)
//                            } else {
//                                isSearchActive = false
//                                keyboardController?.hide()
//                            }

                            isSearchActive = false
                            onQueryChange(EMPTY_STRING)
                            keyboardController?.hide()

                        }
                    ) {
                        Icon(
                            imageVector = Icons.Default.Close,
                            contentDescription = "Close search"
                        )
                    }
                }
            )
        } else {
            Text(
                text = title,
                style = AppTheme.typography.titleLarge,
                modifier = Modifier.align(Alignment.Center)
            )
        }

        // → Search
        if (!isSearchActive) {
            IconButton(
                onClick = { isSearchActive = true },
                modifier = Modifier.align(Alignment.CenterEnd)
            ) {
                Icon(
                    imageVector = Icons.Default.Search,
                    contentDescription = "Search"
                )
            }
        }
    }
}