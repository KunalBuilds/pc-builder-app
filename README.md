# 🖥️ MyPC App - Android (Kotlin)

A PC Builder app for the Indian market. Build, compare, and get AI-powered upgrade suggestions for your PC.

📥 **[Download the App (APK)](installapp.apk?raw=true)**

## 🌟 Premium Features
- 🤖 **AI Build Analysis**: Integration with Google Gemini to provide 4K/1080p FPS predictions and personalized upgrade paths.
- 🎬 **Fluid Animations**: Smooth category transitions and Lottie-powered loading states for a high-end feel.
- ☁️ **The Rig Vault**: Save your custom builds to Firebase Firestore and access them across devices.
- 📳 **Haptic Experience**: Tactile feedback on component selection for a more immersive UI.
- 🔐 **Secure Auth**: Google Sign-In and Email/Password authentication via Firebase.

---

## 📸 App Screenshots

<div align="center">
  <img src="android%20studio%20ss/1.png" width="30%">
  <img src="android%20studio%20ss/2.png" width="30%">
  <img src="android%20studio%20ss/3.png" width="30%">
</div>
<div align="center">
  <img src="android%20studio%20ss/4.png" width="30%">
  <img src="android%20studio%20ss/5.png" width="30%">
</div>

---

## 🚀 Setup Instructions

### Step 1: Firebase Setup (Required for Auth)
1. Go to [Firebase Console](https://console.firebase.google.com/)
2. Create a new project called **MyPCApp**
3. Add an Android app with package: `com.mypcapp`
4. Enable **Authentication** → Sign-in methods:
   - Email/Password ✅
   - Google ✅
5. Download **`google-services.json`** and replace `app/google-services.json`
6. Copy your **Web Client ID** from Firebase → Project Settings → General → Web API Key
7. Put it in `app/src/main/res/values/strings.xml` → `default_web_client_id`

### Step 2: Open in Android Studio
1. Open Android Studio
2. File → Open → select the `my-first-app` folder
3. Wait for Gradle sync to complete
4. Connect Android device or start emulator (API 21+)
5. Run the app ▶️

### Step 3: Stitch AI (Already configured!)
The Stitch AI API key is already integrated in `HelpActivity.kt`.
API Key: `[YOUR_API_KEY_HERE]`

The chatbot has built-in fallback responses if the API is unavailable.

---

## 📁 Project Structure
```
my-first-app/
├── app/
│   ├── build.gradle
│   ├── google-services.json        ← Replace with your Firebase config
│   └── src/main/
│       ├── AndroidManifest.xml
│       ├── java/com/mypcapp/
│       │   ├── ui/
│       │   │   ├── SplashActivity.kt
│       │   │   ├── LoginActivity.kt
│       │   │   ├── MainActivity.kt
│       │   │   ├── ComponentSelectionActivity.kt
│       │   │   ├── BudgetInputActivity.kt
│       │   │   ├── ResultActivity.kt
│       │   │   ├── ComparePCActivity.kt
│       │   │   └── HelpActivity.kt
│       │   ├── adapter/
│       │   │   ├── ComponentAdapter.kt
│       │   │   ├── ComponentResultAdapter.kt
│       │   │   ├── SuggestionAdapter.kt
│       │   │   └── ChatAdapter.kt
│       │   ├── data/
│       │   │   └── ComponentDatabase.kt    ← 30+ real components
│       │   ├── model/
│       │   │   ├── PCComponent.kt
│       │   │   └── PCBuild.kt
│       │   └── network/
│       │       └── StitchApiService.kt     ← Stitch AI integration
│       └── res/
│           ├── layout/      ← All 9 XML layouts
│           ├── values/      ← colors, strings, themes
│           └── menu/
├── build.gradle
├── settings.gradle
└── gradle.properties
```

---

## 🛠️ Next Steps to Add
- [ ] Save PC builds to Firestore
- [ ] Real product pricing API (Amazon Product API)
- [ ] Share build as image
- [ ] User profile screen
- [ ] Dark mode
- [ ] PC compatibility checker (socket matching)
