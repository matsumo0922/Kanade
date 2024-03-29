plugins {
    id("kanade.library")
    id("kanade.library.compose")
    id("kanade.detekt")
}

android {
    namespace = "caios.android.kanade.core.model"
}

dependencies {
    implementation(project(":core:common"))
    
    api(libs.bundles.infra.api)

    implementation(libs.androidx.media)
}
