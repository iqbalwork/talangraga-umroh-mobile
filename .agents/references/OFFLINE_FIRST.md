# 💾 Offline-First & Data Synchronization Guide

This document explains the offline-first caching mechanism and database-bound flows in **Talangraga Umroh Mobile**.

---

## 1. The `networkBoundResource` Pattern

The project implements an offline-first caching helper in `data/src/commonMain/kotlin/com/talangraga/data/repository/RepositoryExt.kt`:

```kotlin
inline fun <LocalType, NetworkType> networkBoundResource(
    crossinline query: () -> Flow<LocalType>,
    crossinline fetch: suspend () -> NetworkType,
    crossinline saveFetchResult: suspend (NetworkType) -> Unit,
    crossinline shouldFetch: (LocalType) -> Boolean = { true }
): Flow<Result<LocalType>> = channelFlow {
    // 1. Emit loading with existing local cached data
    val localData = query().firstOrNull()
    if (localData != null) {
        send(Result.Loading(localData))
    } else {
        send(Result.Loading())
    }

    if (shouldFetch(localData)) {
        try {
            // 2. Fetch from backend API
            val networkResult = fetch()
            // 3. Save to local SQLite database
            saveFetchResult(networkResult)
            // 4. Emit updated cached data as Success
            query().collect { updatedLocalData ->
                send(Result.Success(updatedLocalData))
            }
        } catch (e: Exception) {
            // 5. If network fails, emit Error while preserving local data
            send(Result.Error(e.message ?: "Network error", data = localData))
        }
    } else {
        query().collect { cachedData ->
            send(Result.Success(cachedData))
        }
    }
}
```

---

## 2. Using in Repositories

```kotlin
// Example in RepositoryImpl.kt
override fun getPeriods(): Flow<Result<List<PeriodEntity>>> {
    return networkBoundResource(
        query = { databaseHelper.getPeriodsFlow() },
        fetch = { apiService.getPeriods() },
        saveFetchResult = { response ->
            databaseHelper.insertPeriods(response.data.map { it.toEntity() })
        }
    )
}
```

---

## 3. Benefits
1. **Instant UI rendering**: Shows locally cached data immediately without blank screen delays.
2. **Offline resilience**: App remains fully readable without an active network connection.
3. **Data consistency**: SQLDelight is the single source of truth for the presentation layer.
