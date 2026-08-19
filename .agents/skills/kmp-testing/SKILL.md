---
name: kmp-testing
description: Testing standards with Turbine, AssertK, and Ktor MockEngine in Talangraga Umroh Mobile
---

# 🧪 KMP Testing Guidelines

## 1. Stack
- **Turbine**: Flow testing
- **AssertK**: Fluent assertions
- **Ktor MockEngine**: HTTP client response stubbing
- **kotlinx-coroutines-test**: `runTest` & test dispatchers

## 2. Test Placement
- Tests are placed in `commonTest` so they can run across all platforms.
- Example: `data/src/commonTest/kotlin/RefreshTokenTest.kt`.
