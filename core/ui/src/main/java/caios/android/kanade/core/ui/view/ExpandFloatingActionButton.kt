package caios.android.kanade.core.ui.view

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.awaitFirstDown
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material.icons.filled.Folder
import androidx.compose.material.icons.filled.SimCardDownload
import androidx.compose.material3.Card
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.input.pointer.PointerEventPass
import androidx.compose.ui.input.pointer.changedToUp
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.LayoutCoordinates
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.layout.positionInWindow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import caios.android.kanade.core.design.component.KanadeBackground
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.isActive
import timber.log.Timber

@Composable
fun ExpandFloatingActionButton(
    state: ExpandFloatingActionButtonState,
    titleIcon: ImageVector,
    titleString: String,
    titleClick: () -> Unit,
    modifier: Modifier = Modifier,
    items: List<ExpandFloatingActionButtonItem> = emptyList(),
) {
    val roundedParent by animateDpAsState(targetValue = if (state.isExpanded) 8.dp else 32.dp)

    Card(
        modifier = modifier
            .clip(RoundedCornerShape(roundedParent))
            .background(MaterialTheme.colorScheme.primary)
            .clickable { state.expand() }
            .animateContentSize(),
        shape = RoundedCornerShape(roundedParent),
    ) {
        Column(
            modifier = Modifier.width(IntrinsicSize.Max),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            for (item in items) {
                AnimatedVisibility(visible = state.isExpanded) {
                    ExpandFloatingActionButtonItem(
                        modifier = Modifier
                            .fillMaxWidth()
                            .background(MaterialTheme.colorScheme.surface),
                        item = item,
                    )
                }
            }

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(MaterialTheme.colorScheme.primary)
                    .padding(16.dp),
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Icon(
                    modifier = Modifier.size(32.dp),
                    imageVector = titleIcon,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.onPrimary,
                )

                if (state.isExpanded) {
                    Text(
                        modifier = Modifier.padding(start = 16.dp),
                        text = titleString,
                        style = MaterialTheme.typography.bodyLarge,
                        color = MaterialTheme.colorScheme.onPrimary,
                    )
                }
            }
        }
    }
}

@Composable
private fun ExpandFloatingActionButtonItem(
    item: ExpandFloatingActionButtonItem,
    modifier: Modifier = Modifier,
    color: Color = MaterialTheme.colorScheme.onSurface,
) {
    Row(
        modifier = modifier
            .clickable { item.onClick.invoke() }
            .padding(16.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(16.dp),
    ) {
        Icon(
            modifier = Modifier.size(32.dp),
            imageVector = item.icon,
            contentDescription = null,
            tint = color,
        )

        Text(
            text = item.name,
            style = MaterialTheme.typography.bodyLarge,
            color = color,
        )
    }
}

@Composable
fun rememberExpandFloatingActionButtonState(): ExpandFloatingActionButtonState {
    return remember {
        ExpandFloatingActionButtonState()
    }
}

class ExpandFloatingActionButtonState {
    internal var isExpanded: Boolean = true
    internal var containerCoordinates: LayoutCoordinates? by mutableStateOf(null)
    private var containerRect: Rect = Rect(0f, 0f, 0f, 0f)

    fun expand() {
        isExpanded = true
    }

    fun hide() {
        isExpanded = false
    }

    internal fun setRectOfFAB(rect: Rect) {
        containerRect = rect
    }

    internal fun isOutOfFAB(offset: Offset): Boolean {
        return containerRect.contains(offset)
    }
}

fun Modifier.expandedFloatingActionButtonContainer(state: ExpandFloatingActionButtonState): Modifier {
    return onGloballyPositioned { state.containerCoordinates = it }
        .pointerInput(state) {
            coroutineScope {
                awaitPointerEventScope {
                    while (isActive) {
                        awaitFirstDown()
                        while (true) {
                            val event = awaitPointerEvent(pass = PointerEventPass.Initial)
                            val change = event.changes.firstOrNull() ?: continue

                            if (!state.isExpanded) continue

                            val offset = state.containerCoordinates?.positionInWindow() ?: Offset.Zero
                            val position = change.position + offset

                            if (state.isOutOfFAB(position) || event.changes.all { it.changedToUp() }) {
                                Timber.d("isOutOfFAB: ${state.isOutOfFAB(position)}")
                                state.hide()
                            }
                        }
                    }
                }
            }
        }
}

data class ExpandFloatingActionButtonItem(
    val icon: ImageVector,
    val name: String,
    val onClick: () -> Unit,
)

@Preview
@Composable
fun PreviewExpandFloatingActionButton() {
    KanadeBackground {
        val state = rememberExpandFloatingActionButtonState()

        Box(
            modifier = Modifier
                .fillMaxSize()
                .expandedFloatingActionButtonContainer(state)
        ) {
            ExpandFloatingActionButton(
                modifier = Modifier
                    .align(Alignment.Center)
                    .padding(16.dp),
                state = state,
                titleIcon = Icons.Default.Edit,
                titleString = "新規作成",
                titleClick = { },
                items = listOf(
                    ExpandFloatingActionButtonItem(
                        icon = Icons.Default.FavoriteBorder,
                        name = "お気に入り",
                        onClick = {}
                    ),
                    ExpandFloatingActionButtonItem(
                        icon = Icons.Default.Folder,
                        name = "外部プレイリスト",
                        onClick = {}
                    ),
                    ExpandFloatingActionButtonItem(
                        icon = Icons.Default.SimCardDownload,
                        name = "m3uファイル",
                        onClick = {}
                    ),
                )
            )
        }
    }
}