package com.romanzhurid.brandbook.components.card
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import com.romanzhurid.brandbook.theme.AppTheme

@Composable 
fun NoContent(messageResId: Int) {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = stringResource(messageResId),
            style = AppTheme.typography.titleMedium
        )
    }
}