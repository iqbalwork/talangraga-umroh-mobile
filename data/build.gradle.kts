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

    val stagingUrl = secretProperties["stagingUrl"] as? String ?: ""
    val productionUrl = secretProperties["productionUrl"] as? String ?: ""

    val envConfig = System.getenv("CONFIGURATION") ?: ""
    val taskNames = gradle.startParameter.taskNames.toString()
    
    // Cek dari berbagai sumber
    val isProduction = project.hasProperty("production") ||
            project.findProperty("android.injected.build.variant")?.toString()?.contains("production", ignoreCase = true) == true ||
            envConfig.contains("production", ignoreCase = true) ||
            taskNames.contains("production", ignoreCase = true)

    // Log untuk debugging saat build (Muncul di Build Output)
    println("BuildKonfig Debug: isProduction=$isProduction, CONFIGURATION=$envConfig, Tasks=$taskNames")

    val isRelease = project.hasProperty("release") ||
            project.findProperty("android.injected.build.variant")?.toString()?.contains("release", ignoreCase = true) == true ||
            envConfig.contains("release", ignoreCase = true) ||
            taskNames.contains("release", ignoreCase = true)

    defaultConfigs {
        buildConfigField(BOOLEAN, "IS_DEBUG", (!isRelease).toString())
        buildConfigField(STRING, "BASE_URL", if (isProduction) productionUrl else stagingUrl)
    }
}

kotlin {
    jvmToolchain(17)

    android {
        namespace = "com.talangraga.data"
        compileSdk = libs.versions.android.compileSdk.get().toInt()

        androidResources {
            enable = true
        }
        withHostTest {  }
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
            implementation(libs.androidx.activity.compose)
            implementation(libs.koin.android)
            implementation(libs.ktor.client.okhttp)
            implementation(libs.android.driver)
        }
        commonMain.dependencies {
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

sqldelight {
    databases {
        create("TalangragaDatabase") {
            packageName.set("com.talangraga")
            version = 2
            // optional: specify srcDirs if you place .sq files outside default
            // srcDirs.setFrom("src/commonMain/sqldelight")
//            verifyMigrations.set(false)
        }
    }
}
