plugins {
    id("caios.library")
    id("caios.detekt")
    id("caios.hilt")
}

android {
    namespace = "caios.android.kanade.core.repository"
}

dependencies {
    implementation(project(":core:common"))
    implementation(project(":core:model"))
    implementation(project(":core:datastore"))
}