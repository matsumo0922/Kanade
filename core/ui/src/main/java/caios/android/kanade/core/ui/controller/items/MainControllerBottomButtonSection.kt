package caios.android.kanade.core.ui.controller.items

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.clickable
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material.icons.outlined.Article
import androidx.compose.material.icons.outlined.Bedtime
import androidx.compose.material.icons.outlined.GraphicEq
import androidx.compose.material.icons.outlined.PlaylistPlay
import androidx.compose.material3.Icon
import androidx.compose.material3.LocalContentColor
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import caios.android.kanade.core.common.network.util.ToastUtil
import caios.android.kanade.core.design.R
import caios.android.kanade.core.design.component.KanadeBackground
import caios.android.kanade.core.design.theme.Red40

@OptIn(ExperimentalFoundationApi::class)
@Composable
internal fun MainControllerBottomButtonSection(
    isFavorite: Boolean,
    onClickLyrics: () -> Unit,
    onClickLyricsEdit: () -> Unit,
    onClickFavorite: () -> Unit,
    onClickQueue: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val context = LocalContext.current

    Row(
        modifier = modifier,
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(
            space = 24.dp,
            alignment = Alignment.CenterHorizontally,
        ),
    ) {
        Icon(
            modifier = Modifier
                .size(40.dp)
                .clip(RoundedCornerShape(50))
                .combinedClickable(
                    onClick = onClickLyrics,
                    onLongClick = onClickLyricsEdit,
                )
                .padding(8.dp),
            imageVector = Icons.Outlined.Article,
            contentDescription = null,
        )

        Icon(
            modifier = Modifier
                .size(40.dp)
                .clip(RoundedCornerShape(50))
                .clickable { onClickFavorite.invoke() }
                .padding(8.dp),
            imageVector = if (isFavorite) Icons.Default.Favorite else Icons.Default.FavoriteBorder,
            contentDescription = null,
            tint = if (isFavorite) Red40 else LocalContentColor.current,
        )

        Icon(
            modifier = Modifier
                .size(40.dp)
                .clip(RoundedCornerShape(50))
                .clickable { ToastUtil.show(context, R.string.error_developing_feature) }
                .padding(8.dp),
            imageVector = Icons.Outlined.Bedtime,
            contentDescription = null,
        )

        Icon(
            modifier = Modifier
                .size(40.dp)
                .clip(RoundedCornerShape(50))
                .clickable { onClickQueue.invoke() }
                .padding(8.dp),
            imageVector = Icons.Outlined.PlaylistPlay,
            contentDescription = null,
        )

        Icon(
            modifier = Modifier
                .size(40.dp)
                .clip(RoundedCornerShape(50))
                .clickable { ToastUtil.show(context, R.string.error_developing_feature) }
                .padding(8.dp),
            imageVector = Icons.Outlined.GraphicEq,
            contentDescription = null,
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun MainControllerBottomButtonSectionPreview() {
    KanadeBackground(Modifier.wrapContentSize()) {
        MainControllerBottomButtonSection(
            isFavorite = false,
            onClickLyrics = { },
            onClickLyricsEdit = { },
            onClickFavorite = { },
            onClickQueue = { },
        )
    }
}
