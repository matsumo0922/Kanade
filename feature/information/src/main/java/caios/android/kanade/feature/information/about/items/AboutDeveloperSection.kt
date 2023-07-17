package caios.android.kanade.feature.information.about.items

import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Language
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Divider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.Dimension
import caios.android.kanade.core.design.R
import caios.android.kanade.core.design.icon.Github
import caios.android.kanade.core.design.icon.GooglePlay
import caios.android.kanade.core.design.icon.Instagram
import caios.android.kanade.core.design.icon.Twitter
import caios.android.kanade.core.design.theme.bold
import caios.android.kanade.core.design.theme.center

@Composable
internal fun AboutDeveloperSection(
    onClickTwitter: () -> Unit,
    onClickGithub: () -> Unit,
    onClickGooglePlay: () -> Unit,
    onClickMao: () -> Unit,
    onClickMaoTwitter: () -> Unit,
    onClick358Instagram: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Box(modifier) {
        ConstraintLayout(Modifier.fillMaxWidth()) {
            val (icon, card) = createRefs()

            Card(
                modifier = Modifier.constrainAs(card) {
                    start.linkTo(parent.start, 24.dp)
                    bottom.linkTo(parent.bottom, 16.dp)
                    end.linkTo(parent.end, 24.dp)

                    width = Dimension.fillToConstraints
                },
                shape = RoundedCornerShape(6.dp),
                colors = CardDefaults.cardColors(MaterialTheme.colorScheme.surfaceContainerHigh),
            ) {
                Column(
                    modifier = Modifier.fillMaxWidth(),
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally,
                ) {
                    Text(
                        modifier = Modifier
                            .padding(top = 68.dp)
                            .fillMaxWidth(),
                        text = stringResource(R.string.about_developer_prefix),
                        style = MaterialTheme.typography.bodyMedium.center(),
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                    )

                    Text(
                        modifier = Modifier
                            .padding(top = 8.dp)
                            .fillMaxWidth(),
                        text = stringResource(R.string.about_developer_name),
                        style = MaterialTheme.typography.titleLarge.center(),
                        color = MaterialTheme.colorScheme.onSurface,
                    )

                    Row(
                        modifier = Modifier
                            .padding(top = 24.dp)
                            .fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(
                            space = 16.dp,
                            alignment = Alignment.CenterHorizontally,
                        ),
                    ) {
                        AboutIconButton(
                            modifier = Modifier.size(28.dp),
                            imageVector = Icons.Outlined.Twitter,
                            onClick = onClickTwitter,
                        )

                        AboutIconButton(
                            modifier = Modifier.size(28.dp),
                            imageVector = Icons.Outlined.Github,
                            onClick = onClickGithub,
                        )

                        AboutIconButton(
                            modifier = Modifier.size(28.dp),
                            imageVector = Icons.Outlined.GooglePlay,
                            onClick = onClickGooglePlay,
                        )
                    }

                    Divider(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(24.dp),
                    )

                    Text(
                        modifier = Modifier
                            .padding(horizontal = 24.dp)
                            .fillMaxWidth(),
                        text = stringResource(R.string.about_special_thanks).uppercase(),
                        style = MaterialTheme.typography.bodyMedium.bold(),
                        color = MaterialTheme.colorScheme.primary,
                    )

                    AboutThanksItem(
                        modifier = Modifier.fillMaxWidth(),
                        titleRes = R.string.about_special_thanks_mao,
                        descriptionRes = R.string.about_special_thanks_mao_description,
                        iconPainter = painterResource(R.drawable.vec_maou_damashii),
                    ) {
                        AboutIconButton(
                            modifier = Modifier.size(28.dp),
                            imageVector = Icons.Outlined.Language,
                            onClick = onClickMao,
                        )

                        AboutIconButton(
                            modifier = Modifier.size(28.dp),
                            imageVector = Icons.Outlined.Twitter,
                            onClick = onClickMaoTwitter,
                        )
                    }

                    Divider(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(
                                top = 8.dp,
                                bottom = 8.dp,
                                start = 92.dp,
                                end = 24.dp,
                            ),
                    )

                    AboutThanksItem(
                        modifier = Modifier.fillMaxWidth(),
                        titleRes = R.string.about_special_thanks_358,
                        descriptionRes = R.string.about_special_thanks_358_description,
                        iconPainter = painterResource(R.drawable.ic_358_design),
                    ) {
                        AboutIconButton(
                            modifier = Modifier.size(28.dp),
                            imageVector = Icons.Outlined.Instagram,
                            onClick = onClick358Instagram,
                        )
                    }

                    Divider(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 24.dp, vertical = 8.dp),
                    )

                    Text(
                        modifier = Modifier
                            .padding(
                                top = 16.dp,
                                start = 24.dp,
                                end = 24.dp,
                            )
                            .fillMaxWidth(),
                        text = stringResource(R.string.about_contribute).uppercase(),
                        style = MaterialTheme.typography.bodyMedium.bold(),
                        color = MaterialTheme.colorScheme.primary,
                    )

                    Text(
                        modifier = Modifier
                            .padding(
                                top = 16.dp,
                                bottom = 24.dp,
                                start = 24.dp,
                                end = 24.dp,
                            )
                            .fillMaxWidth(),
                        text = stringResource(R.string.about_contribute_description),
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                    )
                }
            }

            Image(
                modifier = Modifier
                    .size(96.dp)
                    .clip(RoundedCornerShape(50))
                    .border(
                        width = 1.dp,
                        color = MaterialTheme.colorScheme.onSurface,
                        shape = RoundedCornerShape(50),
                    )
                    .constrainAs(icon) {
                        top.linkTo(card.top)
                        bottom.linkTo(card.top)
                        start.linkTo(card.start)
                        end.linkTo(card.end)
                    },
                painter = painterResource(R.drawable.ic_developer_profile1),
                contentDescription = null,
            )
        }
    }
}

@Preview
@Composable
private fun AboutDeveloperSectionPreview() {
    AboutDeveloperSection(
        modifier = Modifier.fillMaxWidth(),
        onClickTwitter = {},
        onClickGithub = {},
        onClickGooglePlay = {},
        onClickMao = {},
        onClickMaoTwitter = {},
        onClick358Instagram = {},
    )
}
