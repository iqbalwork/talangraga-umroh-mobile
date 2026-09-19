---
name: kmp-di-koin
description: Koin 4 setup and dependency injection rules for Talangraga Umroh Mobile
---

# 💉 Koin 4 Dependency Injection Guidelines

## 1. Module Definitions

- **`databaseModule`** (`composeApp/.../di/DatabaseModule.kt`):
  - Injects `DriverFactory`, `TalangragaDatabase`, `DatabaseHelper`, `Settings`, `TokenManager`, and `Session`.
- **`platformModule`** (`composeApp/.../di/PlatformModule.*.kt`):
  - Platform-specific implementations (e.g. `HttpClientEngine`, `FileReader`, `ImageCompressor`).
- **`sharedModule`** (`composeApp/.../di/SharedModule.kt`):
  - Injects `HttpClientFactory`, `RefreshTokenHandler`, `ApiService`, and `Repository`.
- **`viewModelModule`** (`composeApp/.../di/ViewModelModule.kt`):
  - Injects all feature ViewModels via `viewModelOf(::FeatureViewModel)`.
- **`themeModule`** (`composeApp/.../di/ThemeModule.kt`):
  - Injects `ThemeViewModel` and `ThemeManager`.

## 2. Registering a New ViewModel

Always register in `ViewModelModule.kt`:

```kotlin
val viewModelModule = module {
    viewModelOf(::SplashViewModel)
    viewModelOf(::LoginViewModel)
    viewModelOf(::HomeViewModel)
    viewModelOf(::TransactionViewModel)
    viewModelOf(::AddTransactionViewModel)
    viewModelOf(::TransactionDetailViewModel)
    viewModelOf(::PeriodeViewModel)
    viewModelOf(::UserViewModel)
    viewModelOf(::MemberDetailViewModel)
    // Add new ViewModel here:
    viewModelOf(::YourNewViewModel)
}
```
