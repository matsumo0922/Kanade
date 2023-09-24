package caios.android.kanade.feature.welcome.welcome

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.ClickableText
import androidx.compose.material3.Button
import androidx.compose.material3.Checkbox
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import caios.android.kanade.core.design.R
import caios.android.kanade.core.design.theme.bold
import caios.android.kanade.core.design.theme.center

@Composable
internal fun WelcomeScreen(
    navigateToWelcomePermission: () -> Unit,
    modifier: Modifier = Modifier,
) {
    var isAgreedPrivacyPolicy by remember { mutableStateOf(false) }
    var isAgreedTermsOfService by remember { mutableStateOf(false) }

    Column(
        modifier = modifier.background(MaterialTheme.colorScheme.surface),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {

        Spacer(modifier = Modifier.height(24.dp))

        Image(
            modifier = Modifier
                .padding(64.dp, 24.dp)
                .aspectRatio(1f)
                .fillMaxWidth(),
            painter = painterResource(R.drawable.vec_app_icon),
            contentDescription = null,
        )

        Text(
            modifier = Modifier
                .padding(horizontal = 24.dp)
                .fillMaxWidth(),
            text = stringResource(R.string.welcome_title),
            style = MaterialTheme.typography.headlineMedium.bold().center(),
            color = MaterialTheme.colorScheme.onSurface,
        )

        Text(
            modifier = Modifier
                .padding(top = 8.dp)
                .padding(horizontal = 24.dp)
                .fillMaxWidth(),
            text = stringResource(R.string.welcome_description),
            style = MaterialTheme.typography.bodySmall.center(),
            color = MaterialTheme.colorScheme.onSurfaceVariant,
        )

        Spacer(modifier = Modifier.weight(1f))

        Column(
            modifier = Modifier.width(IntrinsicSize.Max),
            verticalArrangement = Arrangement.spacedBy(8.dp),
        ) {
            CheckBoxLinkButton(
                isChecked = isAgreedTermsOfService,
                link = stringResource(R.string.welcome_team_of_service),
                body = stringResource(R.string.welcome_agree),
                onChecked = { isAgreedTermsOfService = it },
                onLinkClick = { },
            )

            CheckBoxLinkButton(
                isChecked = isAgreedPrivacyPolicy,
                link = stringResource(R.string.welcome_privacy_policy),
                body = stringResource(R.string.welcome_agree),
                onChecked = { isAgreedPrivacyPolicy = it },
                onLinkClick = { },
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        Button(
            modifier = Modifier
                .padding(24.dp, 16.dp)
                .fillMaxWidth(),
            shape = RoundedCornerShape(50),
            onClick = { navigateToWelcomePermission.invoke() },
            enabled = isAgreedPrivacyPolicy && isAgreedTermsOfService,
        ) {
            Text(
                text = stringResource(R.string.welcome_button_next),
                style = MaterialTheme.typography.labelMedium,
                color = if (isAgreedPrivacyPolicy && isAgreedTermsOfService) MaterialTheme.colorScheme.onPrimary else MaterialTheme.colorScheme.onSurface ,
            )
        }
    }
}

@Composable
private fun CheckBoxLinkButton(
    isChecked: Boolean,
    link: String,
    body: String,
    onChecked: (Boolean) -> Unit,
    onLinkClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val annotatedString = buildAnnotatedString {
        pushStringAnnotation(
            tag = "link",
            annotation = link,
        )

        withStyle(MaterialTheme.typography.bodySmall.copy(color = MaterialTheme.colorScheme.primary).bold().toSpanStyle()) {
            append(link)
        }

        pop()

        append(body)
    }

    Row(
        modifier = modifier,
        horizontalArrangement = Arrangement.spacedBy(4.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Checkbox(
            modifier = Modifier.size(24.dp),
            checked = isChecked,
            onCheckedChange = onChecked,
        )

        ClickableText(
            text = annotatedString,
            style = MaterialTheme.typography.bodySmall,
            onClick = {
                annotatedString.getStringAnnotations("link", it, it).firstOrNull()?.let { _ ->
                    onLinkClick.invoke()
                }
            },
        )
    }
}

@Preview
@Composable
private fun PreviewWelcomeScreen() {
    WelcomeScreen(
        modifier = Modifier.fillMaxSize(),
        navigateToWelcomePermission = {}
    )
}
