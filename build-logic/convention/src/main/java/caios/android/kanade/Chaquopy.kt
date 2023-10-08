package caios.android.kanade

import com.android.build.api.dsl.CommonExtension

internal fun configureChaquopy(commonExtension: CommonExtension<*, *, *, *, *>) {
    commonExtension.apply {
        defaultConfig {
            ndk {
                abiFilters += listOf("armeabi-v7a", "arm64-v8a", "x86_64")
            }
        }
    }
}
