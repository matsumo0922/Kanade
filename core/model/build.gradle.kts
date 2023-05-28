plugins {
    id("caios.library")
    id("caios.detekt")
}

android {
    namespace = "caios.android.kanade.core.model"
}

dependencies {
    api(libs.bundles.infra.api)
}