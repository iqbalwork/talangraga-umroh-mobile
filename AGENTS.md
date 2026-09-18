# Agent Rules and Context for Kotlin Multiplatform Project (Talangraga Mobile)

This document provides context, architectural guidelines, and rules for AI coding agents working on the **Talangraga Umroh Mobile App**.

---

## 1. Project Overview & Tech Stack

- **Framework**: Kotlin Multiplatform (KMP)
- **Targets**: Android, iOS (Shared logic & Compose UI)
- **UI Framework**: Compose Multiplatform (in `:composeApp` and `:shared/src/commonMain/composeResources`)
- **Architecture Pattern**: MVI (Model-View-Intent) with Clean Architecture principles
- **Dependency Injection**: Koin
- **Networking**: Ktor Client
- **Local Database**: SQLDelight
- **Key-Value Storage**: Multiplatform Settings (Session handling)
- **Image Loading**: Coil Compose
- **Logging**: Napier

---

## 2. Module Structure

### `:shared`
- **Role**: Contains highly reusable common logic, string/image resources (`composeResources`), and cross-cutting concerns that do not depend on UI directly.
- **Rules**: 
  - Do not put UI screens here unless strictly shared components decoupled from features.
  - Resources (strings, drawables, fonts) go into `shared/src/commonMain/composeResources`.

### `:composeApp`
- **Role**: Presentation layer containing all Compose UI, ViewModels, Navigation, and DI setup.
- **Rules**:
  - UI screens go to `src/commonMain/kotlin/com/talangraga/umrohmobile/presentation/<feature>/`.
  - ViewModels must subclass `androidx.lifecycle.ViewModel` and be injected using Koin.
  - DI modules (like `ViewModelModule.kt`, `ThemeModule.kt`) are updated in `com.talangraga.umrohmobile.di`.

### `:data`
- **Role**: The Data and Domain capability module, encompassing data retrieval, caching, network requests, and Mappers.
- **Rules**:
  - **Domain Models**: Defined in `domain/model`.
  - **Domain Interfaces**: Repository interfaces defined in `domain/repository`.
  - **Network Models**: API request/responses defined in `network/model`.
  - **Local Storage**: SQLDelight (`sqldelight` directory) and Settings wrappers.
  - Maps Entities to Domain Models before handing them off to the Presentation layer.

### `:androidApp` & `:iosApp`
- **Role**: Platform-specific entry points binding the application to native lifecycles (e.g. Android `Application` class, iOS `AppDelegate` / `AppMain.swift`).

---

## 3. Architecture Deep Dive & Implementation Rules

### Presentation Layer (MVI)
- We follow the Model-View-Intent pattern using a unidirectional data flow.
- A Screen must have a single `UiState` (StateFlow) representing its state.
- Screen interactions are passed down as `UiEvent` or `Intent` sealed classes to the ViewModel using `.onEvent(event)`.
- One-time events (like navigation, showing toasts) must be handled through a single `UiEffect` (SharedFlow or Channel).
- **DI Registration**: After creating `FeatureViewModel`, immediately register it in `composeApp/.../di/ViewModelModule.kt` using `viewModelOf(::FeatureViewModel)`.

### Data Layer (Offline-First Approach)
- This project leverages local caching with SQLDelight and synchronizes using a `networkBoundResource` pattern.
- Repositories are implemented in `data/.../repository/RepositoryImpl.kt`.
- **Flow Pattern**: Return `Flow<Result<T>>` for network requests and data observation.
- **Error Handling**: Exceptions (e.g. `JsonConvertException`) are caught and wrapped inside `Result.Error(...)`.

### Mappers
- Always use extension functions for mapping between layers (e.g. `UserResponse.toUserEntity()`, `UserEntity.toDomain()`).
- Keep mapping functions in designated package `.../data/mapper/`.

---

## 4. Workflows for Adding a New Feature

When implementing a feature, use the following sequence:
1. **Network Layer**: Add DTOs to `network/model`, and define the API interface in `ApiService.kt`.
2. **Local Layer (If cached)**: Add SQL queries in `.sq` files located in `data/.../sqldelight/...`.
3. **Domain Layer**: Add the contract method to the `domain/repository/Repository` interface.
4. **Data Repository**: Implement the repository method in `RepositoryImpl.kt` by combining `ApiService` and `DatabaseHelper`.
5. **Presentation (ViewModel)**: Create `FeatureViewModel` accepting the `Repository` and reacting to it using `viewModelScope`.
6. **DI Wiring**: Update `ViewModelModule.kt`.
7. **UI**: Build `FeatureScreen` and wire it up in Compose navigation.

---

## 🔄 Cross-Platform Orchestration & Sync Protocol

### 1. Pre-Task Hook (Check Pending Tasks from BE)
Before building new screens or services, check for pending tasks:
```bash
python3 ../scripts/sync.py pending --platform mobile
```

### 2. Post-Task Hook (Mark Synced)
Once Mobile implementation and verification are complete:
```bash
python3 ../scripts/sync.py mark --id "<feature-id>" --platform mobile --status DONE
```

### 3. Master Sync Board
Consult [`.agents/sync/SYNC_BOARD.md`](../.agents/sync/SYNC_BOARD.md) for ecosystem status.
