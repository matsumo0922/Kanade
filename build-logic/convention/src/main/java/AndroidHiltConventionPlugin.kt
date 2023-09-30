
import caios.android.kanade.library
import caios.android.kanade.libs
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.dependencies

class AndroidHiltConventionPlugin : Plugin<Project> {
    override fun apply(target: Project) {
        with(target) {
            with(pluginManager) {
                apply("dagger.hilt.android.plugin")
                apply("org.jetbrains.kotlin.kapt")
            }

            dependencies {
                "implementation"(libs.library("dagger.hilt"))
                "kapt"(libs.library("dagger.hilt.compiler"))
            }
        }
    }
}
