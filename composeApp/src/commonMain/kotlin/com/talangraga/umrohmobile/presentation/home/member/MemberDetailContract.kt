package com.talangraga.umrohmobile.presentation.home.member

import com.talangraga.umrohmobile.presentation.home.SectionState
import com.talangraga.umrohmobile.presentation.transaction.model.TransactionUiData
import com.talangraga.umrohmobile.presentation.user.model.UserUIData

data class MemberDetailState(
    val user: UserUIData? = null,
    val transactionState: SectionState<List<TransactionUiData>> = SectionState.Loading,
    val errorMessage: String? = null
)

sealed interface MemberDetailEvent {
    data class GetUser(val userId: Int) : MemberDetailEvent
}

sealed interface MemberDetailEffect {
    data class ShowToastError(val message: String) : MemberDetailEffect
}
