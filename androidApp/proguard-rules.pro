# Add project specific ProGuard rules here.
# You can control the set of applied configuration files using the
# proguardFiles setting in build.gradle.

# ==============================================================================
# 1. General Kotlin, Coroutines & Attributes
# ==============================================================================
-keepattributes *Annotation*, Signature, InnerClasses, EnclosingMethod, SourceFile, LineNumberTable
-renamesourcefileattribute SourceFile

-keepnames class kotlinx.coroutines.internal.MainDispatcherFactory {}
-keepnames class kotlinx.coroutines.CoroutineExceptionHandler {}
-keepclassmembers class kotlinx.** {
    volatile <fields>;
}

# ==============================================================================
# 2. Kotlinx Serialization
# ==============================================================================
-keepclassmembers class ** {
    @kotlinx.serialization.SerialName <fields>;
}
-keepclassmembers class kotlinx.serialization.json.** {
    *** Companion;
}
-keepclassmembers class * {
    kotlinx.serialization.KSerializer serializer(...);
}
-keepclassmembers class * {
    *** Companion;
}
-keep,allowobfuscation,allowoptimization class * implements kotlinx.serialization.KSerializer
-keep,allowobfuscation,allowoptimization class * implements kotlinx.serialization.internal.GeneratedSerializer
-dontwarn kotlinx.serialization.UnknownFieldException

# ==============================================================================
# 3. Data Models & Navigation Routes (Preserve for Serialization & Reflection)
# ==============================================================================
# Navigation Compose Routes (@Serializable)
-keep class com.talangraga.umrohmobile.navigation.** { *; }

# Presentation UI Data Models
-keep class com.talangraga.umrohmobile.presentation.**.model.** { *; }

# Data Layer Domain & Entity Models
-keep class com.talangraga.data.domain.model.** { *; }
-keep class com.talangraga.data.local.database.model.** { *; }
-keep class com.talangraga.data.network.model.** { *; }
-keep class com.talangraga.data.network.api.ApiResponse** { *; }
-keep class com.talangraga.data.network.api.Result** { *; }

# ==============================================================================
# 4. Ktor Client 3
# ==============================================================================
-keep class io.ktor.** { *; }
-dontwarn io.ktor.**
-dontwarn org.slf4j.**
-dontwarn javax.naming.**

# ==============================================================================
# 5. Koin 4 (Dependency Injection)
# ==============================================================================
-keep class org.koin.core.annotation.** { *; }
-keep @org.koin.core.annotation.* class * { *; }
-keepclassmembers class * extends androidx.lifecycle.ViewModel {
    public <init>(...);
}
-keep class io.insertkoin.** { *; }

# ==============================================================================
# 6. SQLDelight 2 (Database Generated Classes)
# ==============================================================================
# Specifically target SQLDelight generated classes instead of broad package keep
-keep class com.talangraga.TalangragaDatabase** { *; }
-keep class com.talangraga.**Queries { *; }
-keep class com.talangraga.*Data { *; }
-keep class com.talangraga.data.TalangragaDatabaseImpl** { *; }
-keep class app.cash.sqldelight.** { *; }
-keep class app.cash.sqldelight.driver.android.** { *; }

# ==============================================================================
# 7. Multiplatform Settings
# ==============================================================================
-keep class com.russhwolf.settings.** { *; }

# ==============================================================================
# 8. Compose Multiplatform Generated Resources
# ==============================================================================
-keep class talangragaumrohmobile.composeapp.generated.resources.** { *; }

# ==============================================================================
# 9. Coil 3 Image Loader
# ==============================================================================
-keep class coil3.** { *; }
-dontwarn coil3.**
-keep class * implements coil3.util.DecoderServiceLoaderTarget { *; }
-keep class * implements coil3.util.FetcherServiceLoaderTarget { *; }

# ==============================================================================
# 10. BuildKonfig Constants
# ==============================================================================
-keep class com.talangraga.umrohmobile.BuildKonfig { *; }
-keep class com.talangraga.data.BuildKonfig { *; }

# ==============================================================================
# 11. Third-Party Libraries & Logging
# ==============================================================================
# Napier Logging
-keep class io.github.aakira.napier.** { *; }

# Inspektify (Ktor Debug Inspector)
-keep class io.github.bvantur.inspektify.** { *; }

# Kotzilla SDK
-keep class io.kotzilla.** { *; }

# Image Picker KMP
-keep class io.github.ismoy.imagepickerkmp.** { *; }

# Firebase
-keep public class com.google.firebase.** { *; }
