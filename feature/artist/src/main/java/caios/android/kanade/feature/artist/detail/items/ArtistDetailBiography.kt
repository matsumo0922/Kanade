package caios.android.kanade.feature.artist.detail.items

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import caios.android.kanade.core.design.R
import caios.android.kanade.core.design.theme.bold

@Composable
internal fun ArtistDetailBiography(
    biography: String,
    modifier: Modifier = Modifier,
) {
    Column(modifier) {
        Row(
            modifier = Modifier
                .padding(horizontal = 8.dp, vertical = 16.dp)
                .fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Text(
                modifier = Modifier
                    .padding(horizontal = 8.dp)
                    .weight(1f),
                text = stringResource(R.string.common_details),
                style = MaterialTheme.typography.titleLarge.bold(),
                color = MaterialTheme.colorScheme.onSurface,
            )
        }

        Card(
            modifier = Modifier
                .padding(horizontal = 16.dp)
                .fillMaxWidth(),
            shape = RoundedCornerShape(4.dp),
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.surfaceContainerHigh,
                contentColor = MaterialTheme.colorScheme.surfaceContainerHigh,
            ),
        ) {
            Text(
                modifier = Modifier
                    .padding(16.dp)
                    .fillMaxWidth(),
                text = biography,
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurface,
            )
        }
    }
}
