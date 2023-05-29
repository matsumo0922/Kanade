package caios.android.kanade.core.common.network

import android.util.Log
import timber.log.Timber
import java.util.Locale

class KanadeDebugTree : Timber.DebugTree() {

    override fun log(priority: Int, tag: String?, message: String, t: Throwable?) {
        Log.println(priority, tag ?: "[Kanade]", "$message ${getCallerInfo()}")
    }

    private fun getCallerInfo(): String {
        val stackTrace = Throwable().stackTrace

        if (stackTrace.size < 7) {
            // stack[0] KanadeDebugTree.getCallerInfo()
            // stack[1] KanadeDebugTree.log()
            // stack[2] Tree.prepareLog()
            // stack[3] Tree.d()
            // stack[4] 1.d()
            // stack[5] Timber.d()
            // stack[6] CallerClass#xxxx

            return ""
        }

        return String.format(Locale.getDefault(), "@%s:%s", stackTrace[6].fileName, stackTrace[6].lineNumber)
    }
}
