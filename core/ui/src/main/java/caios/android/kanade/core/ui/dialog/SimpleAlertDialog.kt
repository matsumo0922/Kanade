package caios.android.kanade.core.ui.dialog

import androidx.annotation.StringRes
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import caios.android.kanade.core.design.R

@Composable
fun SimpleAlertDialog(
    title: String,
    message: String,
    positiveText: String = stringResource(R.string.common_ok),
    negativeText: String = stringResource(R.string.common_cancel),
    onPositiveClick: () -> Unit = {},
    onNegativeClick: () -> Unit = {},
    onDismiss: () -> Unit = {},
    cancelable: Boolean = true,
) {
    AlertDialog(
        title = { Text(title) },
        text = { Text(message) },
        confirmButton = {
            TextButton(
                onClick = {
                    onPositiveClick.invoke()
                    onDismiss.invoke()
                },
            ) {
                Text(positiveText)
            }
        },
        dismissButton = {
            TextButton(
                onClick = {
                    onNegativeClick.invoke()
                    onDismiss.invoke()
                },
            ) {
                Text(negativeText)
            }
        },
        onDismissRequest = {
            if (cancelable) {
                onNegativeClick.invoke()
                onDismiss.invoke()
            }
        },
    )
}

@Composable
fun SimpleAlertDialog(
    @StringRes title: Int,
    @StringRes message: Int,
    @StringRes positiveText: Int = R.string.common_ok,
    @StringRes negativeText: Int = R.string.common_cancel,
    onPositiveClick: () -> Unit = {},
    onNegativeClick: () -> Unit = {},
    onDismiss: () -> Unit = {},
    cancelable: Boolean = true,
) {
    SimpleAlertDialog(
        title = stringResource(title),
        message = stringResource(message),
        positiveText = stringResource(positiveText),
        negativeText = stringResource(negativeText),
        onPositiveClick = onPositiveClick,
        onNegativeClick = onNegativeClick,
        onDismiss = onDismiss,
        cancelable = cancelable,
    )
}

@Preview
@Composable
private fun SimpleAlertDialogPreview() {
    SimpleAlertDialog(
        title = "テストダイアログ",
        message = "これは SimpleAlertDialog のテストダイアログです",
    )
}
