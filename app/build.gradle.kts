@file:Suppress("UnstableApiUsage")

import com.android.build.api.variant.BuildConfigField
import com.android.build.api.variant.ResValue
import org.jetbrains.kotlin.konan.properties.Properties
import java.io.Serializable

plugins {
    id("kanade.application")
    id("kanade.application.compose")
    id("kanade.hilt")
    id("kanade.detekt")
}

android {
    namespace = "caios.android.kanade"

    splits {
        abi {
            reset()
            include("arm64-v8a", "armeabi-v7a", "x86", "x86_64")
            isUniversalApk = false
        }
    }

    val localProperties = Properties().apply {
        load(project.rootDir.resolve("local.properties").inputStream())
    }

    signingConfigs {
        getByName("debug") {
            storeFile = file("${project.rootDir}/gradle/keystore/debug.keystore")
        }
        create("release") {
            storeFile = file("${project.rootDir}/gradle/keystore/release.jks")
            storePassword = localProperties.getProperty("storePassword") ?: System.getenv("RELEASE_STORE_PASSWORD")
            keyPassword = localProperties.getProperty("keyPassword") ?: System.getenv("RELEASE_KEY_PASSWORD")
            keyAlias = localProperties.getProperty("keyAlias") ?: System.getenv("RELEASE_KEY_ALIAS")
        }
        create("billing") {
            storeFile = file("${project.rootDir}/gradle/keystore/release.jks")
            storePassword = localProperties.getProperty("storePassword") ?: System.getenv("RELEASE_STORE_PASSWORD")
            keyPassword = localProperties.getProperty("keyPassword") ?: System.getenv("RELEASE_KEY_PASSWORD")
            keyAlias = localProperties.getProperty("keyAlias") ?: System.getenv("RELEASE_KEY_ALIAS")
        }
    }

    buildTypes {
        release {
            signingConfig = signingConfigs.getByName("release")
            isMinifyEnabled = true
            proguardFiles(getDefaultProguardFile("proguard-android-optimize.txt"), "proguard-rules.pro")
        }
        debug {
            signingConfig = signingConfigs.getByName("debug")
            isDebuggable = true
            versionNameSuffix = ".D"
            applicationIdSuffix = ".debug3"
        }
        create("billing") {
            signingConfig = signingConfigs.getByName("billing")
            isDebuggable = true
            matchingFallbacks.add("debug")
        }
    }

    androidComponents {
        onVariants {
            val appName = when (it.buildType) {
                "debug" -> "KanadeDebug3"
                "billing" -> "KanadeBilling"
                else -> "Kanade"
            }

            it.resValues.put(it.makeResValueKey("string", "app_name"), ResValue(appName, null))
            it.buildConfigFields.apply {
                putBuildConfig(localProperties, "VERSION_NAME", libs.versions.versionName.get().toStringLiteral())
                putBuildConfig(localProperties, "VERSION_CODE", libs.versions.versionCode.get().toStringLiteral())
                putBuildConfig(localProperties, "DEVELOPER_PASSWORD")
                putBuildConfig(localProperties, "LAST_FM_API_KEY")
                putBuildConfig(localProperties, "LAST_FM_API_SECRET")
                putBuildConfig(localProperties, "MUSIXMATCH_API_KEY")
                putBuildConfig(localProperties, "YTMUSIC_CLIENT_ID")
                putBuildConfig(localProperties, "YTMUSIC_CLIENT_SECRET")
            }

            if (it.buildType == "release") {
                it.packaging.resources.excludes.add("META-INF/**")
            }
        }
    }

    lint {
        // Error: MusicService must extend android.app.Service [Instantiatable]
        disable.add("Instantiatable")
    }
}

kapt {
    correctErrorTypes = true
}

dependencies {
    implementation(project(":core:common"))
    implementation(project(":core:design"))
    implementation(project(":core:model"))
    implementation(project(":core:datastore"))
    implementation(project(":core:repository"))
    implementation(project(":core:ui"))
    implementation(project(":core:music"))
    implementation(project(":core:billing"))

    implementation(project(":feature:album"))
    implementation(project(":feature:artist"))
    implementation(project(":feature:playlist"))
    implementation(project(":feature:song"))
    implementation(project(":feature:home"))
    implementation(project(":feature:menu"))
    implementation(project(":feature:queue"))
    implementation(project(":feature:search"))
    implementation(project(":feature:sort"))
    implementation(project(":feature:lyrics"))
    implementation(project(":feature:information"))
    implementation(project(":feature:setting"))
    implementation(project(":feature:share"))
    implementation(project(":feature:tag"))
    implementation(project(":feature:download"))
    implementation(project(":feature:report"))
    implementation(project(":feature:equalizer"))
    implementation(project(":feature:billing"))
    implementation(project(":feature:welcome"))
    implementation(project(":feature:widget"))

    implementation(platform(libs.firebase.bom))
    implementation(platform(libs.androidx.compose.bom))

    implementation(libs.bundles.ui.implementation)
    implementation(libs.bundles.firebase)
    implementation(libs.bundles.ktor)
    implementation(libs.bundles.youtubedl)

    kapt(libs.bundles.ui.kapt)

    implementation(libs.androidx.core.splashscreen)

    implementation(libs.play.service.oss)
    implementation(libs.google.material)

    debugImplementation(libs.facebook.flipper)
    debugImplementation(libs.facebook.flipper.network)
    debugImplementation(libs.facebook.flipper.leakcanary)
    debugImplementation(libs.facebook.soloader)
    //debugImplementation(libs.leakcanary)
}

plugins.apply("com.google.gms.google-services")

fun MapProperty<String, BuildConfigField<out Serializable>>.putBuildConfig(
    localProperties: Properties,
    key: String,
    value: String? = null,
    type: String = "String",
    comment: String? = null
) {
    put(key, BuildConfigField(type, value ?: localProperties.getProperty(key) ?: System.getenv(key) ?: "\"\"", comment))
}

fun Any.toStringLiteral(): String {
    return "\"$this\""
}
