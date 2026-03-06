# Movie App

A simple Android movie app. The UI was **converted from old XML layouts to Jetpack Compose**; the app now uses Compose for the main flow (home with genre tabs and movie lists, detail screen). Data comes from [The Movie Database (TMDB)](https://www.themoviedb.org/) API.

---

## Tech stack (in use)

- **Kotlin** 1.7.20  
- **Gradle** + **Android Gradle Plugin** 7.4.2  
- **Jetpack Compose** – Material3, Foundation, UI, material-icons-extended, runtime-livedata (Compose BOM 2022.12.00)  
- **Activity Compose** 1.6.1  
- **Navigation Compose** 2.5.3  
- **Hilt** 2.48.1 + **hilt-navigation-compose** 1.0.0  
- **Retrofit** 2.9.0 + **Gson** (converter + 2.8.6)  
- **OkHttp** 4.10.0 + **OkHttp Logging Interceptor** 4.10.0  
- **Coil** 2.2.2 (including **coil-compose**) for image loading  
- **ViewModel** + **LiveData** (lifecycle-viewmodel-ktx, lifecycle-livedata-ktx)  
- **Kotlin Coroutines** (core + android 1.3.9)  
- **Lifecycle** (runtime-ktx, extensions)  
- **AndroidX Core KTX**, **AppCompat**  

---

## Run

1. Open the project in Android Studio and sync Gradle.  
2. Build and run on a device or emulator (e.g. `./gradlew installDebug`).  

API base URL and key are in `app/build.gradle` (buildTypes). Use your own [TMDB API key](https://www.themoviedb.org/settings/api) for production.
