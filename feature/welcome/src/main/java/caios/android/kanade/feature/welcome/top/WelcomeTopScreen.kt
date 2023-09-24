package caios.android.kanade.feature.welcome.top

import android.content.Intent
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.core.net.toUri
import androidx.hilt.navigation.compose.hiltViewModel
import caios.android.kanade.core.design.R
import caios.android.kanade.core.design.theme.bold
import caios.android.kanade.core.design.theme.center
import caios.android.kanade.feature.welcome.WelcomeIndicatorItem

@Composable
internal fun WelcomeTopScreen(
    navigateToWelcomePlus: () -> Unit,
    modifier: Modifier = Modifier,
    viewModel: WelcomeTopViewModel = hiltViewModel(),
) {
    val context = LocalContext.current
    var isAgreedPrivacyPolicy by remember { mutableStateOf(false) }
    var isAgreedTermsOfService by remember { mutableStateOf(false) }

    val teamOfServiceUri = "https://www.matsumo.me/application/kanade/team_of_service".toUri()
    val privacyPolicyUri = "https://www.matsumo.me/application/kanade/privacy_policy".toUri()

    Column(
        modifier = modifier
            .background(MaterialTheme.colorScheme.surface)
            .padding(24.dp),
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
            modifier = Modifier.fillMaxWidth(),
            text = stringResource(R.string.welcome_title),
            style = MaterialTheme.typography.displaySmall.bold().center(),
            color = MaterialTheme.colorScheme.onSurface,
        )

        Text(
            modifier = Modifier
                .padding(top = 12.dp)
                .padding(horizontal = 24.dp)
                .fillMaxWidth(),
            text = stringResource(R.string.welcome_description),
            style = MaterialTheme.typography.bodySmall.center(),
            color = MaterialTheme.colorScheme.onSurfaceVariant,
        )

        Spacer(modifier = Modifier.weight(1f))

        Column(
            modifier = Modifier.width(IntrinsicSize.Max),
            verticalArrangement = Arrangement.spacedBy(16.dp),
        ) {
            CheckBoxLinkButton(
                isChecked = isAgreedTermsOfService,
                link = stringResource(R.string.welcome_team_of_service),
                body = stringResource(R.string.welcome_agree),
                onChecked = { isAgreedTermsOfService = it },
                onLinkClick = { context.startActivity(Intent(Intent.ACTION_VIEW, teamOfServiceUri)) },
            )

            CheckBoxLinkButton(
                isChecked = isAgreedPrivacyPolicy,
                link = stringResource(R.string.welcome_privacy_policy),
                body = stringResource(R.string.welcome_agree),
                onChecked = { isAgreedPrivacyPolicy = it },
                onLinkClick = { context.startActivity(Intent(Intent.ACTION_VIEW, privacyPolicyUri)) },
            )
        }

        Spacer(modifier = Modifier.height(24.dp))

        WelcomeIndicatorItem(
            modifier = Modifier.padding(bottom = 24.dp),
            max = 3,
            step = 1,
        )

        Button(
            modifier = Modifier
                .padding(bottom = 24.dp)
                .fillMaxWidth(),
            shape = RoundedCornerShape(50),
            enabled = isAgreedPrivacyPolicy && isAgreedTermsOfService,
            onClick = {
                navigateToWelcomePlus.invoke()
                viewModel.setAgreedPrivacyPolicy()
                viewModel.setAgreedTermsOfService()
            },
        ) {
            Text(
                modifier = Modifier.padding(8.dp),
                text = stringResource(R.string.welcome_button_next),
                color = if (isAgreedPrivacyPolicy && isAgreedTermsOfService) MaterialTheme.colorScheme.onPrimary else MaterialTheme.colorScheme.onSurface,
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
    val textStyle = TextStyle(
        color = MaterialTheme.colorScheme.primary,
        textAlign = TextAlign.Center,
        fontWeight = FontWeight.Bold,
    )

    val annotatedString = buildAnnotatedString {
        pushStringAnnotation("url", link)

        withStyle(textStyle.toSpanStyle()) {
            append(link)
        }

        pop()

        append(" ")

        withStyle(TextStyle(color = MaterialTheme.colorScheme.onSurface).toSpanStyle()) {
            append(body)
        }
    }

    Row(
        modifier = modifier,
        horizontalArrangement = Arrangement.spacedBy(16.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Checkbox(
            modifier = Modifier.size(12.dp),
            checked = isChecked,
            onCheckedChange = onChecked,
        )

        ClickableText(
            text = annotatedString,
            style = MaterialTheme.typography.bodyMedium,
            onClick = {
                annotatedString.getStringAnnotations("url", it, it).firstOrNull()?.let { _ ->
                    onLinkClick.invoke()
                }
            },
        )
    }
}

@Preview
@Composable
private fun PreviewWelcomeScreen() {
    WelcomeTopScreen(
        modifier = Modifier.fillMaxSize(),
        navigateToWelcomePlus = {},
    )
}
