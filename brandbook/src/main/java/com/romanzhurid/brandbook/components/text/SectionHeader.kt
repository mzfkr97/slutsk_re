package com.romanzhurid.brandbook.components.text

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.romanzhurid.brandbook.theme.AppTheme

@Composable
fun SectionHeader(title: String) {
    Text(
        text = title,
        style = AppTheme.typography.labelMedium,
        color = AppTheme.colorScheme.primary,
        modifier = Modifier
            .fillMaxWidth()
            .background(AppTheme.colorScheme.primaryContainer)
            .padding(
                horizontal = AppTheme.dimensions.medium,
                vertical = AppTheme.dimensions.small
            )
    )
}