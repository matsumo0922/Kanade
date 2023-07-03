package caios.android.kanade.feature.search.items

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import caios.android.kanade.core.design.R
import caios.android.kanade.core.design.component.KanadeBackground

@Composable
internal fun SearchTopBarSection(
    search: suspend (List<String>) -> Unit,
    onClickNavigateUp: () -> Unit,
    modifier: Modifier = Modifier,
) {
    var value by remember { mutableStateOf("") }
    val focusRequester = remember { FocusRequester() }

    LaunchedEffect(value) {
        search.invoke(listOf(value))
    }

    Row(
        modifier = modifier
            .statusBarsPadding()
            .padding(8.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Icon(
            modifier = Modifier
                .size(40.dp)
                .aspectRatio(1f)
                .clip(RoundedCornerShape(50))
                .clickable { onClickNavigateUp() }
                .padding(8.dp),
            imageVector = Icons.Default.ArrowBack,
            contentDescription = null,
        )

        BasicTextField(
            modifier = Modifier
                .weight(1f)
                .focusRequester(focusRequester),
            value = value,
            onValueChange = { value = it },
            decorationBox = { innerTextField ->
                if (value.isEmpty()) {
                    Text(stringResource(R.string.search_title))
                } else {
                    innerTextField.invoke()
                }
            },
            textStyle = MaterialTheme.typography.titleMedium.copy(
                fontWeight = FontWeight.Normal,
                color = MaterialTheme.colorScheme.onSurface,
            ),
            cursorBrush = SolidColor(MaterialTheme.colorScheme.primaryContainer),
        )

        if (value.isNotEmpty()) {
            Icon(
                modifier = Modifier
                    .size(40.dp)
                    .aspectRatio(1f)
                    .clip(RoundedCornerShape(50))
                    .clickable { value = "" }
                    .padding(8.dp),
                imageVector = Icons.Default.Close,
                contentDescription = null,
            )
        }
    }

    LaunchedEffect(true) {
        focusRequester.requestFocus()
    }
}

@Preview(showBackground = true)
@Composable
private fun SearchTopBarSectionPreview() {
    KanadeBackground {
        SearchTopBarSection(
            modifier = Modifier.fillMaxWidth(),
            search = {},
            onClickNavigateUp = {},
        )
    }
}
