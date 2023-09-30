
import caios.android.kanade.configureKotlinAndroid
import caios.android.kanade.libs
import com.android.build.gradle.LibraryExtension
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.configure

class AndroidLibraryConventionPlugin: Plugin<Project> {
    override fun apply(target: Project) {
        with(target) {
            with(pluginManager) {
                apply("com.android.library")
                apply("kotlin-android")
                apply("kotlin-kapt")
                apply("kotlin-parcelize")
                apply("kotlinx-serialization")
                apply("project-report")
                apply("com.google.devtools.ksp")
            }

            extensions.configure<LibraryExtension> {
                configureKotlinAndroid(this)

                defaultConfig.targetSdk = libs.findVersion("targetSdk").get().toString().toInt()
                buildFeatures.viewBinding = true
            }
        }
    }
}
