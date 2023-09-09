package caios.android.kanade.core.billing.models

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class ProductId(val value: String) : Parcelable {
    override fun toString(): String = value
}
