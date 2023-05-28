import org.jetbrains.kotlin.kapt3.base.Kapt.kapt

plugins {
    id("kanade.library")
    id("kanade.library.compose")
    id("kanade.hilt")
    id("kanade.detekt")
}

android {
    namespace = "caios.android.kanade.feature.permission"
}

dependencies {
    implementation(project(":core:model"))
    implementation(project(":core:common"))
    implementation(project(":core:repository"))
    implementation(project(":core:datastore"))
    implementation(project(":core:design"))

    implementation(libs.bundles.ui.implementation)
    kapt(libs.bundles.ui.kapt)
}