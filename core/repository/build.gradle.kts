plugins {
    id("kanade.library")
    id("kanade.library.chaquopy")
    id("kanade.detekt")
    id("kanade.hilt")
}

android {
    namespace = "caios.android.kanade.core.repository"
}

chaquopy {
    defaultConfig {
        version = "3.8"

        pip {
            install("ytmusicapi")
        }
    }
}

dependencies {
    implementation(project(":core:common"))
    implementation(project(":core:model"))
    implementation(project(":core:datastore"))
    implementation(project(":core:database"))

    implementation(libs.bundles.ktor)

    implementation(libs.androidx.media)

    implementation(libs.jsoup)
}
