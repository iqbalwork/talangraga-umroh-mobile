@file:Suppress("USELESS_IS_CHECK")

package com.talangraga.umrohmobile.ui.component

import androidx.compose.animation.core.Animatable
import androidx.compose.foundation.gestures.awaitEachGesture
import androidx.compose.foundation.gestures.awaitFirstDown
import androidx.compose.foundation.gestures.calculateCentroid
import androidx.compose.foundation.gestures.calculateCentroidSize
import androidx.compose.foundation.gestures.calculatePan
import androidx.compose.foundation.gestures.calculateZoom
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.input.pointer.PointerInputScope
import androidx.compose.ui.input.pointer.changedToDownIgnoreConsumed
import androidx.compose.ui.input.pointer.changedToUpIgnoreConsumed
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.input.pointer.util.VelocityTracker
import androidx.compose.ui.input.pointer.util.addPointerInputChange
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.unit.IntSize
import androidx.compose.ui.util.fastAny
import androidx.compose.ui.util.fastForEach
import coil3.compose.AsyncImagePainter
import coil3.compose.SubcomposeAsyncImage
import coil3.compose.SubcomposeAsyncImageContent
import kotlinx.coroutines.launch
import kotlin.math.abs

/**
 * iqbalfauzi
 * Email: work.iqbalfauzi@gmail.com
 * Github: https://github.com/iqbalwork
 */
@Composable
fun ZoomableCoilImage(
    model: Any?,
    modifier: Modifier = Modifier,
    onDismiss: () -> Unit = {},
    onDragChange: (Float) -> Unit = {}
) {

    val scope = rememberCoroutineScope() // 1. GET THE SCOPE

    // Zoom & Pan State
    var scale by remember { mutableStateOf(1f) }
    var panOffset by remember { mutableStateOf(Offset.Zero) }

    // Swipe Dismiss State
    val swipeOffsetY = remember { Animatable(0f) }
    var size by remember { mutableStateOf(IntSize.Zero) }

    Box(
        modifier = modifier
            .fillMaxSize()
            .pointerInput(Unit) {
                detectZoomAndSwipe(
                    onGesture = { _, pan, zoom, _, _, _ ->
                        val newScale = (scale * zoom).coerceIn(1f, 4f)
                        scale = newScale

                        if (scale > 1f) {
                            // Zooming: Reset swipe if needed
                            if (swipeOffsetY.value != 0f) {
                                scope.launch { swipeOffsetY.snapTo(0f) } // 2. USE SCOPE
                                onDragChange(0f)
                            }

                            val maxPanningX = (size.width * (scale - 1)) / 2
                            val maxPanningY = (size.height * (scale - 1)) / 2
                            val newX = (panOffset.x + pan.x).coerceIn(-maxPanningX, maxPanningX)
                            val newY = (panOffset.y + pan.y).coerceIn(-maxPanningY, maxPanningY)
                            panOffset = Offset(newX, newY)
                        } else {
                            // Swiping (Normal scale)
                            panOffset = Offset.Zero
                            val newSwipe = swipeOffsetY.value + pan.y
                            // 2. USE SCOPE for snapTo
                            scope.launch { swipeOffsetY.snapTo(newSwipe.coerceAtLeast(0f)) }

                            val progress = (newSwipe / size.height.toFloat()).coerceIn(0f, 1f)
                            onDragChange(progress)
                        }
                    },
                    onEnd = { velocity ->
                        if (scale == 1f) {
                            val swipeDist = swipeOffsetY.value
                            val threshold = size.height * 0.15f
                            // Fling detection: if velocity > 1000, dismiss even if distance is short
                            val isFling = velocity.y > 1000f

                            if (swipeDist > threshold || isFling) {
                                onDismiss()
                            } else {
                                // Snap back
                                scope.launch { swipeOffsetY.animateTo(0f) } // 2. USE SCOPE
                                onDragChange(0f)
                            }
                        }
                    }
                )
            }
            .onSizeChanged { size = it } // Standard Modifier
    ) {
        SubcomposeAsyncImage(
            model = model,
            contentDescription = null,
            contentScale = ContentScale.Fit,
            modifier = Modifier.fillMaxSize()
        ) {
            val state = painter.state
            when (state) {
                is AsyncImagePainter.State.Loading -> {
                    Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                        CircularProgressIndicator(color = Color.White)
                    }
                }
                else -> {
                    SubcomposeAsyncImageContent(
                        modifier = Modifier
                            .align(Alignment.Center)
                            .graphicsLayer {
                                scaleX = scale
                                scaleY = scale
                                translationX = panOffset.x
                                translationY = panOffset.y + swipeOffsetY.value
                            }
                    )
                }
            }
        }
    }
}

/**
 * A custom gesture detector that tracks Zoom, Pan, and importantly: VELOCITY on release.
 * This fixes the issue where the "Up" event was being missed.
 */
suspend fun PointerInputScope.detectZoomAndSwipe(
    onGesture: (centroid: Offset, pan: Offset, zoom: Float, rotation: Float, timeMillis: Long, pointers: List<androidx.compose.ui.input.pointer.PointerInputChange>) -> Unit,
    onEnd: (velocity: androidx.compose.ui.unit.Velocity) -> Unit
) {
    awaitEachGesture {
        val rotation = 0f
        var zoom = 1f
        var pan = Offset.Zero
        var pastTouchSlop = false
        val touchSlop = viewConfiguration.touchSlop
        val velocityTracker = VelocityTracker()

        awaitFirstDown(requireUnconsumed = false)

        do {
            val event = awaitPointerEvent()
            val canceled = event.changes.fastAny { it.isConsumed }
            if (canceled) break

            val zoomChange = event.calculateZoom()
            val panChange = event.calculatePan()

            if (!pastTouchSlop) {
                zoom *= zoomChange
                pan += panChange

                val centroidSize = event.calculateCentroidSize(useCurrent = false)
                val zoomMotion = abs(1 - zoom) * centroidSize
                val panMotion = pan.getDistance()

                if (zoomMotion > touchSlop || panMotion > touchSlop) {
                    pastTouchSlop = true
                }
            }

            if (pastTouchSlop) {
                val centroid = event.calculateCentroid(useCurrent = false)
                if (zoomChange != 1f || panChange != Offset.Zero) {
                    onGesture(centroid, panChange, zoomChange, rotation, event.changes[0].uptimeMillis, event.changes)
                }

                // Track velocity for the "fling" dismissal
                event.changes.fastForEach {
                    if (it.changedToDownIgnoreConsumed()) {
                        velocityTracker.resetTracking()
                    }
                    velocityTracker.addPointerInputChange(it)
                    if (it.changedToUpIgnoreConsumed()) {
                        // Logic handled in onEnd
                    }
                    it.consume()
                }
            }
        } while (event.changes.fastAny { it.pressed })

        // GESTURE ENDED: Calculate velocity
        val velocity = velocityTracker.calculateVelocity()
        onEnd(velocity)
    }
}
