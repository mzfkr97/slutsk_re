package com.romanzhurid.brandbook.ext

import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.withStyle
import com.romanzhurid.brandbook.theme.AppTheme

@Composable
fun DefaultSpacer() {
    Spacer(modifier = Modifier.height(AppTheme.dimensions.small))
}

@Composable
fun highlightText(
    text: String,
    query: String
): AnnotatedString {
    if (query.isBlank()) return AnnotatedString(text)
    val lowerText = text.lowercase()
    val lowerQuery = query.lowercase()
    val startIndex = lowerText.indexOf(lowerQuery)
    if (startIndex == -1) return AnnotatedString(text)
    val endIndex = startIndex + query.length

    return buildAnnotatedString {
        append(text.substring(0, startIndex))
        withStyle(
            style = SpanStyle(
                color = AppTheme.colorScheme.primary,
                fontWeight = FontWeight.Bold
            )
        ) {
            append(text.substring(startIndex, endIndex))
        }
        append(text.substring(endIndex))
    }
}
