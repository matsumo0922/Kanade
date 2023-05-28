plugins {
    id("caios.library")
    id("caios.detekt")
    id("caios.hilt")
}

android {
    namespace = "caios.android.kanade.core.common"
}

dependencies {
    api(libs.bundles.infra.api)
}