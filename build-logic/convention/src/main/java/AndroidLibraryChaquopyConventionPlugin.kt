
import caios.android.kanade.configureChaquopy
import com.android.build.gradle.LibraryExtension
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.configure

class AndroidLibraryChaquopyConventionPlugin: Plugin<Project> {
    override fun apply(target: Project) {
        with(target) {
            with(pluginManager) {
                apply("com.chaquo.python")
            }

            extensions.configure<LibraryExtension> {
                configureChaquopy(this)
            }
        }
    }
}
