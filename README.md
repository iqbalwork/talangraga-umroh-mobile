# 🕋 Talangraga Umroh

**Talangraga Umroh** is a digital savings management app developed by **Padepokan Talangraga** to help members and administrators manage **Umroh savings programs** in a transparent and structured way.

The app is built using **Kotlin Multiplatform (KMP)** and **Compose Multiplatform**, enabling a **shared codebase** across **Android** and **iOS** platforms.

---

## 📘 Overview

**Padepokan Talangraga** is a spiritual and community organization that runs a *collective Umroh savings program* to help its members prepare financially for their Umroh pilgrimage.

Previously, savings and transactions were tracked manually — using spreadsheets or chat logs — leading to inefficiency and errors.  
This app was created to simplify, centralize, and automate the entire savings tracking process.

### 🎯 Objectives
- Digitize and streamline Umroh savings management.
- Improve transparency between members and admins.
- Display monthly savings progress in real-time.
- Support the digital transformation of Padepokan Talangraga’s financial programs.

### 🧩 Key Features
- **Member Dashboard** — total savings, average deposits, and monthly summary.
- **Transaction Management** — record, verify, and track savings history.
- **Period Tracking** — monitor savings based on custom or monthly periods.
- **Admin Panel** — manage members, approve transactions, and view reports.

---

## ⚙️ Technical Overview

### 🧱 Architecture
UI (Compose Multiplatform)
↓
ViewModel (MVVM)
↓
UseCase (Business Logic)
↓
Repository (Network + Local)
↓
DataSource (Ktor API / SQLDelight DB)

---

## 🧰 Tech Stack

| Layer | Technology | Description |
|-------|-------------|-------------|
| **Language** | Kotlin Multiplatform | Shared logic for Android & iOS |
| **UI Framework** | Compose Multiplatform (Material 3) | Declarative UI for both platforms |
| **Networking** | Ktor Client | REST API communication (Strapi / Ktor Server) |
| **Database** | SQLDelight | Multiplatform local database |
| **Dependency Injection** | Koin | Lightweight DI for modular architecture |
| **Serialization** | kotlinx.serialization | JSON parsing |
| **Reactivity** | Kotlin Flow + Coroutines | Reactive & asynchronous data flow |
| **Build System** | Gradle (Kotlin DSL) | Multiplatform project configuration |
| **IDE** | Android Studio / IntelliJ IDEA | Primary development environment |

---

## 🧩 Project Modules
:composeApp # Shared UI (Android + iOS using Compose)
:shared # Data, domain, and repository layers
:iosApp # iOS entry point (SwiftUI interop)

---

## 🧑‍💻 Developers

Developed by **Padepokan Talangraga Developer Team**  
as part of the digital transformation initiative for the Umroh Savings Program.

---

## 🚀 Status

📱 **Platforms:** Android & iOS  
⚙️ **Version:** Beta Development  
🛠️ **Backend:** Strapi CMS → (planned migration to Ktor Server)

---

## 📄 License

The license will be defined after internal testing and release.  
Currently, this project is internal and used exclusively by **Padepokan Talangraga**.

---
