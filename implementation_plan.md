# My PC App - Android (Kotlin) Implementation Plan

## Structure
- Package: com.mypcapp
- Min SDK: 21, Target SDK: 34
- Architecture: MVVM + Firebase

## Screens
1. SplashActivity
2. LoginActivity (Google Sign-In + Email/Password)
3. MainActivity (Home - buttons: Make New PC, Compare My PC)
4. ComponentSelectionActivity (CPU, GPU, RAM, SSD, etc.)
5. BudgetInputActivity
6. ResultActivity (show PC build, suggest upgrades, buy links)
7. HelpActivity (Call + AI Chatbot via Stitch API)

## Dependencies
- Firebase Auth, Firestore
- Google Sign-In
- Retrofit (HTTP)
- Stitch AI Chat API (YOUR_API_KEY)
- Navigation Component
- Glide (images)
- Material Design 3
