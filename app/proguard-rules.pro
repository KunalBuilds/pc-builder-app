# ─── MyPC Builder — ProGuard Rules ────────────────────────────────────────────

# === Kotlin ===
-keep class kotlin.** { *; }
-keep class kotlin.Metadata { *; }
-keepclassmembers class **$WhenMappings { <fields>; }
-keepclassmembers class kotlin.Lazy { *; }
-dontwarn kotlin.**

# === Kotlin Parcelize ===
-keepclassmembers class * implements android.os.Parcelable {
    static ** CREATOR;
}

# === Coroutines ===
-keepnames class kotlinx.coroutines.** { *; }
-dontwarn kotlinx.coroutines.**

# === Retrofit / OkHttp ===
-dontwarn okhttp3.**
-dontwarn okio.**
-dontwarn javax.annotation.**
-keepattributes Signature
-keepattributes *Annotation*
-keep class retrofit2.** { *; }
-keepclasseswithmembers class * {
    @retrofit2.http.* <methods>;
}

# === Gson (used by Retrofit converter) ===
-keepattributes Signature
-keepattributes *Annotation*
-keep class sun.misc.Unsafe { *; }
-keep class com.google.gson.** { *; }
-keep class * implements com.google.gson.TypeAdapterFactory
-keep class * implements com.google.gson.JsonSerializer
-keep class * implements com.google.gson.JsonDeserializer

# === Keep all data classes (API models + PCComponent etc.) ===
-keep class com.mypcapp.model.** { *; }
-keep class com.mypcapp.network.** { *; }
-keep class com.mypcapp.data.** { *; }

# === Firebase Auth ===
-keepattributes *Annotation*
-keepattributes EnclosingMethod
-keep class com.google.firebase.** { *; }
-keep class com.google.android.gms.** { *; }
-dontwarn com.google.firebase.**
-dontwarn com.google.android.gms.**

# === Firebase Crashlytics ===
-keepattributes *Annotation*
-keepattributes SourceFile,LineNumberTable
-keep public class * extends java.lang.Exception

# === Glide ===
-keep public class * implements com.bumptech.glide.module.GlideModule
-keep class * extends com.bumptech.glide.module.AppGlideModule { <init>(...); }
-keep public enum com.bumptech.glide.load.ImageHeaderParser$** {
    **[] $VALUES;
    public *;
}
-dontwarn com.bumptech.glide.**

# === Lottie ===
-dontwarn com.airbnb.lottie.**
-keep class com.airbnb.lottie.** { *; }

# === Material Components ===
-keep class com.google.android.material.** { *; }

# === AndroidX ===
-keep class androidx.** { *; }
-dontwarn androidx.**

# === Remove debug logging in release ===
-assumenosideeffects class android.util.Log {
    public static int v(...);
    public static int d(...);
    public static int i(...);
}
