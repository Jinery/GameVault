# GameVault 🎮

<div align="center">

![Kotlin](https://img.shields.io/badge/Kotlin-2.4.0-7F52FF.svg?style=for-the-badge&logo=kotlin&logoColor=white)
![Jetpack Compose](https://img.shields.io/badge/Jetpack%20Compose-2026.05.01-4285F4.svg?style=for-the-badge&logo=jetpackcompose&logoColor=white)
![Android](https://img.shields.io/badge/Android-3DDC84.svg?style=for-the-badge&logo=android&logoColor=white)
![Gradle](https://img.shields.io/badge/Gradle-8.9-02303A.svg?style=for-the-badge&logo=gradle&logoColor=white)
![CI](https://img.shields.io/github/actions/workflow/status/kychnoo/GameVault/android-test.yml?style=for-the-badge&logo=githubactions&label=CI)
![License](https://img.shields.io/badge/License-GPLv2-blue.svg?style=for-the-badge&logo=opensourceinitiative&logoColor=white)

</div>

**GameVault** is a modern Android application designed for gamers to discover, search, and explore detailed information about video games. Built with cutting-edge Android technologies following Clean Architecture principles.

## 🚀 Features

- **Explore Games**: Discover popular and trending games on the main dashboard.
- **Deep Search**: Find any game using a powerful search engine powered by the RAWG API.
- **Search History**: Keep track of your previous searches with persistent local storage.
- **Game Details**: View high-quality screenshots, descriptions, ratings, and development team information.
- **Game Suggestions**: Discover similar titles through the "Suggested Games" feature.
- **Shared Element Transitions**: Smooth and immersive transitions when moving between screens.
- **Custom UI Components**: Beautifully crafted animations and a custom-designed bottom navigation bar.

## 📱 Screenshots

<div align="center">
  <table>
    <tr>
      <td><img src="./docs/images/main_screen.png" width="200" alt="Main Screen"/></td>
      <td><img src="./docs/images/search_screen.png" width="200" alt="Search Screen"/></td>
      <td><img src="./docs/images/search_screen_expanded_bar.png" width="200" alt="Expanded Search"/></td>
    </tr>
    <tr>
      <td align="center"><b>🏠 Home</b></td>
      <td align="center"><b>🔍 Search</b></td>
      <td align="center"><b>🔎 Expanded Search</b></td>
    </tr>
    <tr>
      <td><img src="./docs/images/search_screen_filters.png" width="200" alt="Filters"/></td>
      <td><img src="./docs/images/search_screen_with_results.png" width="200" alt="Search Results"/></td>
      <td><img src="./docs/images/game_detail_screen.png" width="200" alt="Game Details"/></td>
    </tr>
    <tr>
      <td align="center"><b>⚙️ Filters</b></td>
      <td align="center"><b>📋 Results</b></td>
      <td align="center"><b>🎮 Details</b></td>
    </tr>
    <tr>
      <td colspan="3" align="center">
        <img src="./docs/images/game_detail_metacritic_and_developers.png" width="200" alt="Metacritic & Developers"/>
        <br><b>⭐ Metacritic & Developers</b>
      </td>
    </tr>
  </table>
</div>

## 🛠 Tech Stack

<div align="center">

### 🏗 **Architecture & Patterns**
![Clean Architecture](https://img.shields.io/badge/Clean%20Architecture-3DDC84?style=flat-square&logo=android&logoColor=white)
![MVVM](https://img.shields.io/badge/MVVM-4285F4?style=flat-square&logo=android&logoColor=white)
![Repository Pattern](https://img.shields.io/badge/Repository%20Pattern-FF6D00?style=flat-square&logo=android&logoColor=white)

### 🖼 **UI & Animations**
![Jetpack Compose](https://img.shields.io/badge/Jetpack%20Compose-2026.05.01-4285F4?style=flat-square&logo=jetpackcompose&logoColor=white)
![Coil](https://img.shields.io/badge/Coil-3.4.0-4CAF50?style=flat-square&logo=coil&logoColor=white)
![Material 3](https://img.shields.io/badge/Material%203-757575?style=flat-square&logo=materialdesign&logoColor=white)

### 🔧 **DI & Networking**
![Koin](https://img.shields.io/badge/Koin-4.2.1-FF6D00?style=flat-square&logo=koin&logoColor=white)
![Retrofit](https://img.shields.io/badge/Retrofit-3.0.0-FF6D00?style=flat-square&logo=retrofit&logoColor=white)
![OkHttp](https://img.shields.io/badge/OkHttp-5.0.0-3DDC84?style=flat-square&logo=square&logoColor=white)

### 🗄 **Database & Caching**
![Room](https://img.shields.io/badge/Room-2.8.4-FF6D00?style=flat-square&logo=sqlite&logoColor=white)
![Kotlinx Serialization](https://img.shields.io/badge/Kotlinx%20Serialization-1.11.0-7F52FF?style=flat-square&logo=kotlin&logoColor=white)

### ⚙️ **Plugins & Tools**
![KSP](https://img.shields.io/badge/KSP-2.3.6-7F52FF?style=flat-square&logo=kotlin&logoColor=white)
![Gradle](https://img.shields.io/badge/Gradle-8.9-02303A?style=flat-square&logo=gradle&logoColor=white)
![Kotlin Compose Plugin](https://img.shields.io/badge/Kotlin%20Compose%20Plugin-2.4.0-7F52FF?style=flat-square&logo=kotlin&logoColor=white)

</div>

---

## 📋 Development Environment

| Component | Version |
|-----------|---------|
| **Android Studio** | Quail 1 (2025.1.3) or newer |
| **Gradle** | 8.9 |
| **JDK** | 17 or 21 |
| **Kotlin** | 2.4.0 |
| **Min SDK** | 24 (Android 7.0) |
| **Target SDK** | 36 (Android 16) |
| **Compile SDK** | 37 (Android 16) |

---

## 🔑 Setup Guide

### 1. Get RAWG API Key
Register at [RAWG.io](https://rawg.io/apidocs) and obtain your API key.

### 2. Create Local Configuration
Create `local.properties` in the project root:

```properties
RAWG_API_KEY=your_api_key_here
```

### 3. Enterprise API Configuration (Optional)
If you have an Enterprise RAWG account, modify `build.gradle.kts`:

```kotlin
val isEnterpriseApiUser = true
```

**Note:** For standard accounts, replace the `games/{id}/suggested` endpoint with `games/{id}/game-series` in `RawgApi.kt`.

### 4. Sync & Build
```bash
./gradlew build
# Or use Android Studio: File → Sync Project with Gradle Files
```

---

## 🚀 Building & Running

### From Android Studio

1. Open project in Android Studio Quail 1+
2. Wait for Gradle sync to complete
3. Select emulator or physical device
4. Hit **Run** ▶️

### APK (Release Build)

```bash
./gradlew assembleRelease
```

APK location: `app/build/outputs/apk/release/`

### Running Tests
```bash
# Unit tests
./gradlew testDebugUnitTest
```

Automated testing on every push and PR:

<div align="center">

| Platform | Status |Description |
|----------|--------|------------|
|GitHub Actions | ![Github CI](https://img.shields.io/github/actions/workflow/status/kychnoo/GameVault/android-test.yml?style=flat-square&logo=githubactions&label=CI) | Runs tests on push to master & PR |
| GitLab CI | ![Gitlab CI](https://img.shields.io/gitlab/pipeline-status/kychnoo/GameVault?style=flat-square&logo=gitlab&label=CI) | Mirror from GitHub with test execution |

</div>

## 📄 License

This project is licensed under the GPL-2.0 License - see the [LICENSE](LICENSE) file for details.

---

<div align=center>
 <h3> Built with ❤️ and Jetpack Compose </h3>
</div>
