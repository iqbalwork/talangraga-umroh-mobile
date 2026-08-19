---
name: kmp-feature-architecture
description: Clean Architecture layering rules across :composeApp, :data, and :shared in Talangraga Umroh Mobile
---

# 🏗️ KMP Feature Architecture

## Layer Responsibilities

```
┌─────────────────────────────────────────────────────────────┐
│                        :composeApp                          │
│  - Presentation (MVI: Contract, ViewModel, Screen, Section) │
│  - Compose Multiplatform UI, Themes, Components             │
│  - Navigation (Screen.kt, NavHost)                          │
│  - DI Modules (ViewModelModule, ThemeModule, DatabaseModule)│
└──────────────────────────────┬──────────────────────────────┘
                               │
                               ▼
┌─────────────────────────────────────────────────────────────┐
│                           :data                             │
│  - Domain: Models (domain/model), Interfaces (domain/repo)  │
│  - Network: Ktor ApiService, DTOs (request/response)        │
│  - Persistence: SQLDelight DB, DatabaseHelper, DriverFactory│
│  - Session: TokenManager, Session (Multiplatform Settings)  │
│  - Repository: RepositoryImpl, networkBoundResource         │
│  - Mappers: DataMapper.kt                                   │
└──────────────────────────────┬──────────────────────────────┘
                               │
                               ▼
┌─────────────────────────────────────────────────────────────┐
│                          :shared                            │
│  - Reusable Foundations (DateUtils, StringExtension)        │
│  - Design Tokens (Color, Typography)                        │
│  - Shared Assets (Fonts, Compose Resources)                 │
└─────────────────────────────────────────────────────────────┘
```

## Adding a Feature Checklist

1. **DTOs**: `data/src/commonMain/kotlin/com/talangraga/data/network/model/request` & `response`.
2. **API Endpoint**: `com.talangraga.data.network.api.ApiService`.
3. **Database (if cached)**: `data/src/commonMain/sqldelight/com/talangraga/*.sq` & `data/local/database/DatabaseHelper.kt`.
4. **Domain Contract**: `com.talangraga.data.domain.repository.Repository.kt`.
5. **Implementation & Mapping**: `RepositoryImpl.kt` & `DataMapper.kt`.
6. **MVI Presentation**: `com.talangraga.umrohmobile.presentation.<feature>/` (`*Contract.kt`, `*ViewModel.kt`, `*Screen.kt`).
7. **DI Registration**: `ViewModelModule.kt`.
8. **Navigation**: `Screen.kt` & Navigation Graph.
