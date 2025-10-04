package com.talangraga.umrohmobile.data.local.database

import androidx.room.ConstructedBy
import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.RoomDatabaseConstructor
import androidx.room.migration.AutoMigrationSpec
import androidx.sqlite.SQLiteConnection
import androidx.sqlite.driver.bundled.BundledSQLiteDriver
import com.talangraga.umrohmobile.data.local.database.dao.PaymentDao
import com.talangraga.umrohmobile.data.local.database.dao.PeriodDao
import com.talangraga.umrohmobile.data.local.database.dao.TransactionDao
import com.talangraga.umrohmobile.data.local.database.dao.UserDao
import com.talangraga.umrohmobile.data.local.database.model.PaymentEntity
import com.talangraga.umrohmobile.data.local.database.model.PeriodEntity
import com.talangraga.umrohmobile.data.local.database.model.TransactionEntity
import com.talangraga.umrohmobile.data.local.database.model.UserEntity
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO

@Database(
    entities = [UserEntity::class, PaymentEntity::class, PeriodEntity::class, TransactionEntity::class],
    version = 5,
    exportSchema = false
)
@ConstructedBy(AppDatabaseConstructor::class)
abstract class TalangragaDatabase : RoomDatabase() {
    abstract fun userDao(): UserDao
    abstract fun periodsDao(): PeriodDao
    abstract fun transactionDao(): TransactionDao
    abstract fun paymentDao(): PaymentDao
}

fun getRoomDatabase(
    builder: RoomDatabase.Builder<TalangragaDatabase>
): TalangragaDatabase {
    return builder
        .setDriver(BundledSQLiteDriver())
        .fallbackToDestructiveMigration(true)
        .setQueryCoroutineContext(Dispatchers.IO)
        .build()
}

// The Room compiler generates the `actual` implementations.
@Suppress("KotlinNoActualForExpect", "EXPECT_ACTUAL_CLASSIFIERS_ARE_IN_BETA_WARNING")
expect object AppDatabaseConstructor : RoomDatabaseConstructor<TalangragaDatabase> {
    override fun initialize(): TalangragaDatabase
}

class AutoMigrationSpec_1_2 : AutoMigrationSpec {
    override fun onPostMigrate(connection: SQLiteConnection) {
        // …
    }
}