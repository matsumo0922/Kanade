plugins {
    id("caios.library")
    id("caios.detekt")
    id("caios.hilt")
    alias(libs.plugins.protobuf)
}

android {
    namespace = "caios.android.kanade.core.datastore"
}

protobuf {
    protoc {
        artifact = libs.protobuf.protoc.get().toString()
    }
    generateProtoTasks {
        all().forEach { task ->
            task.builtins {
                register("java") {
                    option("lite")
                }
                register("kotlin") {
                    option("lite")
                }
            }
        }
    }
}

dependencies {
    implementation(project(":core:common"))
    implementation(project(":core:model"))

    api(libs.bundles.infra.api)

    implementation(libs.androidx.datastore)
    implementation(libs.protobuf.kotlin.lite)
}