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

## [2026-09-18]
### Fixed
- Fixed "Profile not found" and blank data loading issues after Supabase migration.
- Removed hardcoded anon key `Authorization` header from Ktor's `defaultRequest` in `HttpClientFactory.kt`, allowing user Bearer tokens to be attached by the `Auth` plugin.
- Fixed `JsonConvertException: Field 'id' is required for type UserResponse`:
  - Added default value `val id: String = ""` to `UserResponse`.
  - Safely checked `response.status.isSuccess()` in `ApiService.getCurrentUser()` and `getLoginProfile()` before parsing response body, avoiding parsing error responses (e.g. 401) as `UserResponse`.
- Fixed SQLite `(20) statement aborts at 13: [INSERT OR REPLACE INTO UserData(...)] datatype mismatch`:
  - Added SQLDelight migration files `1.sqm` and `2.sqm` and bumped DB version to 3.
  - Implemented automatic schema inspection and self-healing in `DriverFactory.android.kt` to drop and recreate `UserData` / `TransactionData` if the existing on-device database still has `userId` as `INTEGER`.
  - Wrapped `usersQueries.insertUserData` in `DatabaseHelper.kt` with a try-catch to protect against local cache insertion errors.
- Added `getCurrentUser()` and auto-resolution of user ID from `/auth/v1/user` in `ApiService.getLoginProfile()`.
- Added `logout()` in `ApiService` targeting `/auth/v1/logout` and clearing cached tokens.
- Fixed avatar upload path in `ApiService.updateMe()` to follow `storage/v1/object/avatars/$userId/$fileName`.
- Added `periode_id` and `payment_id` fields in `TransactionResponse` with fallback logic in `DataMapper.kt`.
- Updated `HomeViewModel` to set `userType` in `HomeState` upon profile load.
- Verified all Supabase REST endpoints against live instance and Bruno collection.
- Fixed user registration profile image upload to Supabase Storage:
  - Added `imageProfile: ByteArray?` parameter in `ApiService.registerUser()` and passed it through from `RepositoryImpl.registerNewUser()`.
  - Implemented `ApiService.uploadAvatar()` uploading avatar image to Supabase Storage bucket `avatars/$userId/avatar_$timestamp.[jpg|png]` with public URL resolution matching the web implementation.
  - Implemented `ApiService.updateProfileAvatar()` updating `image_profile_url` on `rest/v1/profiles` with retry logic.
  - Integrated `ImageCompressor` in `AddUserViewModel` on `OnImageChange` to compress avatar payload to ~200KB before uploading.
  - Refactored `updateUser()` and `updateMe()` to leverage the unified `uploadAvatar()` implementation.
  - Added unit tests in `ApiServiceTest.kt` verifying registration both with and without avatar upload.
