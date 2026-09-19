# 🤖 AI Agent Development Workflow

This document outlines the standard 8-step workflow for AI coding agents working on **Talangraga Umroh Mobile**.

---

## 8-Step Workflow

1. **State & Memory Check**:
   - Read `.agents/memory/STATE.md` to grasp the current active goal, open items, and blockers.

2. **Codebase Exploration**:
   - Use search tools (`find_by_name`, `grep_search`) to locate relevant files across `:composeApp`, `:data`, `:shared`.

3. **Architectural Decision (ADR)**:
   - For non-trivial changes, draft an ADR under `.agents/adr/` (e.g. `0002-feature-name.md`).

4. **Implementation Plan**:
   - Create a step-by-step implementation plan in `.agents/plan/` or tool plan.

5. **Implementation**:
   - Implement changes adhering to Clean Architecture & MVI patterns:
     - DTOs / Entities / Queries in `:data`
     - Domain Repositories & Mappers in `:data`
     - ViewModels & Compose Screens in `:composeApp`
     - Register DI in `ViewModelModule.kt` / `SharedModule.kt`

6. **Automated & Manual Verification**:
   - Run unit tests (`./gradlew test` / `:data:testDebugUnitTest` / `:composeApp:testDebugUnitTest`).

7. **Record State & Changelog**:
   - Update `.agents/memory/STATE.md` with new progress.
   - Append entry to `.agents/changelog/AI_CHANGELOG.md`.

8. **Handover**:
   - Provide a clean, concise summary of changes and validation results.
