plugins {
    id("kanade.library")
    id("kanade.library.compose")
    id("kanade.hilt")
    id("kanade.detekt")
}

android {
    namespace = "caios.android.kanade.core.music"
}

dependencies {
    implementation(project(":core:common"))
    implementation(project(":core:model"))
    implementation(project(":core:design"))
    implementation(project(":core:database"))
    implementation(project(":core:datastore"))
    implementation(project(":core:repository"))

    implementation(libs.bundles.youtubedl)

    implementation(libs.kotlinx.coroutines.guava)

    implementation(libs.androidx.constraintlayout)
    implementation(libs.androidx.lifecycle.viewmodel.compose)
    implementation(libs.androidx.media)

    implementation(libs.google.exoplayer)
    implementation(libs.coil.compose)
    implementation(libs.ffmpeg)
}
