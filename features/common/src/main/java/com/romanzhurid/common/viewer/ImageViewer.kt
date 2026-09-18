package com.romanzhurid.common.viewer

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import coil3.compose.AsyncImage
import net.engawapg.lib.zoomable.rememberZoomState
import net.engawapg.lib.zoomable.zoomable

@Composable
fun ImageViewer(
    imageUrl: String,
    onDismiss: () -> Unit,
) {
    Dialog(
        properties = DialogProperties(
            usePlatformDefaultWidth = false,
            decorFitsSystemWindows = false,
        ),
        onDismissRequest = onDismiss,
    ) {
        val zoomState = rememberZoomState()

        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.Black)
        ) {
            AsyncImage(
                model = imageUrl,
                contentDescription = null,
                onSuccess = { state ->
                    zoomState.setContentSize(state.painter.intrinsicSize)
                },
                modifier = Modifier
                    .fillMaxSize()
                    .zoomable(zoomState),
                contentScale = ContentScale.Fit,
            )

            IconButton(
                modifier = Modifier
                    .align(Alignment.TopEnd)
                    .padding(
                        top = 48.dp,
                        end = 24.dp
                    )
                    .background(
                        color = Color.LightGray.copy(alpha = 0.7f),
                        shape = CircleShape
                    ),
                onClick = onDismiss,
            ) {
                Icon(
                    imageVector = Icons.Default.Close,
                    contentDescription = null,
                    tint = Color.White,
                )
            }
        }
    }
}
