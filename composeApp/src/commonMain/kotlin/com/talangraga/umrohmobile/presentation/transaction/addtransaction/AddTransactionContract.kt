package com.talangraga.umrohmobile.presentation.transaction.addtransaction

import com.talangraga.data.local.database.model.PaymentEntity
import com.talangraga.data.local.database.model.PeriodEntity
import com.talangraga.umrohmobile.presentation.user.model.UserUIData

data class CollectiveMember(
    val user: UserUIData,
    val amount: String
)

data class AddTransactionState(
    val users: List<UserUIData> = emptyList(),
    val periods: List<PeriodEntity> = emptyList(),
    val payments: List<PaymentEntity> = emptyList(),
    val errorMessage: String? = null,
    val selectedUser: UserUIData? = null,
    val selectedPeriod: PeriodEntity? = null,
    val selectedPayment: PaymentEntity? = null,
    val imageUri: ByteArray? = null,
    val isLoading: Boolean = false,
    val isCollective: Boolean = false,
    val collectiveMembers: List<CollectiveMember> = emptyList()
)

sealed interface AddTransactionEvent {
    data object GetListUser : AddTransactionEvent
    data object GetPeriods : AddTransactionEvent
    data object GetListPayments : AddTransactionEvent
    data class SetSelectedPeriod(val period: PeriodEntity?) : AddTransactionEvent
    data class SetSelectedPayment(val payment: PaymentEntity?) : AddTransactionEvent
    data class SetImageUri(val uri: ByteArray?) : AddTransactionEvent
    data class SetSelectedUser(val user: UserUIData?): AddTransactionEvent
    data class SetIsCollective(val isCollective: Boolean) : AddTransactionEvent
    data class AddCollectiveMember(val user: UserUIData, val amount: String) : AddTransactionEvent
    data class RemoveCollectiveMember(val user: UserUIData) : AddTransactionEvent
    data class SubmitTransaction(
        val amount: String,
        val dateMillis: Long?,
        val time: String,
        val user: UserUIData?
    ) : AddTransactionEvent
}

sealed interface AddTransactionEffect {
    data class ShowToastError(val message: String) : AddTransactionEffect
    data class ShowToastSuccess(val message: String) : AddTransactionEffect
    data object NavigateBack : AddTransactionEffect
}
