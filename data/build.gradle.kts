import com.codingfeline.buildkonfig.compiler.FieldSpec.Type.BOOLEAN
import com.codingfeline.buildkonfig.compiler.FieldSpec.Type.STRING
import org.jetbrains.kotlin.gradle.ExperimentalKotlinGradlePluginApi
import org.jetbrains.kotlin.gradle.dsl.JvmTarget
import java.util.Properties

plugins {
    alias(libs.plugins.kotlinMultiplatform)
    alias(libs.plugins.androidLibrary)
    alias(libs.plugins.composeMultiplatform)
    alias(libs.plugins.composeCompiler)
    alias(libs.plugins.kotlinx.serialization)
    alias(libs.plugins.kotlinParcelize)
    alias(libs.plugins.buildKonfig)
    alias(libs.plugins.sqldelight)
}

buildkonfig {
    packageName = "com.talangraga.data"

    val secretPropertiesFile = rootProject.file("secret.properties")
    val secretProperties = Properties().apply {
        if (secretPropertiesFile.exists()) {
            load(secretPropertiesFile.inputStream())
        }
    }

//    val stagingBaseUrl = secretProperties["staging.baseUrl"] as? String ?: ""

//    val stagingUrl = project.findProperty("stagingUrl") ?: ""
    val stagingUrl = secretProperties["stagingUrl"] as? String ?: ""
    val productionUrl = secretProperties["productionUrl"] as? String ?: ""
//    val productionUrl = project.findProperty("productionUrl") ?: ""

    defaultConfigs {
        buildConfigField(BOOLEAN, "IS_DEBUG", "true")
        buildConfigField(STRING, "BASE_URL", stagingUrl)
    }
    // flavor is passed as a first argument of defaultConfigs
    defaultConfigs("production") {
        buildConfigField(BOOLEAN, "IS_DEBUG", "false")
        buildConfigField(STRING, "BASE_URL", productionUrl)
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
            baseName = "data"
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
            implementation(libs.android.driver)
        }
        commonMain.dependencies {
            implementation(compose.runtime)
            implementation(compose.foundation)
            implementation(compose.material3)
            implementation(compose.ui)
            implementation(compose.components.resources)
            implementation(compose.components.uiToolingPreview)
            implementation(libs.navigation.compose)
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
            implementation(libs.ktor.client.auth)
            implementation(libs.kotlinx.serialization.json)
            implementation(libs.coil.compose)
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

            implementation(project(":shared"))
        }
        iosMain.dependencies {
            implementation(libs.ktor.client.darwin)
            implementation(libs.native.driver)
        }
        nativeMain.dependencies {
            implementation(libs.native.driver)
        }
        commonTest.dependencies {
            implementation(libs.kotlin.test)
            implementation(libs.ktor.client.mock)
            implementation(libs.assertk)
            implementation(libs.turbine)
        }
    }
}

android {
    namespace = "com.talangraga.data"
    compileSdk = libs.versions.android.compileSdk.get().toInt()

    defaultConfig {
        minSdk = libs.versions.android.minSdk.get().toInt()
        targetSdk = libs.versions.android.targetSdk.get().toInt()
    }
    buildTypes {
        getByName("release") {
            isMinifyEnabled = true
        }
        getByName("debug") {
            isMinifyEnabled = false
        }
    }
//    flavorDimensions += "version"
//    productFlavors {
//        create("staging") {
//            dimension = "version"
//        }
//        create("production") {
//            dimension = "version"
//        }
//    }
}

dependencies {
    debugImplementation(compose.uiTooling)
}

sqldelight {
    databases {
        create("TalangragaDatabase") {
            packageName.set("com.talangraga")
            // optional: specify srcDirs if you place .sq files outside default
            // srcDirs.setFrom("src/commonMain/sqldelight")
//            verifyMigrations.set(false)
        }
    }
}
