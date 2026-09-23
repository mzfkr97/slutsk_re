package com.romanzhurid.brandbook.components.button

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.scaleIn
import androidx.compose.animation.scaleOut
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.outlined.StarBorder
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ripple
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import com.romanzhurid.brandbook.theme.AppTheme

@Composable
fun FavoriteButton(
    isFavorite: Boolean,
    onClick: () -> Unit
) {
    Box(
        modifier = Modifier
            .size(48.dp)
            .clip(CircleShape)
            .clickable(
                interactionSource = remember { MutableInteractionSource() },
                indication = ripple(
                    bounded = false,
                    radius = 28.dp,
                    color = AppTheme.colorScheme.tertiary
                ),
                onClick = onClick
            ),
        contentAlignment = Alignment.Center
    ) {
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
            }
        )
    }
}
