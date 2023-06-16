import android.os.Bundle

fun buildBundle(f: Bundle.() -> Unit): Bundle {
    return Bundle().apply(f)
}