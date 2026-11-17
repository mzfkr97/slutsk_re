package com.romanzhurid.common.ext

import androidx.compose.runtime.Composable
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.ui.Modifier
import com.romanzhurid.brandbook.theme.AppTheme

@Composable
fun AppSmallSpacer() {
    Spacer(modifier = Modifier.padding(top = AppTheme.dimensions.small))
}
