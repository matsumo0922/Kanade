package caios.android.kanade.feature.share

import android.app.Activity
import android.content.Intent
import android.net.Uri
import caios.android.kanade.core.common.network.util.ToastUtil
import caios.android.kanade.core.design.R
import caios.android.kanade.core.model.music.Song

object ShareUtil {
    fun showShareDialog(activity: Activity, songs: List<Song>) {
        if (songs.isEmpty()) {
            ToastUtil.show(activity, R.string.share_error_no_data)
            return
        }

        val uris = songs.map { it.uri } as ArrayList<Uri>
        val intent = Intent().apply {
            action = Intent.ACTION_SEND_MULTIPLE
            type = "audio/*"
            putParcelableArrayListExtra(Intent.EXTRA_STREAM, uris)
        }

        activity.startActivity(Intent.createChooser(intent, activity.getString(R.string.share_title)))
    }
}
