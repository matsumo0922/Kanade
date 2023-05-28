package caios.android.kanade

import com.android.build.gradle.internal.dsl.BaseAppModuleExtension
import org.gradle.api.Project
import org.gradle.api.artifacts.VersionCatalogsExtension
import org.gradle.api.plugins.ExtensionAware
import org.gradle.kotlin.dsl.getByType

internal fun Project.configureApplication() {
    androidExt {
        val libs = extensions.getByType<VersionCatalogsExtension>().named("libs")
        
        defaultConfig {
            applicationId = "caios.android.kanade"
            
            versionName = libs.findVersion("versionName").get().toString()
            versionCode = libs.findVersion("versionCode").get().toString().toInt()
            
            testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        }
        
        splits {
            abi {
                isEnable = true
                isUniversalApk = true

                reset()
                include("x86", "x86_64", "armeabi-v7a", "arm64-v8a")
            }
        }
    }
}

private fun Project.androidExt(configure: BaseAppModuleExtension.() -> Unit) {
    (this as ExtensionAware).extensions.configure("android", configure)
}