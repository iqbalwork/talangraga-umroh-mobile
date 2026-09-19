# 📜 Code Conventions Guide

This document defines naming rules, file organization, and architectural conventions for the **Talangraga Umroh Mobile** KMP project.

---

## 1. Module & Layer Boundaries

```
:composeApp          --> Presentation Layer (Compose Multiplatform, ViewModels, Navigation, UI Themes)
      │
      ▼
   :data             --> Business & Data Layer (Domain, Repository, SQLDelight DB, Ktor Network, Session)
      ▲
      │
  :shared            --> Shared Foundations (Typography, Colors, Formatting Utils, String Extensions)
```

- **No DTOs or DB Entities in UI**: The presentation layer must only interact with Domain Models or dedicated UI Data Models (`*UiData`).
- **No Compose UI in `:data`**: `:data` remains purely platform-agnostic and UI-free.
- **Shared Tokens in `:shared`**: Theme palettes and typography foundations live in `:shared`.

---

## 2. Presentation Layer: MVI Pattern

Every screen or major feature must implement the Model-View-Intent (MVI) architecture:

### Contract (`*Contract.kt`)
```kotlin
// FeatureContract.kt
sealed class SectionState<out T> {
    data object Loading : SectionState<Nothing>()
    data class Success<T>(val data: T) : SectionState<T>()
    data class Error(val message: String?) : SectionState<Nothing>()
}

data class FeatureState(
    val data: SectionState<FeatureUiData> = SectionState.Loading,
    val isLoading: Boolean = false,
    val errorMessage: String? = null,
)

sealed interface FeatureEvent {
    data object Refresh : FeatureEvent
    data class SubmitAction(val id: String) : FeatureEvent
}

sealed interface FeatureEffect {
    data class ShowToast(val message: String) : FeatureEffect
    data object NavigateToDetail : FeatureEffect
}
```

### ViewModel (`*ViewModel.kt`)
- Subclasses `androidx.lifecycle.ViewModel`.
- Holds state as `StateFlow<FeatureState>`.
- Emits one-time effects through `Channel<FeatureEffect>` or `SharedFlow`.
- Processes user interactions via `fun onEvent(event: FeatureEvent)`.
- Dispatches coroutines on `viewModelScope`.

### Screen (`*Screen.kt`)
- Collects state via `collectAsStateWithLifecycle()`.
- Dispatches events to `viewModel.onEvent(...)`.
- Listens to effects via `LaunchedEffect(viewModel.effect)`.
- Delegates complex subviews to `section/` or `components/` files.

---

## 3. Data Layer Conventions

### Repositories
- Defined as interfaces in `com.talangraga.data.domain.repository.Repository`.
- Implemented in `com.talangraga.data.repository.RepositoryImpl`.
- All multi-item or cached queries return `Flow<Result<T>>`.
- Direct one-shot actions return `Result<T>`.

### Data Mappers
- Extension functions placed in `com.talangraga.data.mapper.DataMapper.kt`.
- Follow strict conversion naming:
  - `fun ResponseDTO.toEntity(): Entity`
  - `fun Entity.toDomain(): DomainModel`
  - `fun DomainModel.toUiData(): UiData`
  - `fun RequestModel.toRequest(): RequestDTO`

---

## 4. DI Registration (Koin 4)
- **ViewModel Registration**: Always add newly created ViewModels to `composeApp/src/commonMain/kotlin/com/talangraga/umrohmobile/di/ViewModelModule.kt`.
- **Use `viewModelOf(::FeatureViewModel)`** for constructor-based injection.
