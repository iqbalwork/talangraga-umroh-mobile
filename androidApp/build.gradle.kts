import java.io.FileInputStream
import java.util.Properties

plugins {
    alias(libs.plugins.androidApplication)
    alias(libs.plugins.composeMultiplatform)
    alias(libs.plugins.composeCompiler)
    alias(libs.plugins.google.services)
    alias(libs.plugins.crashlytics)
}

val secretPropertiesFile = rootProject.file("secret.properties")
val secretProperties = Properties()

if (secretPropertiesFile.exists()) {
    secretProperties.load(FileInputStream(secretPropertiesFile))
} else {
    secretProperties.setProperty("signing_keystore_password", System.getenv("signing_keystore_password") ?: "")
    secretProperties.setProperty("signing_key_password", System.getenv("signing_key_password") ?: "")
    secretProperties.setProperty("signing_key_alias", System.getenv("signing_key_alias") ?: "")
    secretProperties.setProperty("signing_key_location", System.getenv("signing_key_location") ?: "")
}

val stagingUrl = secretProperties["stagingUrl"] as? String ?: ""
val productionUrl = secretProperties["productionUrl"] as? String ?: ""

kotlin {
    jvmToolchain(17)
}

android {
    namespace = "com.talangraga.umroh"
    compileSdk = libs.versions.android.compileSdk.get().toInt()

    defaultConfig {
        applicationId = "com.talangraga.umroh"
        minSdk = libs.versions.android.minSdk.get().toInt()
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
//        consumerProguardFiles("consumer-rules.pro")
    }

    packaging {
        resources {
            excludes += "/META-INF/{AL2.0,LGPL2.1}"
        }
    }

    signingConfigs {
        create("release") {
            storePassword = secretProperties["signing_keystore_password"] as? String
            keyAlias = secretProperties["signing_key_alias"] as? String
            keyPassword = secretProperties["signing_key_password"] as? String
            storeFile = secretProperties["signing_key_location"]?.let { file(it) }
        }
    }

    buildTypes {
        release {
            signingConfig = signingConfigs.getByName("release")
            isMinifyEnabled = true
            isShrinkResources = true
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
        debug {
            signingConfig = signingConfigs.getByName("release")
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
    }

    flavorDimensions += "version"
    productFlavors {
        create("staging") {
            dimension = "version"
            applicationIdSuffix = ".dev"
            versionNameSuffix = "-dev"
//            applicationIdSuffix = ".staging"
        }
        create("production") {
            dimension = "version"
        }
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }

    buildFeatures {
        compose = true
    }
}

dependencies {
    implementation(project(":composeApp"))
    implementation(platform(libs.firebase.bom))
    implementation(libs.firebase.crashlytics)
    implementation(libs.firebase.analytics)

//    implementation(libs.androidx.core.ktx)
//    implementation(libs.androidx.appcompat)
//    implementation(libs.material)
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.testExt.junit)
    androidTestImplementation(libs.androidx.espresso.core)
}
