plugins {
    id("kanade.application")
    id("kanade.application.compose")
    id("kanade.hilt")
    id("kanade.detekt")
}

apply(from =  "${project.rootDir}/gradle/keystore/default.gradle")

android {
    namespace = "caios.android.kanade"

    buildTypes {
        release {
            isMinifyEnabled = true
            proguardFiles(getDefaultProguardFile("proguard-android-optimize.txt"), "proguard-rules.pro")
        }
        debug {
            isDebuggable = true
            versionNameSuffix = ".D"
            applicationIdSuffix = ".debug"
            signingConfig = signingConfigs.getByName("debug")
        }
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

    implementation(project(":feature:album"))
    implementation(project(":feature:artist"))
    implementation(project(":feature:playlist"))
    implementation(project(":feature:song"))
    implementation(project(":feature:home"))
    implementation(project(":feature:menu"))

    implementation(platform(libs.firebase.bom))
    implementation(platform(libs.androidx.compose.bom))

    implementation(libs.bundles.ui.implementation)
    implementation(libs.bundles.firebase)
    implementation(libs.bundles.ktor)

    kapt(libs.bundles.ui.kapt)

    implementation(libs.androidx.core.splashscreen)

    implementation(libs.play.service.billing)
    implementation(libs.play.service.oss)
    implementation(libs.google.material)

    debugImplementation(libs.facebook.flipper)
    debugImplementation(libs.facebook.flipper.network)
    debugImplementation(libs.facebook.flipper.leakcanary)
    debugImplementation(libs.facebook.soloader)

    debugImplementation(libs.leakcanary)

    testImplementation("junit:junit:4.13.2")
    androidTestImplementation("androidx.test.ext:junit:1.1.5")
    androidTestImplementation("androidx.test.espresso:espresso-core:3.5.1")
}