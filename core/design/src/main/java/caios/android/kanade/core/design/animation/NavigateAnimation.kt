package caios.android.kanade.core.design.animation

import androidx.compose.animation.core.CubicBezierEasing
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.scaleIn
import androidx.compose.animation.slideIn
import androidx.compose.animation.slideOut
import androidx.compose.ui.graphics.TransformOrigin
import androidx.compose.ui.unit.IntOffset

object NavigateAnimation {
    val decelerateEasing = CubicBezierEasing(0.0f, 0.0f, 0.0f, 1.0f)

    object Library {
        val enter = fadeIn(tween(240)) + scaleIn(
            initialScale = 0.92f,
            transformOrigin = TransformOrigin.Center,
            animationSpec = tween(240, 0, decelerateEasing),
        )

        val exit = fadeOut(tween(240))
    }

    object Detail {
        val enter = fadeIn(tween(200)) + slideIn(
            animationSpec = tween(220, 0, decelerateEasing),
            initialOffset = { IntOffset(0, (-it.height * 0.1).toInt()) },
        )

        val exit = fadeOut(tween(200)) + slideOut(
            animationSpec = tween(220, 0, decelerateEasing),
            targetOffset = { IntOffset(0, (-it.height * 0.1).toInt()) },
        )

        val popEnter = fadeIn(tween(200)) + slideIn(
            animationSpec = tween(220, 0, decelerateEasing),
            initialOffset = { IntOffset(0, (it.height * 0.1).toInt()) },
        )

        val popExit = fadeOut(tween(200)) + slideOut(
            animationSpec = tween(220, 0, decelerateEasing),
            targetOffset = { IntOffset(0, (it.height * 0.1).toInt()) },
        )
    }
}
