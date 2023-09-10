plugins {
    id("kanade.library")
    id("kanade.library.compose")
    id("kanade.hilt")
    id("kanade.detekt")
}

android {
    namespace = "caios.android.kanade.feature.billing"
}

dependencies {
    implementation(project(":core:model"))
    implementation(project(":core:common"))
    implementation(project(":core:repository"))
    implementation(project(":core:datastore"))
    implementation(project(":core:design"))
    implementation(project(":core:music"))
    implementation(project(":core:ui"))
    implementation(project(":core:billing"))

    implementation(libs.bundles.ui.implementation)
    implementation(libs.bundles.billing)
    kapt(libs.bundles.ui.kapt)
}
