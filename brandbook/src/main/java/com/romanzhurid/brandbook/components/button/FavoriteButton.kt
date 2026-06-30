package com.romanzhurid.brandbook.components.button

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.outlined.StarBorder
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.runtime.Composable
import com.romanzhurid.brandbook.theme.AppTheme

@Composable
fun FavoriteButton(
    isFavorite: Boolean,
    onClick: () -> Unit
) {
    IconButton(onClick = onClick) {
        Icon(
            imageVector = if (isFavorite) {
                Icons.Filled.Star
            } else {
                Icons.Outlined.StarBorder
            },
            contentDescription = null,
            tint = if (isFavorite) {
                AppTheme.colorScheme.tertiary
            } else {
                AppTheme.colorScheme.onSurfaceVariant
            },
        )
    }
}
