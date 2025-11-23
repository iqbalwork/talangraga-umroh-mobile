package com.talangraga.umrohmobile.ui.component

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.Placeholder
import coil3.compose.AsyncImage
import coil3.compose.LocalPlatformContext
import coil3.request.ImageRequest
import coil3.request.crossfade
import org.jetbrains.compose.resources.painterResource
import talangragaumrohmobile.composeapp.generated.resources.Res
import talangragaumrohmobile.composeapp.generated.resources.compose_multiplatform

@Composable
fun BasicImage(
    model: String,
    placeholder: Painter? = painterResource(Res.drawable.compose_multiplatform),
    error: Painter? = painterResource(Res.drawable.compose_multiplatform),
    contentScale: ContentScale = ContentScale.Crop,
    modifier: Modifier = Modifier
) {
    val context = LocalPlatformContext.current
    AsyncImage(
        model = ImageRequest.Builder(context)
            .data(model)
            .crossfade(true)
            .build(),
        placeholder = placeholder,
        error = error,
        contentDescription = null,
        contentScale = contentScale,
        modifier = modifier
    )
}
