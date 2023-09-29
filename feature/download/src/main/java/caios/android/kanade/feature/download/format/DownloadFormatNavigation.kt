package caios.android.kanade.feature.download.format

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.ui.Modifier
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavType
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import caios.android.kanade.core.design.animation.NavigateAnimation
import caios.android.kanade.core.model.download.VideoInfo
import kotlinx.serialization.json.Json
import java.net.URLDecoder
import java.net.URLEncoder

const val DownloadFormatInfo = "DownloadInfo"
const val DownloadFormatScreenRoute = "DownloadFormat/{$DownloadFormatInfo}"

fun NavController.navigateToDownloadFormat(videoInfo: VideoInfo) {
    val formatter = Json { ignoreUnknownKeys = true }
    this.navigate("DownloadFormat/${URLEncoder.encode(formatter.encodeToString(VideoInfo.serializer(), videoInfo), "UTF-8")}")
}

fun NavGraphBuilder.downloadFormatScreen(
    navigateToBillingPlus: () -> Unit,
    navigateToTagEdit: (Long) -> Unit,
    terminate: () -> Unit,
) {
    val formatter = Json { ignoreUnknownKeys = true }

    composable(
        route = DownloadFormatScreenRoute,
        arguments = listOf(navArgument(DownloadFormatInfo) { type = NavType.StringType }),
        enterTransition = { NavigateAnimation.Vertical.enter },
        exitTransition = { NavigateAnimation.Vertical.exit },
        popEnterTransition = { NavigateAnimation.Vertical.popEnter },
        popExitTransition = { NavigateAnimation.Vertical.popExit },
    ) {
        DownloadFormatRoute(
            modifier = Modifier.fillMaxSize(),
            videoInfo = formatter.decodeFromString(VideoInfo.serializer(), URLDecoder.decode(it.arguments?.getString(DownloadFormatInfo)!!, "UTF-8")),
            navigateToBillingPlus = navigateToBillingPlus,
            navigateToTagEdit = navigateToTagEdit,
            terminate = terminate,
        )
    }
}
