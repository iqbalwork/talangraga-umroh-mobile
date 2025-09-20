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

    @Query("DELETE FROM payment_data WHERE payment_id = :paymentId")
    suspend fun deleteById(paymentId: Int)

    @Query("DELETE FROM payment_data")
    suspend fun deleteAll()

    @Query("SELECT * FROM payment_data WHERE payment_id = :paymentId")
    suspend fun getPaymentById(paymentId: Int): PaymentEntity?

    @Query("SELECT * FROM payment_data")
    fun getAllPayments(): Flow<List<PaymentEntity>>

}