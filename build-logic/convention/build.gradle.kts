plugins {
    `kotlin-dsl`
}

java {
    sourceCompatibility = JavaVersion.VERSION_17
    targetCompatibility = JavaVersion.VERSION_17
}

dependencies {
    implementation(libs.android.gradlePlugin)
    implementation(libs.kotlin.gradlePlugin)
    implementation(libs.secret.gradlePlugin)
    implementation(libs.detekt.gradlePlugin)
    implementation(libs.firebase.crashlytics)
    implementation(libs.gms.services)
    implementation(libs.gms.oss)
}

gradlePlugin {
    plugins {
        register("androidApplication") {
            id = "caios.application"
            implementationClass = "AndroidApplicationConventionPlugin"
        }
        register("androidApplicationCompose") {
            id = "caios.application.compose"
            implementationClass = "AndroidApplicationComposeConventionPlugin"
        }
        register("androidLibrary") {
            id = "caios.library"
            implementationClass = "AndroidLibraryConventionPlugin"
        }
        register("androidLibraryCompose") {
            id = "caios.library.compose"
            implementationClass = "AndroidLibraryComposeConventionPlugin"
        }
        register("androidHilt") {
            id = "caios.hilt"
            implementationClass = "AndroidHiltConventionPlugin"
        }
        register("AndroidDetekt") {
            id = "caios.detekt"
            implementationClass = "AndroidDetektConventionPlugin"
        }
    }
}
