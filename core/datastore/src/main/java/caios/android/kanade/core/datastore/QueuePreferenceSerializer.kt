package caios.android.kanade.core.datastore

import androidx.datastore.core.CorruptionException
import androidx.datastore.core.Serializer
import com.google.protobuf.InvalidProtocolBufferException
import java.io.InputStream
import java.io.OutputStream
import javax.inject.Inject

class QueuePreferenceSerializer @Inject constructor() : Serializer<QueuePreference> {

    override val defaultValue: QueuePreference = QueuePreference.getDefaultInstance()

    override suspend fun readFrom(input: InputStream): QueuePreference {
        try {
            return QueuePreference.parseFrom(input)
        } catch (e: InvalidProtocolBufferException) {
            throw CorruptionException("Cannot read proto.", e)
        }
    }

    override suspend fun writeTo(t: QueuePreference, output: OutputStream) = t.writeTo(output)
}
