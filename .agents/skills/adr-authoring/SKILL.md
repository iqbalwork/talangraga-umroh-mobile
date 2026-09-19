---
name: adr-authoring
description: Rules and templates for authoring Architectural Decision Records (ADRs) in Talangraga Umroh Mobile
---

# 🏛️ ADR Authoring Guide

## Numbering & File Naming
- Files go to `.agents/adr/NNNN-kebab-case-title.md` (e.g. `0001-setup-agent-decision-records.md`).
- Always pad numbers to 4 digits (`0001`, `0002`, ...).

## Template
```markdown
# ADR-NNNN: Title

## Status
[Proposed | Accepted | Deprecated | Superseded by ADR-XXXX]

## Context
What is the problem, motivation, or technical requirement driving this decision?

## Decision
What is the specific architectural or design decision?

## Consequences
- Positive outcomes
- Trade-offs / downsides
- Follow-up work required
```
