package caios.android.kanade.feature.playlist.fab

import androidx.annotation.StringRes
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Input
import androidx.compose.material.icons.filled.Public
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import caios.android.kanade.core.common.network.util.ToastUtil
import caios.android.kanade.core.design.R

@Composable
fun FabPlaylistRoute(
    navigateToCreatePlaylist: () -> Unit,
    navigateToImportPlaylist: () -> Unit,
    terminate: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val context = LocalContext.current

    Column(
        modifier = modifier
            .background(MaterialTheme.colorScheme.surface)
            .padding(vertical = 8.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        FabItem(
            modifier = Modifier
                .fillMaxWidth()
                .clickable {
                    terminate.invoke()
                    navigateToCreatePlaylist.invoke()
                },
            textRes = R.string.playlist_fab_create_new,
            imageVector = Icons.Default.Add,
        )

        FabItem(
            modifier = Modifier
                .fillMaxWidth()
                .clickable {
                    terminate.invoke()
                    navigateToImportPlaylist.invoke()
                },
            textRes = R.string.playlist_fab_external,
            imageVector = Icons.Default.Public,
        )

        FabItem(
            modifier = Modifier
                .fillMaxWidth()
                .clickable {
                    ToastUtil.show(context, R.string.error_developing_feature)
                    terminate.invoke()
                },
            textRes = R.string.playlist_fab_import,
            imageVector = Icons.Default.Input,
        )
    }
}

@Composable
private fun FabItem(
    @StringRes textRes: Int,
    imageVector: ImageVector,
    modifier: Modifier = Modifier,
) {
    Row(
        modifier = modifier.padding(24.dp, 16.dp),
        horizontalArrangement = Arrangement.spacedBy(16.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Icon(
            modifier = Modifier
                .size(32.dp)
                .clip(RoundedCornerShape(16.dp))
                .padding(4.dp),
            imageVector = imageVector,
            contentDescription = null,
        )

        Text(
            modifier = Modifier.weight(1f),
            text = stringResource(textRes),
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurface,
        )
    }
}
