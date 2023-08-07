package caios.android.kanade.feature.search.scan

import android.net.Uri
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import caios.android.kanade.core.design.R
import kotlinx.coroutines.launch

@Composable
internal fun ScanMediaDialog(
    uri: Uri,
    terminate: () -> Unit,
    modifier: Modifier = Modifier,
    viewModel: ScanMediaViewModel = hiltViewModel(),
) {
    val context = LocalContext.current
    val scope = rememberCoroutineScope()
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    LaunchedEffect(uri) {
        viewModel.scan(context, uri)
    }

    LaunchedEffect(uiState.progress, uiState.total) {
        if (uiState.progress == uiState.total && uiState.total != 0) {
            scope.launch {
                viewModel.refreshLibrary()
                terminate.invoke()
            }
        }
    }

    Column(
        modifier = modifier
            .background(MaterialTheme.colorScheme.surface)
            .padding(24.dp),
        verticalArrangement = Arrangement.spacedBy(24.dp),
    ) {
        Text(
            modifier = Modifier.fillMaxWidth(),
            text = stringResource(R.string.scan_title),
            style = MaterialTheme.typography.titleMedium,
            color = MaterialTheme.colorScheme.onSurface,
        )

        if (uiState.total == 0) {
            Text(
                modifier = Modifier.fillMaxWidth(),
                text = stringResource(R.string.scan_initializing),
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
            )

            LinearProgressIndicator(
                modifier = Modifier.fillMaxWidth(),
            )
        } else {
            Text(
                modifier = Modifier.fillMaxWidth(),
                text = uiState.currentItem.toString(),
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                minLines = 2,
                maxLines = 2,
                overflow = TextOverflow.Ellipsis,
            )

            LinearProgressIndicator(
                modifier = Modifier.fillMaxWidth(),
                progress = (uiState.progress.toDouble() / uiState.total.toDouble()).toFloat(),
            )
        }
    }
}
