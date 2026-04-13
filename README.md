# Moises Music Challenge

A multi-platform Android music application showcasing MVVM architecture with Jetpack Compose, supporting Android Phone and Android Automotive.

## Architecture

The project follows with **MVVM** with **Clean Architecture** principles with the following module structure:

```
MoisesChallenge/
- app/                # Phone app module
- automotive/         # Android Automotive module
- core/
  - domain/           # Domain models, repository interfaces, use cases
  - data/             # Repository implementations, caching (Room), paging
  - network/          # Network abstraction layer (Retrofit + iTunes API)
  - player/           # Media integration
  - ui/               # Shared Compose UI components, theme, and UI models
  - resources/        # Shared resources (strings, drawables)
- shared/
  - player/           # Shared PlayerViewModel and PlayerScreenContract
  - album/            # Shared AlbumViewModel and AlbumScreenContract
```

## Features

### Phone App
- Splash screen with branding
- Songs list with search functionality
- Pagination support
- Music player with full controls (play/pause, next/previous, seek)
- Album detail view with track list
- Repeat modes
- Progress slider with seek functionality
- Mini player bar for background playback
- Track action sheet (view album)
- Playing indicator animation

### Android Automotive
- Full-screen player optimized for car displays
- Large touch targets for driver safety
- Playback controls (play/pause, next/previous, repeat, seek)
- Progress bar with seek functionality
- Album detail view with track list
- Playing indicator animation

## Tech Stack

- **Language**: Kotlin
- **UI**: Jetpack Compose
- **Architecture**: MVVM + Clean Architecture
- **DI**: Hilt
- **Networking**: Retrofit + Kotlinx Serialization
- **Database**: Room (for caching)
- **Pagination**: Paging 3
- **Image Loading**: Coil
- **Testing**: JUnit, MockK, Turbine, Truth

## Requirements

- Android Studio Ladybug or later
- JDK 17
- Android SDK 35
- For Android Automotive: Android Automotive emulator (API 28+)

## Building and Running

### Phone App
```bash
./gradlew :app:installDebug
```

### Android Automotive
1. Create an Android Automotive emulator in Android Studio
2. Run:
```bash
./gradlew :automotive:installDebug
```

## Testing

Run all unit tests:
```bash
./gradlew test
```

## Project Structure Highlights

### Network Abstraction Layer
The network layer is designed to be replaceable. The `MusicNetworkDataSource` interface can be implemented with any music API (Spotify, Deezer, etc.) without affecting other layers.

```kotlin
interface MusicNetworkDataSource {
    suspend fun searchMusic(query: String, limit: Int, offset: Int): Result<ITunesSearchResponse>
    suspend fun lookupTrack(trackId: Long): Result<ITunesSearchResponse>
    suspend fun getAlbumTracks(albumId: Long): Result<ITunesSearchResponse>
}
```

### Shared UI Components
The `core:ui` module contains reusable Compose components with style variants for different platforms:

- `TrackListItem` - Track row with Mobile/Auto styles
- `ProgressSlider` - Seekable progress bar with Mobile/Auto styles
- `PlayingIndicator` - Animated equalizer bars
- `AlbumBackgroundBlur` - Blurred album artwork background
- `PlayerControls` - Play/pause, skip, repeat buttons

### MVVM Pattern
Each screen follows the MVVM pattern with a Contract interface:
- **View**: Composable functions
- **ViewModel**: Holds UI state and implements ActionHandler
- **Contract**: Defines ActionHandler interface and NoOp preview implementation

### Shared ViewModels
The `shared/` modules contain ViewModels and Contracts that are reused between Phone and Automotive apps:

```kotlin
// shared/player - Used by both app and automotive
@HiltViewModel
class PlayerViewModel @Inject constructor(
    getPlayerStateUseCase: GetPlayerStateUseCase,
    private val playerControlsUseCase: PlayerControlsUseCase
) : ViewModel(), PlayerScreenContract.ActionHandler

// shared/album - Used by both app and automotive  
@HiltViewModel
class AlbumViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val musicRepository: MusicRepository,
    private val playTrackUseCase: PlayTrackUseCase,
    getPlayerStateUseCase: GetPlayerStateUseCase
) : ViewModel(), AlbumScreenContract.ActionHandler
```

This eliminates code duplication while allowing platform-specific UI implementations.

### Dependency Injection
Hilt is used throughout the project with modules organized by layer:
- `NetworkModule`: Retrofit, OkHttp, API services
- `DataModule`: Room database, DAOs, repositories
- `PlayerModule`: Media player dependencies
