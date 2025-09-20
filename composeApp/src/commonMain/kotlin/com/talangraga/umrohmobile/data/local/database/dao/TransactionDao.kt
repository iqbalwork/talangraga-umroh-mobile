package com.talangraga.umrohmobile.data.local.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.talangraga.umrohmobile.data.local.database.model.TransactionEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface TransactionDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun inserts(transactions: List<TransactionEntity>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(transactionEntity: TransactionEntity)

    @Query("DELETE FROM transaction_data WHERE id = :id")
    suspend fun deleteTransactionById(id: Int)

    @Query("DELETE FROM transaction_data")
    suspend fun deleteAll()

    @Query("SELECT * FROM transaction_data WHERE id = :id")
    fun getTransactionById(id: Int): TransactionEntity?

    @Query("SELECT * FROM transaction_data")
    fun getAllTransactions(): Flow<List<TransactionEntity>>

}