package caios.android.kanade.core.model.entity

object YTMusicInfo {
    const val YTM_DOMAIN = "https://music.youtube.com"
    const val YTM_BASE_API = "$YTM_DOMAIN/youtubei/v1/"
    const val YTM_PARAMS = "?alt=json"
    const val YTM_PARAMS_KEY = "&key=AIzaSyC9XL3ZjWddXya6X74dJoCTL-WEYFDNX30"

    const val USER_AGENT = "Mozilla/5.0 (Windows NT 10.0; Win64; x64; rv:88.0) Gecko/20100101 Firefox/88.0"

    const val OAUTH_SCOPE = "https://www.googleapis.com/auth/youtube"
    const val OAUTH_CODE_URL = "https://www.youtube.com/o/oauth2/device/code"
    const val OAUTH_TOKEN_URL = "https://oauth2.googleapis.com/token"
    const val OAUTH_USER_AGENT = "$USER_AGENT Cobalt/Version"
}
