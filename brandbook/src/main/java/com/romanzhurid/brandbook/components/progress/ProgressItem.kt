package com.romanzhurid.brandbook.components.progress

import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.window.Dialog
import com.romanzhurid.brandbook.R
import com.romanzhurid.brandbook.theme.AppTheme

@Preview(showBackground = true)
@Composable
fun ProgressItemPreview() {
    AppTheme {
        ProgressItem(
            resId = R.string.common__loading,
            onCancel = { },
        )
    }
}

@Composable
fun ProgressItem(
    resId: Int,
    onCancel: (() -> Unit)? = null
) {
    Dialog(
        onDismissRequest = {
            onCancel?.invoke()
        }
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .clickable(
                    interactionSource = remember { MutableInteractionSource() },
                    indication = null
                ) {},
            contentAlignment = Alignment.Center
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                DotsCircularLoader()

                Spacer(modifier = Modifier.height(AppTheme.dimensions.xMedium))

                Text(
                    text = stringResource(resId),
                    fontSize = AppTheme.localFontsSize.medium,
                    color = AppTheme.colorScheme.onPrimary,
                    textAlign = TextAlign.Center
                )
            }
        }
    }
}
