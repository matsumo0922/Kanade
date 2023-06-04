package caios.android.kanade.core.ui.controller

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.BottomSheetState
import androidx.compose.material.BottomSheetValue
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.constraintlayout.compose.ConstraintLayout

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun BottomController(
    bottomSheetState: BottomSheetState,
    onClickController: () -> Unit,
    modifier: Modifier = Modifier,
) {
    var oldProgress by remember { mutableStateOf(bottomSheetState.currentValue) }
    val dragProgress = when (bottomSheetState.currentValue) {
        BottomSheetValue.Collapsed -> {
            if (oldProgress != BottomSheetValue.Collapsed) 0f else bottomSheetState.progress
        }
        BottomSheetValue.Expanded -> {
            if (oldProgress != BottomSheetValue.Expanded) 1f else 1f - bottomSheetState.progress
        }
    }

    oldProgress = bottomSheetState.currentValue

    ConstraintLayout(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Red.copy(green = dragProgress)),
    ) {
    }
}
