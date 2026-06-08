# Add project specific ProGuard rules here.
# You can control the set of applied configuration files using the
# proguardFiles setting in build.gradle.

# --- General Kotlin & Coroutines ---
-keepattributes *Annotation*, Signature, InnerClasses, EnclosingMethod
-keepnames class kotlinx.coroutines.internal.MainDispatcherFactory {}
-keepnames class kotlinx.coroutines.CoroutineExceptionHandler {}
-keepclassmembers class kotlinx.** {
    volatile <fields>;
}

# --- Kotlinx Serialization ---
-keepclassmembers class ** {
    @kotlinx.serialization.SerialName <fields>;
}
-keepclassmembers class kotlinx.serialization.json.** {
    *** Companion;
}
-keep @kotlinx.serialization.Serializable class * { *; }
-dontwarn kotlinx.serialization.UnknownFieldException

# --- Ktor 3 ---
-keep class io.ktor.** { *; }
-dontwarn io.ktor.**
-dontwarn org.slf4j.**
-dontwarn javax.naming.**

# --- Koin 4 ---
-keep class org.koin.core.annotation.** { *; }
-keep @org.koin.core.annotation.* class * { *; }
-keepclassmembers class * extends androidx.lifecycle.ViewModel {
    public <init>(...);
}
-keep class io.insertkoin.** { *; }

# --- SQLDelight 2 ---
-keep class app.cash.sqldelight.** { *; }
-keep class com.talangraga.** { *; }
-keep class app.cash.sqldelight.driver.android.** { *; }

# --- Coil 3 ---
-keep class coil3.** { *; }
-dontwarn coil3.**
-keep class * implements coil3.util.DecoderServiceLoaderTarget { *; }
-keep class * implements coil3.util.FetcherServiceLoaderTarget { *; }

# --- Napier Logging ---
-keep class io.github.aakira.napier.** { *; }

# --- Inspektify ---
-keep class io.github.bvantur.inspektify.** { *; }

# --- Kotzilla ---
-keep class io.kotzilla.** { *; }

# --- Image Picker KMP ---
-keep class io.github.ismoy.imagepickerkmp.** { *; }

# --- Data Models (Preserve for Serialization) ---
-keep class com.talangraga.umrohmobile.presentation.**.model.** { *; }
-keep class com.talangraga.data.domain.model.** { *; }
-keep class com.talangraga.data.local.database.model.** { *; }
-keep class com.talangraga.data.network.model.** { *; }

# --- BuildKonfig ---
-keep class com.talangraga.umrohmobile.BuildKonfig { *; }
-keep class com.talangraga.data.BuildKonfig { *; }

# --- Firebase ---
# Most Firebase rules are handled by the plugin, but keeping some core attributes is safer
-keepattributes *Annotation*
-keepattributes SourceFile,LineNumberTable
-keep public class com.google.firebase.** { *; }

# Uncomment this to preserve the line number information for debugging stack traces.
-keepattributes SourceFile,LineNumberTable

# If you keep the line number information, uncomment this to hide the original source file name.
-renamesourcefileattribute SourceFile
