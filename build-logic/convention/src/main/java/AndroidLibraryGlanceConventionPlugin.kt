
import caios.android.kanade.libs
import caios.android.kanade.version
import com.android.build.gradle.LibraryExtension
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.configure
import org.gradle.kotlin.dsl.dependencies

class AndroidLibraryGlanceConventionPlugin: Plugin<Project> {
    override fun apply(target: Project) {
        with(target) {
            extensions.configure<LibraryExtension> {
                buildFeatures.compose = true
                composeOptions.kotlinCompilerExtensionVersion = libs.version("kotlinCompiler")
            }

            dependencies {
                add("implementation", libs.findBundle("glance").get())
            }
        }
    }
}
