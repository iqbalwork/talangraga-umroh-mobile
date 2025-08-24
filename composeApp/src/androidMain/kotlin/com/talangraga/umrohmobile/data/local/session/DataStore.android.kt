import android.content.Context

fun createDataStore(context: Context): SessionStore = createDataStore(
    producePath = { context.filesDir.resolve(DATA_STORE_FILE_NAME).absolutePath }
)