package caios.android.kanade.core.design.component

import android.content.Context
import android.graphics.drawable.Animatable
import android.util.AttributeSet
import android.widget.Checkable
import androidx.annotation.DrawableRes
import androidx.appcompat.widget.AppCompatImageView
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.core.content.ContextCompat
import androidx.core.graphics.drawable.DrawableCompat

@Composable
fun AnimatedIcon(
    @DrawableRes animatedIcon: Int,
    isSelected: Boolean,
    tint: Color,
    modifier: Modifier = Modifier,
) {
    AndroidView(
        modifier = modifier.size(24.dp),
        factory = { context ->
            CheckableImageView(context).apply {
                val drawable = ContextCompat.getDrawable(context, animatedIcon)
                drawable?.let { DrawableCompat.setTint(it, tint.toArgb()) }
                setImageDrawable(drawable)
                isChecked = isSelected
                if (drawable is Animatable) drawable.start()
            }
        },
        update = { view ->
            view.isChecked = isSelected
        },
    )
}

private class CheckableImageView(context: Context, attrs: AttributeSet? = null) : AppCompatImageView(context, attrs), Checkable {

    private var mChecked = false

    override fun onCreateDrawableState(extraSpace: Int): IntArray {
        val drawableState = super.onCreateDrawableState(extraSpace + 1)
        if (isChecked) {
            mergeDrawableStates(drawableState, CHECKED_STATE_SET)
        }
        return drawableState
    }

    override fun toggle() {
        isChecked = !mChecked
    }

    override fun isChecked(): Boolean = mChecked

    override fun setChecked(checked: Boolean) {
        if (mChecked != checked) {
            mChecked = checked
            refreshDrawableState()
        }
    }

    companion object {
        private val CHECKED_STATE_SET = intArrayOf(
            android.R.attr.state_checked,
        )
    }
}
