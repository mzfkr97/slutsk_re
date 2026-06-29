package com.romanzhurid.brandbook.components.button

import androidx.compose.animation.AnimatedContent
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import com.romanzhurid.brandbook.R
import com.romanzhurid.brandbook.theme.AppTheme

@Preview(showBackground = true)
@Composable
fun AppBaseButtonPreview() {
    AppTheme {
        AppBaseButton(
            text = stringResource(R.string.common__loading),
            onClick = { }
        )
    }
}

@Composable
fun AppBaseButton(
    modifier: Modifier = Modifier,
    text: String? = null,
    isEnabled: Boolean = true,
    isLoading: Boolean = false,
    onClick: () -> Unit,
    content: (@Composable RowScope.() -> Unit)? = null
) {
    Button(
        modifier = modifier.heightIn(min = AppTheme.dimensions.buttonHeight),
        enabled = isEnabled && !isLoading,
        onClick = onClick,
        shape = RoundedCornerShape(AppTheme.dimensions.xxxMedium),
        contentPadding = PaddingValues(
            horizontal = AppTheme.dimensions.medium,
            vertical = AppTheme.dimensions.small
        ),
        colors = ButtonDefaults.buttonColors(
            containerColor = MaterialTheme.colorScheme.primary,
            contentColor = MaterialTheme.colorScheme.onPrimary,
            disabledContainerColor = MaterialTheme.colorScheme.surfaceVariant,
            disabledContentColor = MaterialTheme.colorScheme.onSurfaceVariant
        )
    ) {
        AnimatedContent(
            targetState = isLoading,
            label = "button_loading_animation"
        ) { loading ->
            if (loading) {
                CircularProgressIndicator(
                    modifier = Modifier.size(AppTheme.dimensions.xMedium),
                    strokeWidth = AppTheme.dimensions.xMicro,
                    color = MaterialTheme.colorScheme.onPrimary
                )
            } else {
                when {
                    content != null -> content()
                    text != null -> Text(
                        text = text,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                }
            }
        }
    }
}
