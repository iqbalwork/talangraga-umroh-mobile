# 📝 AI Development Changelog

## [2026-08-19]
### Added
- Implemented Material You (Material Design 3 & Dynamic Color) theme system across Compose Multiplatform.
- Added full semantic color tokens (`*Container`, `surfaceContainer*`, `outlineVariant`, `errorContainer`) in `Color.kt`.
- Configured dynamic color persistence in `ThemePreference` and reactive state in `ThemeManager`.
- Added platform-specific dynamic color support detection via `isDynamicColorSupported()`.
- Modernized theme settings UI in `ProfileScreen.kt` with `FilterChip` and Material You dynamic switch toggle.
- Created `.agents/AGENTS.md` and complete agent knowledge base framework following the Bobobox mobile KMP standard.
- Added comprehensive reference documents in `.agents/references/`:
  - `PROJECT_SETUP.md`
  - `CODE_CONVENTIONS.md`
  - `REFRESHTOKEN.md`
  - `APP_WIRING.md`
  - `OFFLINE_FIRST.md`
  - `PRODUCTION.md`
  - `AI_WORKFLOW.md`
  - `TESTING.md`
- Added dedicated `compose-multiplatform-presentation` skill covering 100% shared UI across Android & iOS, MVI, Compose Resources, type-safe navigation, and expect/actual platform utilities.
- Initialized `.agents/memory/STATE.md`, `.agents/adr/0001-setup-agent-decision-records.md`, and `.agents/plan/`.
- Authored [ADR-0002](adr/0002-migration-to-go-backend-service.md) for Go backend service migration.
- Updated `network_security_config.xml` with local development domains (`10.0.2.2`, `127.0.0.1`, `localhost`).
- Fixed token refresh deserialization in `RefreshTokenHandler` to properly unwrap `DataResponse<TokenResponse>`.
- Normalized route paths in `ApiService` (`periodes`, `transactions`) to prevent redirect overhead on Gin router.
- Successfully verified tests across KMP targets with `./gradlew :data:allTests`.
