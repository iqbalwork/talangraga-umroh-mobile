import com.codingfeline.buildkonfig.compiler.FieldSpec.Type.BOOLEAN
import com.codingfeline.buildkonfig.compiler.FieldSpec.Type.STRING
import org.jetbrains.kotlin.gradle.ExperimentalKotlinGradlePluginApi
import org.jetbrains.kotlin.gradle.dsl.JvmTarget

plugins {
    alias(libs.plugins.kotlinMultiplatform)
    alias(libs.plugins.androidApplication)
    alias(libs.plugins.composeMultiplatform)
    alias(libs.plugins.composeCompiler)
    alias(libs.plugins.kotlinx.serialization)
    alias(libs.plugins.kotlinParcelize)
    alias(libs.plugins.kotzilla)
    alias(libs.plugins.buildKonfig)
    alias(libs.plugins.google.services)
    alias(libs.plugins.crashlytics)
}

buildkonfig {
    packageName = "com.talangraga.umrohmobile"

    val kotzillaStagingKey = project.findProperty("kotzillaStagingKey") ?: ""
    val kotzillaProductionKey = project.findProperty("kotzillaProductionKey") ?: ""

    defaultConfigs {
        buildConfigField(BOOLEAN, "IS_DEBUG", "true")
        buildConfigField(STRING, "KOTZILLA_KEY", "$kotzillaStagingKey")
    }
    // flavor is passed as a first argument of defaultConfigs
    defaultConfigs("production") {
        buildConfigField(BOOLEAN, "IS_DEBUG", "false")
        buildConfigField(STRING, "KOTZILLA_KEY", "$kotzillaProductionKey")
    }

}

kotlin {
    androidTarget {
        @OptIn(ExperimentalKotlinGradlePluginApi::class)
        compilerOptions {
            jvmTarget.set(JvmTarget.JVM_11)
            freeCompilerArgs.add("-Xexpect-actual-classes")
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
            // Required when using NativeSQLiteDriver
            linkerOpts.add("-lsqlite3")
            freeCompilerArgs += "-Xexpect-actual-classes"
            freeCompilerArgs += "-Xbinary=bundleId=com.talangraga.umrohmobile.app"
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
            implementation(libs.coil.compose)
            implementation(libs.coil.network.ktor)
            implementation(libs.navigation.compose)
            implementation(libs.compose.ui.backhandler)
            implementation(libs.material.icons.extended)
            implementation(libs.androidx.lifecycle.viewmodelCompose)
            implementation(libs.androidx.lifecycle.runtimeCompose)
            implementation(libs.kotzilla.sdk.compose)
            implementation(libs.koin.core)
            implementation(libs.koin.compose)
            implementation(libs.koin.compose.viewmodel)
            implementation(libs.ktor.client.core)
            implementation(libs.ktor.client.content.negotiation)
            implementation(libs.ktor.serialization.kotlinx.json)
            implementation(libs.ktor.client.logging)
            implementation(libs.ktor.client.auth)
            implementation(libs.kotlinx.serialization.json)
            implementation(libs.constraintlayout.compose.multiplatform)
            implementation(libs.napier)
            implementation(libs.kotlinx.datetime)
            implementation(libs.inspektify.ktor3)
            // SQLDelight
            implementation(libs.runtime)
            // optionally coroutines extensions
            implementation(libs.coroutines.extensions)
            implementation(libs.multiplatform.settings)
            implementation(libs.multiplatform.settings.serialization)
            implementation(libs.multiplatform.settings.coroutines)

            // Gitlive Firebase
            implementation(libs.firebase.app)
            implementation(libs.firebase.analytic)
            implementation(libs.firebase.crashlytic)

            // Media Picker
            implementation(libs.image.picker.kmp)

            implementation(project(":data"))
            implementation(project(":shared"))
        }
        iosMain.dependencies {
            implementation(libs.ktor.client.darwin)
        }
        nativeMain.dependencies {
        }
        commonTest.dependencies {
            implementation(libs.kotlin.test)
            implementation(libs.ktor.client.mock)
        }
    }
}

kotlin.sourceSets.all {
    kotlin.srcDir("build/generated/ksp/${name}/kotlin")
}

android {
    namespace = "com.talangraga.umrohmobile"
    compileSdk = libs.versions.android.compileSdk.get().toInt()

    defaultConfig {
        applicationId = "com.talangraga.umrohmobile"
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
            isMinifyEnabled = true
        }
        getByName("debug") {
            isMinifyEnabled = false
        }
    }

    flavorDimensions += "version"
    productFlavors {
        create("staging") {
            dimension = "version"
            applicationIdSuffix = ".staging"
        }
        create("production") {
            dimension = "version"
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
