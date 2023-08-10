package caios.android.kanade.feature.information.about

import android.content.Intent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberTopAppBarState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.core.net.toUri
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import caios.android.kanade.core.common.network.KanadeConfig
import caios.android.kanade.core.common.network.util.ToastUtil
import caios.android.kanade.core.design.R
import caios.android.kanade.core.model.UserData
import caios.android.kanade.core.model.Version
import caios.android.kanade.core.ui.AsyncLoadContents
import caios.android.kanade.core.ui.view.KanadeTopAppBar
import caios.android.kanade.feature.information.about.items.AboutAppSection
import caios.android.kanade.feature.information.about.items.AboutDeveloperSection
import caios.android.kanade.feature.information.about.items.AboutSupportSection
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.toImmutableList

@Composable
internal fun AboutRoute(
    navigateToVersionHistory: (ImmutableList<Version>) -> Unit,
    navigateToDonate: () -> Unit,
    terminate: () -> Unit,
    modifier: Modifier = Modifier,
    viewModel: AboutViewModel = hiltViewModel(),
) {
    val context = LocalContext.current
    val screenState by viewModel.screenState.collectAsStateWithLifecycle()

    fun openLink(url: String) {
        context.startActivity(Intent(Intent.ACTION_VIEW, url.toUri()))
    }

    AsyncLoadContents(
        modifier = modifier,
        screenState = screenState,
    ) { uiState ->
        if (uiState != null) {
            AboutScreen(
                modifier = Modifier.background(MaterialTheme.colorScheme.surface),
                userData = uiState.userData,
                config = uiState.config,
                onClickGithub = { openLink("https://github.com/matsumo0922/KanadeMark3") },
                onClickGithubProfile = { openLink("https://github.com/matsumo0922") },
                onClickGithubIssue = { openLink("https://github.com/matsumo0922/KanadeMark3/issues/new") },
                onClickDiscord = { ToastUtil.show(context, R.string.error_developing_feature) },
                onClickGooglePlay = { openLink("https://play.google.com/store/apps/details?id=caios.android.kanade") },
                onClickGooglePlayDeveloper = { openLink("https://play.google.com/store/apps/developer?id=CAIOS") },
                onClickTwitter = { openLink("https://twitter.com/matsumo0922") },
                onClickMao = { openLink("https://maou.audio/") },
                onClickMaoTwitter = { openLink("https://twitter.com/koichi_maou?ref_src=twsrc%5Egoogle%7Ctwcamp%5Eserp%7Ctwgr%5Eauthor") },
                onClick358Design = { openLink("https://www.instagram.com/0358_design/") },
                onClickVersionHistory = { navigateToVersionHistory.invoke(uiState.versions.toImmutableList()) },
                onClickDonate = { navigateToDonate.invoke() },
                onTerminate = terminate,
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun AboutScreen(
    userData: UserData,
    config: KanadeConfig,
    onClickGithub: () -> Unit,
    onClickGithubProfile: () -> Unit,
    onClickGithubIssue: () -> Unit,
    onClickDiscord: () -> Unit,
    onClickGooglePlay: () -> Unit,
    onClickGooglePlayDeveloper: () -> Unit,
    onClickTwitter: () -> Unit,
    onClickMao: () -> Unit,
    onClickMaoTwitter: () -> Unit,
    onClick358Design: () -> Unit,
    onClickVersionHistory: () -> Unit,
    onClickDonate: () -> Unit,
    onTerminate: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val state = rememberTopAppBarState()
    val behavior = TopAppBarDefaults.pinnedScrollBehavior(state)

    Scaffold(
        modifier = modifier.nestedScroll(behavior.nestedScrollConnection),
        topBar = {
            KanadeTopAppBar(
                modifier = Modifier.fillMaxWidth(),
                title = stringResource(R.string.about_title),
                behavior = behavior,
                onTerminate = onTerminate,
            )
        },
    ) { paddingValues ->
        LazyColumn(
            modifier = Modifier.fillMaxWidth(),
            contentPadding = paddingValues,
        ) {
            item {
                AboutAppSection(
                    modifier = Modifier.fillMaxWidth(),
                    userData = userData,
                    config = config,
                    onClickGithub = onClickGithub,
                    onClickDiscord = onClickDiscord,
                    onClickGooglePlay = onClickGooglePlay,
                )
            }

            item {
                AboutDeveloperSection(
                    modifier = Modifier.fillMaxWidth(),
                    onClickTwitter = onClickTwitter,
                    onClickGithub = onClickGithubProfile,
                    onClickGooglePlay = onClickGooglePlayDeveloper,
                    onClickMao = onClickMao,
                    onClickMaoTwitter = onClickMaoTwitter,
                    onClick358Instagram = onClick358Design,
                )
            }

            item {
                AboutSupportSection(
                    modifier = Modifier
                        .padding(top = 8.dp, bottom = 24.dp)
                        .fillMaxWidth(),
                    onClickVersionHistory = onClickVersionHistory,
                    onClickRateApp = onClickGooglePlay,
                    onClickEmail = onClickGithubIssue,
                    onClickDonation = onClickDonate,
                )
            }
        }
    }
}
