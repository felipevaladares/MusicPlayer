# MusicPlayer

A modern Android music player app built with Kotlin and Jetpack Compose that allows users to search for songs from the iTunes API, cache them locally, and manage a recently played list.

## Features

- **Search Songs**: Search for songs by artist or title using the iTunes API
- **Local Caching**: Songs are cached locally using Room database for offline access
- **Recently Played**: View a list of recently played songs on app launch
- **Album Details**: Fetch and display album information with all songs
- **Pull to Refresh**: Refresh search results with pull-to-refresh functionality
- **Modern UI**: Built with Jetpack Compose for a smooth, responsive interface

## TODO / Future Enhancements
- **Screenshot tests**: Add tests using [paparazzi](github.com/cashapp/paparazzi) to ensure UI consistency and catch visual regressions
- **Design System**: Implement a design system for consistent styling across the app, including colors, typography, and spacing
- **Album**: Play songs from the album screen
- **Player controls**: Implement basic player controls (play, pause, skip)
- **Pagination**: Implement pagination for search results to load more songs as the user scrolls
- **Error Handling**: Improve error handling and user feedback for network issues or empty search results

## Tech Stack

- **Language**: Kotlin
- **UI Framework**: Jetpack Compose
- **Architecture**: MVVM/MVI with Repository pattern
- **Dependency Injection**: Hilt
- **Database**: Room
- **Networking**: Retrofit with Moshi
- **Async Programming**: Coroutines and Flow
- **Build Tool**: Gradle with Kotlin DSL

## Project Structure

```
MusicPlayer/
├── app/                    # Main application module
│   ├── src/main/java/com/felpster/musicplayer/
│   │   ├── data/           # Data layer (Repository, DAO, API)
│   │   ├── di/             # Dependency injection modules
│   │   ├── domain/         # Domain models and repository interfaces
│   │   └── presentation/   # UI layer (ViewModels, Composables)
│   └── src/test/           # Unit tests
├── core-ui/                # Shared UI components
├── gradle/                 # Gradle wrapper and properties
└── build.gradle.kts        # Project build configuration
```

## Setup Instructions

### Prerequisites

- Android Studio Arctic Fox or later
- JDK 17 or later
- Android SDK API 28+

### Installation

1. Clone the repository:
   ```bash
   git clone https://github.com/yourusername/MusicPlayer.git
   cd MusicPlayer
   ```

2. Open the project in Android Studio

3. Sync the project with Gradle files

4. Build and run the app on an emulator or device

### API Configuration

The app uses the iTunes Search API, which is publicly available and requires no API key.

## Usage

1. **Launch the App**: The app starts by displaying recently played songs
2. **Search**: Use the search bar to find songs by artist or title
3. **Play Songs**: Tap on a song to navigate to the player (player implementation not included in this repo)
4. **View Albums**: Press the options menu in a song and select "View Album" to see album details
5. **Refresh**: Pull down on the song list to refresh search results
