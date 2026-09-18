package com.romanzhurid.brandbook.components.text

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.romanzhurid.brandbook.theme.AppTheme

@Composable
fun SectionHeader(title: String) {
    Row(
        modifier = Modifier.fillMaxWidth()
            .background(color = AppTheme.colorScheme.onPrimary)
            .padding(8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = title,
            style = AppTheme.typography.titleMedium,
            color = AppTheme.colorScheme.primary
        )

        Spacer(modifier = Modifier.width(12.dp))

        HorizontalDivider(
            color = AppTheme.colorScheme.outlineVariant
        )
    }
}
