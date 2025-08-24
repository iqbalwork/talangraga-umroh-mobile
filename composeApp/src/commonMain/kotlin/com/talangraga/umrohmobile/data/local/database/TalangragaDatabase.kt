package com.talangraga.umrohmobile.data.local.database

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.migration.AutoMigrationSpec
import androidx.sqlite.SQLiteConnection
import androidx.sqlite.driver.bundled.BundledSQLiteDriver
import com.talangraga.umrohmobile.data.local.database.dao.UserDao
import com.talangraga.umrohmobile.data.local.database.model.UserEntity
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO

@Database(entities = [UserEntity::class], version = 1, exportSchema = false)
abstract class TalangragaDatabase : RoomDatabase() {
    abstract fun userDao(): UserDao
}

fun getRoomDatabase(
    builder: RoomDatabase.Builder<TalangragaDatabase>
): TalangragaDatabase {
    return builder
        .setDriver(BundledSQLiteDriver())
        .setQueryCoroutineContext(Dispatchers.IO)
        .build()
}

class AutoMigrationSpec_1_2 : AutoMigrationSpec {
    override fun onPostMigrate(connection: SQLiteConnection) {
        // …
    }
}