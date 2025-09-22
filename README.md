# AichoPaicho - Android Application

AichoPaicho is an Android application designed to help users manage personal records and contacts, with a focus on tracking financial entries or other types of records associated with these contacts. It supports local data storage and synchronization with Firebase (Firestore and Firebase Authentication) for backup and multi-device access.

## Key Features

*   **User Management**:
    *   User registration and login (Google Sign-In via Firebase Authentication).
    *   Offline-first approach with a local user profile.
    *   Online synchronization of user data.
*   **Contact Management**:
    *   Create, view, update, and delete contacts.
    *   Store contact details like name and phone numbers.
    *   Link contacts to a user account.
*   **Record Tracking**:
    *   Log records/transactions with details such as type, amount, date, description, and completion status.
    *   Associate records with specific contacts.
*   **Data Synchronization**:
    *   Robust sync mechanism with Firestore to backup and restore user data, contacts, and records.
    *   Timestamp-based conflict resolution to merge local and remote data.
    *   Background synchronization using Android WorkManager.
*   **Settings & Configuration**:
    *   User-configurable settings, including currency preferences.
    *   Enable/disable backup and sync.
    *   View app information (version, build number).
*   **Modern Android UI**:
    *   Built with Jetpack Compose for a declarative and modern user interface.
    *   Clean dashboard to display summaries and navigate through app features.

## Technology Stack

*   **Programming Language**: Kotlin
*   **UI Framework**: Jetpack Compose
*   **Architecture**: MVVM (Model-View-ViewModel) with a clear separation of concerns (UI, ViewModel, Repository, Data Source).
*   **Dependency Injection**: Hilt (Dagger Hilt)
*   **Local Database**: Room Persistence Library
*   **Backend & Sync**:
    *   Firebase Firestore (for cloud database)
    *   Firebase Authentication (for Google Sign-In)
*   **Asynchronous Programming**: Kotlin Coroutines & Flow
*   **Background Tasks**: Android WorkManager
*   **Build System**: Gradle

## Project Structure

The project is organized into standard Android app modules and follows a typical modern Android architecture:

*   **/app/src/main/java/com/aspiring_creators/aichopaicho/**
    *   **`data/`**: Contains data layer components:
        *   **`dao/`**: Room Data Access Objects (e.g., `UserDao`).
        *   **`entity/`**: Room entity classes (e.g., `User.kt`, `Contact.kt`, `Record.kt`).
        *   **`repository/`**: Repository classes abstracting data sources (e.g., `UserRepository.kt`, `SyncRepository.kt`).
        *   `BackgroundSyncWorker.kt`: Handles background data synchronization.
    *   **`ui/`**: Contains UI layer components (Jetpack Compose):
        *   **`screens/`**: Composable functions for different application screens (e.g., `DashboardScreen.kt`, `SettingsScreen.kt`).
        *   **`component/`**: Reusable UI components.
        *   **`theme/`**: App theme and styling.
    *   **`viewmodel/`**: ViewModel classes (e.g., `DashboardScreenViewModel.kt`, `SettingsViewModel.kt`).
    *   `AichoPaichoApp.kt`: Main Application class.
    *   `CurrencyUtils.kt`: Utility functions.
*   **`/app/src/main/res/`**: Android resources (layouts, drawables, values).
*   **Build Scripts**: `build.gradle.kts` (app and project level), `gradle/libs.versions.toml`.

## License

This project is licensed under the Apache License, Version 2.0. See the [LICENSE](LICENSE) file for details.
