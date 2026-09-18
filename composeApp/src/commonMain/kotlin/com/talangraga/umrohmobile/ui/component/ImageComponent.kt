package com.talangraga.umrohmobile.ui.component

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.layout.ContentScale
import coil3.compose.AsyncImage
import coil3.compose.LocalPlatformContext
import coil3.request.ImageRequest
import coil3.request.crossfade
import org.jetbrains.compose.resources.painterResource
import talangragaumrohmobile.composeapp.generated.resources.Res
import talangragaumrohmobile.composeapp.generated.resources.compose_multiplatform

import androidx.compose.runtime.remember
import com.talangraga.umrohmobile.presentation.utils.resolveImageUrl

@Composable
fun BasicImage(
    model: Any?,
    placeholder: Painter? = painterResource(Res.drawable.compose_multiplatform),
    error: Painter? = painterResource(Res.drawable.compose_multiplatform),
    contentScale: ContentScale = ContentScale.Crop,
    modifier: Modifier = Modifier
) {
    val context = LocalPlatformContext.current
    val processedModel = remember(model) {
        if (model is String) resolveImageUrl(model) else model
    }
    AsyncImage(
        model = ImageRequest.Builder(context)
            .data(processedModel)
            .crossfade(true)
            .build(),
        placeholder = placeholder,
        error = error,
        contentDescription = null,
        contentScale = contentScale,
        modifier = modifier
    )
}
