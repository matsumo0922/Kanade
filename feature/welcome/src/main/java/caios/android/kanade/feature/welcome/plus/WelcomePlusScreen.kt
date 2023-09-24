package caios.android.kanade.feature.welcome.plus

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import caios.android.kanade.core.design.R
import caios.android.kanade.core.design.theme.bold
import caios.android.kanade.core.design.theme.center
import caios.android.kanade.feature.welcome.WelcomeIndicatorItem
import kotlinx.coroutines.launch

@Composable
internal fun WelcomePlusScreen(
    navigateToBillingPlus: () -> Unit,
    navigateToWelcomePermission: () -> Unit,
    modifier: Modifier = Modifier,
    viewModel: WelcomePlusViewModel = hiltViewModel(),
) {
    val context = LocalContext.current
    val scope = rememberCoroutineScope()

    Column(
        modifier = modifier
            .background(MaterialTheme.colorScheme.surface)
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Image(
            modifier = Modifier.padding(
                top = 8.dp,
                start = 8.dp,
                end = 8.dp,
            ),
            painter = painterResource(R.drawable.vec_welcome_plus),
            contentDescription = null,
        )

        Text(
            modifier = Modifier.padding(top = 32.dp),
            text = stringResource(R.string.welcome_plus_title),
            style = MaterialTheme.typography.displaySmall.bold(),
            color = MaterialTheme.colorScheme.primary,
        )

        Text(
            modifier = Modifier.padding(top = 12.dp),
            text = stringResource(R.string.welcome_plus_description),
            style = MaterialTheme.typography.bodySmall.center(),
            color = MaterialTheme.colorScheme.onSurfaceVariant,
        )

        Spacer(modifier = Modifier.height(48.dp))

        Column(
            modifier = Modifier
                .padding(horizontal = 16.dp)
                .fillMaxWidth(),
            verticalArrangement = Arrangement.spacedBy(8.dp),
        ) {
            Button(
                modifier = Modifier.fillMaxWidth(),
                onClick = {
                    scope.launch {
                        viewModel.verify(context)
                    }
                },
            ) {
                Text(stringResource(R.string.welcome_plus_restore))
            }

            OutlinedButton(
                modifier = Modifier.fillMaxWidth(),
                onClick = { navigateToBillingPlus.invoke() },
            ) {
                Text(stringResource(R.string.welcome_plus_purchase))
            }
        }

        Spacer(modifier = Modifier.weight(1f))

        WelcomeIndicatorItem(
            modifier = Modifier.padding(bottom = 24.dp),
            max = 3,
            step = 2,
        )

        Button(
            modifier = Modifier
                .padding(bottom = 24.dp)
                .fillMaxWidth(),
            shape = RoundedCornerShape(50),
            onClick = { navigateToWelcomePermission.invoke() },
        ) {
            Text(
                modifier = Modifier.padding(8.dp),
                text = stringResource(R.string.welcome_plus_complete_button),
            )
        }
    }
}

@Preview
@Composable
private fun WelcomePlusScreenPreview() {
    WelcomePlusScreen(
        modifier = Modifier.fillMaxSize(),
        navigateToBillingPlus = {},
        navigateToWelcomePermission = {},
    )
}
