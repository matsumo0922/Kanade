package caios.android.kanade.core.datastore.serializer

import androidx.datastore.core.CorruptionException
import androidx.datastore.core.Serializer
import caios.android.kanade.core.datastore.MusicPreference
import com.google.protobuf.InvalidProtocolBufferException
import java.io.InputStream
import java.io.OutputStream
import javax.inject.Inject

class MusicPreferenceSerializer @Inject constructor() : Serializer<MusicPreference> {

    override val defaultValue: MusicPreference = MusicPreference.getDefaultInstance()

    override suspend fun readFrom(input: InputStream): MusicPreference {
        try {
            return MusicPreference.parseFrom(input)
        } catch (e: InvalidProtocolBufferException) {
            throw CorruptionException("Cannot read proto.", e)
        }
    }

    override suspend fun writeTo(t: MusicPreference, output: OutputStream) = t.writeTo(output)
}
