package caios.android.kanade

import com.android.build.gradle.internal.dsl.BaseAppModuleExtension
import org.gradle.api.Project
import org.gradle.api.plugins.ExtensionAware

internal fun Project.configureApplication() {
    androidExt {
        defaultConfig {
            applicationId = "caios.android.kanade"
            
            versionName = libs.version("versionName")
            versionCode = libs.version("versionCode").toInt()
            
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
