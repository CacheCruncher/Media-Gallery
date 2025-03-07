## 📷 Simple Gallery App

A lightweight Android Gallery Application that allows users to view images and play videos seamlessly. The app follows modern Android development practices and is built with a clean architecture approach.

## 🎯 Features

- 🖼️ Display images and videos.
- 📂 Show albums present on the device.
- 🌍 Supports multiple languages: **Arabic, English**.
- 🌙☀️ Supports themes: **Dark and Light**.

## 🏗️ Tech Stack & Architecture

This app uses Clean Architecture with MVVM for a maintainable, testable structure.

* **Clean Architecture:** Core business logic is independent of UI/frameworks.
* **MVVM:** Presentation layer separates UI (View) from logic (ViewModel).

### Layers:

- **Data Layer**: Retrieves media data from the device using queries on the Media Store.
- **Domain Layer**: Contains interactors that map data models to UI models, ensuring separation of business logic and UI logic.
- **Presentation Layer**: Includes View, ViewModel, Data Binding, and View Binding for seamless UI updates.

## Libraries Used

- 🏗️ **Dependency Injection**: Hilt
- 🖼️ **Image Loading**: Glide
- 🔀 **Navigation**: Jetpack Navigation Component
- 🔄 **State Management**: StateFlow (to transfer data from the data layer to UI rendering)
- 🔗 **Data Binding**: Used for binding UI data to data sources
- 📂 **Media Store**: Used for querying media files from the device
- 🧪 **Unit Testing**: PowerMockito (for ViewModel, Transformer, Repository testing)

## Permissions

- 🔑 Handles media access permissions dynamically based on API levels.

  **API 33 or above:**
  ```xml
  <uses-permission android:name="android.permission.READ_MEDIA_IMAGES" />
  <uses-permission android:name="android.permission.READ_MEDIA_VIDEO" />
  ```
  
  **Below API 33:**
  ```xml
  <uses-permission android:name="android.permission.READ_EXTERNAL_STORAGE" />
  ```
  
## 📸 Screenshots
| Album Screen | Album Details Screen |
|-------------|----------------------|
| ![Album Screen](https://github.com/user-attachments/assets/016cad35-530c-462d-97d2-20e08e4fb913) | ![Album Details Screen](https://github.com/user-attachments/assets/8d8d771d-bbd6-4262-870b-08f6406ce37c) |

| Photo Screen | Video Screen |
|-------------|-------------|
| ![Photo Screen](https://github.com/user-attachments/assets/2cbddd4c-d8fa-41f8-aa34-97426bde40d5) | ![Video Screen](https://github.com/user-attachments/assets/ac369c99-d0fd-477c-b4a7-2787dcde6ffc) |


## 🚀 How to Run the App 
1️⃣ Clone the repository:  
   ``` git clone https://github.com/CacheCruncher/Media-Gallery.git  ```

2️⃣ Open in Android Studio and build the project

3️⃣ Run on an Emulator or Physical Device

