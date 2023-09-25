package caios.android.kanade.feature.information.about.items

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.Dimension
import caios.android.kanade.core.common.network.KanadeConfig
import caios.android.kanade.core.design.R
import caios.android.kanade.core.design.icon.Discord
import caios.android.kanade.core.design.icon.Github
import caios.android.kanade.core.design.icon.GooglePlay
import caios.android.kanade.core.model.UserData

@Composable
internal fun AboutAppSection(
    userData: UserData,
    config: KanadeConfig,
    onClickGithub: () -> Unit,
    onClickDiscord: () -> Unit,
    onClickGooglePlay: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Box(modifier) {
        Card(
            modifier = Modifier
                .padding(24.dp)
                .fillMaxWidth(),
            shape = RoundedCornerShape(6.dp),
            colors = CardDefaults.cardColors(MaterialTheme.colorScheme.surfaceContainerHigh),
        ) {
            ConstraintLayout(Modifier.fillMaxWidth()) {
                val (icon, title, version, buttons) = createRefs()

                Image(
                    modifier = Modifier
                        .size(104.dp)
                        .constrainAs(icon) {
                            top.linkTo(parent.top, 16.dp)
                            start.linkTo(parent.start, 24.dp)
                            bottom.linkTo(parent.bottom, 16.dp)
                        },
                    painter = painterResource(R.drawable.vec_app_icon_no_background),
                    contentDescription = null,
                )

                Text(
                    modifier = Modifier.constrainAs(title) {
                        top.linkTo(icon.top, 8.dp)
                        start.linkTo(icon.end, 16.dp)
                        end.linkTo(parent.end, 16.dp)

                        width = Dimension.fillToConstraints
                    },
                    text = stringResource(R.string.about_name),
                    style = MaterialTheme.typography.titleLarge,
                    color = MaterialTheme.colorScheme.onSurface,
                )

                Text(
                    modifier = Modifier.constrainAs(version) {
                        top.linkTo(title.bottom, 4.dp)
                        start.linkTo(title.start)
                        end.linkTo(title.end)

                        width = Dimension.fillToConstraints
                    },
                    text = "${config.versionName}:${config.versionCode}" + when {
                        userData.isPlusMode && userData.isDeveloperMode -> "[P+D]"
                        userData.isPlusMode -> "[Premium]"
                        userData.isDeveloperMode -> "[Developer]"
                        else -> ""
                    },
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                )

                Row(
                    modifier = Modifier.constrainAs(buttons) {
                        bottom.linkTo(icon.bottom, 4.dp)
                        start.linkTo(title.start)
                        end.linkTo(title.end)

                        width = Dimension.fillToConstraints
                    },
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(16.dp),
                ) {
                    AboutIconButton(
                        modifier = Modifier.size(28.dp),
                        imageVector = Icons.Outlined.Github,
                        onClick = { onClickGithub.invoke() },
                    )

                    AboutIconButton(
                        modifier = Modifier.size(28.dp),
                        imageVector = Icons.Outlined.Discord,
                        onClick = { onClickDiscord.invoke() },
                    )

                    AboutIconButton(
                        modifier = Modifier.size(28.dp),
                        imageVector = Icons.Outlined.GooglePlay,
                        onClick = { onClickGooglePlay.invoke() },
                    )
                }
            }
        }
    }
}

@Preview
@Composable
private fun AboutAppSectionPreview() {
    AboutAppSection(
        modifier = Modifier.fillMaxWidth(),
        userData = UserData.dummy(),
        config = KanadeConfig.dummy(),
        onClickGithub = { },
        onClickDiscord = { },
        onClickGooglePlay = { },
    )
}
