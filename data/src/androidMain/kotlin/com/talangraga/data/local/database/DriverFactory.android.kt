package com.talangraga.data.local.database

import android.content.Context
import app.cash.sqldelight.db.SqlDriver
import app.cash.sqldelight.driver.android.AndroidSqliteDriver
import com.talangraga.TalangragaDatabase

@Suppress("EXPECT_ACTUAL_CLASSIFIERS_ARE_IN_BETA_WARNING")
actual class DriverFactory(private val context: Context) {
    actual fun createDriver(): SqlDriver {
        val driver = AndroidSqliteDriver(TalangragaDatabase.Schema, context, "umroh.db")
        try {
            val cursor = driver.executeQuery(
                identifier = null,
                sql = "PRAGMA table_info(UserData)",
                mapper = { c ->
                    var isInt = false
                    while (c.next().value) {
                        val colName = c.getString(1)
                        val colType = c.getString(2)
                        if (colName == "userId" && colType?.contains("INT", ignoreCase = true) == true) {
                            isInt = true
                        }
                    }
                    app.cash.sqldelight.db.QueryResult.Value(isInt)
                },
                parameters = 0
            )
            if (cursor.value) {
                driver.execute(null, "DROP TABLE IF EXISTS UserData", 0)
                driver.execute(null, "DROP TABLE IF EXISTS TransactionData", 0)
                driver.execute(
                    null,
                    """
                    CREATE TABLE IF NOT EXISTS UserData (
                        userId TEXT PRIMARY KEY,
                        username TEXT NOT NULL,
                        fullname TEXT NOT NULL,
                        email TEXT,
                        phone TEXT,
                        domisili TEXT,
                        userType TEXT,
                        imageProfileUrl TEXT
                    );
                    """.trimIndent(),
                    0
                )
                driver.execute(
                    null,
                    """
                    CREATE TABLE IF NOT EXISTS TransactionData (
                        transactionId INTEGER PRIMARY KEY,
                        amount INTEGER NOT NULL,
                        reportedDate TEXT NOT NULL,
                        transactionDate TEXT NOT NULL,
                        statusTransaksi TEXT NOT NULL,
                        buktiTransferUrl TEXT NOT NULL,
                        paymentType TEXT,
                        paymentName TEXT,
                        reportedBy TEXT,
                        confirmedBy TEXT,
                        userName TEXT,
                        userId TEXT,
                        periodId INTEGER
                    );
                    """.trimIndent(),
                    0
                )
            }
        } catch (_: Exception) {
            // Ignore if inspection fails
        }
        return driver
    }
}
