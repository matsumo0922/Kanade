plugins {
    id("kanade.library")
    id("kanade.library.compose")
    id("kanade.hilt")
    id("kanade.detekt")
}

android {
    namespace = "caios.android.kanade.core.ui"
}

dependencies {
    implementation(project(":core:model"))
    implementation(project(":core:common"))
    implementation(project(":core:repository"))
    implementation(project(":core:datastore"))
    implementation(project(":core:design"))
    implementation(project(":core:music"))

    implementation(libs.bundles.ui.implementation)
    kapt(libs.bundles.ui.kapt)

    implementation(libs.androidx.palette)
}