plugins {
    id("caios.library")
    id("caios.library.compose")
    id("caios.detekt")
}

android {
    namespace = "caios.android.kanade.core.design"
}

dependencies {
    implementation(project(":core:model"))

    api(libs.bundles.infra.api)
    implementation(libs.bundles.ui.implementation)
    kapt(libs.bundles.ui.kapt)
}