package com.talangraga.umrohmobile.data.local.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import com.talangraga.umrohmobile.data.local.database.model.PeriodEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface PeriodDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(periodEntity: PeriodEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun inserts(periods: List<PeriodEntity>)

    @Query("SELECT * FROM periode_data")
    fun getCachedPeriods(): List<PeriodEntity>

    @Query("SELECT * FROM periode_data")
    fun getAllPeriods(): Flow<List<PeriodEntity>>

    @Query("DELETE FROM periode_data")
    suspend fun deleteAllPeriods()

    @Query("DELETE FROM periode_data WHERE document_id = :documentId")
    suspend fun deletePeriodById(documentId: String)

    @Query("DELETE FROM periode_data WHERE period_id IN (:ids)")
    suspend fun deleteByIds(ids: List<Int>)

    @Query("SELECT * FROM periode_data WHERE document_id = :documentId LIMIT 1")
    suspend fun getPeriodById(documentId: String): PeriodEntity?

    @Transaction
    suspend fun clearAndInserts(periods: List<PeriodEntity>) {
        deleteAllPeriods()
        inserts(periods)
    }

}