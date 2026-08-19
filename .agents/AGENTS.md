# 🤖 Talangraga Umroh Mobile - Agent Knowledge Base

Quick index of project knowledge base, conventions, and AI agent protocols. Read linked reference documents in [`references/`](references/) as needed during tasks.

---

## 📌 Quick Reference Index

| Topic | Reference Document | Key Takeaway |
| :--- | :--- | :--- |
| **Project Setup** | [PROJECT_SETUP.md](references/PROJECT_SETUP.md) | Multi-module Compose Multiplatform (`:composeApp`, `:data`, `:shared`, `:androidApp`, `:iosApp`), JDK 21, adding new features. |
| **Code Conventions** | [CODE_CONVENTIONS.md](references/CODE_CONVENTIONS.md) | MVI pattern (`*Contract`, `*ViewModel`, `*Screen`, `*Section`), Clean Architecture layering, mapper rules. |
| **Tech Stack** | [PROJECT_SETUP.md](references/PROJECT_SETUP.md#tech-stack) | Ktor 3, SQLDelight 2, Koin 4, Compose Multiplatform 1.10, Multiplatform Settings, Napier, BuildKonfig, Coil 3, GitLive Firebase. |
| **Auth & Refresh** | [REFRESHTOKEN.md](references/REFRESHTOKEN.md) | `RefreshTokenHandler`, `TokenManager`, Bearer token interceptor, auto 401 refresh & session persistence. |
| **App Wiring & DI** | [APP_WIRING.md](references/APP_WIRING.md) | `initializeKoin` orchestration (`databaseModule`, `platformModule`, `sharedModule`, `themeModule`, `viewModelModule`), Navigation Compose routes. |
| **Offline-First** | [OFFLINE_FIRST.md](references/OFFLINE_FIRST.md) | Database-first pattern via `networkBoundResource` with SQLDelight caching and `Result<T>` flow. |
| **Production & Build** | [PRODUCTION.md](references/PRODUCTION.md) | BuildKonfig environment config, AGP 9.2, Kotlin 2.2, ProGuard rules, Google Services. |
| **AI Workflow** | [AI_WORKFLOW.md](references/AI_WORKFLOW.md) | 8-step AI development workflow (Brainstorm -> ADR -> Plan -> Code -> Test -> Changelog). |
| **Testing** | [TESTING.md](references/TESTING.md) | `commonTest` conventions with Turbine, AssertK, Ktor MockEngine, and Coroutines Test. |

---

## ⚠️ Non-Negotiable Core Rules

1. **Architecture & Module Separation**:
   - **`:composeApp`**: Presentation layer only (Compose Multiplatform UI, ViewModels, Compose Navigation, UI Themes/Components).
   - **`:data`**: Domain models (`domain/model`), Repository interfaces (`domain/repository`), SQLDelight Database (`DatabaseHelper`, `.sq` files), Network (`ApiService`, `HttpClientFactory`, `RefreshTokenHandler`), and Session (`TokenManager`, `Session`).
   - **`:shared`**: Cross-cutting reusable utilities (`Color`, `DateUtils`, `StringExtension`, `Typography`) and shared font/resource bundles.
   - **`:androidApp` & `:iosApp`**: Platform-specific entry wrappers.

2. **MVI Presentation Pattern**:
   - Every feature screen must have a dedicated contract: `*Contract.kt` containing `*State`, `*Event`, and `*Effect` (plus optional `SectionState<T>`).
   - Dispatches user actions via `viewModel.onEvent(event)` using Unidirectional Data Flow (UDF).
   - One-time side effects (toasts, dialog triggers, navigation) must be emitted via `UiEffect` Channel/Flow.
   - Split large screens into modular sections in `section/` or `components/` subfolders.

3. **DI Registration (Koin 4)**:
   - When creating a new ViewModel in `:composeApp`, immediately register it in `com.talangraga.umrohmobile.di.ViewModelModule` using `viewModelOf(::FeatureViewModel)`.
   - When creating a new Repository/Service in `:data`, register it in `SharedModule.kt` / `DatabaseModule.kt`.

4. **Data Layer & Offline-First**:
   - For cached data, always use `networkBoundResource` returning `Flow<Result<T>>` (emitting local SQLDelight data first, fetching from API, storing to DB, and emitting updated data).
   - For one-off network requests without caching, wrap execution in `safeApiCall` returning `Result<T>`.

5. **Layer Isolation & Mappers**:
   - Never leak API DTO responses or SQLDelight entities directly into the UI layer.
   - Always map across boundaries using extension functions in `com.talangraga.data.mapper.DataMapper` (e.g. `toEntity()`, `toDomain()`, `toUiData()`).

6. **ADRs Before Big Changes**: Document significant architectural decisions in [adr/](adr/) before starting tasks.
7. **Memory & State**: Inspect [memory/STATE.md](memory/STATE.md) at session start and update before handover.
8. **Changelog**: Log completed changes in [changelog/AI_CHANGELOG.md](changelog/AI_CHANGELOG.md) after finishing tasks.

---

## 🛠️ Harness & Tools

* **Memory**: [memory/STATE.md](memory/STATE.md) (session focus)
* **Plans**: Put step-by-step breakdowns in [plan/](plan/).
* **ADRs**: Agent Decision Records in [adr/](adr/).
* **Changelog**: Record tracking in [changelog/AI_CHANGELOG.md](changelog/AI_CHANGELOG.md).

---

## 🪄 Available Skills

### Compose Multiplatform UI
- [Compose Multiplatform Presentation](skills/compose-multiplatform-presentation/SKILL.md): Shared CMP screen structure, MVI Contract/ViewModel patterns, Compose Resources, and multiplatform insets.
- [Adaptive UI](skills/adaptive/SKILL.md): Responsive and adaptive layouts for multi-platform form factors.
- [Material Design 3](skills/material-design-3-ui/SKILL.md): M3 design system styling, themes, and components.

### KMP Architecture
- [KMP Feature Architecture](skills/kmp-feature-architecture/SKILL.md): Clean Architecture & MVI layering across `:composeApp`, `:data`, `:shared`.
- [KMP DI (Koin)](skills/kmp-di-koin/SKILL.md): Koin 4 module registration, `initializeKoin` wiring, and ViewModel injection.
- [KMP Data Layer](skills/kmp-data-layer/SKILL.md): Ktor 3 + SQLDelight 2 behind `networkBoundResource`.
- [KMP Testing](skills/kmp-testing/SKILL.md): `commonTest` conventions with Turbine, AssertK, Ktor MockEngine.

### Process & Build
- [ADR Authoring](skills/adr-authoring/SKILL.md): Template, numbering, and indexing rules for Agent Decision Records.
- [R8 Analyzer](skills/r8-analyzer/SKILL.md): Optimize app size and ProGuard keep rules.

---

## 🏛️ Agent Decision Records (ADRs)

Full history in [adr/](adr/).
- **[ADR-0001: Setup Agent Decision Records & Knowledge Base](adr/0001-setup-agent-decision-records.md)**
- **[ADR-0002: Migration to Go Backend Service](adr/0002-migration-to-go-backend-service.md)**

---

## 🪄 Versioning & License
Internal development at **Padepokan Talangraga**.  
Version: `1.0.0` (Beta Development)  
Author: **Padepokan Talangraga Developer Team**
