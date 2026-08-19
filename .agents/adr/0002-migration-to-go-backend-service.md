# ADR-0002: Migration to Go Backend Service (Local & Future Staging/Prod)

## Status
Proposed

## Context
The backend service has been rewritten and migrated to a high-performance Go service (`talangraga-umroh-go`) running Gin, GORM, and JWT authentication with local file storage/MinIO/Cloudinary support.
The mobile app (`talangraga-umroh-mobile`) needs to migrate its networking, contracts, DTO mappings, and local environment configurations to seamlessly integrate with the new Go backend service.

Key motivations:
1. Ensure full compatibility between Ktor 3 client models in `:data` and Go backend endpoints/DTOs.
2. Standardize error handling and response unwrapping around Go's `BaseResponse` (`code`, `message`, `data`).
3. Fix token refresh unwrapping in `RefreshTokenHandler` where response payload is nested under `data`.
4. Establish local development connectivity configuration (supporting Android emulator `10.0.2.2`, iOS simulator `localhost`, and physical device Wi-Fi IPs) with proper cleartext HTTP security configuration.

## Decision
1. **Network Layer Alignment**:
   - Align all endpoint paths in `ApiService` with the Go backend router specifications (`/auth/*`, `/users/*`, `/periodes`, `/payments`, `/transactions/*`), normalizing trailing slash inconsistencies.
   - Adjust `RefreshTokenHandler` to properly deserialize `DataResponse<TokenResponse>` instead of expecting flat JSON on `/auth/refresh`.
2. **DTO & Serialization Compatibility**:
   - Verify and maintain parity for `UserResponse`, `PeriodeResponse`, `PaymentResponse`, `TransactionResponse`, and multipart payload field names (`image_profile`, `file`).
   - Support standard RFC3339 timestamp parsing for transactions and period dates.
3. **Local Dev Environment & Configuration**:
   - Provide flexible local base URL management via `secret.properties` (`stagingUrl` / `localUrl`).
   - Ensure `network_security_config.xml` allows cleartext traffic for `10.0.2.2`, `127.0.0.1`, `localhost`, and local LAN subnets.
4. **Offline-First & Caching Continuity**:
   - Retain `networkBoundResource` and SQLDelight local database caching behavior in `:data` layer without breaking existing UI contracts.

## Consequences
- **Positive**:
  - Full interoperability between CMP mobile app and the new Go backend.
  - Reliable token refresh lifecycle and session management.
  - Smooth local development and testing workflow on both Android and iOS targets.
- **Trade-offs / Follow-up**:
  - Developers need to configure their host machine IP or emulator host alias (`10.0.2.2`) in `secret.properties` when running backend locally.
  - Must ensure the Go backend is running and seeded (`go run cmd/api/main.go` or docker-compose) during local testing.
