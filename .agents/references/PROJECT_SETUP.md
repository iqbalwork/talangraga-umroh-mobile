# 🛠️ Project Setup Guide

This document provides an overview of the project structure and setup instructions for the **Talangraga Umroh Mobile** KMP project.

---

## 1. Project Structure

The project is organized into multiple modules following Kotlin Multiplatform (KMP) & Compose Multiplatform best practices:

- **`:composeApp`**: Shared Presentation layer containing Compose UI screens, ViewModels, navigation routes, theme system, and DI module declarations.
- **`:data`**: Core business logic, domain models & repository interfaces (`domain/`), network client with Ktor (`network/`), local persistence with SQLDelight (`local/database`), session handling (`local/session`), and data mappers (`mapper/`).
- **`:shared`**: Cross-cutting reusable utilities (`Color`, `DateUtils`, `StringExtension`, `Typography`) and shared resource assets (fonts, strings).
- **`:androidApp`**: Android platform entry point (`MainActivity`, `TalangragaApplication`, AndroidManifest, Google Services).
- **`iosApp/`**: Native iOS application entry point embedding the shared Compose controller.

---

## 2. Tech Stack

| Layer | Technology | Details |
| :--- | :--- | :--- |
| **Language** | Kotlin 2.2+ | Multiplatform code sharing across Android & iOS |
| **UI Framework** | Compose Multiplatform 1.10+ | Declarative Material 3 UI |
| **Networking** | Ktor 3.3.0 | ContentNegotiation, Auth Bearer tokens, Inspektify logging |
| **Database** | SQLDelight 2.2.1 | Multiplatform SQLite persistence with reactive flows |
| **Key-Value Storage** | Multiplatform Settings 1.3.0 | Secure session & token storage |
| **Dependency Injection** | Koin 4.1.1 + Kotzilla | Modular dependency injection & ViewModel lifecycle integration |
| **Image Loading** | Coil 3.3.0 | Multiplatform async image loader with Ktor network integration |
| **Logging** | Napier 2.7.1 | Multiplatform logging |
| **Configuration** | BuildKonfig 0.17.1 | Compile-time environment configuration (e.g. `BASE_URL`) |
| **Monitoring** | GitLive Firebase 2.4.0 | Multiplatform Firebase Crashlytics & Analytics |
| **Testing** | Turbine + AssertK + MockEngine | Reactive Flow and HTTP mock testing in `commonTest` |

---

## 3. Prerequisites

- **JDK**: Version 21 (foojay toolchain configured).
- **Android Studio**: Latest stable version with Kotlin Multiplatform plugin.
- **Xcode**: Latest version with CocoaPods/SPM configuration (for iOS builds).
- **Gradle**: 9.2+ with AGP 9.2.1.

---

## 4. How to Add a New Feature

When introducing a new feature:

1. **Network Layer (`:data`)**:
   - Define request/response DTOs in `com.talangraga.data.network.model.request` and `response`.
   - Add API endpoints to `ApiService.kt`.

2. **Local Persistence (`:data`)**:
   - If offline caching is required, add table definitions and queries in `.sq` files under `data/src/commonMain/sqldelight/`.
   - Define Entity models in `data/local/database/model/`.

3. **Domain Layer (`:data`)**:
   - Define domain model data classes in `domain/model/`.
   - Declare repository methods in `domain/repository/Repository.kt`.

4. **Data Repository (`:data`)**:
   - Implement methods in `RepositoryImpl.kt` using `networkBoundResource` (for cached data) or `safeApiCall` (for un-cached requests).
   - Add mapping extensions in `com.talangraga.data.mapper.DataMapper.kt`.

5. **Presentation Layer (`:composeApp`)**:
   - Create feature directory under `com.talangraga.umrohmobile.presentation.<feature>/`.
   - Define `*Contract.kt` (`*State`, `*Event`, `*Effect`).
   - Implement `*ViewModel.kt` accepting repository/session dependencies.
   - Register ViewModel in `com.talangraga.umrohmobile.di.ViewModelModule.kt` via `viewModelOf(::FeatureViewModel)`.
   - Build UI screens (`*Screen.kt`, `*Section.kt`) and register route in `com.talangraga.umrohmobile.navigation.Screen.kt` and `AppNavGraph`.
