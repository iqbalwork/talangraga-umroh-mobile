# 🧪 Testing Guide

This document outlines testing standards and tools for **Talangraga Umroh Mobile**.

---

## 1. Testing Frameworks

- **Turbine** (`app.cash.turbine`): Testing Kotlin Coroutine `Flow` emissions and StateFlow state transitions.
- **AssertK** (`com.willowtreeapps.assertk:assertk`): Fluent assertions for Kotlin.
- **Ktor MockEngine** (`io.ktor:ktor-client-mock`): Mocking HTTP network responses for repository and API client testing.
- **kotlinx-coroutines-test**: Dispatcher management and `runTest`.

---

## 2. Common Test Conventions

Unit tests are written in `commonTest` so they execute across both Android and iOS targets.

### Flow Testing with Turbine Example:
```kotlin
@Test
fun testGetPeriodsFlow() = runTest {
    repository.getPeriods().test {
        val initialLoading = awaitItem()
        assertThat(initialLoading).isInstanceOf(Result.Loading::class)

        val success = awaitItem()
        assertThat(success).isInstanceOf(Result.Success::class)

        awaitComplete()
    }
}
```

---

## 3. Running Tests

```bash
# Run all unit tests
./gradlew test

# Run tests for specific modules
./gradlew :data:allTests
./gradlew :composeApp:allTests
```
