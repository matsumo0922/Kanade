package caios.android.kanade.feature.report

import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.BugReport
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Divider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.TextUnitType
import androidx.compose.ui.unit.dp
import caios.android.kanade.core.design.R
import caios.android.kanade.core.design.theme.bold

@Composable
internal fun CrushReportScreen(
    report: String,
    onClickCopy: (String) -> Unit,
    modifier: Modifier = Modifier,
) {
    Scaffold(
        modifier = modifier,
        bottomBar = {
            Divider()

            Button(
                modifier = Modifier
                    .fillMaxWidth()
                    .navigationBarsPadding()
                    .padding(horizontal = 16.dp, vertical = 8.dp),
                onClick = { onClickCopy.invoke(report) },
                contentPadding = ButtonDefaults.ButtonWithIconContentPadding,
            ) {
                Icon(
                    modifier = Modifier.size(18.dp),
                    imageVector = Icons.Default.BugReport,
                    contentDescription = null,
                )

                Text(
                    modifier = Modifier.padding(start = 6.dp),
                    text = stringResource(R.string.report_crush_copy),
                )
            }
        },
    ) {
        Column(
            modifier = Modifier
                .padding(it)
                .verticalScroll(rememberScrollState())
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(24.dp),
        ) {
            Text(
                modifier = Modifier
                    .padding(top = 24.dp)
                    .fillMaxWidth(),
                text = stringResource(R.string.report_crush_title),
                style = MaterialTheme.typography.headlineLarge.bold(),
            )

            Text(
                modifier = Modifier.fillMaxWidth(),
                text = stringResource(R.string.report_crush_description),
                style = MaterialTheme.typography.bodyMedium,
            )

            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(8.dp),
                colors = CardDefaults.cardColors(MaterialTheme.colorScheme.surfaceContainerHigh),
            ) {
                Text(
                    modifier = Modifier
                        .padding(16.dp)
                        .fillMaxWidth()
                        .horizontalScroll(rememberScrollState()),
                    text = report,
                    lineHeight = TextUnit(20f, TextUnitType.Sp),
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                )
            }
        }
    }
}

@Preview
@Composable
private fun CrushReportScreenPreview() {
    CrushReportScreen(
        modifier = Modifier.fillMaxSize(),
        onClickCopy = { },
        report = """
App version: 1.10.0-beta.3 (10920)
Device information: Android 13 (API 33)
Supported ABIs: [arm64-v8a, armeabi-v7a, armeabi]
Yt-dlp version: 2023.07.06

java.lang.IllegalStateException: Test Exception
	at com.junkfood.seal.App: onCreate 2.invokeSuspend(App.kt:87)
	at kotlin.coroutines.jvm.internal.BaseContinuationImpl.resumeWith(ContinuationImpl.kt:33)
	at kotlinx.coroutines.DispatchedTask.run(DispatchedTask.kt:106)
	at kotlinx.coroutines.internal.LimitedDispatcher: Worker.run(LimitedDispatcher.kt:115)
	at kotlinx.coroutines.scheduling.TaskImpl.run(Tasks.kt:100)
	at kotlinx.coroutines.scheduling.CoroutineScheduler.runSafely(CoroutineScheduler.kt:584)
	at kotlinx.coroutines.scheduling.CoroutineScheduler: Worker.executeTask(CoroutineScheduler.kt:793)
	at kotlinx.coroutines.scheduling.CoroutineScheduler: Worker.runWorker(CoroutineScheduler.kt:697)
	at kotlinx.coroutines.scheduling.CoroutineScheduler: Worker.run(CoroutineScheduler.kt:684)
        """.trimIndent(),
    )
}
