package caios.android.kanade.feature.setting.ytmusic

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.dialog

const val YTMusicLoginRoute = "YTMusicLogin"

fun NavController.navigateToYTMusicLogin() {
    this.navigate(YTMusicLoginRoute)
}

fun NavGraphBuilder.ytmusicLoginDialog(
    terminate: () -> Unit,
) {
    dialog(
        route = YTMusicLoginRoute,
    ) {
        YTMusicLoginRoute(
            modifier = Modifier
                .fillMaxWidth()
                .wrapContentHeight()
                .clip(RoundedCornerShape(16.dp)),
            terminate = terminate,
        )
    }
}
