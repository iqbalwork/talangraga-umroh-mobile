package com.talangraga.umrohmobile.data.local.database

import app.cash.sqldelight.coroutines.asFlow
import app.cash.sqldelight.coroutines.mapToList
import com.talangraga.umrohmobile.TalangragaDatabase
import com.talangraga.umrohmobile.data.local.database.model.PaymentEntity
import com.talangraga.umrohmobile.data.local.database.model.PeriodEntity
import com.talangraga.umrohmobile.data.local.database.model.TransactionEntity
import com.talangraga.umrohmobile.data.local.database.model.UserEntity
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class DatabaseHelper(factory: DriverFactory) {

    private val database = TalangragaDatabase(factory.createDriver())
    private val paymentQueries = database.paymentQueries
    private val periodQueries = database.periodsQueries
    private val transactionsQueries = database.transactionsQueries
    private val usersQueries = database.userQueries

    fun insertPayments(list: List<PaymentEntity>) {
        list.forEach { payment ->
            paymentQueries.insertPayment(
                paymentId = payment.paymentId.toLong(),
                paymentName = payment.paymentName,
                paymentType = payment.paymentType
            )
        }
    }

    fun clearPayments() = paymentQueries.deleteAllPayments()

    fun deletePaymentById(paymentId: Long) {
        paymentQueries.deletePaymentById(paymentId)
    }

    fun deletePaymentByIds(paymentIds: List<Int>) {
        paymentIds.forEach { deletePaymentById(it.toLong()) }
    }

    fun getAllPayments() = paymentQueries.selectAllPayments().executeAsList().map { it.toPaymentEntity() }

    fun getAllPaymentsAsFlow(): Flow<List<PaymentEntity>> =
        paymentQueries.selectAllPayments()
            .asFlow()
            .mapToList(Dispatchers.IO)
            .map { data ->
                data.map { it.toPaymentEntity() }
            }

    fun insertPeriods(list: List<PeriodEntity>) {
        list.forEach { (periodId, periodeName, startDate, endDate) ->
            periodQueries.insertPeriodData(
                periodId = periodId.toLong(),
                periodeName = periodeName,
                startDate = startDate,
                endDate = endDate
            )
        }
    }

    fun clearPeriods() = periodQueries.deleteAllPeriodData()

    fun deletePeriodById(periodId: Long) {
        periodQueries.deletePeriodDataById(periodId)
    }

    fun deletePeriodByIds(periodIds: List<Int>) {
        periodIds.forEach { deletePeriodById(it.toLong()) }
    }

    fun getAllPeriods() =
        periodQueries.selectAllPeriodData().executeAsList().map { it.toPeriodEntity() }

    fun getAllPeriodsAsFlow(): Flow<List<PeriodEntity>> =
        periodQueries.selectAllPeriodData()
            .asFlow()
            .mapToList(Dispatchers.IO)
            .map { data ->
                data.map { it.toPeriodEntity() }
            }

    fun insertTransactions(list: List<TransactionEntity>) {
        list.forEach { (transactionId, amount, reportedDate, transactionDate, statusTransaksi, buktiTransferUrl, paymentType, paymentName, reportedBy, confirmedBy) ->
            transactionsQueries.insertTransactionData(
                transactionId = transactionId.toLong(),
                amount = amount.toLong(),
                reportedDate = reportedDate,
                transactionDate = transactionDate,
                statusTransaksi = statusTransaksi,
                buktiTransferUrl = buktiTransferUrl,
                paymentType = paymentType,
                paymentName = paymentName,
                reportedBy = reportedBy,
                confirmedBy = confirmedBy
            )
        }
    }

    fun clearTransactions() = transactionsQueries.deleteAllTransactionData()

    fun deleteTransactionById(id: Long) {
        transactionsQueries.deleteTransactionDataById(id)
    }

    fun deleteTransactionByIds(ids: List<Int>) {
        ids.forEach { deleteTransactionById(it.toLong()) }
    }

    fun getAllTransactions() = transactionsQueries.selectAllTransactionData().executeAsList().map { it.toTransactionEntity() }

    fun getAllTransactionsAsFlow(): Flow<List<TransactionEntity>> =
        transactionsQueries.selectAll()
            .asFlow()
            .mapToList(Dispatchers.IO)
            .map { data ->
                data.map { it.toTransactionEntity() }
            }

    fun insertUsers(list: List<UserEntity>) {
        list.forEach { (userId, userName, fullname, email, phone, domisili, userType, imageProfileUrl) ->
            usersQueries.insertUserData(
                userId = userId.toLong(),
                username = userName,
                fullname = fullname,
                email = email,
                phone = phone,
                domisili = domisili,
                userType = userType,
                imageProfileUrl = imageProfileUrl
            )
        }
    }

    fun clearUsers() = usersQueries.deleteAllUserData()

    fun deleteUserById(userId: Long) = usersQueries.deleteUserDataById(userId = userId)

    fun deleteUserByIds(userIds: List<Int>) {
        userIds.forEach { deleteUserById(it.toLong()) }
    }

    fun getAllUsers() = usersQueries.selectAllUserData().executeAsList().map { it.toUserEntity() }

    fun getAllUsersAsFlow(): Flow<List<UserEntity>> =
        usersQueries.selectAllUserData()
            .asFlow()
            .mapToList(Dispatchers.IO)
            .map { data ->
                data.map { it.toUserEntity() }
            }

}
