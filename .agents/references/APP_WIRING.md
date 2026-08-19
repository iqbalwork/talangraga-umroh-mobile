# 🔌 App Wiring & Dependency Injection Guide

This document describes how DI (Koin 4) and Navigation are organized and wired in **Talangraga Umroh Mobile**.

---

## 1. Koin DI Modules

DI modules are defined under `composeApp/src/commonMain/kotlin/com/talangraga/umrohmobile/di/`:

| Module | Responsibility | Key Injections |
| :--- | :--- | :--- |
| **`databaseModule`** | Local SQLite persistence & session | `DriverFactory`, `DatabaseHelper`, `TalangragaDatabase`, `TokenManager`, `Session` |
| **`platformModule`** | Platform specific helpers | `PlatformHelper`, `HttpClientEngine` |
| **`sharedModule`** | Networking, Repositories, APIs | `HttpClientFactory`, `RefreshTokenHandler`, `ApiService`, `Repository` |
| **`themeModule`** | UI Theme state management | `ThemeViewModel` |
| **`viewModelModule`** | Presentation ViewModels | `HomeViewModel`, `LoginViewModel`, `TransactionViewModel`, `UserViewModel`, `PeriodeViewModel`, `SplashViewModel` |

---

## 2. Initialization

Koin is initialized through `initializeKoin`:

```kotlin
// composeApp/src/commonMain/kotlin/com/talangraga/umrohmobile/di/KoinHelper.kt
fun initializeKoin(config: KoinAppDeclaration? = null) {
    startKoin {
        config?.invoke(this)
        modules(
            databaseModule,
            platformModule,
            sharedModule,
            themeModule,
            viewModelModule,
        )
    }
}
```

- **Android**: Called in `androidApp/.../TalangragaApplication.kt` with `androidContext(this)`.
- **iOS**: Called in iOS entry point / `App.kt`.

---

## 3. Navigation Compose Routes

Navigation routes are structured using sealed class `Screen` in `com.talangraga.umrohmobile.navigation.Screen.kt`:

- `Screen.Splash`
- `Screen.Login`
- `Screen.Main` (Bottom Navigation host)
  - `Screen.Home`
  - `Screen.Transaction`
  - `Screen.Periode`
  - `Screen.User`
- `Screen.AddTransaction`
- `Screen.MemberDetail`

Routes are wired in `App.kt` using `NavHost` and `rememberNavController()`.
