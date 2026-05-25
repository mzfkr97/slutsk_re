package com.romanzhurid.brandbook.components.errorbottomsheet

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.slideInVertically
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ErrorOutline
import androidx.compose.material.icons.filled.Storage
import androidx.compose.material.icons.filled.WifiOff
import androidx.compose.material3.BottomSheetDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.romanzhurid.brandbook.components.button.AppBaseButton
import com.romanzhurid.brandbook.theme.AppTheme
import kotlinx.coroutines.launch
import com.romanzhurid.brandbook.R
import com.romanzhurid.brandbook.components.errorbottomsheet.ErrorState.*

@Preview(showBackground = true)
@Composable
fun ErrorBottomSheetPreview() {
    AppTheme {
        ErrorBottomSheet(
            ErrorState(
                stringResource(R.string.common__error),
                stringResource(R.string.common__loading)
            )
        )
    }
}
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ErrorBottomSheet(
    errorState: ErrorState?,
    onClick: (() -> Unit)? = null,
    onDismiss: (() -> Unit)? = null
) {
    if (errorState == null) return

    val icon = when (errorState.type) {
        ErrorType.NO_INTERNET -> Icons.Default.WifiOff
        ErrorType.NO_CONNECTION_TO_SERVER -> Icons.Default.Storage
        ErrorType.UNKNOWN -> Icons.Default.ErrorOutline
    }

    val sheetState = rememberModalBottomSheetState(
        skipPartiallyExpanded = true
    )
    val scope = rememberCoroutineScope()

    ModalBottomSheet(
        sheetState = sheetState,
        onDismissRequest = { onDismiss?.invoke() },
        dragHandle = { BottomSheetDefaults.DragHandle() },
        contentWindowInsets = { WindowInsets(0, 0, 0, 0) }
    ) {

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .navigationBarsPadding()
                .padding(horizontal = AppTheme.dimensions.medium)
                .padding(bottom = AppTheme.dimensions.large),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(AppTheme.dimensions.medium)
        ) {

            // ✅ Анимация иконки
            AnimatedVisibility(visible = true,
                enter = fadeIn() + slideInVertically { it / 2 }) {
                Icon(
                    imageVector = icon,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.error,
                    modifier = Modifier.size(48.dp)
                )
            }
            Text(
                text = errorState.title.orEmpty(),
                style = MaterialTheme.typography.titleLarge,
                color = MaterialTheme.colorScheme.error
            )
            Text(
                text = errorState.message.orEmpty(),
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                textAlign = TextAlign.Center
            )

            Spacer(modifier = Modifier.height(8.dp))

            AppBaseButton(
                modifier = Modifier.fillMaxWidth(),
                text = stringResource(android.R.string.ok),
                onClick = {
                    scope.launch {
                        sheetState.hide()
                        onClick?.invoke()
                    }
                }
            )
        }
    }
}
