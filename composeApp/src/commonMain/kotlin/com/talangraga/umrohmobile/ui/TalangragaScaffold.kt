package com.talangraga.umrohmobile.ui

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.FabPosition
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.ScaffoldDefaults
import androidx.compose.material3.contentColorFor
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.talangraga.umrohmobile.ui.component.ToastComponent
import com.talangraga.umrohmobile.ui.component.ToastType
import kotlinx.coroutines.delay

/**
 * iqbalfauzi
 * Email: work.iqbalfauzi@gmail.com
 * Github: https://github.com/iqbalwork
 */

object ToastManager {
    private var showToast: ((String?, String, ToastType, String?, (() -> Unit)?) -> Unit)? = null

    fun register(show: (String?, String, ToastType, String?, (() -> Unit)?) -> Unit) {
        showToast = show
    }

    fun show(
        title: String? = null,
        message: String,
        type: ToastType = ToastType.Info,
        actionButtonText: String? = null,
        onActionClick: (() -> Unit)? = null
    ) {
        showToast?.invoke(title, message, type, actionButtonText, onActionClick)
    }
}

@Composable
fun TalangragaScaffold(
    modifier: Modifier = Modifier,
    topBar: @Composable () -> Unit = {},
    bottomBar: @Composable () -> Unit = {},
    snackbarHost: @Composable () -> Unit = {},
    floatingActionButton: @Composable () -> Unit = {},
    floatingActionButtonPosition: FabPosition = FabPosition.End,
    containerColor: Color = MaterialTheme.colorScheme.background,
    contentColor: Color = contentColorFor(containerColor),
    contentWindowInsets: WindowInsets = ScaffoldDefaults.contentWindowInsets,
    content: @Composable (PaddingValues) -> Unit,
) {
    var toastState by remember { mutableStateOf<ToastData?>(null) }

    LaunchedEffect(Unit) {
        ToastManager.register { title, message, type, actionText, actionClick ->
            toastState = ToastData(title, message, type, actionText, actionClick)
        }
    }

    LaunchedEffect(toastState) {
        if (toastState != null) {
            delay(3000) // Auto-dismiss after 3 seconds
            toastState = null
        }
    }

    Scaffold(
        modifier = modifier,
        topBar = topBar,
        bottomBar = bottomBar,
        snackbarHost = snackbarHost,
        floatingActionButton = floatingActionButton,
        floatingActionButtonPosition = floatingActionButtonPosition,
        containerColor = containerColor,
        contentColor = contentColor,
        contentWindowInsets = contentWindowInsets,
    ) { paddingValues ->
        Box(modifier = Modifier.fillMaxSize()) {
            content(paddingValues)

            // Toast Overlay
            AnimatedVisibility(
                visible = toastState != null && !toastState?.message.isNullOrBlank(),
                enter = slideInVertically { -it } + fadeIn(),
                exit = slideOutVertically { -it } + fadeOut(),
                modifier = Modifier
                    .align(Alignment.TopCenter)
                    .padding(top = paddingValues.calculateTopPadding() + 16.dp, start = 16.dp, end = 16.dp)
            ) {
                toastState?.let { data ->
                    ToastComponent(
                        title = data.title,
                        message = data.message,
                        type = data.type,
                        actionButtonText = data.actionButtonText,
                        onActionClick = data.onActionClick,
                        onDismiss = { toastState = null }
                    )
                }
            }
        }
    }
}

private data class ToastData(
    val title: String?,
    val message: String,
    val type: ToastType,
    val actionButtonText: String?,
    val onActionClick: (() -> Unit)?
)
