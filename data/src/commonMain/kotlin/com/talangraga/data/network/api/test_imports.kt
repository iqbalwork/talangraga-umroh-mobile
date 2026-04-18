import io.ktor.client.request.forms.ChannelProvider
import io.ktor.utils.io.ByteReadChannel

fun test(file: ByteArray) {
    val p = ChannelProvider(size = file.size.toLong()) { ByteReadChannel(file) }
}
