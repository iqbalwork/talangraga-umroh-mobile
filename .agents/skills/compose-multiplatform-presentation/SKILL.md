---
name: compose-multiplatform-presentation
description: Guidelines and patterns for Compose Multiplatform presentation layer in Talangraga Umroh Mobile
---

# 🎨 Compose Multiplatform Presentation Layer Guidelines

## 1. Core Architecture & Multiplatform UI Sharing

In **Talangraga Umroh Mobile**, the entire UI and presentation layer is 100% shared across **Android** and **iOS** using **Compose Multiplatform** inside `:composeApp/src/commonMain/kotlin/com/talangraga/umrohmobile/`.

```
composeApp/src/commonMain/kotlin/com/talangraga/umrohmobile/
├── application/
│   └── App.kt                         # Shared Compose App root (Theme, Navigation Host, Dialog/Toast host)
├── navigation/
│   ├── Screen.kt                      # Type-safe navigation routes (@Serializable)
│   ├── BottomNavRoute.kt              # Bottom bar tab definitions
│   └── *NavHost.kt                    # Sub-graph navigators
├── presentation/
│   └── <feature>/                     # Feature modules (home, login, main, transaction, periode, user, splash)
│       ├── *Contract.kt               # MVI Contract (UiState, UiEvent, UiEffect, SectionState)
│       ├── *ViewModel.kt              # Multiplatform ViewModel + Koin injection
│       ├── *Screen.kt                 # Screen entry composable (Scaffold, Insets, Effect listener)
│       └── section/                   # Decomposed previewable UI sections & dialogs
└── ui/
    ├── component/                     # Design system atoms & molecules (Buttons, Inputs, Modals, Scaffold)
    ├── theme/                         # Shared Material 3 Theme (Light/Dark mode, Typography, Colors)
    └── section/                       # Reusable bottom sheets and dialogs
```

---

## 2. Strict MVI Pattern (Model-View-Intent)

Every feature screen must follow unidirectional data flow (UDF):

### Contract Definition (`*Contract.kt`)
```kotlin
package com.talangraga.umrohmobile.presentation.example

// 1. SectionState for granular loading/error per UI section
sealed class SectionState<out T> {
    data object Loading : SectionState<Nothing>()
    data class Success<T>(val data: T) : SectionState<T>()
    data class Error(val message: String?) : SectionState<Nothing>()
}

// 2. Single immutable screen state
data class ExampleState(
    val profileSection: SectionState<UserUIData> = SectionState.Loading,
    val itemsSection: SectionState<List<ItemUiData>> = SectionState.Loading,
    val isLoading: Boolean = false,
    val errorMessage: String? = null,
    val isFormValid: Boolean = false,
)

// 3. User intents / UI events
sealed interface ExampleEvent {
    data object Refresh : ExampleEvent
    data class InputChanged(val value: String) : ExampleEvent
    data class Submit(val id: Int) : ExampleEvent
}

// 4. One-time side effects (navigation, toasts, dialog triggers)
sealed interface ExampleEffect {
    data class ShowToast(val message: String) : ExampleEffect
    data class NavigateToDetail(val id: Int) : ExampleEffect
}
```

### Multiplatform ViewModel (`*ViewModel.kt`)
```kotlin
package com.talangraga.umrohmobile.presentation.example

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

class ExampleViewModel(
    private val repository: Repository,
) : ViewModel() {

    private val _uiState = MutableStateFlow(ExampleState())
    val uiState: StateFlow<ExampleState> = _uiState.asStateFlow()

    private val _effect = Channel<ExampleEffect>(Channel.BUFFERED)
    val effect = _effect.receiveAsFlow()

    fun onEvent(event: ExampleEvent) {
        when (event) {
            is ExampleEvent.Refresh -> loadData()
            is ExampleEvent.InputChanged -> handleInputChange(event.value)
            is ExampleEvent.Submit -> submit(event.id)
        }
    }

    private fun loadData() {
        viewModelScope.launch {
            _uiState.update { it.copy(itemsSection = SectionState.Loading) }
            repository.getItems().collect { result ->
                when (result) {
                    is Result.Loading -> _uiState.update { 
                        it.copy(itemsSection = SectionState.Loading) 
                    }
                    is Result.Success -> _uiState.update { 
                        it.copy(itemsSection = SectionState.Success(result.data.map { it.toUiData() })) 
                    }
                    is Result.Error -> {
                        _uiState.update { it.copy(itemsSection = SectionState.Error(result.message)) }
                        _effect.send(ExampleEffect.ShowToast(result.message))
                    }
                }
            }
        }
    }
}
```

### Screen Entry Composable (`*Screen.kt`)
```kotlin
@Composable
fun ExampleScreen(
    navController: NavHostController,
    viewModel: ExampleViewModel = koinViewModel(),
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    // Handle one-time side-effects (Toasts, navigation)
    LaunchedEffect(viewModel.effect) {
        viewModel.effect.collectLatest { effect ->
            when (effect) {
                is ExampleEffect.ShowToast -> ToastManager.show(effect.message)
                is ExampleEffect.NavigateToDetail -> navController.navigate(Screen.Detail(effect.id))
            }
        }
    }

    TalangragaScaffold(
        topBar = { /* ... */ }
    ) { paddingValues ->
        ExampleContent(
            state = uiState,
            onEvent = viewModel::onEvent,
            modifier = Modifier.padding(paddingValues)
        )
    }
}
```

---

## 3. Compose Resources & Multiplatform Assets

Always use Jetpack / Compose Multiplatform resource accessors:
- **Strings**: `stringResource(Res.string.app_name)`
- **Drawables**: `painterResource(Res.drawable.talangraga_logo)`
- **Fonts**: Custom font families loaded via `Font(Res.font.inter_medium)` and `Font(Res.font.space_grotesk_bold)`.
- Resource files are located in `composeApp/src/commonMain/composeResources/` and `shared/src/commonMain/composeResources/`.

---

## 4. Edge-to-Edge, Safe Insets & Platform WindowInsets

Compose Multiplatform runs seamlessly on Android and iOS when properly handling insets:
- Use `WindowInsets.statusBars`, `WindowInsets.navigationBars`, or `WindowInsets.safeDrawing`.
- Use `TalangragaScaffold` to automatically manage system padding.
- For keyboard interactions, use `Modifier.imePadding()` or `WindowInsets.ime`.

---

## 5. Expect / Actual for Platform UI Utilities

When accessing platform-specific APIs (Camera, Image Compressor, File System):
- Define `expect` class/function in `composeApp/src/commonMain/kotlin/com/talangraga/umrohmobile/presentation/utils/`.
- Provide `actual` implementation in:
  - `composeApp/src/androidMain/kotlin/...` (e.g. `FileReader.android.kt`, `ImageCompressor.android.kt`)
  - `composeApp/src/iosMain/kotlin/...` (e.g. `FileReader.ios.kt`, `ImageCompressor.ios.kt`)
- Inject platform utilities into ViewModels or Composables via Koin `platformModule`.

---

## 6. Type-Safe Navigation 2.8+ / 2.9+

Navigation routes use `@Serializable` objects/data classes in `Screen.kt`:
```kotlin
sealed interface Screen {
    @Serializable data object SplashRoute : Screen
    @Serializable data object LoginRoute : Screen
    @Serializable data object MainRoute : Screen
    @Serializable data class AddTransactionRoute(val isCollective: Boolean = false) : Screen
    @Serializable data class TransactionDetailRoute(val transactionJson: String) : Screen
}
```
Extract parameters via `backStackEntry.toRoute<Screen.TransactionDetailRoute>()`.
