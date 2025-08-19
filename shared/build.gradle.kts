import org.jetbrains.kotlin.gradle.ExperimentalKotlinGradlePluginApi
import org.jetbrains.kotlin.gradle.dsl.JvmTarget

plugins {
    alias(libs.plugins.kotlinMultiplatform)
    alias(libs.plugins.androidLibrary)
    alias(libs.plugins.kotlinx.serialization)
    alias(libs.plugins.buildConfig)
}

buildConfig {
    // Define a string constant named 'BASE_URL'
    packageName = "com.talangraga.umrohmobile.shared"
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
            baseName = "Shared"
            isStatic = true
        }
    }

    sourceSets {
        commonMain.dependencies {
            // put your Multiplatform dependencies here
            implementation(libs.ktor.client.core)
            implementation(libs.ktor.client.content.negotiation)
            implementation(libs.ktor.client.logging)
            implementation(libs.ktor.serialization.kotlinx.json)
            implementation(libs.kotlinx.serialization.json)
            implementation(libs.koin.core)
            implementation(libs.kstore)
            implementation(libs.kstore.file)
            implementation(libs.napier.logging)
        }
        androidMain.dependencies {
            implementation(libs.ktor.client.okhttp)
            implementation(libs.koin.android)
//            implementation(libs.multiplatform.settings.android)
        }
        iosMain.dependencies {
            implementation(libs.ktor.client.darwin)
//            implementation(libs.multiplatform.settings.ios)
        }
        commonTest.dependencies {
            implementation(libs.kotlin.test)
        }
    }

    targets.all {
        compilations.all {
            compileTaskProvider.configure {
                compilerOptions {
                    freeCompilerArgs.add("-Xexpect-actual-classes")
                }
            }
        }
    }
}

android {
    namespace = "com.talangraga.umrohmobile.shared"
    compileSdk = libs.versions.android.compileSdk.get().toInt()
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }
    defaultConfig {
        minSdk = libs.versions.android.minSdk.get().toInt()
    }
}
