import com.codingfeline.buildkonfig.compiler.FieldSpec.Type.BOOLEAN
import com.codingfeline.buildkonfig.compiler.FieldSpec.Type.STRING
import java.util.Properties

plugins {
    alias(libs.plugins.kotlinMultiplatform)
    alias(libs.plugins.android.kotlin.multiplatform.library)
    alias(libs.plugins.composeMultiplatform)
    alias(libs.plugins.composeCompiler)
    alias(libs.plugins.kotlinx.serialization)
    alias(libs.plugins.kotlinParcelize)
    alias(libs.plugins.kotzilla)
    alias(libs.plugins.buildKonfig)
}

buildkonfig {
    packageName = "com.talangraga.umrohmobile"

    val secretPropertiesFile = rootProject.file("secret.properties")
    val secretProperties = Properties().apply {
        if (secretPropertiesFile.exists()) {
            load(secretPropertiesFile.inputStream())
        }
    }

    val kotzillaStagingKey = secretProperties["kotzillaStagingKey"] as? String ?: ""
    val kotzillaProductionKey = secretProperties["kotzillaProductionKey"] as? String ?: ""

    val isProduction = project.hasProperty("production") ||
            project.findProperty("android.injected.build.variant")?.toString()?.contains("production", ignoreCase = true) == true ||
            System.getenv("CONFIGURATION")?.contains("production", ignoreCase = true) == true ||
            gradle.startParameter.taskNames.any { it.contains("production", ignoreCase = true) }

    val isRelease = project.hasProperty("release") ||
            project.findProperty("android.injected.build.variant")?.toString()?.contains("release", ignoreCase = true) == true ||
            System.getenv("CONFIGURATION")?.contains("release", ignoreCase = true) == true ||
            gradle.startParameter.taskNames.any { it.contains("release", ignoreCase = true) }

    defaultConfigs {
        buildConfigField(BOOLEAN, "IS_DEBUG", (!isRelease).toString())
        buildConfigField(STRING, "KOTZILLA_KEY", if (isProduction) kotzillaProductionKey else kotzillaStagingKey)
    }
}

kotlin {
    jvmToolchain(17)

    android {
        namespace = "com.talangraga.umrohmobile"
        compileSdk = libs.versions.android.compileSdk.get().toInt()

        androidResources {
            enable = true
        }
        withHostTest {

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
            api(project.dependencies.platform(libs.firebase.bom))
            implementation(libs.firebase.crashlytics)
            implementation(libs.firebase.analytics)
            api(libs.ui.tooling.preview)
            api(libs.ui.tooling)
            api(libs.androidx.activity.compose)
            api(libs.koin.android)
            api(libs.ktor.client.okhttp)
        }
        commonMain.dependencies {
            api(libs.jetbrains.runtime)
            api(libs.foundation)
            api(libs.material3)
            api(libs.ui)
            api(libs.components.resources)
            api(libs.ui.tooling.preview)
            api(libs.coil.compose)
            api(libs.coil.network.ktor)
            api(libs.navigation.compose)
            api(libs.compose.ui.backhandler)
            api(libs.material.icons.extended)
            api(libs.androidx.lifecycle.viewmodelCompose)
            api(libs.androidx.lifecycle.runtimeCompose)
            api(libs.kotzilla.sdk.compose)
            api(libs.koin.core)
            api(libs.koin.compose)
            api(libs.koin.compose.viewmodel)
            api(libs.ktor.client.core)
            api(libs.ktor.client.content.negotiation)
            api(libs.ktor.serialization.kotlinx.json)
            api(libs.ktor.client.logging)
            api(libs.ktor.client.auth)
            api(libs.kotlinx.serialization.json)
            api(libs.constraintlayout.compose.multiplatform)
            api(libs.napier)
            api(libs.kotlinx.datetime)
            api(libs.inspektify.ktor3)
            // SQLDelight
            api(libs.runtime)
            // optionally coroutines extensions
            api(libs.coroutines.extensions)
            api(libs.multiplatform.settings)
            api(libs.multiplatform.settings.serialization)
            api(libs.multiplatform.settings.coroutines)

            // Gitlive Firebase
            api(libs.firebase.app)
            api(libs.firebase.analytic)
            api(libs.firebase.crashlytic)

            // Media Picker
            api(libs.image.picker.kmp)

            api(project(":data"))
            api(project(":shared"))
        }
        iosMain.dependencies {
            api(libs.ktor.client.darwin)
        }
        nativeMain.dependencies {
        }
        commonTest.dependencies {
            api(libs.kotlin.test)
            api(libs.ktor.client.mock)
        }
    }
}
