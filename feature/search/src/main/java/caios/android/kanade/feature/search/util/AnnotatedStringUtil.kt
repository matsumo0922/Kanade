package caios.android.kanade.feature.search.util

import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.withStyle

@Composable
internal fun getAnnotatedString(targetStr: String, range: IntRange): AnnotatedString {
    val startStr = targetStr.substring(0, (range.first).coerceAtLeast(0))
    val annotatedStr = targetStr.substring(range)
    val endStr = targetStr.substring((range.last + 1).coerceAtMost(targetStr.length))

    return buildAnnotatedString {
        append(startStr)

        withStyle(SpanStyle(color = MaterialTheme.colorScheme.primary)) {
            append(annotatedStr)
        }

        append(endStr)
    }
}
