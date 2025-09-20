package com.talangraga.umrohmobile.data.local.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.talangraga.umrohmobile.data.local.database.model.PaymentEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface PaymentDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun inserts(payments: List<PaymentEntity>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(payment: PaymentEntity)

    @Query("DELETE FROM payment_data WHERE id = :id")
    suspend fun deleteById(id: Int)

    @Query("DELETE FROM payment_data")
    suspend fun deleteAll()

    @Query("SELECT * FROM payment_data WHERE id = :id")
    fun getById(id: Int): PaymentEntity?

    @Query("SELECT * FROM payment_data")
    fun getAllPayments(): Flow<List<PaymentEntity>>

}