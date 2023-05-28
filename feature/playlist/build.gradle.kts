plugins {
    id("caios.library")
    id("caios.library.compose")
    id("caios.detekt")
}

android {
    namespace = "caios.android.kanade.feature.playlist"
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