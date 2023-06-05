plugins {
    id("kanade.library")
    id("kanade.detekt")
}

android {
    namespace = "caios.android.kanade.core.model"
}

dependencies {
    api(libs.bundles.infra.api)

    implementation(libs.androidx.media.common)
}