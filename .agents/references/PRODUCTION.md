# 🚀 Production & Build Configuration Guide

This document outlines build settings, environment management, and release configurations for **Talangraga Umroh Mobile**.

---

## 1. Environment Management via BuildKonfig

Compile-time constants are injected via BuildKonfig in `data/build.gradle.kts` and `composeApp/build.gradle.kts`:

```kotlin
buildkonfig {
    packageName = "com.talangraga.data"
    defaultConfigs {
        buildConfigField(STRING, "BASE_URL", "https://api.talangraga.com/")
    }
}
```

---

## 2. Platform SDK Levels & Versions

- **AGP**: `9.2.1`
- **Kotlin**: `2.2.21`
- **Compile SDK**: `37`
- **Min SDK**: `26`
- **Target SDK**: `35`
- **JVM Target**: `21`

---

## 3. R8 / ProGuard Optimization

- Proguard rules defined in `androidApp/proguard-rules.pro` and `androidApp/consumer-rules.pro`.
- Multiplatform serialization and SQLDelight keep rules are properly configured to prevent reflection stripping.
