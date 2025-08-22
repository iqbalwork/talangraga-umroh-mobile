import org.jetbrains.kotlin.gradle.ExperimentalKotlinGradlePluginApi
import org.jetbrains.kotlin.gradle.dsl.JvmTarget

plugins {
    alias(libs.plugins.kotlinMultiplatform)
    alias(libs.plugins.androidApplication)
    alias(libs.plugins.composeMultiplatform)
    alias(libs.plugins.composeCompiler)
    alias(libs.plugins.kotlinx.serialization)
    alias(libs.plugins.buildConfig)
}

buildConfig {
    // Define a string constant named 'BASE_URL'
    packageName = "com.talangraga.talangragaumrohmobile"
    val buildType = project.findProperty("buildType") ?: "release"
    val stagingUrl = project.findProperty("stagingUrl") ?: ""
    val productionUrl = project.findProperty("productionUrl") ?: ""
    val token = project.findProperty("token") ?: ""
    // Set the default value for all build types
    buildConfigField("String", "BUILD_TYPE", "\"$buildType\"")
    buildConfigField(type = "String", name = "STAGING_BASE_URL", expression = "\"$stagingUrl\"")
    buildConfigField(
        type = "String",
        name = "PRODUCTION_BASE_URL",
        expression = "\"$productionUrl\""
    )
    buildConfigField(type = "String", name = "TOKEN", expression = "\"$token\"")
}

kotlin {
    androidTarget {
        @OptIn(ExperimentalKotlinGradlePluginApi::class)
        compilerOptions {
            jvmTarget.set(JvmTarget.JVM_11)
        }
    }
    
    listOf(
        iosX64(),
        iosArm64(),
        iosSimulatorArm64()
    ).forEach { iosTarget ->
        iosTarget.binaries.framework {
            baseName = "ComposeApp"
            isStatic = true
            freeCompilerArgs += "-Xbinary=bundleId=com.talangraga.talangragaumrohmobile.app"
        }
    }
    
    sourceSets {
        androidMain.dependencies {
            implementation(compose.preview)
            implementation(libs.androidx.activity.compose)
            implementation(libs.koin.android)
            implementation(libs.ktor.client.okhttp)
        }
        commonMain.dependencies {
            implementation(compose.runtime)
            implementation(compose.foundation)
            implementation(compose.material3)
            implementation(compose.ui)
            implementation(compose.components.resources)
            implementation(compose.components.uiToolingPreview)
            implementation(libs.voyager.navigator)
            implementation(libs.voyager.screenmodel)
            implementation(libs.voyager.bottomsheetnavigator)
            implementation(libs.voyager.tabnavigator)
            implementation(libs.voyager.transition)
            implementation(libs.voyager.koin)
            implementation(libs.material.icons.extended)
            implementation(libs.androidx.lifecycle.viewmodelCompose)
            implementation(libs.androidx.lifecycle.runtimeCompose)
            implementation(libs.koin.core)
            implementation(libs.koin.compose)
            implementation(libs.koin.compose.viewmodel)
            implementation(libs.ktor.client.core)
            implementation(libs.ktor.client.content.negotiation)
            implementation(libs.ktor.serialization.kotlinx.json)
            implementation(libs.ktor.client.logging)
            implementation(libs.kstore.core)
            implementation(libs.kstore.file)
            implementation(libs.kotlinx.serialization.json)
        }
        iosMain.dependencies{
            implementation(libs.ktor.client.darwin)
            implementation(libs.kstore.file)
        }
        commonTest.dependencies {
            implementation(libs.kotlin.test)
        }
    }
}

android {
    namespace = "com.talangraga.talangragaumrohmobile"
    compileSdk = libs.versions.android.compileSdk.get().toInt()

    defaultConfig {
        applicationId = "com.talangraga.talangragaumrohmobile"
        minSdk = libs.versions.android.minSdk.get().toInt()
        targetSdk = libs.versions.android.targetSdk.get().toInt()
        versionCode = 1
        versionName = "1.0"
    }
    packaging {
        resources {
            excludes += "/META-INF/{AL2.0,LGPL2.1}"
        }
    }
    buildTypes {
        getByName("release") {
            isMinifyEnabled = false
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }
}

dependencies {
    debugImplementation(compose.uiTooling)
}
