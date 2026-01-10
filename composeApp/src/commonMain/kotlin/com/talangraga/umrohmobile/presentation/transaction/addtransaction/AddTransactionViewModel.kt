package com.talangraga.umrohmobile.presentation.transaction.addtransaction

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.talangraga.data.domain.repository.Repository
import com.talangraga.data.network.api.Result
import com.talangraga.umrohmobile.presentation.transaction.model.PaymentGroupUIData
import com.talangraga.umrohmobile.presentation.transaction.model.PaymentUIData
import com.talangraga.umrohmobile.presentation.user.model.UserUIData
import com.talangraga.umrohmobile.presentation.utils.toUIData
import com.talangraga.umrohmobile.presentation.utils.toUiData
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.update

/**
 * iqbalfauzi
 * Email: work.iqbalfauzi@gmail.com
 * Github: https://github.com/iqbalwork
 */
class AddTransactionViewModel(
    private val repository: Repository
) : ViewModel() {

    private val _users = MutableStateFlow<List<UserUIData>>(emptyList())
    val users = _users.asStateFlow()

    private val _errorMessage = MutableStateFlow<String?>(null)
    val errorMessage = _errorMessage.asStateFlow()

    private val _paymentGroup = MutableStateFlow<List<PaymentGroupUIData>>(emptyList())
    val paymentGroup = _paymentGroup.asStateFlow()

    val imageUri = mutableStateOf<ByteArray?>(null)
    val user = mutableStateOf<UserUIData?>(null)
    val tabungan = mutableStateOf<String?>(null)
    val payment = mutableStateOf<PaymentUIData?>(null)
    val date = mutableStateOf("")
    val time = mutableStateOf("")

    init {
        getListUser()
        getPayments()
    }

    fun onImageChange(bytes: ByteArray) {
        imageUri.value = bytes
    }

    fun onUserChange(newValue: UserUIData) {
        user.value = newValue
    }

    fun onTabunganChange(newValue: String) {
        tabungan.value = newValue
    }

    fun onPaymentChange(newValue: PaymentUIData) {
        payment.value = newValue
    }

    fun onDateChange(newValue: String) {
        date.value = newValue
    }

    fun onTimeChange(value: String) {
        time.value = value
    }

    fun addTransaction() {

    }

    private fun getListUser() {
        repository.getLocalUsers().onEach { result ->
            when (result) {
                is Result.Error -> {
                    _errorMessage.update { result.t.message }
                }

                is Result.Success -> {
                    val data = result.data.map { it.toUiData() }
                    _users.update { data }
                }
            }
        }.launchIn(viewModelScope)
    }

    private fun getPayments() {
        repository.getPayments()
            .onEach { result ->
                when (result) {
                    is Result.Error -> {
                        _errorMessage.update { result.t.message }
                    }

                    is Result.Success -> {
                        val data = result.data.map { it.toUIData() }.groupBy { it.paymentType }
                        val paymentGroup = mutableListOf<PaymentGroupUIData>()
                        data.forEach {
                            val group = PaymentGroupUIData(it.key, it.value)
                            paymentGroup.add(group)
                        }
                        _paymentGroup.update { paymentGroup }
                    }
                }
            }.launchIn(viewModelScope)
    }
}
