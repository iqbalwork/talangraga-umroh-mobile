# ADR-0001: Setup Agent Decision Records & Knowledge Base

## Status
Accepted

## Context
As the project grows, AI coding agents and human developers require clear, persistent architectural context, coding conventions, and decision history.

## Decision
Adopt the standardized `.agents/` directory structure with:
1. `AGENTS.md` - Primary index and core non-negotiable rules.
2. `references/` - In-depth technical guides (Project setup, conventions, auth refresh, offline-first, etc.).
3. `memory/` - Working memory across sessions (`STATE.md`).
4. `changelog/` - AI development changelog (`AI_CHANGELOG.md`).
5. `adr/` - Agent Decision Records for recording key technical and architectural decisions.

## Consequences
- Better context continuity across AI sessions.
- Clear separation between `:composeApp` (Presentation/UI), `:data` (Business logic/Domain/Persistence), and `:shared` (Shared utilities).
