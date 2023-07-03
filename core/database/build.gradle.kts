plugins {
    id("kanade.library")
    id("kanade.detekt")
    id("kanade.hilt")
}

android {
    namespace = "caios.android.kanade.core.database"
}

dependencies {
    api(libs.bundles.infra.api)

    implementation(libs.androidx.room)
    implementation(libs.androidx.room.ktx)

    ksp(libs.androidx.room.compiler)
}
