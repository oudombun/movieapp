# Movie App

A minimal Android movie discovery app built with **Kotlin** and **Jetpack Compose**. Browse movies by category, view details, and explore featured, recommended, and upcoming titles. Data is powered by [The Movie Database (TMDB)](https://www.themoviedb.org/) API.

---

## Tech Stack

### Language & runtime
- **Kotlin** 1.7.20  
- **JVM target** 1.8  

### Build & tooling
- **Gradle** 7.x  
- **Android Gradle Plugin** 7.4.2  
- **Kotlin Parcelize** (Parcelable support)  
- **kapt** (annotation processing)  

### UI
- **Jetpack Compose** (Compose BOM 2022.12.00)  
  - `androidx.compose.ui:ui`  
  - `androidx.compose.foundation:foundation`  
  - `androidx.compose.material3:material3`  
  - `androidx.compose.material:material-icons-extended`  
- **Activity Compose** 1.6.1  
- **Navigation Compose** 2.5.3  
- **Coil** 2.2.2 (image loading: `coil`, `coil-compose`)  
- **View Binding** & **Data Binding** (legacy XML screens)  
- **Material Components** (legacy)  
- **ConstraintLayout** 2.0.2  
- **RecyclerView** 1.1.0  

### Architecture & state
- **ViewModel** (lifecycle-viewmodel-ktx)  
- **LiveData** (lifecycle-livedata-ktx, compose runtime-livedata)  
- **Kotlin Coroutines** (core + android 1.3.9)  
- **Lifecycle** (extensions, runtime-ktx)  

### Dependency injection
- **Hilt** (Dagger) 2.48.1  
  - `hilt-android`, `hilt-android-compiler`  
  - **Hilt Navigation Compose** 1.0.0  

### Networking
- **Retrofit** 2.9.0  
- **Gson** (converter + standalone 2.8.6)  
- **OkHttp** 4.10.0  
- **OkHttp Logging Interceptor** 4.10.0  

### Local storage
- **Room** 2.2.5 (runtime, ktx, compiler, testing)  
- **DataStore Preferences** 1.0.0-alpha01  

### Navigation (legacy)
- **Navigation Component** (fragment-ktx, ui-ktx) 2.3.0  

### Other libraries
- **Facebook Shimmer** 0.5.0  
- **Jsoup** 1.13.1  
- **Glide** 4.14.2 (+ compiler)  
- **AndroidX Core KTX**, **AppCompat**, **Legacy Support**  

### Testing
- **JUnit** 4.13  
- **AndroidX Test** (Ext JUnit, Espresso)  
- **Room Testing**  

---

## Project structure (high level)

- `app/src/main/java/com/oudombun/movieminiapp/`
  - **UI**: `ui/activities/`, `ui/compose/`, `ui/fragment/`, `ui/viewmodels/`, `ui/adapter/`, `ui/bindingAdapter/`
  - **Data**: `data/network/`, `data/response/`, `data/Repository.kt`
  - **DI**: `di/NetworkModule.kt`
  - **App**: `MyApplication.kt`, `util/` (e.g. `NetworkResult.kt`)

Compose entry: `MainActivity` → `AppNav()` → `NavHost` (home / detail). Home uses dynamic genre tabs, horizontal movie rows, and skeleton loading; detail is a full-screen Compose screen with back navigation and no system ActionBar (theme uses `NoActionBar`).

---

## Setup & run

1. **Clone** the repository.  
2. Open in **Android Studio** (or use CLI).  
3. Ensure **Android SDK** is installed (compileSdk/targetSdk 33, minSdk 21).  
4. **Sync** Gradle and run on a device or emulator:

```bash
./gradlew assembleDebug
# or
./gradlew installDebug
```

API key and base URL are set in `app/build.gradle` buildTypes (debug/release). Replace the API key with your own [TMDB API key](https://www.themoviedb.org/settings/api) for production.

---

## License

This project is for reference/portfolio use. Movie data and images are provided by TMDB; see [TMDB terms](https://www.themoviedb.org/documentation/api/terms-of-use).
