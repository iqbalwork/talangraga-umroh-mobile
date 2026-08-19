---
name: kmp-data-layer
description: Data layer guidelines for SQLDelight, Ktor 3, and networkBoundResource in Talangraga Umroh Mobile
---

# 📦 KMP Data Layer Guidelines

## 1. Network API (`ApiService`)
- Defined in `com.talangraga.data.network.api.ApiService`.
- Uses Ktor `HttpClient` with JSON serialization.
- Wrap non-cached requests in `safeApiCall`:
```kotlin
suspend fun <T> safeApiCall(apiCall: suspend () -> T): Result<T> {
    return try {
        Result.Success(apiCall())
    } catch (e: Exception) {
        Result.Error(e.message ?: "Unknown error")
    }
}
```

## 2. SQLDelight Database
- Database name: `TalangragaDatabase`.
- `.sq` files located in `data/src/commonMain/sqldelight/com/talangraga/`:
  - `Periods.sq`
  - `Transactions.sq`
  - `User.sq`
  - `Payment.sq`
- Handled through `DatabaseHelper` with `asFlow().mapToList(Dispatchers.IO)`.

## 3. `networkBoundResource` Offline-First
- Query SQLDelight flow first.
- Fetch from API and save to SQLDelight.
- Emit updated local data.
- Gracefully handle offline or network exceptions by emitting cached data with `Result.Error`.
