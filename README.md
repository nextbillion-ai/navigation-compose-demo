# NextBillion Navigation Kotlin Demo

A demonstration Android application showcasing the integration of NextBillion Maps and Navigation SDKs using Jetpack Compose.

## Overview

This project demonstrates how to implement a navigation application using NextBillion's SDKs with modern Android development practices:

- Jetpack Compose for UI
- Maps Component for interactive maps
- Navigation Component for screen navigation
- Material 3 design system
- Kotlin-first approach

## Features

- Interactive map with location tracking
- Route planning and visualization
- Turn-by-turn navigation
- Multiple navigation examples:
  - Basic navigation
  - Parameter passing
  - Navigation launching and callback listener

## Prerequisites

- Android Studio Arctic Fox (2020.3.1) or newer
- JDK 8 or newer
- JVM 11
- Android Gradle Plugin 8.0+
- Android SDK with API level 34
- NextBillion API key

## Setup

1. Clone the repository
2. Open the project in Android Studio
3. Add your NextBillion API key to the project:
   - Replace your API key in the `DemoApplication` class: `Nextbillion.getInstance(context, "YOUR_API_KEY")`
4. Sync the project with Gradle files
5. Run the application on an emulator or physical device

## Required Permissions

The following permissions need to be declared in the `AndroidManifest.xml`:

```xml
    <uses-permission android:name="android.permission.ACCESS_FINE_LOCATION"/>
    <uses-permission android:name="android.permission.ACCESS_COARSE_LOCATION"/>
    <uses-permission android:name="android.permission.FOREGROUND_SERVICE"/>
    <uses-permission android:name="android.permission.ACCESS_BACKGROUND_LOCATION" />
    <uses-permission android:name="android.permission.INTERNET" />
    <uses-permission android:name="android.permission.FOREGROUND_SERVICE_LOCATION"/>
    <uses-permission android:name="android.permission.POST_NOTIFICATIONS" />

```

## Important Implementation Notes

1. **Navigation Activity Requirements**:
   - If you're using `NBNavigationView` as a navigation component, the activity must extend `FragmentActivity`
   - Example: `class NavigationActivity : FragmentActivity(), NavigationListener, RouteListener { ... }`

2. **Activity Configuration**:
   - For activities that host navigation, add the following to the `AndroidManifest.xml`:
   ```xml
     android:configChanges="orientation|screenSize|keyboardHidden"
   ```

## Project Structure

- `app/src/main/java/ai/nextbillion/compose/example/`
  - `navigation/` - Navigation graph and routes
  - `screens/` - Compose UI screens
  - `ui/` - UI components and theme
  - `utils/` - Utility classes
  - `viewModel/` - ViewModels for state management

## Dependencies

- NextBillion SDKs:
  - `nb-navigation-compose:2.0.2`
  - `nb-maps-compose:1.1.5`
  - `nb-navigation-android:2.0.2`

- AndroidX and Compose:
  - Navigation Compose
  - Material 3
  - ConstraintLayout Compose
  - CardView

## Usage

1. Launch the application
2. Select a navigation example from the home screen
3. For the map example:
   - Long press on the map to set a destination
   - View the calculated route
   - Start navigation by tapping on the route

![Screenshot_20250408_152431.png](..%2F..%2F..%2Freport_11%2FScreenshot_20250408_152431.png)
![Screenshot_20250408_152519.png](..%2F..%2F..%2Freport_11%2FScreenshot_20250408_152519.png)
## Troubleshooting

- **API Key**: Verify your API key is correctly set in the `DemoApplication` class
- **Permissions**: Ensure all required permissions are granted at runtime for Android 6.0+

## License

This project is licensed under the MIT License - see the LICENSE file for details.

## Acknowledgments

- NextBillion for providing the Maps and Navigation SDKs
- Android team for Jetpack Compose and other modern Android libraries 