import android.content.Context
import app.cash.sqldelight.db.SqlDriver
import app.cash.sqldelight.driver.android.AndroidSqliteDriver
import com.talangraga.umrohmobile.TalangragaDatabase

actual class DriverFactory(private val context: Context) {
    actual fun createDriver(): SqlDriver =
        AndroidSqliteDriver(TalangragaDatabase.Schema, context, "umroh.db")
}
