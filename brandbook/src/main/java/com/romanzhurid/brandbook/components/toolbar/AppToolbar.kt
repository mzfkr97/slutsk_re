package com.romanzhurid.brandbook.components.toolbar

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.outlined.ArrowBack
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import com.romanzhurid.brandbook.R
import com.romanzhurid.brandbook.theme.AppTheme

@Preview(showBackground = true)
@Composable
fun AppToolbarPreview() {
    AppTheme {
        AppToolbar(
            title = R.string.common__attention,
        )
    }
}

@Composable
fun AppToolbar(
    title: Int,
    subtitle: String? = null,
    showBackBtn: Boolean = true,
    onBack: (() -> Unit)? = null,
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .statusBarsPadding()
            .height(AppTheme.dimensions.xxLarge)
            .padding(horizontal = AppTheme.dimensions.small)
    ) {
        if (showBackBtn && onBack != null) {
            IconButton(
                onClick = { onBack() },
                modifier = Modifier.align(Alignment.CenterStart)
            ) {
                Icon(
                    imageVector = Icons.AutoMirrored.Outlined.ArrowBack,
                    contentDescription = "Back"
                )
            }
        }

        Column(
            modifier = Modifier.align(Alignment.Center),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = stringResource(title),
                style = AppTheme.typography.titleLarge
            )
            subtitle?.let {
                Text(
                    text = it,
                    style = AppTheme.typography.labelSmall
                )
            }
        }
    }
}
