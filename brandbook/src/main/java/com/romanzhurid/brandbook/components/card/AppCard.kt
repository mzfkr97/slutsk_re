package com.romanzhurid.brandbook.components.card

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import com.romanzhurid.brandbook.theme.AppTheme

@Composable
fun AppCard(
    modifier: Modifier = Modifier,
    borderColor: Color = AppTheme.colorScheme.outline,
    content: @Composable ColumnScope.() -> Unit,
) {
    Card(
        modifier = modifier.fillMaxWidth(),
        shape = RoundedCornerShape(AppTheme.dimensions.medium),
        colors = CardDefaults.cardColors(
            containerColor = AppTheme.colorScheme.surface
        ),
        elevation = CardDefaults.cardElevation(AppTheme.dimensions.xMicro),
        border = BorderStroke(
            width = AppTheme.dimensions.micro,
            color = borderColor
        )
    ) {
        content()
    }
}